package com.codex.composer.api.v1.overlay.impl;

import com.codex.ambarella.api.v1.util.math.Vec2d;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;

import java.util.Locale;

//? if minecraft: >=1.21
import net.minecraft.client.render.RenderTickCounter;

public abstract class Overlay {
    public Anchor anchor;

    protected Overlay(Anchor anchor) {
        this.anchor = anchor;
    }

    public abstract void render(DrawContext context, /*? if minecraft: <=1.20.6 {*//*float*//*? } else {*/RenderTickCounter/*? }*/ f);
    public abstract void tick();
    public abstract boolean shouldRemove();
    public abstract Identifier getId();

    public boolean blockedBy(Overlay other) {
        return false;
    }

    public enum Anchor implements StringIdentifiable {
        TOP_LEFT(0f, 0f, 0, 0, 1, 1),
        TOP(0.5f, 0f, -0.5f, 0, 0, 1),
        TOP_RIGHT(1f, 0f, -1f, 0, -1, 1),
        RIGHT(1f, 0.5f, -1f, -0.5f, -1, 0),
        BOTTOM_RIGHT(1f, 1f, -1f, -1f, -1, -1),
        BOTTOM(0.5f, 1f, -0.5f, -1f, -1, -1),
        BOTTOM_LEFT(0f, 1f, 0f, -1f, 1, -1),
        LEFT(0f, 0.5f, 0, -0.5f, 1, 0),
        CENTER_UP(0.5f, 0.5f, -0.5f, -0.5f, 0, 0),
        CENTER_DOWN(0.5f, 0.5f, -0.5f, -0.5f, 0, 0),
        CENTER_LEFT(0.5f, 0.5f, -0.5f, -0.5f, 0, 0),
        CENTER_RIGHT(0.5f, 0.5f, -0.5f, -0.5f, 0, 0);

        public final float xFactor;
        public final float yFactor;
        public final float widthOffset;
        public final float heightOffset;
        public final float xPaddingFactor;
        public final float yPaddingFactor;

        public static final com.mojang.serialization.Codec<Anchor> CODEC = StringIdentifiable.createCodec(Anchor::values);

        Anchor(float xFactor, float yFactor, float widthOffset, float heightOffset, float xPaddingFactor, float yPaddingFactor) {
            this.xFactor = xFactor;
            this.yFactor = yFactor;
            this.widthOffset = widthOffset;
            this.heightOffset = heightOffset;
            this.xPaddingFactor = xPaddingFactor;
            this.yPaddingFactor = yPaddingFactor;
        }

        public int x() {
            return Math.round(MinecraftClient.getInstance().getWindow().getScaledWidth() * xFactor);
        }

        public int y() {
            return Math.round(MinecraftClient.getInstance().getWindow().getScaledHeight() * yFactor);
        }

        public int xOffset(Vec2d size) {
            return Math.toIntExact(Math.round(size.x * widthOffset));
        }

        public int yOffset(Vec2d size) {
            return Math.toIntExact(Math.round(size.y * heightOffset));
        }

        public int padX(Vec2d padding) {
            return Math.toIntExact(Math.round(padding.x * xPaddingFactor));
        }

        public int padY(Vec2d padding) {
            return Math.toIntExact(Math.round(padding.y * yPaddingFactor));
        }

        @Override
        public String asString() {
            return name().toLowerCase(Locale.ROOT);
        }
    }
}
