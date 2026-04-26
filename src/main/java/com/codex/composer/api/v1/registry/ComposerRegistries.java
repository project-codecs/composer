package com.codex.composer.api.v1.registry;

import net.minecraft.registry.Registry;
import com.codex.composer.api.v1.overlay.impl.Overlay;
import com.codex.composer.api.v1.util.misc.PacketSerializer;

public class ComposerRegistries {
    public static Registry<PacketSerializer<? extends Overlay>> OVERLAY_SERIALIZERS;
}
