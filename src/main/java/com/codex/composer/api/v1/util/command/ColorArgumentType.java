package com.codex.composer.api.v1.util.command;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.network.PacketByteBuf;

import java.util.concurrent.CompletableFuture;

public class ColorArgumentType implements ArgumentType<Integer> {
    private static final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
    private final boolean includeAlpha;

    protected ColorArgumentType(boolean includeAlpha) {
        this.includeAlpha = includeAlpha;
    }

    public static ColorArgumentType rgb() {
        return new ColorArgumentType(false);
    }

    public static ColorArgumentType rgba() {
        return new ColorArgumentType(true);
    }

    @Override
    public Integer parse(StringReader reader) throws CommandSyntaxException {
        int start = reader.getCursor();
        if (!reader.canRead() || reader.peek() != '#') {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException()
                    .create("Color must start with #");
        }
        reader.skip();

        StringBuilder hex = new StringBuilder();
        while (reader.canRead() && hex.length() < 8 && isHex(reader.peek())) {
            hex.append(reader.read());
        }

        if (hex.length() != 6 && (hex.length() != 8 && includeAlpha)) {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException()
                    .create("Color must be in #RRGGBB or #RRGGBBAA format");
        }

        return (int) Long.parseLong(hex.toString(), 16);
    }

    private boolean isHex(char c) {
        return (c >= '0' && c <= '9') ||
                (c >= 'a' && c <= 'f') ||
                (c >= 'A' && c <= 'F');
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        String remaining = builder.getRemaining();

        if (!remaining.startsWith("#")) {
            builder.suggest("#");
            return builder.buildFuture();
        }

        int length = remaining.length() - 1;
        if (length >= (includeAlpha ? 8 : 6)) return builder.buildFuture();

        for (char c : HEX_DIGITS) {
            builder.suggest(remaining + c);
        }

        return builder.buildFuture();
    }

    public enum Serializer implements ArgumentSerializer<ColorArgumentType, Serializer.Properties> {
        INSTANCE;

        @Override
        public void writePacket(Properties properties, PacketByteBuf buf) {
            buf.writeBoolean(properties.includeAlpha);
        }

        @Override
        public Properties fromPacket(PacketByteBuf buf) {
            return new Properties(buf.readBoolean());
        }

        @Override
        public void writeJson(Properties properties, JsonObject json) {
            json.addProperty("include_alpha", properties.includeAlpha);
        }

        @Override
        public Properties getArgumentTypeProperties(ColorArgumentType argumentType) {
            return new Properties(argumentType.includeAlpha);
        }

        public static class Properties implements ArgumentSerializer.ArgumentTypeProperties<ColorArgumentType> {
            private final boolean includeAlpha;

            public Properties(boolean includeAlpha) {
                this.includeAlpha = includeAlpha;
            }

            @Override
            public ColorArgumentType createType(CommandRegistryAccess registryAccess) {
                return new ColorArgumentType(includeAlpha);
            }

            @Override
            public ArgumentSerializer<ColorArgumentType, ?> getSerializer() {
                return Serializer.INSTANCE;
            }
        }
    }
}
