package com.codex.composer.mixin.impl.multiblocks;

import com.codex.composer.api.v1.multiblock.ingame.MultiblockHost;
import net.minecraft.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin {
    @Inject(method = "markRemoved", at = @At("HEAD"))
    private void composer$onRemoved(CallbackInfo ci) {
        if ((Object) this instanceof MultiblockHost host) host.getMultiblockController().handleRemoved();
    }
}