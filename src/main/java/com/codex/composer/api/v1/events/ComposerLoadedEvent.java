package com.codex.composer.api.v1.events;

import com.codex.composer.api.v1.overlay.impl.Overlay;
import com.codex.composer.api.v1.util.misc.PacketSerializer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.registry.Registry;

public class ComposerLoadedEvent {
    public static final Event<ComposerCommonLoad> EVENT = EventFactory.createArrayBacked(ComposerCommonLoad.class, callbacks -> (container, overlays) -> {
        for (ComposerCommonLoad callback : callbacks) callback.run(container, overlays);
    });

    @FunctionalInterface
    public interface ComposerCommonLoad {
        void run(ModContainer container, Registry<PacketSerializer<? extends Overlay>> overlaySerializerRegistry);
    }
}
