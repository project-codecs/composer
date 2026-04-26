package com.codex.composer.internal.command;

import com.codex.composer.api.v1.util.commands.ComposerCommand;
import com.codex.composer.internal.networking.ShowCreditsPayload;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.EnumArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;

import java.util.List;
import java.util.Locale;

public class CreditsCommand extends ComposerCommand {
    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(
                CommandManager.literal("credits").then(
                        CommandManager.argument("for", EntityArgumentType.players()).then(
                                CommandManager.argument("type", CreditsTypeArgumentType.create()).executes(this::roll)
                        )
                )
        );
    }

    private int roll(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        if (shouldCancel()) return throwOnNonDebug(ctx);
        CreditsType type = CreditsTypeArgumentType.get(ctx, "type");
        List<ServerPlayerEntity> players = (List<ServerPlayerEntity>) EntityArgumentType.getPlayers(ctx, "for"); // Safe to type cast because internally it is an ArrayList<SPE>

        players.forEach(player -> ServerPlayNetworking.send(player, new ShowCreditsPayload(type.credits, type.poem)));

        return success(ctx, Text.translatable("composer.credits.success" + type.suffix, players.size()));
    }

    @Override
    protected Text buildPrefix() {
        return wrapBrackets(createGradient(Text.translatable("composer.registry.prefix"), 0xffaa00, 0xffff55));
    }

    @Override
    protected boolean debugOnly() {
        return true;
    }

    public enum CreditsType implements StringIdentifiable {
        CREDITS(true, false, "_credits"),
        POEM(false, true, "_poem"),
        BOTH(true, true, "_both");
        public static final com.mojang.serialization.Codec<CreditsType> CODEC = StringIdentifiable.createCodec(CreditsType::values);

        public final boolean credits;
        public final boolean poem;
        public final String suffix;

        CreditsType(boolean credits, boolean poem, String suffix) {
            this.credits = credits;
            this.poem = poem;
            this.suffix = suffix;
        }

        @Override
        public String asString() {
            return name().toLowerCase(Locale.ROOT);
        }
    }

    public static class CreditsTypeArgumentType extends EnumArgumentType<CreditsType> {
        protected CreditsTypeArgumentType() {
            super(CreditsType.CODEC, CreditsType::values);
        }

        public static CreditsTypeArgumentType create() {
            return new CreditsTypeArgumentType();
        }

        public static CreditsType get(CommandContext<ServerCommandSource> ctx, String name) {
            return ctx.getArgument(name, CreditsType.class);
        }
    }

}
