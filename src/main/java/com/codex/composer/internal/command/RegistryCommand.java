package com.codex.composer.internal.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.commons.io.function.IOConsumer;
import com.codex.composer.api.v1.util.commands.ComposerCommand;
import com.codex.composer.api.v1.util.misc.AbstractPseudoRegistry;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class RegistryCommand extends ComposerCommand {
    public static final RegistryCommand INSTANCE = new RegistryCommand();

    public RegistryCommand() {

    }

    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(
                CommandManager.literal("registry")
                        .then(CommandManager.literal("dump")
                                .then(CommandManager.literal("pseudo")
                                        .then(CommandManager.argument("id", IdentifierArgumentType.identifier())
                                                .suggests(this::pseudoRegistries)
                                                .executes(this::dumpPseudoRegistry)
                                        )
                                )
                                .then(CommandManager.literal("vanilla")
                                        .then(CommandManager.argument("id", IdentifierArgumentType.identifier())
                                                .suggests(this::registries)
                                                .executes(this::dumpRegistry)
                                        )
                                )
                        )

                        .then(CommandManager.literal("dump_all")
                                .then(CommandManager.literal("pseudo")
                                        .executes(this::dumpPseudoRegistries)
                                )
                                .then(CommandManager.literal("vanilla")
                                        .executes(this::dumpRegistries)
                                )
                        )
        );
    }

    private int dumpPseudoRegistry(CommandContext<ServerCommandSource> ctx) {
        if (shouldCancel()) return throwOnNonDebug(ctx);
        Identifier id = ctx.getArgument("id", Identifier.class);
        AbstractPseudoRegistry<?> registry = AbstractPseudoRegistry.registry(id);

        if (registry == null) {
            return error(ctx, Text.literal("Registry not found: " + id));
        }

        return takeDump(ctx, id, writer -> {
            for (var entry : registry.getAll().entrySet()) {
                writer.write(entry.toString());
                writer.newLine();
            }
        });
    }

    private int dumpPseudoRegistries(CommandContext<ServerCommandSource> context) {
        if (shouldCancel()) return throwOnNonDebug(context);
        return AbstractPseudoRegistry.identified()
                .stream()
                .map(AbstractPseudoRegistry::entry)
                .mapToInt(reg -> takeDump(context, reg.getA(), writer -> {
                    for (var entry : reg.getB().getAll().entrySet()) {
                        writer.write(entry.toString());
                        writer.newLine();
                    }
                }))
                .sum();
    }

    private int dumpRegistry(CommandContext<ServerCommandSource> ctx) {
        if (shouldCancel()) return throwOnNonDebug(ctx);
        Identifier id = ctx.getArgument("id", Identifier.class);
        Registry<?> registry = Registries.REGISTRIES.get(id);

        if (registry == null) {
            return error(ctx, Text.literal("Registry not found: " + id));
        }

        return takeDump(ctx, id, writer -> {
            for (Identifier key : registry.getKeys().stream().map(RegistryKey::getValue).toList()) {
                writer.write(key + " = " + registry.get(key));
                writer.newLine();
            }
        });
    }

    private int dumpRegistries(CommandContext<ServerCommandSource> context) {
        if (shouldCancel()) return throwOnNonDebug(context);
        return Registries.REGISTRIES.getKeys()
                .stream()
                .map(RegistryKey::getValue)
                .map(Registries.REGISTRIES::get)
                .filter(Objects::nonNull)
                .mapToInt(registry -> takeDump(context, registry.getKey().getValue(), writer -> {
                    for (Identifier key : registry.getKeys().stream().map(RegistryKey::getValue).toList()) {
                        writer.write(key + " = " + registry.get(key));
                        writer.newLine();
                    }
                }))
                .sum();
    }

    private Path createDumpFile(ServerCommandSource source, Identifier id) throws IOException {
        Path dumpDir = source.getServer()
                .getRunDirectory()
                /*? if minecraft: <=1.20.6 { *//*.toPath()*//*? }*/
                .resolve("dumps");

        Files.createDirectories(dumpDir);

        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));

        String fileName = "dump-" + id.toString().replace("_", "-") + "-" + timestamp + ".txt";
        return dumpDir.resolve(fileName);
    }

    private int takeDump(CommandContext<ServerCommandSource> ctx, Identifier id, IOConsumer<BufferedWriter> writerLogic) {
        Path filePath;
        try {
            filePath = createDumpFile(ctx.getSource(), id);
        } catch (Exception e) {
            return error(ctx, Text.literal("Failed to create dumps directory: " + e.getMessage()));
        }

        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
            writerLogic.accept(writer);
        } catch (Exception e) {
            return error(ctx, Text.literal("Failed to write registry dump: " + e.getMessage()));
        }

        return feedback(ctx, Text.literal("Registry dump written to " + filePath.getFileName()));
    }


    private CompletableFuture<Suggestions> pseudoRegistries(CommandContext<ServerCommandSource> ignoredContext, SuggestionsBuilder builder) {
        AbstractPseudoRegistry.identified()
                .stream()
                .map(Identifier::toString)
                .forEach(builder::suggest);

        return builder.buildFuture();
    }

    private CompletableFuture<Suggestions> registries(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        Registries.REGISTRIES.getKeys()
                .stream()
                .map(RegistryKey::getValue)
                .map(Identifier::toString)
                .forEach(builder::suggest);

        return builder.buildFuture();
    }

    @Override
    protected Text buildPrefix() {
        return wrapBrackets(createGradient(Text.translatable("composer.registry.prefix"), 0xffaa00, 0xffff55));
    }

    @Override
    protected boolean debugOnly() {
        return true;
    }
}
