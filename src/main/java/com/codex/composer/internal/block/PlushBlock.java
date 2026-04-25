package com.codex.composer.internal.block;

import com.codex.composer.api.v1.block.AbstractPlushieBlock;

import com.codex.composer.api.v1.block.entity.PlushieBlockEntity;
import com.codex.composer.internal.block.entity.LilBroPlushBlockEntity;
import com.codex.composer.internal.registry.ModBlockEntities;
import com.codex.composer.internal.registry.ModSounds;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

//? if minecraft: >=1.20.4 {
import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockWithEntity;
//? }

public class PlushBlock extends AbstractPlushieBlock {
    public PlushBlock(Settings settings) {
        super(settings);
    }

    //? if minecraft: >=1.20.4 {
    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return BlockWithEntity.createCodec(PlushBlock::new);
    }
    //? }

    @Override
    protected void playSound(World world, BlockState state, BlockPos pos, PlayerEntity player) {
        Vec3d mid = pos.toCenterPos();
        world.playSound(null, mid.x, mid.y, mid.z, ModSounds.LILBRO_SQUISH, SoundCategory.BLOCKS, 1f, 1f);
    }

    @Override
    protected BlockEntityType<PlushieBlockEntity> getType() {
        return ModBlockEntities.PLUSH;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new LilBroPlushBlockEntity(pos, state);
    }
}
