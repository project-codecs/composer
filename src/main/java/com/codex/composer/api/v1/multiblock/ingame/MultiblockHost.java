package com.codex.composer.api.v1.multiblock.ingame;

import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

/**
 * Implement this on ANY block entity (furnace, custom, ...) to make it a multiblock controller.
 * Create one {@link MultiblockController} in a field and return it from {@link #getMultiblockController()}.
 * No special block class is required, and no ticking is needed from the host.
 */
public interface MultiblockHost {
    /** @return Id of the multiblock definition this controller uses. */
    Identifier multiblock();

    MultiblockController getMultiblockController();

    /**
     * @return The world direction the definition's +Z axis points to. {@link Direction#SOUTH} means unrotated.
     * Only horizontal directions are supported.
     */
    default Direction getOrientation(BlockState state) {
        return Direction.SOUTH;
    }

    default void onMultiblockFormed() {}

    default void onMultiblockBroken() {}

    /** Called when the set of extensions changes, and once after load when the formed multiblock is rediscovered. */
    default void onExtensionsChanged() {}
}