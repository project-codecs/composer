package com.codex.composer.internal.block.entity;

import com.codex.composer.api.v1.block.entity.AbstractPlushieBlockEntity;
import com.codex.composer.internal.registry.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class LilBroPlushBlockEntity extends AbstractPlushieBlockEntity {
    public LilBroPlushBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PLUSH, pos, state);
    }
}
