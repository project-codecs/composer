package com.codex.composer.api.v1.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3i;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public abstract class ComposerMultiblockProvider implements DataProvider {
    private final List<CompletableFuture<?>> futures = new ArrayList<>();
    private final FabricDataOutput output;

    public ComposerMultiblockProvider(FabricDataOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        generate(writer);
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[]{}));
    }

    public abstract void generate(DataWriter writer);

    protected MultiblockJsonBuilder create(Identifier id) {
        return new MultiblockJsonBuilder(this, id);
    }

    @Override
    public String getName() {
        return "Multiblocks";
    }

    public static class MultiblockJsonBuilder {
        private final ComposerMultiblockProvider provider;
        private final Identifier id;
        private final List<List<String>> layers = new ArrayList<>();
        private final Map<Character, JsonElement> pattern = new HashMap<>();
        private final Map<Identifier, JsonObject> extensions = new LinkedHashMap<>();
        private final Set<Identifier> referencedExtensions = new HashSet<>();
        private Vec3i controllerPos = null;
        private int rowWidth = -1;
        private int layerWidth = -1;

        private MultiblockJsonBuilder(ComposerMultiblockProvider provider, Identifier id) {
            this.provider = provider;
            this.id = id;
        }

        public LayerBuilder layer() {
            return new LayerBuilder(this, rowWidth);
        }

        public PatternBuilder pattern(char character) {
            return new PatternBuilder(this, character);
        }

        public ExtensionBuilder extension(Identifier extensionId) {
            return new ExtensionBuilder(this, extensionId);
        }

        public ExtensionBuilder extension(String extensionId) {
            return extension(Identifier.of(id.getNamespace(), extensionId));
        }

        public MultiblockJsonBuilder controller(Vec3i pos) {
            this.controllerPos = pos;
            return this;
        }

        public MultiblockJsonBuilder controller(int x, int y, int z) {
            return controller(new Vec3i(x, y, z));
        }

        private MultiblockJsonBuilder push(LayerBuilder builder) {
            if (rowWidth == -1) rowWidth = builder.rowWidth;
            if (layerWidth == -1) layerWidth = builder.rows.size();
            else if (builder.rows.size() != layerWidth) throw new IllegalArgumentException("All layers must contain the same amount of rows! Use spaces as air if needed.");
            layers.add(builder.rows);
            return this;
        }

        private MultiblockJsonBuilder push(char character, JsonElement value, Identifier[] accepted) {
            if (accepted.length == 0) {
                pattern.put(character, value);
            } else {
                JsonObject obj = new JsonObject();
                obj.add("block", value);

                JsonArray array = new JsonArray();
                for (Identifier ext : accepted) {
                    array.add(ext.toString());
                    referencedExtensions.add(ext);
                }
                obj.add("extensions", array);
                pattern.put(character, obj);
            }
            return this;
        }

        public void write(DataWriter writer) {
            if (controllerPos == null) throw new IllegalStateException("'controllerPos' must be set for a multiblock!");
            JsonObject json = new JsonObject();
            JsonArray layers = new JsonArray();
            for (List<String> layer : this.layers) {
                JsonArray rows = new JsonArray();
                layer.forEach(rows::add);
                layers.add(rows);
            }

            JsonObject pattern = new JsonObject();
            this.pattern.forEach((key, value) -> pattern.add(key.toString(), value));

            this.layers.forEach(layer -> layer.forEach(row -> {
                for (int i = 0; i < row.length(); i++) {
                    char chr = row.charAt(i);
                    if (chr != ' ' && chr != '*' && !pattern.keySet().contains(String.valueOf(chr))) throw new IllegalArgumentException("Unknown character '" + chr + "' in multiblock layer!");
                }
            }));

            for (Identifier ref : referencedExtensions) {
                if (!extensions.containsKey(ref)) throw new IllegalArgumentException("Pattern references extension '" + ref + "' which was never defined with extension()!");
            }

            JsonObject controllerPos = new JsonObject();
            controllerPos.addProperty("x", this.controllerPos.getX());
            controllerPos.addProperty("y", this.controllerPos.getY());
            controllerPos.addProperty("z", this.controllerPos.getZ());

            json.addProperty("id", id.toString());
            json.add("layers", layers);
            json.add("pattern", pattern);
            json.add("controller_pos", controllerPos);

            if (!extensions.isEmpty()) {
                JsonObject exts = new JsonObject();
                extensions.forEach((key, value) -> exts.add(key.toString(), value));
                json.add("extensions", exts);
            }

            provider.futures.add(DataProvider.writeToPath(
                    writer,
                    json,
                    provider.output.getPath().resolve("data/" + id.getNamespace() + "/multiblocks/" + id.getPath() + ".json")
            ));
        }

        public static class PatternBuilder {
            private final MultiblockJsonBuilder parent;
            private final char character;

            private PatternBuilder(MultiblockJsonBuilder parent, char character) {
                this.parent = parent;
                this.character = character;
            }

            public MultiblockJsonBuilder block(Block block, Identifier... extensions) {
                return parent.push(character, new JsonPrimitive(Registries.BLOCK.getId(block).toString()), extensions);
            }

            public MultiblockJsonBuilder tag(TagKey<Block> tag, Identifier... extensions) {
                return parent.push(character, new JsonPrimitive("#" + tag.id()), extensions);
            }
        }

        public static class ExtensionBuilder {
            private final MultiblockJsonBuilder parent;
            private final Identifier id;
            private final JsonArray blocks = new JsonArray();
            private int min = 0;
            private int max = Integer.MAX_VALUE;

            private ExtensionBuilder(MultiblockJsonBuilder parent, Identifier id) {
                this.parent = parent;
                this.id = id;
            }

            public ExtensionBuilder block(Block block) {
                blocks.add(Registries.BLOCK.getId(block).toString());
                return this;
            }

            public ExtensionBuilder tag(TagKey<Block> tag) {
                blocks.add("#" + tag.id());
                return this;
            }

            public ExtensionBuilder min(int min) {
                this.min = min;
                return this;
            }

            public ExtensionBuilder max(int max) {
                this.max = max;
                return this;
            }

            public MultiblockJsonBuilder end() {
                if (blocks.isEmpty()) throw new IllegalStateException("Extension '" + id + "' needs at least one block or tag!");
                JsonObject obj = new JsonObject();
                obj.add("block", blocks.size() == 1 ? blocks.get(0) : blocks);
                if (min > 0) obj.addProperty("min", min);
                if (max != Integer.MAX_VALUE) obj.addProperty("max", max);
                parent.extensions.put(id, obj);
                return parent;
            }
        }

        public static class LayerBuilder {
            private final MultiblockJsonBuilder parent;
            private final List<String> rows = new ArrayList<>();
            private int rowWidth;

            private LayerBuilder(MultiblockJsonBuilder parent, int rowWidth) {
                this.parent = parent;
                this.rowWidth = rowWidth;
            }

            public LayerBuilder row(String row) {
                if (rowWidth == -1) rowWidth = row.length();
                if (row.length() != rowWidth) throw new IllegalArgumentException("Every row of every layer of a multiblock must be the same! Use spaces as air if needed.");
                rows.add(row);
                return this;
            }

            public LayerBuilder rows(String row, int times) {
                if (times <= 0) throw new IllegalArgumentException("'times' must not be 0 or less in LayerBuilder#rows!");
                for (int i = 0; i < times - 1; i++) {
                    this.row(row);
                }
                return this.row(row);
            }

            public MultiblockJsonBuilder end() {
                return parent.push(this);
            }

            public MultiblockJsonBuilder repeat(int times) {
                if (times <= 0) throw new IllegalArgumentException("'times' must not be 0 or less in LayerBuilder#repeat!");
                for (int i = 0; i < times - 1; i++) {
                    parent.push(this);
                }
                return parent.push(this);
            }
        }
    }
}