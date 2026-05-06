package com.codex.composer.api.v1.feature.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.codex.composer.api.v1.data.GsonSerializer;

public enum BooleanConfigSerializer implements GsonSerializer<Boolean> {
    INSTANCE;

    @Override
    public Boolean read(JsonElement json) {
        return json.getAsBoolean();
    }

    @Override
    public JsonElement writeToJson(Boolean value) {
        return new JsonPrimitive(value);
    }
}

