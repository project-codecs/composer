package com.codex.composer.api.v1.registry;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import com.codex.composer.api.v1.overlay.impl.Overlay;
import com.codex.composer.api.v1.util.misc.PacketSerializer;
import com.codex.composer.internal.Composer;

public class ComposerRegistryKeys {
    public static final RegistryKey<Registry<PacketSerializer<? extends Overlay>>> OVERLAY_SERIALIZERS_KEY = of("overlay_serializer");

    private static <T> RegistryKey<Registry<T>> of(String name) {
        return RegistryKey.ofRegistry(Composer.identify(name));
    }
}
