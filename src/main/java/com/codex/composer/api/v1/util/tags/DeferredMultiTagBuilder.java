package com.codex.composer.api.v1.util.tags;

import com.codex.composer.api.v1.registry.lazy.EmptyDeferredRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class DeferredMultiTagBuilder extends EmptyDeferredRegistry {
    public DeferredMultiTagBuilder(String modId) {
        super(modId);
    }

    public <T> DeferredTagBuilder<T> builder(RegistryKey<Registry<T>> registry) {
        return new DeferredTagBuilder<>(modId, registry);
    }

    public DeferredTagBuilder<Block> block() { return builder(RegistryKeys.BLOCK); }
    public DeferredTagBuilder<BlockEntityType<?>> blockEntityType() { return builder(RegistryKeys.BLOCK_ENTITY_TYPE); }
    public DeferredTagBuilder<Item> item() { return builder(RegistryKeys.ITEM); }
    public DeferredTagBuilder<Fluid> fluid() { return builder(RegistryKeys.FLUID); }
    public DeferredTagBuilder<Enchantment> enchantment() { return builder(RegistryKeys.ENCHANTMENT); }
    public DeferredTagBuilder<EntityType<?>> entityType() { return builder(RegistryKeys.ENTITY_TYPE); }
}
