package com.codex.composer.internal.registry;

import org.jetbrains.annotations.ApiStatus;
import com.codex.composer.api.v1.registry.lazy.DeferredRegistryRegistry;

import static com.codex.composer.api.v1.registry.ComposerRegistryKeys.*;
import static com.codex.composer.api.v1.registry.ComposerRegistries.*;

@ApiStatus.Internal
public class ModRegistries {
    private static final DeferredRegistryRegistry REGISTRY = new DeferredRegistryRegistry();

    @ApiStatus.Internal
    public static void initialize() {
        OVERLAY_SERIALIZERS = REGISTRY.create(OVERLAY_SERIALIZERS_KEY, ModOverlaySerializers::registerAndGetDefault);

        REGISTRY.finalizeRegistries();
    }
}
