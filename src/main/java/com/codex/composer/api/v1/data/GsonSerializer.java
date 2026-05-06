package com.codex.composer.api.v1.data;

import com.google.gson.JsonElement;

public interface GsonSerializer<T> {
    T read(JsonElement json);
    JsonElement writeToJson(T value);
}
