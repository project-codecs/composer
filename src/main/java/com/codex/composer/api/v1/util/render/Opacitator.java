package com.codex.composer.api.v1.util.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.math.MathHelper;

import java.util.function.BiConsumer;

public class Opacitator {
    public static void drawWithOpacity(float opacity, boolean apply, BiConsumer<Float, Integer> call) {
        if (apply) {
            //? if minecraft: <=1.21.4
            //RenderSystem.enableBlend();
            RenderSystem.setShaderColor(1f, 1f, 1f, opacity);
        }

        call.accept(opacity, Math.round(MathHelper.clamp(opacity * 255, 0, 255)));

        if (apply) {
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
            //? if minecraft: <=1.21.4
            //RenderSystem.disableBlend();
        }
    }

    public static void drawWithOpacity(float opacity, BiConsumer<Float, Integer> call) {
        drawWithOpacity(opacity, true, call);
    }
}
