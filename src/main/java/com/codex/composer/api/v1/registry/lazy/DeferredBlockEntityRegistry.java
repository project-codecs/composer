package com.codex.composer.api.v1.registry.lazy;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;


public class DeferredBlockEntityRegistry extends EmptyDeferredRegistry {
    public DeferredBlockEntityRegistry(String modId) {
        super(modId);
    }

    public final <T extends BlockEntity> BlockEntityType<T> register(String name, FabricBlockEntityTypeBuilder.Factory<T> factory, Block... blocks) {
        return Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                id(name),
                FabricBlockEntityTypeBuilder.create(factory, blocks).build()
        );
    }
}
