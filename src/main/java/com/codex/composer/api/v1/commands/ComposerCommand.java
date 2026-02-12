package com.codex.composer.api.v1.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import com.codex.composer.api.v1.util.math.Math;

/**
 * Base class for implementing Minecraft commands with convenient utilities.
 * <p> Provides:
 * <ul>
 *     <li>Feedback methods for sending messages to players or command sources.</li>
 *     <li>Pre-styled messages for success, warning, or error outputs.</li>
 *     <li>Prefix and bracket styling helpers.</li>
 *     <li>Utility methods for colored and styled text.</li>
 * </ul>
 * Extend this class to implement commands and provide a concrete {@link #buildPrefix()}.
 */
@SuppressWarnings("SameReturnValue")
public abstract class ComposerCommand implements CommandRegistrationCallback {

    /**
     * Sends feedback to a command source with an optional success flag.
     *
     * @param src     the command context
     * @param message the message to send
     * @param success whether the command was successful
     * @return 1 if success, 0 otherwise
     */
    public int feedback(CommandContext<ServerCommandSource> src, Text message, boolean success) {
        src.getSource().sendFeedback(() -> appendPrefix(message), false);
        return success ? 1 : 0;
    }

    /** Convenience overload assuming success. */
    public int feedback(CommandContext<ServerCommandSource> src, Text message) {
        return feedback(src, message, true);
    }

    /** Sends feedback to a player with an optional success flag. */
    public int feedback(PlayerEntity player, Text message, boolean success) {
        player.sendMessage(appendPrefix(message), false);
        return success ? 1 : 0;
    }

    /** Convenience overload assuming success. */
    public int feedback(PlayerEntity player, Text message) {
        return feedback(player, message, true);
    }

    /** Sends an error message to a command source. */
    @SuppressWarnings("UnusedReturnValue")
    public int error(CommandContext<ServerCommandSource> src, Text message) {
        return feedback(src, styled(message.copy(), style -> style.withColor(errorColor())), false);
    }

    /** Sends an error message to a player. */
    public int error(PlayerEntity player, Text message) {
        return feedback(player, styled(message.copy(), style -> style.withColor(errorColor())), false);
    }

    /** Sends a success message to a command source. */
    public int success(CommandContext<ServerCommandSource> src, Text message) {
        return feedback(src, styled(message.copy(), style -> style.withColor(successColor())), true);
    }

    /** Sends a success message to a player. */
    public int success(PlayerEntity player, Text message) {
        return feedback(player, styled(message.copy(), style -> style.withColor(successColor())), true);
    }

    /** Sends a warning message to a command source. */
    public int warn(CommandContext<ServerCommandSource> src, Text message) {
        return feedback(src, styled(message.copy(), style -> style.withColor(warnColor())), true);
    }

    /** Sends a warning message to a player. */
    public int warn(PlayerEntity player, Text message) {
        return feedback(player, styled(message.copy(), style -> style.withColor(warnColor())), true);
    }

    /** Sends a highlighted message to a command source. */
    public int highlight(CommandContext<ServerCommandSource> src, Text message) {
        return feedback(src, styled(message.copy(), style -> style.withColor(highlightColor())), true);
    }

    /** Sends a highlighted message to a player. */
    public int highlight(PlayerEntity player, Text message) {
        return feedback(player, styled(message.copy(), style -> style.withColor(highlightColor())), true);
    }

    /** Sends an informational message to a command source. */
    public int info(CommandContext<ServerCommandSource> src, Text msg) {
        src.getSource().sendFeedback(() -> msg, false);
        return 1;
    }

    /** Sends an informational message to a player. */
    public int info(PlayerEntity player, Text msg) {
        player.sendMessage(msg, false);
        return 1;
    }

    /** Broadcasts a message to all players on the server. */
    public void broadcast(ServerCommandSource source, Text message) {
        source.getServer().getPlayerManager().broadcast(appendPrefix(message), false);
    }

    /** Returns a copy of the text with a style modifier applied. */
    public Text styled(Text base, java.util.function.UnaryOperator<Style> modifier) {
        return base.copy().styled(modifier);
    }

    /** Creates a literal text with a custom color. */
    public Text colored(String msg, int color) {
        return Text.literal(msg).styled(s -> s.withColor(color));
    }

    /** Convenience for success text. */
    public Text success(String msg) {
        return colored(msg, successColor());
    }

    /** Convenience for warning text. */
    public Text warning(String msg) {
        return colored(msg, warnColor());
    }

    /** Convenience for error text. */
    public Text error(String msg) {
        return colored(msg, errorColor());
    }

    /** Convenience for highlighted text */
    public Text highlight(String msg) {
        return colored(msg, highlightColor());
    }

    /** Appends the command prefix to a text message. */
    public Text appendPrefix(Text text) {
        return buildPrefix().copy().append(ScreenTexts.space()).append(text);
    }

    /** Wraps a text in brackets using the default bracket color. */
    public Text wrapBrackets(Text text) {
        return wrapBrackets(text, bracketsColor());
    }

    /** Wraps a text in brackets using a specified color. */
    public Text wrapBrackets(Text text, int color) {
        return Text.literal("")
                .append(Text.literal("[").setStyle(Style.EMPTY.withColor(color)))
                .append(text)
                .append(Text.literal("]").setStyle(Style.EMPTY.withColor(color)));
    }

    /** Returns "true" or "false" styled appropriately for success/error. */
    public Text boolText(boolean value) {
        return value ? colored("true", successColor()) : colored("false", errorColor());
    }

    /**
     * Generates a {@link Text} with the text value of the passed Text object, formatted with a gradient
     * from and to the passed colors.
     * @param text The text content
     * @param startColor Start color for the gradient
     * @param endColor End color for the gradient
     * @return Formatted {@link Text}
     */
    public static Text createGradient(Text text, int startColor, int endColor) {
        MutableText out = Text.literal("").copy();
        int len = text.getString().length();

        if (len == 0) return out;

        for (int i = 0; i < len; i++) {
            double t = len == 1 ? 0.0 : (double) i / (len - 1);

            int color = Math.lerpColor(startColor, endColor, t);
            char c = text.getString().charAt(i);

            out.append(
                    Text.literal(String.valueOf(c))
                            .setStyle(Style.EMPTY.withColor(color))
            );
        }

        return out;
    }

    /**
     * Generates a {@link Text} with the text value of the passed string, formatted with a gradient
     * from and to the passed colors.
     * @param text The text content
     * @param startColor Start color for the gradient
     * @param endColor End color for the gradient
     * @return Formatted {@link Text}
     */
    public static Text createGradient(String text, int startColor, int endColor) {
        return createGradient(Text.literal(text), startColor, endColor);
    }

    /** Default error color. */
    protected int errorColor() {
        return 0xFF5555;
    }

    /** Default warning color. */
    protected int warnColor() {
        return 0xFFFF55;
    }

    /** Default success color. */
    protected int successColor() {
        return 0x55FF55;
    }

    /** Default bracket color. */
    protected int bracketsColor() {
        return 0xAAAAAA;
    }

    /** Default highlight color. */
    protected int highlightColor() {
        return 0xffaa00;
    }

    /**
     * Builds the prefix for messages sent by this command.
     * <p>
     * Must be implemented by subclasses.
     * </p>
     *
     * @return the prefix text
     */
    protected abstract Text buildPrefix();
}
