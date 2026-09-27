package com.codex.composer.internal.client.render.block_entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import org.jetbrains.annotations.NotNull;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

//? if minecraft: <=1.21.4
import net.minecraft.client.render.RenderLayers;

//? if minecraft: >=1.21.5
//import net.minecraft.util.math.Vec3d;

//? if minecraft: >=1.21.5 <=1.21.6 || =1.21.10
//import net.minecraft.client.render.RenderLayer;

//? if minecraft: =1.21.6
//import net.fabricmc.fabric.api.renderer.v1.render.BlockVertexConsumerProvider;

//? if minecraft: <=1.21.6 {
import net.minecraft.client.render.VertexConsumerProvider;
import com.codex.composer.mixin.impl.local.BlockRenderManagerAccessor;
import com.codex.composer.api.v1.block.entity.AbstractPlushieBlockEntity;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.util.math.MathHelper;
//? }

//? if minecraft: >=1.21.9 {
/*import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.render.OverlayTexture;
import com.codex.composer.api.v1.block.entity.AbstractPlushieBlockEntity;
import com.codex.composer.internal.registry.ModBlockEntities;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
*///? }

//? if minecraft: >=1.21.9 <26 {
//import net.minecraft.client.render.model.BlockStateModel;
//import net.minecraft.world.BlockRenderView;
//import net.minecraft.client.render.block.BlockRenderManager;
//import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.render.RenderLayers;
//import com.codex.composer.mixin.impl.local.BlockRenderManagerAccessor;
//import net.minecraft.util.math.random.Random;
//? }

//? if minecraft: >=26 {
/*import net.minecraft.client.render.block.BlockModelManager;
import net.minecraft.client.render.block.BlockRenderState;
import net.minecraft.client.render.entity.EntityRendererId;
*///? }

@SuppressWarnings("ClassCanBeRecord")
@Environment(EnvType.CLIENT)
public class PlushBlockEntityRenderer<T extends BlockEntity> implements BlockEntityRenderer<T/*? if minecraft: >=1.21.9 {*//*, PlushBlockEntityRenderer.PlushBlockEntityRenderState*//*? }*/> {
    //? if minecraft: <26 {
    private final BlockRenderManager renderManager;
    //? } else {
    /*private final BlockModelManager models;
    *///? }

    public PlushBlockEntityRenderer(BlockEntityRendererFactory.@NotNull Context ctx) {
        //? if minecraft: <=1.21.6 {
        this.renderManager = ctx.getRenderManager();
        //? } else if minecraft: <26 {
        /*this.renderManager = ctx.renderManager();
         *///? } else {
        /*this.models = ctx.blockModelResolver();
        *///? }
    }

    //? if minecraft: >=1.21.9 {
    /*@Override
    public void render(PlushBlockEntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        matrices.push();

        matrices.scale(1, 1 - state.squish, 1);
        matrices.translate(0.5, 0, 0.5);
        matrices.scale(1 + state.squish / 2, 1, 1 + state.squish / 2);
        matrices.translate(-0.5, 0, -0.5);

        //? if minecraft: >26 {
        /^state.displayBlock.render(matrices, queue, state.lightmapCoordinates, OverlayTexture.DEFAULT_UV, 0);
        ^///? } else {
        BlockStateModel bm = this.renderManager.getModel(state.blockState);
        var mr = ((BlockRenderManagerAccessor) renderManager).composer$getModelRenderer();
        var vcp = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        mr.render(
                state.renderView, bm.getParts(Random.create()), state.blockState, state.pos, matrices,
                vcp.getBuffer(/^? if minecraft: >= 1.21.11{ ^//^RenderLayers.cutout()^//^? } else {^/RenderLayer.getCutout()/^? }^/), false, OverlayTexture.DEFAULT_UV
        );
        //? }

        matrices.pop();
    }

    @Override
    public PlushBlockEntityRenderState createRenderState() {
        return new PlushBlockEntityRenderState();
    }

    @Override
    public void updateRenderState(T be, PlushBlockEntityRenderState state, float tickDelta, Vec3d cameraPos, ModelCommandRenderer.@Nullable CrumblingOverlayCommand crumblingOverlay) {
        BlockEntityRenderState.updateBlockEntityRenderState(be, state, crumblingOverlay);
        double squish = be instanceof AbstractPlushieBlockEntity plushie ? plushie.squash : 0;
        double lastSquish = squish * 3;
        state.type = ModBlockEntities.PLUSH;
        state.squish = (float) Math.pow(1 - 1f / (1f + MathHelper.lerp(tickDelta, lastSquish, squish)), 2);

        //? if minecraft: <26
        state.renderView = be.getWorld();

        //? if minecraft: >26
        //models.setBlockRenderState(state.displayBlock, be.getCachedState(), EntityRendererId.create());
    }

    public static final class PlushBlockEntityRenderState extends BlockEntityRenderState {
        //? if minecraft: <26
        public BlockRenderView renderView = null;

        //? if minecraft: >26
        //public BlockRenderState displayBlock = new BlockRenderState();

        public float squish = 0f;
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