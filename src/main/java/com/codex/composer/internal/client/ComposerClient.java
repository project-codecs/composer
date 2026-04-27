package com.codex.composer.internal.client;

import com.codex.composer.internal.networking.*;
import com.codex.composer.internal.overlay.OverlayHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import com.codex.composer.internal.Composer;

import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import com.codex.composer.internal.client.render.block_entity.PlushBlockEntityRenderer;
import com.codex.composer.internal.registry.ModBlockEntities;
import com.codex.composer.internal.registry.ModBlocks;

//? if minecraft: >=1.21.4 <=1.21.5 {
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
//? } else if minecraft: >=1.21.6 {
/*import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
*///? } else {
/*import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
 *///? }

//? if minecraft: <=1.21.5 {
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
//? } else {
/*import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.render.BlockRenderLayer;
*///? }

public class ComposerClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        //? if minecraft: <=1.21.5 {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PLUSH, RenderLayer.getCutout());
        //? } else {
        /*BlockRenderLayerMap.putBlock(ModBlocks.PLUSH, BlockRenderLayer.CUTOUT);
        *///? }
        BlockEntityRendererFactories.register(ModBlockEntities.PLUSH, PlushBlockEntityRenderer::new);

        //? if minecraft: >=1.21.4 <=1.21.5 {
        HudLayerRegistrationCallback.EVENT.register(w -> w.addLayer(IdentifiedLayer.of(Composer.identify("overlays"), OverlayHandler::render)));
        //? } else if minecraft: >=1.21.5 {
        /*HudElementRegistry.addLast(Composer.identify("overlays"), OverlayHandler::render);
        *///? } else {
        /*HudRenderCallback.EVENT.register(OverlayHandler::render);
        *///? }

        ClientTickEvents.START_CLIENT_TICK.register(OverlayHandler::tick);

        ClearOverlaysPayload.registerHandler();
        ShowOverlayPayload.registerHandler();
        ShowCreditsPayload.registerHandler();
    }
}
