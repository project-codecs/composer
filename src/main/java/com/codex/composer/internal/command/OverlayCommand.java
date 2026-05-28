package com.codex.composer.internal.command;

import com.codex.ambarella.api.v1.util.misc.PredicateVoid;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import com.codex.composer.api.v1.util.command.ComposerCommand;
import com.codex.composer.api.v1.overlay.impl.AnimatedOverlay;
import com.codex.composer.api.v1.overlay.impl.Overlay;
import com.codex.composer.api.v1.util.command.AnimatedOverlayAnimationArgumentType;
import com.codex.composer.api.v1.util.command.ColorArgumentType;
import com.codex.composer.api.v1.util.command.OverlayAnchorArgumentType;
import com.codex.composer.internal.networking.ClearOverlaysPayload;

import static net.minecraft.server.command.CommandManager.*;
import static com.codex.composer.api.v1.overlay.Overlays.*;

public class OverlayCommand extends ComposerCommand {
    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(
                literal("overlay")
                        .then(constructClear())
                        .then(constructText())
                        .then(constructTexture())
        );

        dispatcher.register(
                literal("clear_overlays")
                        .requires(PredicateVoid::always)
                        .then(
                                argument("credits", BoolArgumentType.bool()).then(
                                        argument("poem", BoolArgumentType.bool()).executes(ctx -> {
                                            ServerPlayerEntity player = ctx.getSource().getPlayerOrThrow();
                                            boolean visible = ctx.getArgument("credits", Boolean.class);
                                            boolean queue = ctx.getArgument("poem", Boolean.class);
                                            ServerPlayNetworking.send(player, new ClearOverlaysPayload(visible, queue));
                                            if (visible && queue) return success(ctx, Text.translatable("composer.overlay.cleared_all"));
                                            else if (visible) return success(ctx, Text.translatable("composer.overlay.cleared_all_visible"));
                                            else return success(ctx, Text.translatable("composer.overlay.cleared_all_queued"));
                                        })
                                )
                        )
        );
    }

    private ArgumentBuilder<ServerCommandSource, ?> who() {
        return argument("who", EntityArgumentType.players());
    }

    private ArgumentBuilder<ServerCommandSource, ?> constructClear() {
        return literal("clear").then(
                who().then(
                        argument("credits", BoolArgumentType.bool()).then(
                                argument("poem", BoolArgumentType.bool()).executes(this::clear)
                        )
                )
        );
    }

    private ArgumentBuilder<ServerCommandSource, ?> constructText() {
        return literal("text")
                .then(who()
                        .then(argument("anchor", OverlayAnchorArgumentType.all())
                                .then(argument("animations", AnimatedOverlayAnimationArgumentType.all())
                                        .then(argument("text", StringArgumentType.string())
                                                .then(argument("color", ColorArgumentType.rgb())
                                                        .then(argument("scale", DoubleArgumentType.doubleArg())
                                                                .then(argument("shadow", BoolArgumentType.bool())
                                                                        .then(argument("duration", IntegerArgumentType.integer())
                                                                                .executes(this::textScaleDuration)
                                                                        )
                                                                        .then(argument("enter", IntegerArgumentType.integer())
                                                                                .then(argument("hold", IntegerArgumentType.integer())
                                                                                        .then(argument("exit", IntegerArgumentType.integer())
                                                                                                .executes(this::textScaleFade)
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                        .then(argument("shadow", BoolArgumentType.bool())
                                                                .then(argument("duration", IntegerArgumentType.integer())
                                                                        .executes(this::textDuration)
                                                                )
                                                                .then(argument("enter", IntegerArgumentType.integer())
                                                                        .then(argument("hold", IntegerArgumentType.integer())
                                                                                .then(argument("exit", IntegerArgumentType.integer())
                                                                                        .executes(this::textFade)
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                );
    }

    private ArgumentBuilder<ServerCommandSource, ?> constructTexture() {
        return literal("texture")
                .then(who()
                        .then(argument("texture", IdentifierArgumentType.identifier())
                                .then(argument("anchor", OverlayAnchorArgumentType.all())
                                        .then(argument("animations", AnimatedOverlayAnimationArgumentType.all())
                                                .then(argument("scale", DoubleArgumentType.doubleArg())
                                                        .then(argument("duration", IntegerArgumentType.integer())
                                                                .executes(this::textureScaleDuration)
                                                        )
                                                        .then(argument("enter", IntegerArgumentType.integer())
                                                                .then(argument("hold", IntegerArgumentType.integer())
                                                                        .then(argument("exit", IntegerArgumentType.integer())
                                                                                .executes(this::textureScaleFade)
                                                                        )
                                                                )
                                                        )
                                                )
                                                .then(argument("duration", IntegerArgumentType.integer())
                                                        .executes(this::textureDuration)
                                                )
                                                .then(argument("enter", IntegerArgumentType.integer())
                                                        .then(argument("hold", IntegerArgumentType.integer())
                                                                .then(argument("exit", IntegerArgumentType.integer())
                                                                        .executes(this::textureFade)
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                );
    }

    private int clear(CommandContext<ServerCommandSource> ctx) {
        try {
            ServerPlayerEntity player = ctx.getSource().getPlayerOrThrow();
            boolean visible = ctx.getArgument("credits", Boolean.class);
            boolean queue = ctx.getArgument("poem", Boolean.class);
            ServerPlayNetworking.send(player, new ClearOverlaysPayload(visible, queue));
            if (visible && queue) return success(ctx, Text.translatable("composer.overlay.cleared_all_for", player.getName()));
            else if (visible) return success(ctx, Text.translatable("composer.overlay.cleared_all_visible_for", player.getName()));
            else return success(ctx, Text.translatable("composer.overlay.cleared_all_queued_for", player.getName()));
        } catch (Exception e) {
            return error(ctx, Text.translatable("composer.command.exception.player_not_found"));
        }
    }

    private int textureScaleDuration(CommandContext<ServerCommandSource> ctx) {
        try {
            var players = EntityArgumentType.getPlayers(ctx, "who");
            Identifier texture = ctx.getArgument("texture", Identifier.class);
            if (texture == null) throw new Exception();

            Overlay.Anchor anchor = OverlayAnchorArgumentType.getAnchor(ctx, "anchor");
            AnimatedOverlay.Animation animation = AnimatedOverlayAnimationArgumentType.getAnchor(ctx, "animations");
            double scale = ctx.getArgument("scale", double.class);
            int duration = ctx.getArgument("duration", int.class);

            players.forEach(player ->
                    sendTextured(player, texture, anchor, animation, scale, duration)
            );

            return feedback(ctx, Text.translatable(
                    "composer.overlay.sent_texture_scale_duration",
                    texture.toString(),
                    scale,
                    duration
            ));
        } catch (CommandSyntaxException e) {
            return error(ctx, Text.translatable("composer.command.exception.no_players_found"));
        } catch (Exception e) {
            return error(ctx, Text.translatable("composer.overlay.invalid_texture"));
        }
    }

    private int textureScaleFade(CommandContext<ServerCommandSource> ctx) {
        try {
            var players = EntityArgumentType.getPlayers(ctx, "who");
            Identifier texture = ctx.getArgument("texture", Identifier.class);
            if (texture == null) throw new Exception();

            Overlay.Anchor anchor = OverlayAnchorArgumentType.getAnchor(ctx, "anchor");
            AnimatedOverlay.Animation animation = AnimatedOverlayAnimationArgumentType.getAnchor(ctx, "animations");
            double scale = ctx.getArgument("scale", double.class);
            int enter = ctx.getArgument("enter", int.class);
            int hold = ctx.getArgument("hold", int.class);
            int exit = ctx.getArgument("exit", int.class);

            players.forEach(player ->
                    sendTextured(player, texture, anchor, animation, scale, enter, hold, exit)
            );

            return feedback(ctx, Text.translatable(
                    "composer.overlay.sent_texture_scale_fade",
                    texture.toString(),
                    scale,
                    enter,
                    hold,
                    exit
            ));
        } catch (CommandSyntaxException e) {
            return error(ctx, Text.translatable("composer.command.exception.no_players_found"));
        } catch (Exception e) {
            return error(ctx, Text.translatable("composer.overlay.invalid_texture"));
        }
    }

    private int textureDuration(CommandContext<ServerCommandSource> ctx) {
        try {
            var players = EntityArgumentType.getPlayers(ctx, "who");
            Identifier texture = ctx.getArgument("texture", Identifier.class);
            if (texture == null) throw new Exception();

            Overlay.Anchor anchor = OverlayAnchorArgumentType.getAnchor(ctx, "anchor");
            AnimatedOverlay.Animation animation = AnimatedOverlayAnimationArgumentType.getAnchor(ctx, "animations");
            int duration = ctx.getArgument("duration", int.class);

            players.forEach(player ->
                    sendTextured(player, texture, anchor, animation, duration)
            );

            return feedback(ctx, Text.translatable(
                    "composer.overlay.sent_texture_duration",
                    texture.toString(),
                    duration
            ));
        } catch (CommandSyntaxException e) {
            return error(ctx, Text.translatable("composer.command.exception.no_players_found"));
        } catch (Exception e) {
            return error(ctx, Text.translatable("composer.overlay.invalid_texture"));
        }
    }

    private int textureFade(CommandContext<ServerCommandSource> ctx) {
        try {
            var players = EntityArgumentType.getPlayers(ctx, "who");
            Identifier texture = ctx.getArgument("texture", Identifier.class);
            if (texture == null) throw new Exception();

            Overlay.Anchor anchor = OverlayAnchorArgumentType.getAnchor(ctx, "anchor");
            AnimatedOverlay.Animation animation = AnimatedOverlayAnimationArgumentType.getAnchor(ctx, "animations");
            int enter = ctx.getArgument("enter", int.class);
            int hold = ctx.getArgument("hold", int.class);
            int exit = ctx.getArgument("exit", int.class);

            players.forEach(player -> sendTextured(player, texture, anchor, animation, enter, hold, exit));

            return feedback(ctx, Text.translatable(
                    "composer.overlay.sent_texture_fade",
                    texture.toString(),
                    enter,
                    hold,
                    exit
            ));
        } catch (CommandSyntaxException e) {
            return error(ctx, Text.translatable("composer.command.exception.no_players_found"));
        } catch (Exception e) {
            return error(ctx, Text.translatable("composer.overlay.invalid_texture"));
        }
    }

    private int textScaleDuration(CommandContext<ServerCommandSource> ctx) {
        try {
            var players = EntityArgumentType.getPlayers(ctx, "who");
            Overlay.Anchor anchor = OverlayAnchorArgumentType.getAnchor(ctx, "anchor");
            AnimatedOverlay.Animation animation = AnimatedOverlayAnimationArgumentType.getAnchor(ctx, "animations");
            String text = StringArgumentType.getString(ctx, "text");
            int color = ctx.getArgument("color", Integer.class);
            double scale = ctx.getArgument("scale", double.class);
            boolean shadow = BoolArgumentType.getBool(ctx, "shadow");
            int duration = ctx.getArgument("duration", int.class);

            players.forEach(player -> sendText(player, anchor, animation, text, color, scale, shadow, duration));

            return feedback(ctx, Text.translatable(
                    "composer.overlay.sent_text_scale_duration",
                    text,
                    String.format("#%06X", color & 0xFFFFFF),
                    scale,
                    duration
            ));
        } catch (Exception e) {
            return error(ctx, Text.translatable("composer.command.exception.no_players_found"));
        }
    }

    private int textScaleFade(CommandContext<ServerCommandSource> ctx) {
        try {
            var players = EntityArgumentType.getPlayers(ctx, "who");
            Overlay.Anchor anchor = OverlayAnchorArgumentType.getAnchor(ctx, "anchor");
            AnimatedOverlay.Animation animation = AnimatedOverlayAnimationArgumentType.getAnchor(ctx, "animations");
            String text = StringArgumentType.getString(ctx, "text");
            int color = ctx.getArgument("color", Integer.class);
            double scale = ctx.getArgument("scale", double.class);
            boolean shadow = BoolArgumentType.getBool(ctx, "shadow");
            int enter = ctx.getArgument("enter", int.class);
            int hold = ctx.getArgument("hold", int.class);
            int exit = ctx.getArgument("exit", int.class);

            players.forEach(player -> sendText(player, anchor, animation, text, color, scale, shadow, enter, hold, exit));

            return feedback(ctx, Text.translatable(
                    "composer.overlay.sent_text_scale_fade",
                    text,
                    String.format("#%06X", color & 0xFFFFFF),
                    scale,
                    enter,
                    hold,
                    exit
            ));
        } catch (Exception e) {
            return error(ctx, Text.translatable("composer.command.exception.no_players_found"));
        }
    }

    private int textDuration(CommandContext<ServerCommandSource> ctx) {
        try {
            var players = EntityArgumentType.getPlayers(ctx, "who");
            Overlay.Anchor anchor = OverlayAnchorArgumentType.getAnchor(ctx, "anchor");
            AnimatedOverlay.Animation animation = AnimatedOverlayAnimationArgumentType.getAnchor(ctx, "animations");
            String text = StringArgumentType.getString(ctx, "text");
            int color = ctx.getArgument("color", Integer.class);
            boolean shadow = BoolArgumentType.getBool(ctx, "shadow");
            int duration = ctx.getArgument("duration", int.class);

            players.forEach(player -> sendText(player, anchor, animation, text, color, 1.0, shadow, duration));

            return feedback(ctx, Text.translatable(
                    "composer.overlay.sent_text_duration",
                    text,
                    String.format("#%06X", color & 0xFFFFFF),
                    duration
            ));
        } catch (Exception e) {
            return error(ctx, Text.translatable("composer.command.exception.no_players_found"));
        }
    }

    private int textFade(CommandContext<ServerCommandSource> ctx) {
        try {
            var players = EntityArgumentType.getPlayers(ctx, "who");
            Overlay.Anchor anchor = OverlayAnchorArgumentType.getAnchor(ctx, "anchor");
            AnimatedOverlay.Animation animation = AnimatedOverlayAnimationArgumentType.getAnchor(ctx, "animations");
            String text = StringArgumentType.getString(ctx, "text");
            int color = ctx.getArgument("color", Integer.class);
            boolean shadow = BoolArgumentType.getBool(ctx, "shadow");
            int enter = ctx.getArgument("enter", int.class);
            int hold = ctx.getArgument("hold", int.class);
            int exit = ctx.getArgument("exit", int.class);

            players.forEach(player -> sendText(player, anchor, animation, text, color, 1.0, shadow, enter, hold, exit));

            return feedback(ctx, Text.translatable(
                    "composer.overlay.sent_text_fade",
                    text,
                    String.format("#%06X", color & 0xFFFFFF),
                    enter,
                    hold,
                    exit
            ));
        } catch (Exception e) {
            return error(ctx, Text.translatable("composer.command.exception.no_players_found"));
        }
    }

    private ServerPlayerEntity who(CommandContext<ServerCommandSource> ctx) {
        return ctx.getSource().getPlayer();
    }

    @Override
    protected Text buildPrefix() {
        return wrapBrackets(createGradient(Text.translatable("composer.overlay.prefix"), 0xffaa00, 0xffff55));
    }
}
