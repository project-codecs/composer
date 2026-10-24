package com.codex.composer.api.v1.block.entity;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

/**
 * Implement on block entities of extension blocks (hatches, ports...) to be told which controller they belong to.
 */
public interface MultiblockExtensionBlockEntity {
    void onAttached(BlockPos controllerPos, Identifier extensionId);
    void onDetached(BlockPos controllerPos);
}