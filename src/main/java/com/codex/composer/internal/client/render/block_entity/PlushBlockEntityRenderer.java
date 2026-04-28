package com.codex.composer.internal.client.render.block_entity;

import com.codex.composer.api.v1.block.entity.AbstractPlushieBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

//? if minecraft: <=1.21.4
import net.minecraft.client.render.RenderLayers;

//? if minecraft: >=1.21.5
//import net.minecraft.util.math.Vec3d;

//? if minecraft: >=1.21.5 <=1.21.6
//import net.minecraft.client.render.RenderLayer;

//? if minecraft: =1.21.6
//import net.fabricmc.fabric.api.renderer.v1.render.BlockVertexConsumerProvider;

//? if minecraft: <=1.21.6 {
import net.minecraft.client.render.VertexConsumerProvider;
import com.codex.composer.mixin.impl.local.BlockRenderManagerAccessor;
//? }

//? if minecraft: >=1.21.9 {
/*import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import org.jetbrains.annotations.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
*///? }

@SuppressWarnings("ClassCanBeRecord")
@Environment(EnvType.CLIENT)
public class PlushBlockEntityRenderer<T extends BlockEntity> implements BlockEntityRenderer<T/*? if minecraft: >=1.21.9 {*//*, PlushBlockEntityRenderer.PlushBlockEntityRenderState*//*? }*/> {
    //? if minecraft: <=1.21.6
    private final BlockRenderManager renderManager;

    public PlushBlockEntityRenderer(BlockEntityRendererFactory.@NotNull Context ctx) {
        //? if minecraft: <=1.21.6
        this.renderManager = ctx.getRenderManager();
    }

    //? if minecraft: >=1.21.9 {
    /*@Override
    public void render(PlushBlockEntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        matrices.push();

        double squish = state.squash;
        double lastSquish = state.lastSquash;
        float squash = (float) Math.pow(
                1 - 1f / (1f + MathHelper.lerp(state.tickDelta, lastSquish, squish)), 2
        );

        matrices.scale(1, 1 - squash, 1);
        matrices.translate(0.5, 0, 0.5);
        matrices.scale(1 + squash / 2, 1, 1 + squash / 2);
        matrices.translate(-0.5, 0, -0.5);

        BlockRenderManager renderManager = MinecraftClient.getInstance().getBlockRenderManager();
        var bakedModel = renderManager.getModel(state.blockState);

        queue.submitBlock(matrices, state.blockState, 0xF000F0, OverlayTexture.DEFAULT_UV, 0);

        matrices.pop();
    }

    @Override
    public PlushBlockEntityRenderState createRenderState() {
        return new PlushBlockEntityRenderState();
    }

    @Override
    public void updateRenderState(BlockEntity entity, PlushBlockEntityRenderState state, float tickProgress, Vec3d cameraPos, @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
        if (entity instanceof AbstractPlushieBlockEntity plushie) {
            state.squash = plushie.squash;
            state.lastSquash = plushie.squash * 3;
        } else {
            state.squash = 0;
            state.lastSquash = 0;
        }
        state.blockState = entity.getCachedState();
        state.blockPos = entity.getPos();
        state.world = entity.getWorld();
        state.tickDelta = tickProgress;
    }

    public static class PlushBlockEntityRenderState extends BlockEntityRenderState {
        double squash;
        double lastSquash;
        float tickDelta;
        BlockState blockState;
        BlockPos blockPos;
        World world;
    }
    *///? } else {
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
    //? }
}