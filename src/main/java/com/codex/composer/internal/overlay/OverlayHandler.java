package com.codex.composer.internal.overlay;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import org.jetbrains.annotations.ApiStatus;
import com.codex.composer.api.v1.overlay.OverlayManager;
import com.codex.composer.api.v1.overlay.impl.Overlay;

//? if minecraft: >=1.21
import net.minecraft.client.render.RenderTickCounter;

@ApiStatus.Internal
public class OverlayHandler {
    @ApiStatus.Internal
    public static void tick(MinecraftClient ignored) {
        OverlayManager man = OverlayManager.getInstance();

        man.getVisible().forEach(Overlay::tick);
        man.getVisible().removeIf(Overlay::shouldRemove);

        Overlay next;
        while ((next = man.getQueue().peek()) != null && man.getVisible().stream().noneMatch(next::blockedBy)) {
            man.getVisible().add(man.getQueue().poll());
        }
    }

    @ApiStatus.Internal
    public static void render(DrawContext context, /*? if minecraft: <=1.20.6 {*//*float*//*? } else {*/RenderTickCounter/*? }*/ f) {
        OverlayManager man = OverlayManager.getInstance();
        man.getVisible().forEach(overlay -> overlay.render(context, f));
    }
}
