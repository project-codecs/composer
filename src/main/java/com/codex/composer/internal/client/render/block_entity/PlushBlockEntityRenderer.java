package com.codex.composer.internal.client.render.block_entity;

import com.codex.composer.api.v1.block.entity.AbstractPlushieBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import com.codex.composer.mixin.impl.local.BlockRenderManagerAccessor;

//? if minecraft: <=1.21.4
import net.minecraft.client.render.RenderLayers;

//? if minecraft: >=1.21.5 {
/*import net.minecraft.util.math.Vec3d;
import net.minecraft.client.render.RenderLayer;
*///? }

//? if minecraft: >=1.21.6
//import net.fabricmc.fabric.api.renderer.v1.render.BlockVertexConsumerProvider;

@SuppressWarnings("ClassCanBeRecord")
@Environment(EnvType.CLIENT)
public class PlushBlockEntityRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {
    private final BlockRenderManager renderManager;

    public PlushBlockEntityRenderer(BlockEntityRendererFactory.@NotNull Context ctx) {
        this.renderManager = ctx.getRenderManager();
    }

    public void render(@NotNull T entity, float tickDelta, @NotNull MatrixStack matrices, @NotNull VertexConsumerProvider consumerProvider, int light, int overlay/*? if minecraft: >=1.21.5 {*//*, Vec3d cameraPos*//*? }*/) {
        matrices.push();
        var squish = entity instanceof AbstractPlushieBlockEntity plushie ? plushie.squash : 0;
        var lastSquish = squish * 3;
        var squash = (float) Math.pow(1 - 1f / (1f + MathHelper.lerp(tickDelta, lastSquish, squish)), 2);
        matrices.scale(1, 1 - squash, 1);
        matrices.translate(0.5, 0, 0.5);
        matrices.scale(1 + squash / 2, 1, 1 + squash / 2);
        matrices.translate(-0.5, 0, -0.5);
        var state = entity.getCachedState();
        var bakedModel = this.renderManager.getModel(state);

        //? if minecraft: <=1.21.5 {
        @SuppressWarnings("UnnecessaryLocalVariable") VertexConsumerProvider vertexConsumers = consumerProvider;
        //? } else {
        /*BlockVertexConsumerProvider vertexConsumers = a -> consumerProvider.getBuffer(RenderLayer.getCutout());
        *///? }

        //? if minecraft: <=1.21.4
        ((BlockRenderManagerAccessor) this.renderManager).composer$getModelRenderer().render(matrices.peek(), vertexConsumers.getBuffer(RenderLayers.getEntityBlockLayer(state/*? if minecraft: <=1.21 { *//*, false*//*?}*/)), state, bakedModel, 0xFF, 0xFF, 0xFF, light, overlay);
        //? if minecraft: >=1.21.5
        //((BlockRenderManagerAccessor) this.renderManager).composer$getModelRenderer().render(entity.getWorld(), bakedModel, state, entity.getPos(), matrices, vertexConsumers, false, 0, overlay);
        matrices.pop();
    }
}