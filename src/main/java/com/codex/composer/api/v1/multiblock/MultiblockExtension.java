package com.codex.composer.api.v1.multiblock;

import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;

import java.util.function.Predicate;

/**
 * Definition of an extension: a block that may stand in for some normal block of the multiblock.
 * @param min Minimum amount required for the multiblock to be complete
 * @param max Maximum amount allowed for the multiblock to be complete
 */
public record MultiblockExtension(Identifier id, Predicate<BlockState> predicate, int min, int max) {
}