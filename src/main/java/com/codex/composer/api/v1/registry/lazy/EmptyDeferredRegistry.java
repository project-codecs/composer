package com.codex.composer.api.v1.registry.lazy;

import net.minecraft.util.Identifier;

public class EmptyDeferredRegistry {
    protected final String modId;

    public EmptyDeferredRegistry(String modId) {
        this.modId = modId;
    }

    protected Identifier id(String path) {
        return Identifier.of(modId, path);
    }
}
