package com.codex.composer.api.v1.feature.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.codex.composer.api.v1.data.GsonSerializer;

public enum IntConfigSerializer implements GsonSerializer<Integer> {
    INSTANCE;

    @Override
    public Integer read(JsonElement json) {
        return json.getAsInt();
    }

    @Override
    public JsonElement writeToJson(Integer value) {
        return new JsonPrimitive(value);
    }
}
