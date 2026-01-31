package com.codex.composer.internal.command;

import com.codex.composer.api.v1.runtime.ServerHolder;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import com.codex.composer.api.v1.commands.ComposerCommand;
import com.codex.composer.api.v1.feature.ComposerFeatures;
import com.codex.composer.api.v1.feature.FeatureHandle;
import com.codex.composer.api.v1.feature.FeatureNode;
import com.codex.composer.api.v1.feature.state.FeatureState;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class FeatureCommand extends ComposerCommand {

    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher,
                         CommandRegistryAccess registryAccess,
                         CommandManager.RegistrationEnvironment env) {

        dispatcher.register(
                CommandManager.literal("feature")
                        .requires(src -> src.hasPermissionLevel(2))
                        .then(CommandManager.argument("mod", StringArgumentType.word())
                                .suggests(this::mods)
                                .then(CommandManager.literal("enable").then(feature(this::enable)))
                                .then(CommandManager.literal("disable").then(feature(this::disable)))
                                .then(CommandManager.literal("describe").then(feature(this::describe)))
                                .then(CommandManager.literal("status").then(feature(this::status)))
                        )
        );
    }

    @Override
    protected Text buildPrefix() {
        return wrapBrackets(createGradient(Text.translatable("composer.feature.prefix"), 0xffaa00, 0xffff55));
    }

    private CompletableFuture<Suggestions> mods(CommandContext<ServerCommandSource> ctx, SuggestionsBuilder b) {
        ComposerFeatures.getInstance().getRegisteredNamespaces().forEach(b::suggest);
        return b.buildFuture();
    }

    private CompletableFuture<Suggestions> features(CommandContext<ServerCommandSource> ctx, SuggestionsBuilder b) {
        String modId = ctx.getArgument("mod", String.class);
        ComposerFeatures.getInstance().getFeaturePathsForNamespace(modId).forEach(b::suggest);
        return b.buildFuture();
    }

    private ArgumentBuilder<ServerCommandSource, ?> feature(Command<ServerCommandSource> exec) {
        return CommandManager.argument("feature", StringArgumentType.greedyString())
                .suggests(this::features)
                .executes(exec);
    }

    private Identifier resolveId(CommandContext<ServerCommandSource> ctx) {
        String modId = ctx.getArgument("mod", String.class);
        String path = ctx.getArgument("feature", String.class);
        return Identifier.of(modId, path);
    }

    private int enable(CommandContext<ServerCommandSource> ctx) {
        Identifier id = resolveId(ctx);
        FeatureState state = ServerHolder.get().features();

        if (ComposerFeatures.getInstance().featureMissing(id)) {
            return error(ctx, Text.translatable("composer.feature.missing", id.toString()));
        }

        state.setEnabled(id.toString(), true);
        return success(ctx, Text.translatable("composer.feature.enable", id.toString())
);
    }

    private int disable(CommandContext<ServerCommandSource> ctx) {
        Identifier id = resolveId(ctx);
        FeatureState state = ServerHolder.get().features();

        if (ComposerFeatures.getInstance().featureMissing(id)) {
            return error(ctx, Text.translatable("composer.feature.missing", id.toString())
);
        }

        state.setEnabled(id.toString(), false);
        return highlight(ctx, Text.translatable("composer.feature.disable", id.toString())
);
    }

    private int describe(CommandContext<ServerCommandSource> ctx) {
        Identifier id = resolveId(ctx);
        ComposerFeatures f = ComposerFeatures.getInstance();
        FeatureHandle handle = ComposerFeatures.get(id);

        if (f.featureMissing(id) || handle == null) {
            return error(ctx, Text.translatable("composer.feature.missing", id.toString()));
        }

        String key = handle.getTranslationKey();
        return feedback(ctx, Text.translatableWithFallback(key, Text.translatable("composer.feature.description.missing").getString()));
    }

    private int status(CommandContext<ServerCommandSource> ctx) {
        Identifier id = resolveId(ctx);

        FeatureHandle handle = ComposerFeatures.get(id);
        if (handle == null) {
            return error(ctx, Text.translatable("composer.feature.missing", id.toString())
);
        }

        return feedback(ctx, ScreenTexts.LINE_BREAK.copy().append("  ").append(buildStatus(handle, 0)));
    }

    private Text buildStatus(FeatureHandle handle, int indent) {
        String prefix = " ".repeat(indent * 2);
        boolean enabled = handle.enabled();

        StringBuilder sb = new StringBuilder();
        sb.append(prefix).append(handle.id().getPath())
                .append(" [").append(enabled ? "enabled" : "disabled").append("]");

        // Include configs
        FeatureNode node = handle.node();
        if (!node.configDefaults.isEmpty()) {
            sb.append(" {");
            node.configDefaults.forEach((key, cd) -> {
                Object value = handle.getConfigAny(key);
                sb.append(key).append("=").append(value).append(", ");
            });
            sb.setLength(sb.length() - 2); // remove trailing comma
            sb.append("}");
        }

        Text result = Text.literal(sb.toString());

        Collection<FeatureHandle> children = ComposerFeatures.getChildren(handle);
        if (!children.isEmpty()) {
            for (FeatureHandle child : children) {
                result = Text.of(result.getString() + "\n" + buildStatus(child, indent + 1).getString());
            }
        }

        return result;
    }
}
