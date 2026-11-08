package com.codex.composer.api.v1.events;

import com.codex.composer.api.v1.overlay.impl.Overlay;
import com.codex.composer.api.v1.util.misc.PacketSerializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.registry.Registry;

public class ComposerLoadedEvent {
    @Environment(EnvType.CLIENT)
    public static final Event<ComposerClientLoad> CLIENT = EventFactory.createArrayBacked(ComposerClientLoad.class, callbacks -> container -> {
        for (ComposerClientLoad callback : callbacks) callback.run(container);
    });

    public static final Event<ComposerCommonLoad> COMMON = EventFactory.createArrayBacked(ComposerCommonLoad.class, callbacks -> (container, overlays) -> {
        for (ComposerCommonLoad callback : callbacks) callback.run(container, overlays);
    });

    @FunctionalInterface
    public interface ComposerCommonLoad {
        void run(ModContainer container, Registry<PacketSerializer<? extends Overlay>> overlaySerializerRegistry);
    }

    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface ComposerClientLoad {
        void run(ModContainer container);
    }
}
