package com.codex.composer.api.v1.util.tags;

import com.codex.composer.api.v1.registry.lazy.EmptyDeferredRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;

public class DeferredTagBuilder<T> extends EmptyDeferredRegistry {
    private final RegistryKey<Registry<T>> registry;

    public DeferredTagBuilder(String modId, RegistryKey<Registry<T>> registry) {
        super(modId);
        this.registry = registry;
    }

    public TagKey<T> of(String name) {
        return TagKey.of(registry, id(name));
    }
}
