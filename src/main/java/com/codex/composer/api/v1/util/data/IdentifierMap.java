package com.codex.composer.api.v1.util.data;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public final class IdentifierMap<T> extends HashMap<Identifier, T> {
    private final String modId;
    private final Boolean hasModId;

    public IdentifierMap() {
        this(null);
    }

    public IdentifierMap(String modId) {
        super();
        this.modId = modId;
        this.hasModId = modId != null && !modId.isBlank();
    }

    public T put(String key, T value) {
        if (hasModId) throw new IllegalArgumentException("Cannot use put(String, T) on an IdentifierMap with no modId provided!");
        return put(Identifier.of(modId, key), value);
    }

    public IdentifierMap<T> withModId(String modId) {
        boolean with = modId != null && !modId.isBlank();
        IdentifierMap<T> target = with ? new IdentifierMap<>(modId) : new IdentifierMap<>();
        target.putAll(this);
        return target;
    }

    public void registerTo(BiConsumer<Identifier, T> consumer) {
        for (Entry<Identifier, T> entry : entrySet()) {
            consumer.accept(entry.getKey(), entry.getValue());
        }
    }

    public <M> void registerTo(Consumer<M> consumer, BiFunction<Identifier, T, M> middleman) {
        for (Entry<Identifier, T> entry : entrySet()) {
            consumer.accept(middleman.apply(entry.getKey(), entry.getValue()));
        }
    }

    public <M> void registerTo(BiConsumer<Identifier, M> consumer, BiFunction<Identifier, T, M> middleman) {
        for (Entry<Identifier, T> entry : entrySet()) {
            consumer.accept(entry.getKey(), middleman.apply(entry.getKey(), entry.getValue()));
        }
    }
}
