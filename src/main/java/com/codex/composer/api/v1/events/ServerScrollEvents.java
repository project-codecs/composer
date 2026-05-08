package com.codex.composer.api.v1.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

/**
 * Provides server-side scroll events.
 * <p>
 * Supports multiple priority levels (HIGH, MEDIUM, LOW) for event handling.
 * Listeners can cancel further processing by returning true from {@link ServerScrollAction#onScroll}.
 * </p>
 */
public class ServerScrollEvents {
    public static final Event<ServerScrollAction> HIGH_PRIORITY = createEvent();
    public static final Event<ServerScrollAction> MEDIUM_PRIORITY = createEvent();
    public static final Event<ServerScrollAction> LOW_PRIORITY = createEvent();

    private static Event<ServerScrollAction> createEvent() {
        return EventFactory.createArrayBacked(ServerScrollAction.class,
                callbacks -> (channel, player, sender, scrollAmount) -> callActions(callbacks, channel, player, sender, scrollAmount)
        );
    }

    @FunctionalInterface
    public interface ServerScrollAction {
        /**
         * Called when a scroll action occurs on the server.
         *
         * @param channel      the identifier for the scroll channel
         * @param player       the player performing the scroll
         * @param sender       the packet sender for replying
         * @param scrollAmount the amount scrolled
         * @return true to cancel further processing, false otherwise
         */
        boolean onScroll(Identifier channel, ServerPlayerEntity player, PacketSender sender, double scrollAmount);

        default boolean onChannel(Identifier channel, Identifier correctChannel) {
            return channel.equals(correctChannel);
        }
    }

    private static boolean callActions(ServerScrollAction[] callbacks, Identifier channel, ServerPlayerEntity player, PacketSender sender, double scrollAmount) {
        for (ServerScrollAction action : callbacks)
            if (action.onScroll(channel, player, sender, scrollAmount)) return true;
        return false;
    }
}
