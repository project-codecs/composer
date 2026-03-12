package com.codex.composer.api.v1.datagen.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

//? if minecraft: <=1.21.3 {
/*import net.minecraft.data.client.Model;
import net.minecraft.data.client.TextureKey;
*///? } else {
import net.minecraft.client.data.Model;
import net.minecraft.client.data.TextureKey;
import net.minecraft.client.data.ModelSupplier;
import net.minecraft.client.data.TextureMap;
//? }

public class DisplayModel extends Model {
    private final DisplayBuilder<?> display;

    public DisplayModel(DisplayBuilder<?> display) {
        super(Optional.of(display.model), Optional.empty(), TextureKey.LAYER0);
        this.display = display;
    }

    //? if minecraft: <=1.21.3 {
    /*@Override
    public JsonObject createJson(Identifier id, Map<TextureKey, Identifier> textures) {
        JsonObject json = super.createJson(id, textures);

        if (display != null && !display.isEmpty()) {
            json.add("display", display.toJson());
        }

        return json;
    }
    *///? } else {
    @Override
    public Identifier upload(Identifier id, TextureMap textures, BiConsumer<Identifier, ModelSupplier> modelCollector) {
        Map<TextureKey, Identifier> map = this.createTextureMap(textures);
        modelCollector.accept(id, () -> {
            JsonObject jsonObject = new JsonObject();
            this.parent.ifPresent((identifier) -> jsonObject.addProperty("parent", identifier.toString()));
            if (!map.isEmpty()) {
                JsonObject jsonObject2 = new JsonObject();
                map.forEach((textureKey, identifier) -> jsonObject2.addProperty(textureKey.getName(), identifier.toString()));
                jsonObject.add("textures", jsonObject2);
            }

            if (display != null && !display.isEmpty()) {
                jsonObject.add("display", display.toJson());
            }

            return jsonObject;
        });
        return id;
    }
    //? }

    public static DisplayBuilder<Builder> builder() {
        return new Builder().displayBuilder;
    }

    public static class Builder {
        private final DisplayBuilder<Builder> displayBuilder = new DisplayBuilder<>(this);

        private Builder() {

        }

        public DisplayModel build() {
            return new DisplayModel(displayBuilder);
        }
    }

    @SuppressWarnings("unused")
    public static class DisplayBuilder<T> {
        private final T parent;
        private final JsonObject display = new JsonObject();
        private Identifier model = Identifier.of("minecraft", "item/generated");

        public DisplayBuilder(T parent) {
            this.parent = parent;
        }

        @SuppressWarnings("SizeReplaceableByIsEmpty") // We disable this because this is introduced with a 1.20.6 version bump
        public boolean isEmpty() {
            return display.size() == 0;
        }

        public TransformBuilder<T> thirdPersonRight() {
            return next(DisplayKey.THIRD_PERSON_RIGHT);
        }

        public TransformBuilder<T> thirdPersonLeft() {
            return next(DisplayKey.THIRD_PERSON_LEFT);
        }

        public TransformBuilder<T> firstPersonRight() {
            return next(DisplayKey.FIRST_PERSON_RIGHT);
        }

        public TransformBuilder<T> firstPersonLeft() {
            return next(DisplayKey.FIRST_PERSON_LEFT);
        }

        public TransformBuilder<T> ground() {
            return next(DisplayKey.GROUND);
        }

        public TransformBuilder<T> gui() {
            return next(DisplayKey.GUI);
        }

        public TransformBuilder<T> head() {
            return next(DisplayKey.HEAD);
        }

        public TransformBuilder<T> fixed() {
            return next(DisplayKey.FIXED);
        }

        public TransformBuilder<T> next(DisplayKey key) {
            return new TransformBuilder<>(this, key.key);
        }

        public DisplayBuilder<T> model(Model model) {
            model.parent.ifPresent(id -> this.model = id);
            return this;
        }

        private void add(String key, JsonObject obj) {
            display.add(key, obj);
        }

        public JsonObject toJson() {
            return display;
        }

        public T build() {
            return parent;
        }
    }

    public static class TransformBuilder<T> {
        private final DisplayBuilder<T> parent;
        private final String key;

        private float[] rotation = null;
        private float[] translation = null;
        private float[] scale = null;

        public TransformBuilder(DisplayBuilder<T> parent, String key) {
            this.parent = parent;
            this.key = key;
        }

        public TransformBuilder<T> rotation(float x, float y, float z) {
            rotation = new float[]{x,y,z};
            return this;
        }

        public TransformBuilder<T> translation(float x, float y, float z) {
            translation = new float[]{x,y,z};
            return this;
        }

        public TransformBuilder<T> scale(float x, float y, float z) {
            scale = new float[]{x,y,z};
            return this;
        }

        public TransformBuilder<T> copyFrom(DisplayKey key) {
            if (!parent.display.has(key.key)) throw new IllegalStateException("Trying to copy values of state that hasn't been provided!");
            JsonObject obj = parent.display.getAsJsonObject(key.key);
            if (obj.has("rotation")) copyFrom(obj.getAsJsonArray("rotation"), rotation = new float[]{0,0,0});
            if (obj.has("translation")) copyFrom(obj.getAsJsonArray("translation"), translation = new float[]{0,0,0});
            if (obj.has("scale")) copyFrom(obj.getAsJsonArray("scale"), scale = new float[]{0,0,0});
            return this;
        }

        private void copyFrom(JsonArray json, float[] real) {
            for (int i = 0; i < 3; i++) {
                real[i] = json.get(i).getAsFloat();
            }
        }

        public DisplayBuilder<T> done() {
            JsonObject obj = new JsonObject();

            if (rotation != null) obj.add("rotation", toArray(rotation));
            if (translation != null) obj.add("translation", toArray(translation));
            if (scale != null) obj.add("scale", toArray(scale));

            if (!obj.asMap().isEmpty()) parent.add(key, obj);

            return parent;
        }

        private JsonArray toArray(float[] arr) {
            JsonArray array = new JsonArray();
            for (float f : arr) array.add(f);
            return array;
        }
    }

    public enum DisplayKey {
        THIRD_PERSON_RIGHT("thirdperson_righthand"),
        THIRD_PERSON_LEFT("thirdperson_lefthand"),
        FIRST_PERSON_RIGHT("firstperson_righthand"),
        FIRST_PERSON_LEFT("firstperson_lefthand"),
        GROUND("ground"),
        HEAD("head"),
        GUI("gui"),
        FIXED("fixed");

        private final String key;

        DisplayKey(String key) {
            this.key = key;
        }
    }
}