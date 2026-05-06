package com.codex.composer.api.v1.feature.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.codex.composer.api.v1.data.GsonSerializer;

public enum StringConfigSerializer implements GsonSerializer<String> {
    INSTANCE;

    @Override
    public String read(JsonElement json) {
        return json.getAsString();
    }

    @Override
    public JsonElement writeToJson(String value) {
        return new JsonPrimitive(value);
    }
}
