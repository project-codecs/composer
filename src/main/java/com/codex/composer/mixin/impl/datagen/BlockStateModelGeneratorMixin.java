package com.codex.composer.mixin.impl.datagen;

import net.minecraft.block.Block;
import net.minecraft.data.family.BlockFamily;
import org.spongepowered.asm.mixin.Mixin;

//~ if <=1.21.3 'client.data' -> 'data.client'
import net.minecraft.client.data.BlockStateModelGenerator;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockStateModelGenerator.class)
public class BlockStateModelGeneratorMixin {
    @Mixin(BlockStateModelGenerator.BlockTexturePool.class)
    public static abstract class BlockTexturePoolMixin {
        @Shadow protected abstract BlockStateModelGenerator.BlockTexturePool block(Block block);

        @Inject(method = "family", at = @At("TAIL"))
        public void composer$appendExtra(BlockFamily family, CallbackInfoReturnable<BlockStateModelGenerator.BlockTexturePool> cir) {
            family.getVariants().forEach((variant, block) -> {
                if (variant == BlockFamily.Variant.CUT || variant == BlockFamily.Variant.POLISHED || variant == BlockFamily.Variant.MOSAIC) block(block);
            });
        }
    }
}
