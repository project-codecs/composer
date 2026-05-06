package com.codex.composer.api.v1.feature.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.codex.composer.api.v1.data.GsonSerializer;

public enum DoubleConfigSerializer implements GsonSerializer<Double> {
    INSTANCE;

    @Override
    public Double read(JsonElement json) {
        return json.getAsDouble();
    }

    @Override
    public JsonElement writeToJson(Double value) {
        return new JsonPrimitive(value);
    }
}
