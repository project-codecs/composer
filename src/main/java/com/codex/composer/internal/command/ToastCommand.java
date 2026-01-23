package com.codex.composer.internal.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import com.codex.composer.api.v1.commands.ComposerCommand;
import com.codex.composer.api.v1.toast.ToastManager;
import com.codex.composer.api.v1.toast.impl.NotifyToast;
import com.codex.composer.api.v1.toast.impl.SimpleToast;
import com.codex.composer.api.v1.util.misc.PredicateVoid;
import com.codex.composer.api.v1.util.command.ColorArgumentType;
import com.codex.composer.api.v1.util.command.ToastCornerArgumentType;
import com.codex.composer.internal.networking.ClearToastsPayload;
import com.codex.composer.internal.networking.TriggerToastPayload;

public class ToastCommand extends ComposerCommand {
    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(
                CommandManager.literal("toast")
                        .then(CommandManager.literal("clear").then(who().executes(this::clear)))
                        .then(CommandManager.literal("simple").then(who().then(CommandManager.argument("icon_texture", StringArgumentType.string()).then(toastOptions(this::simple)))))
                        .then(CommandManager.literal("notify").then(who().then(toastOptions(this::notify))))
        );

        dispatcher.register(
                CommandManager.literal("clear_toasts")
                        .requires(PredicateVoid::always)
                        .executes(ctx -> {
                            ServerPlayerEntity player = ctx.getSource().getPlayerOrThrow();
                            ServerPlayNetworking.send(player, new ClearToastsPayload());
                            return success(ctx, Text.translatable("composer.toast.cleared_all"));
                        })
        );
    }

    private ArgumentBuilder<ServerCommandSource, ?> toastOptions(Command<ServerCommandSource> command) {
        return CommandManager.argument("where", ToastCornerArgumentType.corners()).then(
                CommandManager.argument("message", StringArgumentType.string()).then(
                        CommandManager.argument("background_color", ColorArgumentType.rgba()).then(
                                CommandManager.argument("border_color", ColorArgumentType.rgba()).executes(command)
                        )
                )
        );
    }

    private ArgumentBuilder<ServerCommandSource, ?> who() {
        return CommandManager.argument("who", EntityArgumentType.players());
    }

    private int clear(CommandContext<ServerCommandSource> context) {
        try {
            var player = EntityArgumentType.getPlayer(context, "who");
            ServerPlayNetworking.send(player, new ClearToastsPayload());
            return success(player, Text.translatable("composer.toast.cleared_for_player", player.getName()));
        } catch (Exception e) {
            return error(context, Text.translatable("composer.command.exception.player_not_found"));
        }
    }

    private int simple(CommandContext<ServerCommandSource> context) {
        try {
            var players = EntityArgumentType.getPlayers(context, "who");
            String message = StringArgumentType.getString(context, "message");
            ToastManager.Corner corner = ToastCornerArgumentType.getCorner(context, "where");
            int backgroundColor = context.getArgument("background_color", Integer.class);
            int borderColor = context.getArgument("border_color", Integer.class);
            Identifier iconTexture = Identifier.tryParse(StringArgumentType.getString(context, "icon_texture"));
            if (iconTexture == null) throw new Exception();

            players.forEach(player -> ServerPlayNetworking.send(player, new TriggerToastPayload<>(new SimpleToast(iconTexture, message, backgroundColor, borderColor), corner)));

            return feedback(context, Text.translatable(
                    "composer.toast.sent_simple",
                    message,
                    iconTexture.toString(),
                    String.format("#%06X", backgroundColor & 0xFFFFFF),
                    String.format("#%06X", borderColor & 0xFFFFFF)
            ));
        } catch (CommandSyntaxException e) {
            return error(context, Text.translatable("composer.command.exception.no_players_found"));
        } catch (Exception e) {
            return error(context, Text.translatable("composer.toast.invalid_icon_texture"));
        }
    }

    private int notify(CommandContext<ServerCommandSource> context) {
        try {
            var players = EntityArgumentType.getPlayers(context, "who");
            ToastManager.Corner corner = ToastCornerArgumentType.getCorner(context, "where");
            String message = StringArgumentType.getString(context, "message");
            int backgroundColor = context.getArgument("background_color", Integer.class);
            int borderColor = context.getArgument("border_color", Integer.class);

            players.forEach(player -> ServerPlayNetworking.send(player, new TriggerToastPayload<>(new NotifyToast(message, backgroundColor, borderColor), corner)));

            return feedback(context, Text.translatable(
                    "composer.toast.sent_notify",
                    message,
                    String.format("#%06X", backgroundColor & 0xFFFFFF),
                    String.format("#%06X", borderColor & 0xFFFFFF)
            ));
        } catch (Exception e) {
            return error(context, Text.translatable("composer.command.exception.no_players_found"));
        }
    }

    @Override
    protected Text buildPrefix() {
        return wrapBrackets(createGradient(Text.translatable("composer.toast.prefix"), 0xffaa00, 0xffff55));
    }
}
