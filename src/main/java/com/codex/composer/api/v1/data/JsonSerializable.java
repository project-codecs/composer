package com.codex.composer.api.v1.data;

import com.google.gson.JsonObject;

/**
 * Base interface for objects that can be serialized to JSON.
 * <p>
 * Implementing classes provide logic to write their state into an {@link JsonObject}.
 * A default method is provided to create a new {@link JsonObject} and write into it.
 * </p>
 *
 * @param <T> the concrete type of the implementing class
 */
public interface JsonSerializable<T extends JsonSerializable<T>> {

    /**
     * Writes this object's state into the provided {@link JsonObject}.
     *
     * @param object the {@link JsonObject} to write data into
     * @return the same {@link JsonObject} containing serialized data
     */
    JsonObject write(JsonObject object);

    default JsonObject write() {
        return write(new JsonObject());
    }
}
