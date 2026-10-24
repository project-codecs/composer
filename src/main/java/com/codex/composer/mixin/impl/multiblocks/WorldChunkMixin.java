package com.codex.composer.mixin.impl.multiblocks;

import com.codex.composer.api.v1.multiblock.ingame.MultiblockController;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldChunk.class)
public abstract class WorldChunkMixin {
    @Shadow public abstract World getWorld();

    @Inject(method = "setBlockState", at = @At("RETURN"))
    //? if minecraft: <=1.21.4 {
    private void composer$onBlockChanged(BlockPos pos, BlockState state, boolean moved, CallbackInfoReturnable<BlockState> cir) {
    //? } else {
    /*private void composer$onBlockChanged(BlockPos pos, BlockState state, int flags, CallbackInfoReturnable<BlockState> cir) {
    *///? }
        if (cir.getReturnValue() == null) return;
        MultiblockController.notifyBlockChanged(getWorld(), pos);
    }
}