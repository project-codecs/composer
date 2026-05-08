package com.codex.composer.internal.registry;

import com.codex.composer.api.v1.block.entity.AbstractPlushieBlockEntity;
import com.codex.composer.internal.block.entity.LilBroPlushBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import com.codex.composer.api.v1.registry.lazy.DeferredBlockEntityRegistry;
import com.codex.composer.internal.Composer;

public class ModBlockEntities {
    private static final DeferredBlockEntityRegistry REGISTRY = new DeferredBlockEntityRegistry(Composer.MOD_ID);

    public static final BlockEntityType<AbstractPlushieBlockEntity> PLUSH = REGISTRY.register(
            "plush",
            LilBroPlushBlockEntity::new,
            ModBlocks.PLUSH
    );

    @SuppressWarnings("EmptyMethod")
    public static void initialize() {

    }
}
