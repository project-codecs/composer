package com.codex.composer.api.v1.events.builtin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;
import com.codex.composer.api.v1.events.ClientScrollEvents;

/**
 * A scroll event listener that filters events by the held item in a specific hand.
 * <p>
 * Automatically checks if the player is holding the specified item in the given hand
 * before calling the abstract {@link #onScroll(MinecraftClient, ItemStack, ClientWorld, ClientPlayerEntity, double)} method.
 * </p>
 */
public abstract class ItemFilterClientScrollEvent implements ClientScrollEvents.ClientScrollAction {
    private final Hand hand;
    private final Item item;

    /**
     * Constructs a new ItemFilterClientScrollEvent.
     *
     * @param hand the hand to check ({@link Hand#MAIN_HAND} or {@link Hand#OFF_HAND})
     * @param item the item to filter for
     */
    protected ItemFilterClientScrollEvent(Hand hand, Item item) {
        this.hand = hand;
        this.item = item;
    }

    @Override
    public boolean onScroll(MinecraftClient client, @Nullable ClientWorld world, @Nullable ClientPlayerEntity player, double scrollAmount) {
        if (player == null) return false;

        ItemStack stack = player.getStackInHand(hand);
        if (stack.isOf(item)) return onScroll(client, stack, world, player, scrollAmount);

        return false;
    }

    /**
     * Called when a scroll event occurs and the player is holding the specified item.
     *
     * @param client       the Minecraft client instance
     * @param stack        the item stack in the specified hand
     * @param world        the client world, nullable
     * @param player       the client player, nullable
     * @param scrollAmount the amount scrolled
     * @return true to cancel further event processing, false otherwise
     */
    public abstract boolean onScroll(MinecraftClient client, ItemStack stack, @Nullable ClientWorld world, @Nullable ClientPlayerEntity player, double scrollAmount);
}
