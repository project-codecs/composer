package com.codex.composer.internal.data.loader.json;

import com.codex.composer.api.v1.util.misc.CubeMatrix;
import com.codex.composer.internal.Composer;
import com.google.gson.*;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3i;
import org.joml.Vector3i;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.*;

public class MultiblockJsonLoader {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Identifier.class, new IdentifierDeserializer())
            .registerTypeAdapter(Vec3i.class, new Vec3iDeserializer())
            .create();

    public static ParsedMultiblock parse(Identifier fileId, Resource resource) {
        try (var reader = new InputStreamReader(resource.getInputStream())) {
            RawMultiblock raw = GSON.fromJson(reader, RawMultiblock.class);

            if (raw == null) {
                Composer.LOGGER.warn("Multiblock file {} is empty or invalid!", fileId);
                return null;
            }

            if (raw.id == null) {
                Composer.LOGGER.warn("Invalid identifier for multiblock {}!", fileId.toString());
                return null;
            }

            return process(raw);

        } catch (IOException e) {
            Composer.LOGGER.warn("Failed to read multiblock file {}!", fileId);
            return null;
        }
    }

    private static ParsedMultiblock process(RawMultiblock raw) {
        CubeMatrix<Key> matrix = new CubeMatrix<>();

        for (int y = 0; y < raw.layers.size(); y++) {
            List<String> layer = raw.layers.get(y);

            for (int z = 0; z < layer.size(); z++) {
                String row = layer.get(z);
                char[] entries = row.toCharArray();

                for (int x = 0; x < entries.length; x++) {
                    char c = entries[x];
                    JsonPrimitive value = raw.pattern.get(String.valueOf(c));
                    matrix.set(new Vector3i(x, y, layer.size() - 1 - z), new Key(c, value));
                }
            }
        }

        List<Key> keys = raw.pattern.entrySet().stream()
                .map(entry -> new Key(entry.getKey().charAt(0), entry.getValue().getAsJsonPrimitive()))
                .toList();

        return new ParsedMultiblock(raw.id, matrix, keys, raw.controller_pos);
    }

    public static class ParsedMultiblock {
        private final Identifier id;
        private final CubeMatrix<Key> matrix;
        private final List<Key> keys;
        private final Vec3i controllerPos;

        public ParsedMultiblock(Identifier id, CubeMatrix<Key> matrix, List<Key> keys, Vec3i controllerPos) {
            this.id = id;
            this.matrix = matrix;
            this.keys = keys;
            this.controllerPos = controllerPos;
        }

        public Identifier getId() { return id; }
        public CubeMatrix<Key> getMatrix() { return matrix; }
        public List<Key> getKeys() { return keys; }
        public Vec3i getControllerPos() { return controllerPos; }
    }

    public record Key(char symbol, JsonPrimitive value) {}

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static class RawMultiblock {
        Identifier id;
        List<List<String>> layers;
        Map<String, JsonPrimitive> pattern;
        Vec3i controller_pos;
    }

    private static class IdentifierDeserializer implements JsonDeserializer<Identifier> {
        @Override
        public Identifier deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return Identifier.tryParse(json.getAsString());
        }
    }

    private static class Vec3iDeserializer implements JsonDeserializer<Vec3i> {
        @Override
        public Vec3i deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = json.getAsJsonObject();
            return new Vec3i(object.get("x").getAsInt(), object.get("y").getAsInt(), object.get("z").getAsInt());
        }
    }
}