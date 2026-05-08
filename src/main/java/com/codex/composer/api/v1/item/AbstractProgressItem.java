package com.codex.composer.api.v1.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

//? if minecraft: >=1.20.6
import static com.codex.composer.internal.registry.ModDataComponentTypes.STEPS;

/**
 * Base class for items that have a multistep progress mechanic.
 * <p>
 * Each item tracks its current step in NBT and can produce a resulting
 * item once fully completed. Subclasses define the final item and any
 * behavior when progress changes or completes.
 * </p>
 */
@SuppressWarnings("EmptyMethod")
public abstract class AbstractProgressItem extends Item {
    //? if minecraft: <=1.20.4
    //private static final String STEP_KEY = "Step";
    private final int maxSteps;

    /**
     * @param settings Item settings
     * @param steps    Maximum number of steps until completion
     */
    public AbstractProgressItem(Settings settings, int steps) {
        super(settings);
        this.maxSteps = steps;
    }

    /**
     * Called when the item reaches its final step. Should return the item
     * that represents the completed state.
     *
     * @param oldStack The original item stack
     * @return New ItemStack representing the completed item
     */
    protected abstract ItemStack getCompletedItem(ItemStack oldStack);

    /**
     * Called when the item's progress increases.
     * Default implementation does nothing.
     */
    protected void onProgressIncreased(ItemStack stack, PlayerEntity player, int newStep) { }

    /**
     * Called when the item finishes its process (reaches max steps).
     * Default implementation does nothing.
     */
    protected void onFinishProcess(ItemStack stack, World world, PlayerEntity player) { }

    /** Returns the current step stored in the item's NBT. */
    //? if minecraft: >=1.20.6
    @SuppressWarnings("DataFlowIssue")
    public static int getStep(ItemStack stack) {
        //? if minecraft: <=1.20.4 {
        /*return stack.getOrCreateNbt().getInt(STEP_KEY);
        *///? } else {
        return stack.contains(STEPS) ? stack.get(STEPS) : 0;
        //? }
    }

    /** Sets the current step in the item's NBT. */
    public static void setStep(ItemStack stack, int value) {
        //? if minecraft: <=1.20.4 {
        /*stack.getOrCreateNbt().putInt(STEP_KEY, value);
        *///? } else {
        stack.set(STEPS, value);
        //? }
    }

    /** Returns the maximum number of steps this item can reach. */
    public int getMaxSteps() {
        return maxSteps;
    }

    /**
     * Attempts to increment the progress of this item by {@code amount}.
     * <p>
     * Returns {@link ItemStack#EMPTY} if progress is incomplete,
     * or the completed item if progress reaches the max steps.
     * </p>
     */
    public ItemStack tryIncrementProgress(ItemStack stack, World world, PlayerEntity player, int amount) {
        int current = getStep(stack);
        if (current >= getMaxSteps()) return ItemStack.EMPTY;

        int newStep = Math.min(current + amount, getMaxSteps());
        setStep(stack, newStep);
        onProgressIncreased(stack, player, newStep);

        if (newStep >= getMaxSteps()) {
            onFinishProcess(stack, world, player);
            return getCompletedItem(stack);
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        int step = getStep(stack);
        return Math.round((float) step / getMaxSteps() * 13);
    }
}
