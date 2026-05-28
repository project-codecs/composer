package com.codex.composer.api.v1.overlay.impl;

import com.codex.ambarella.api.v1.util.math.Vec2d;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.DrawContext;
import org.joml.Vector2i;

//? if minecraft: >=1.21
import net.minecraft.client.render.RenderTickCounter;

public abstract class AlignedOverlay extends Overlay {
    protected AlignedOverlay(Anchor anchor) {
        super(anchor);
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT && !lateInit()) init();
    }

    public void render(DrawContext context, /*? if minecraft: <=1.20.6 {*//*float*//*? } else {*/RenderTickCounter/*? }*/ f) {
        if (shouldRemove()) return;
        Vector2i pos = calculatePosition();
        render(context, f, pos.x, pos.y);
    }

    protected final Vector2i calculatePosition() {
        if (getSize() == null || getPadding() == null) return new Vector2i(0, 0);
        return new Vector2i(anchor.x() + anchor.xOffset(getSize()) + anchor.padX(getPadding()), anchor.y() + anchor.yOffset(getSize()) + anchor.padY(getPadding()));
    }

    protected abstract void render(DrawContext context, /*? if minecraft: <=1.20.6 {*//*float*//*? } else {*/RenderTickCounter/*? }*/ f, int x, int y);
    protected abstract Vec2d getPadding();
    protected abstract Vec2d getSize();

    protected void init() {

    }

    protected boolean lateInit() {
        return false;
    }
}
