package com.codex.composer.api.v1.overlay.impl;

//? if minecraft: >=1.21
import net.minecraft.client.render.RenderTickCounter;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.StringIdentifiable;
import org.joml.Vector2i;
import com.codex.composer.api.v1.util.math.Vec2;
import static com.codex.composer.api.v1.util.interpolate.CubicInterpolation.*;

import java.util.Locale;

public abstract class AnimatedOverlay extends AlignedOverlay {
    protected int enter;
    protected int hold;
    protected int exit;
    protected Animation anims;
    private final SlideDirection slide;

    private int age = 0;

    public AnimatedOverlay(Anchor anchor, Animation anims, int enter, int hold, int exit) {
        super(anchor);
        this.slide = SlideDirection.get(anchor);
        this.anims = anims;
        this.enter = Math.max(0, enter);
        this.hold = Math.max(0, hold);
        this.exit = Math.max(0, exit);
    }

    public AnimatedOverlay(Anchor anchor, Animation anims, int duration) {
        this(anchor, anims, Math.floorDiv(duration, 3), Math.floorDiv(duration, 3), Math.floorDiv(duration, 3));
    }

    @Override
    public void tick() {
        age++;
    }

    @Override
    public void render(DrawContext context, /*? if minecraft: <=1.20.6 {*//*float*//*? } else {*/RenderTickCounter/*? }*/ f) {
        if (!anims.slide) {
            super.render(context, f);
            return;
        }
        Vector2i real = calculatePosition();
        Vec2 size = getSize();
        Vec2 padding = getPadding();
        if (size == null || padding == null ) return;
        Vec2 volume = size.add(padding);

        Vec2 move = new Vec2(
                volume.x() * 1.5 * slide.xMod,
                volume.y() * 1.5 * slide.yMod
        );

        Vec2 offset;

        float time = Math.max(0f, age - 1 + /*? if minecraft: <=1.20.6 {*//*f*//*? } else {*/f.getTickDelta(false)/*? }*/);

        float slideInEnd = enter;
        float holdEnd = slideInEnd + hold;
        float slideOutEnd = holdEnd + exit;

        if (time < slideInEnd) {
            if (enter == 0) offset = new Vec2(0, 0);
            else {
                float t = fClamp01(time / enter);
                offset = move.mul(1 - easeInOutCubic(t));
            }
        } else if (time < holdEnd) {
            offset = new Vec2(0, 0);
        } else if (time < slideOutEnd) {
            if (exit == 0) offset = new Vec2(0, 0);
            else {
                float t = fClamp01((time - holdEnd) / exit);
                offset = move.mul(easeInOutCubic(t));
            }
        } else {
            offset = move;
        }

        if (offset == null) offset = new Vec2(0, 0);
        Vector2i pos = real.add(offset.x(), offset.y());
        render(context, f, pos.x, pos.y);
    }

    public float getOpacity(/*? if minecraft: <=1.20.6 {*//*float*//*? } else {*/RenderTickCounter/*? }*/ delta) {
        if (!anims.fade) return 1f;

        float time = Math.max(0f, age - 1 + /*? if minecraft: <=1.20.6 {*//*delta*//*? } else {*/delta.getTickDelta(false)/*? }*/);

        float fadeInEnd = enter;
        float holdEnd = fadeInEnd + hold;
        float fadeOutEnd = holdEnd + exit;

        if (time < fadeInEnd) {
            if (enter == 0) return 1f;

            float t = fClamp01(time / enter);
            return (float) easeInOutCubic(t);
        }

        if (time < holdEnd) {
            return 1f;
        }

        if (time < fadeOutEnd) {
            if (exit == 0) return 0f;

            float t = fClamp01((time - holdEnd) / exit);
            return 1.0f - (float) easeInOutCubic(t);
        }

        return 0f;
    }

    @Override
    public boolean shouldRemove() {
        return age >= enter + hold + exit;
    }

    public <T extends AnimatedOverlay> T readAnimation(PacketByteBuf buf) {
        this.anims = buf.readEnumConstant(Animation.class);
        this.enter = buf.readInt();
        this.hold = buf.readInt();
        this.exit = buf.readInt();
        //noinspection unchecked
        return (T) this;
    }

    public void writeAnimation(PacketByteBuf buf) {
        buf.writeEnumConstant(anims);
        buf.writeInt(enter);
        buf.writeInt(hold);
        buf.writeInt(exit);
    }

    public enum Animation implements StringIdentifiable {
        NONE(false, false),
        FADE(true, false),
        SLIDE(false, true),
        BOTH(true, true);

        public static final com.mojang.serialization.Codec<Animation> CODEC = StringIdentifiable.createCodec(Animation::values);
        public final boolean fade;
        public final boolean slide;

        Animation(boolean fade, boolean slide) {

            this.fade = fade;
            this.slide = slide;
        }

        @Override
        public String asString() {
            return name().toLowerCase(Locale.ROOT);
        }
    }

    private enum SlideDirection {
        UP(0, -1),
        DOWN(0, 1),
        LEFT(-1, 0),
        RIGHT(1, 0);

        public final float xMod;
        public final float yMod;

        SlideDirection(float xMod, float yMod) {
            this.xMod = xMod;
            this.yMod = yMod;
        }

        public static SlideDirection get(Anchor anchor) {
            return switch (anchor) {
                case CENTER_LEFT, TOP_LEFT, BOTTOM_LEFT, LEFT -> LEFT;
                case CENTER_UP, TOP -> UP;
                case CENTER_RIGHT, TOP_RIGHT, BOTTOM_RIGHT, RIGHT -> RIGHT;
                case CENTER_DOWN, BOTTOM -> DOWN;
            };
        }
    }
}
