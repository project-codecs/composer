package com.codex.composer.internal.registry;

import com.codex.composer.api.v1.util.command.AnimatedOverlayAnimationArgumentType;
import com.codex.composer.api.v1.util.command.ColorArgumentType;
import com.codex.composer.api.v1.util.command.OverlayAnchorArgumentType;
import com.codex.composer.api.v1.util.command.ToastCornerArgumentType;
import com.codex.composer.internal.Composer;
import com.codex.composer.internal.command.CreditsCommand;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.util.Identifier;

public class ModArgumentTypes {
    public static final Identifier COLOR = Composer.identify("color");
    public static final Identifier TOAST_CORNER = Composer.identify("toast_corner");
    public static final Identifier OVERLAY_ANCHOR = Composer.identify("overlay_anchor");
    public static final Identifier ANIMATED_OVERLAY_ANIMATION = Composer.identify("animated_overlay_animation");
    public static final Identifier CREDITS_TYPE = Composer.identify("credits_type");

    public static void initialize() {
        ArgumentTypeRegistry.registerArgumentType(COLOR, ColorArgumentType.class, ColorArgumentType.Serializer.INSTANCE);
        ArgumentTypeRegistry.registerArgumentType(TOAST_CORNER, ToastCornerArgumentType.class, ConstantArgumentSerializer.of(ToastCornerArgumentType::corners));
        ArgumentTypeRegistry.registerArgumentType(OVERLAY_ANCHOR, OverlayAnchorArgumentType.class, ConstantArgumentSerializer.of(OverlayAnchorArgumentType::all));
        ArgumentTypeRegistry.registerArgumentType(ANIMATED_OVERLAY_ANIMATION, AnimatedOverlayAnimationArgumentType.class, ConstantArgumentSerializer.of(AnimatedOverlayAnimationArgumentType::all));
        ArgumentTypeRegistry.registerArgumentType(CREDITS_TYPE, CreditsCommand.CreditsTypeArgumentType.class, ConstantArgumentSerializer.of(CreditsCommand.CreditsTypeArgumentType::create));
    }
}
