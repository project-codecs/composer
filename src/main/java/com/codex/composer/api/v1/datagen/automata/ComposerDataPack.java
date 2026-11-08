package com.codex.composer.api.v1.datagen.automata;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.gson.*;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.registry.RegistryWrapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("UnusedReturnValue")
public class ComposerDataPack {
    private final MergedProvider group;

    private ComposerDataPack(FabricDataGenerator.Pack pack) {
        this.group = pack.addProvider(MergedProvider::new);
    }

    public static ComposerDataPack of(FabricDataGenerator.Pack pack) {
        return new ComposerDataPack(pack);
    }

    public <T extends DataProvider> T addProvider(FabricDataGenerator.Pack.Factory<T> factory) {
        return group.add(factory.create(group.output));
    }

    public <T extends DataProvider> T addProvider(FabricDataGenerator.Pack.RegistryDependentFactory<T> factory) {
        return group.add(factory.create(group.output, group.lookup));
    }

    public ComposerDataPack oreSets() { return oreSets(true); }
    public ComposerDataPack oreSets(boolean blockTags) { OreSetDataProviders.generateFor(this, blockTags); return this; }

    public ComposerDataPack blockSets() { return blockSets(true); }
    public ComposerDataPack blockSets(boolean blockTags) { ComposedBlockSetDataProviders.generateFor(this, blockTags); return this; }

    private static final class MergedProvider implements DataProvider {
        private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

        final FabricDataOutput output;
        final CompletableFuture<RegistryWrapper.WrapperLookup> lookup;
        private final List<DataProvider> providers = new ArrayList<>();

        MergedProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> lookup) {
            this.output = output;
            this.lookup = lookup;
        }

        <T extends DataProvider> T add(T provider) {
            providers.add(provider);
            return provider;
        }

        @Override
        public CompletableFuture<?> run(DataWriter writer) {
            List<Buffer> buffers = new ArrayList<>();
            CompletableFuture<?>[] futures = new CompletableFuture<?>[providers.size()];

            for (int i = 0; i < providers.size(); i++) {
                Buffer buffer = new Buffer();
                buffers.add(buffer);
                futures[i] = providers.get(i).run(buffer);
            }

            return CompletableFuture.allOf(futures).thenRun(() -> {
                try {
                    flush(buffers, writer);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        }

        private static void flush(List<Buffer> buffers, DataWriter writer) throws IOException {
            Map<Path, List<byte[]>> all = new TreeMap<>();
            for (Buffer buffer : buffers) {
                for (Map.Entry<Path, List<byte[]>> e : buffer.files.entrySet()) {
                    all.computeIfAbsent(e.getKey(), k -> new ArrayList<>()).addAll(e.getValue());
                }
            }
            for (Map.Entry<Path, List<byte[]>> e : all.entrySet()) {
                byte[] bytes = merge(e.getKey(), e.getValue());
                writer.write(e.getKey(), bytes, Hashing.sha1().hashBytes(bytes));
            }
        }

        private static byte[] merge(Path path, List<byte[]> blobs) {
            String p = path.toString().replace('\\', '/');
            boolean json = p.endsWith(".json");
            boolean tag = json && p.contains("/tags/");

            if (!tag && blobs.size() == 1) return blobs.get(0);
            if (tag) return toBytes(mergeTags(blobs));

            byte[] first = blobs.get(0);
            if (!json) {
                for (byte[] b : blobs) {
                    if (!Arrays.equals(first, b)) throw new IllegalStateException("Conflicting non-JSON output at " + p);
                }
                return first;
            }

            JsonElement result = parse(first);
            for (int i = 1; i < blobs.size(); i++) result = deepMerge(p, result, parse(blobs.get(i)));
            return toBytes(result);
        }

        private static JsonObject mergeTags(List<byte[]> blobs) {
            boolean replace = false;
            Map<String, JsonElement> values = new TreeMap<>();

            for (byte[] blob : blobs) {
                JsonObject tag = parse(blob).getAsJsonObject();
                if (tag.has("replace") && tag.get("replace").getAsBoolean()) replace = true;
                if (!tag.has("values")) continue;

                for (JsonElement entry : tag.getAsJsonArray("values")) {
                    String id = entry.isJsonPrimitive() ? entry.getAsString() : entry.getAsJsonObject().get("id").getAsString();
                    JsonElement existing = values.get(id);
                    if (existing == null || (existing.isJsonObject() && entry.isJsonPrimitive())) values.put(id, entry);
                }
            }

            JsonObject out = new JsonObject();
            out.addProperty("replace", replace);
            JsonArray array = new JsonArray();
            values.values().forEach(array::add);
            out.add("values", array);
            return out;
        }

        private static JsonElement deepMerge(String path, JsonElement a, JsonElement b) {
            if (a.equals(b)) return a;
            if (a.isJsonObject() && b.isJsonObject()) {
                JsonObject out = a.getAsJsonObject().deepCopy();
                for (Map.Entry<String, JsonElement> e : b.getAsJsonObject().entrySet()) {
                    JsonElement existing = out.get(e.getKey());
                    out.add(e.getKey(), existing == null ? e.getValue() : deepMerge(path + "#" + e.getKey(), existing, e.getValue()));
                }
                return out;
            }
            throw new IllegalStateException("Conflicting data generated for " + path);
        }

        private static JsonElement parse(byte[] bytes) {
            return JsonParser.parseString(new String(bytes, StandardCharsets.UTF_8));
        }

        private static byte[] toBytes(JsonElement element) {
            return GSON.toJson(element).getBytes(StandardCharsets.UTF_8);
        }

        @Override
        public String getName() {
            return "Composer Merged Data";
        }

        private static final class Buffer implements DataWriter {
            final Map<Path, List<byte[]>> files = new ConcurrentHashMap<>();

            @Override
            public void write(Path path, byte[] data, HashCode hashCode) {
                files.computeIfAbsent(path.normalize(), k -> Collections.synchronizedList(new ArrayList<>())).add(data);
            }
        }
    }
}