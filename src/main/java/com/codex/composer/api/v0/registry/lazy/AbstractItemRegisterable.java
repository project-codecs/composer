package com.codex.composer.api.v0.registry.lazy;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

/**
 * Used for transferring from the v1 item registry to the v2 one by datagen
 */
public interface AbstractItemRegisterable {
    Item register(String name);
    BlockItem registerBlock(Block block, String name);
}
