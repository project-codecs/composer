package com.codex.composer.mixin.impl.local;

//? if legacy {
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockRenderManager.class)
public interface BlockRenderManagerAccessor {
    @Accessor("blockModelRenderer")
    BlockModelRenderer composer$getModelRenderer();
}
//? } else
//@org.spongepowered.asm.mixin.Mixin(net.minecraft.client.MinecraftClient.class) public class BlockRenderManagerAccessor { } // Dummy mixin

