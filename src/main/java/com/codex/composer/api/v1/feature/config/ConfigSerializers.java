package com.codex.composer.api.v1.feature.config;

import com.codex.composer.api.v1.data.EnumSerializer;
import com.codex.composer.api.v1.data.GsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry of default config serializers.
 * Registering custom ones is possible, tho I don't know why you would need it.
 */
public final class ConfigSerializers {
    private static final Map<String, GsonSerializer<?>> REGISTRY = new HashMap<>();

    static {
        REGISTRY.put("boolean", BooleanConfigSerializer.INSTANCE);
        REGISTRY.put("int", IntConfigSerializer.INSTANCE);
        REGISTRY.put("double", DoubleConfigSerializer.INSTANCE);
        REGISTRY.put("string", StringConfigSerializer.INSTANCE);
    }

    public static <T> void register(String key, GsonSerializer<T> serializer) {
        REGISTRY.put(key, serializer);
    }

    @SuppressWarnings("unchecked")
    public static <T> EnumSerializer<T> get(String key) {
        return (EnumSerializer<T>) REGISTRY.get(key);
    }
}
