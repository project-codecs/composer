package com.codex.composer.api.v1.util.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("unused")
public interface JsonUtils {
    static void ifBoolPresent(JsonObject json, String key, Consumer<Boolean> mapper) {
        getOptionalBool(json, key).ifPresent(mapper);
    }

    static void ifBool(JsonElement element, Consumer<Boolean> mapper) {
        if (element instanceof JsonPrimitive p && p.isBoolean()) mapper.accept(p.getAsBoolean());
    }

    static Optional<Boolean> getOptionalBool(JsonObject json, String key) {
        return Optional.ofNullable(getBool(json, key, null));
    }

    static Boolean getBool(JsonObject json, String key) {
        return getBool(json, key, false);
    }

    static Boolean getBool(JsonObject json, String key, Boolean def) {
        if (json.has(key) && json.get(key) instanceof JsonPrimitive p && p.isBoolean()) return p.getAsBoolean();
        return def;
    }

    static void ifStrPresent(JsonObject json, String key, Consumer<String> mapper) {
        getOptionalStr(json, key).ifPresent(mapper);
    }

    static void ifStr(JsonElement element, Consumer<String> mapper) {
        if (element instanceof JsonPrimitive p && p.isString()) mapper.accept(p.getAsString());
    }

    static Optional<String> getOptionalStr(JsonObject json, String key) {
        return Optional.ofNullable(getStr(json, key, null));
    }

    static String getStr(JsonObject json, String key) {
        return getStr(json, key, "");
    }

    static String getStr(JsonObject json, String key, String def) {
        if (json.has(key) && json.get(key) instanceof JsonPrimitive p && p.isString()) return p.getAsString();
        return def;
    }

    static List<String> asStringList(JsonElement element) {
        List<String> out = new ArrayList<>();
        if (element instanceof JsonArray array) array.forEach(e -> ifStr(e, out::add));
        else ifStr(element, out::add);
        return out;
    }

    static void ifIntPresent(JsonObject json, String key, Consumer<Integer> mapper) {
        getOptionalInt(json, key).ifPresent(mapper);
    }

    static void ifInt(JsonElement element, Consumer<Integer> mapper) {
        if (element instanceof JsonPrimitive p && p.isNumber()) mapper.accept(p.getAsInt());
    }

    static Optional<Integer> getOptionalInt(JsonObject json, String key) {
        return Optional.ofNullable(getInt(json, key, null));
    }

    static Integer getInt(JsonObject json, String key) {
        return getInt(json, key, 0);
    }

    static Integer getInt(JsonObject json, String key, Integer def) {
        if (json.has(key) && json.get(key) instanceof JsonPrimitive p && p.isNumber()) return p.getAsInt();
        return def;
    }

    static void ifDoublePresent(JsonObject json, String key, Consumer<Double> mapper) {
        getOptionalDouble(json, key).ifPresent(mapper);
    }

    static void ifDouble(JsonElement element, Consumer<Double> mapper) {
        if (element instanceof JsonPrimitive p && p.isNumber()) mapper.accept(p.getAsDouble());
    }

    static Optional<Double> getOptionalDouble(JsonObject json, String key) {
        return Optional.ofNullable(getDouble(json, key, null));
    }

    static Double getDouble(JsonObject json, String key) {
        return getDouble(json, key, 0d);
    }

    static Double getDouble(JsonObject json, String key, Double def) {
        if (json.has(key) && json.get(key) instanceof JsonPrimitive p && p.isNumber()) return p.getAsDouble();
        return def;
    }

    static void ifFloatPresent(JsonObject json, String key, Consumer<Float> mapper) {
        getOptionalFloat(json, key).ifPresent(mapper);
    }

    static void ifFloat(JsonElement element, Consumer<Float> mapper) {
        if (element instanceof JsonPrimitive p && p.isNumber()) mapper.accept(p.getAsFloat());
    }

    static Optional<Float> getOptionalFloat(JsonObject json, String key) {
        return Optional.ofNullable(getFloat(json, key, null));
    }

    static Float getFloat(JsonObject json, String key) {
        return getFloat(json, key, 0f);
    }

    static Float getFloat(JsonObject json, String key, Float def) {
        if (json.has(key) && json.get(key) instanceof JsonPrimitive p && p.isNumber()) return p.getAsFloat();
        return def;
    }

    static void ifPresent(JsonObject json, String key, Consumer<JsonObject> mapper) {
        getOptional(json, key).ifPresent(mapper);
    }

    static Optional<JsonObject> getOptional(JsonObject json, String key) {
        return Optional.ofNullable(get(json, key, null));
    }

    static JsonObject get(JsonObject json, String key) {
        return get(json, key, new JsonObject());
    }

    static JsonObject get(JsonObject json, String key, JsonObject def) {
        if (json.has(key) && json.get(key) instanceof JsonObject o) return o;
        return def;
    }

    static void ifArray(JsonObject json, String key, Consumer<JsonArray> mapper) {
        getOptionalArray(json, key).ifPresent(mapper);
    }

    static Optional<JsonArray> getOptionalArray(JsonObject json, String key) {
        return Optional.ofNullable(getArray(json, key, null));
    }

    static JsonArray getArray(JsonObject json, String key) {
        return getArray(json, key, new JsonArray());
    }

    static JsonArray getArray(JsonObject json, String key, JsonArray def) {
        if (json.has(key) && json.get(key) instanceof JsonArray a) return a;
        return def;
    }

    static <T extends Enum<T> & EnumSerializable<T>> T getEnum(JsonObject json, String key, T def) {
        String str = getStr(json, key, null);
        if (str == null) return def;
        T value = def.fromString(str);
        return value != null ? value : def;
    }

    static <T extends Enum<T> & EnumSerializable<T>> void ifEnum(JsonElement element, Function<String, T> fromString, Consumer<T> mapper) {
        ifStr(element, str -> {
            T value = fromString.apply(str);
            if (value != null) mapper.accept(value);
        });
    }

    interface EnumSerializable<T extends Enum<T> & EnumSerializable<T>> {
        String asString();

        @SuppressWarnings("unchecked")
        default T fromString(String string) {
            T self = (T) this;
            for (T value : self.getDeclaringClass().getEnumConstants()) {
                if (Objects.equals(value.asString(), string)) return value;
            }
            return null;
        }
    }

    static void ifObject(JsonElement element, Consumer<JsonObject> mapper) {
        if (element != null && element.isJsonObject()) mapper.accept(element.getAsJsonObject());
    }
}