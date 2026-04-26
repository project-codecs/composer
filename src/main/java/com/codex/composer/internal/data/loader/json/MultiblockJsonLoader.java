package com.codex.composer.internal.data.loader.json;

import com.codex.composer.internal.Composer;
import com.google.gson.*;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3i;

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
        List<Layer> processedLayers = new ArrayList<>();

        for (List<String> layer : raw.layers) {
            List<List<Key>> shape = new ArrayList<>();

            for (String row : layer) {
                List<Key> keyRow = new ArrayList<>();

                for (char c : row.toCharArray()) {
                    JsonObject value = raw.pattern.get(String.valueOf(c));

                    keyRow.add(new Key(c, value));
                }

                shape.add(keyRow);
            }

            processedLayers.add(new Layer(shape));
        }

        List<Key> keys = raw.pattern.entrySet().stream()
                .map(entry -> new Key(entry.getKey().charAt(0), entry.getValue()))
                .toList();

        return new ParsedMultiblock(raw.id, processedLayers, keys, raw.controller_pos);
    }

    public static class ParsedMultiblock {
        private final Identifier id;
        private final List<Layer> layers;
        private final List<Key> keys;
        private final Vec3i controllerPos;

        public ParsedMultiblock(Identifier id, List<Layer> layers, List<Key> keys, Vec3i controllerPos) {
            this.id = id;
            this.layers = layers;
            this.keys = keys;
            this.controllerPos = controllerPos;
        }

        public Identifier getId() {
            return id;
        }

        public List<Layer> getLayers() {
            return layers;
        }

        public List<Key> getKeys() {
            return keys;
        }

        public Vec3i getControllerPos() {
            return controllerPos;
        }
    }

    public record Layer(List<List<Key>> shape) {}

    public record Key(char symbol, JsonObject value) {}


    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static class RawMultiblock {
        Identifier id;
        List<List<String>> layers;
        Map<String, JsonObject> pattern;
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