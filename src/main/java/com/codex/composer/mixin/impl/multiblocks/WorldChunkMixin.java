package com.codex.composer.mixin.impl.multiblocks;

import com.codex.composer.api.v1.block.MultiblockControllerBlock;
import com.codex.composer.internal.cca.ModCardinalComponents;
import com.codex.composer.internal.cca.chunk.MultiblocksComponent;
import com.codex.composer.internal.multiblock.MultiblockUpdateHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldChunk.class)
public abstract class WorldChunkMixin {
    @Shadow public abstract World getWorld();
    @Unique private boolean executeOnPlaced = false;

    @Inject(method = "setBlockState", at = @At("HEAD"))
    //? if minecraft: <=1.21.4 {
    private void composer$saveBlockState(BlockPos pos, BlockState state, boolean moved, CallbackInfoReturnable<BlockState> cir) {
    //? } else {
    /*private void composer$saveBlockState(BlockPos pos, BlockState state, int flags, CallbackInfoReturnable<BlockState> cir) {
    *///? }
        World world = getWorld();
        BlockState previous = world.getBlockState(pos);

        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof MultiblockControllerBlock ctrl) {
            if (previous./*? if legacy {*/isOf/*? } else {*//*is*//*? }*/(be.getCachedState().getBlock()) && !state./*? if legacy {*/isOf/*? } else {*//*is*//*? }*/(be.getCachedState().getBlock())) ctrl.onBroken(world, pos);
        } else executeOnPlaced = true;
    }

    @Inject(method = "setBlockState", at = @At("TAIL"))
    //? if minecraft: <=1.21.4 {
    private void composer$onBlockUpdate(BlockPos pos, BlockState state, boolean moved, CallbackInfoReturnable<BlockState> cir) {
    //? } else {
    /*private void composer$onBlockUpdate(BlockPos pos, BlockState state, int flags, CallbackInfoReturnable<BlockState> cir) {
        *///? }
        World world = getWorld();

        if (executeOnPlaced) {
            BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof MultiblockControllerBlock ctrl) ctrl.onPlaced(world, pos);
        }

        executeOnPlaced = false;

        MultiblocksComponent component = ModCardinalComponents.MULTIBLOCKS.get(world);
        if (component.multiblocks.isEmpty()) return;

        MultiblockUpdateHandler.runUpdates(world, component, pos);
    }
}