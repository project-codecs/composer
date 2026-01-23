package com.codex.composer.internal.client;

import com.codex.composer.api.v1.toast.ToastManager;
import com.codex.composer.internal.overlay.OverlayHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import com.codex.composer.internal.client.render.block_entity.PlushBlockEntityRenderer;
import com.codex.composer.internal.networking.ClearOverlaysPayload;
import com.codex.composer.internal.networking.ClearToastsPayload;
import com.codex.composer.internal.networking.ShowOverlayPayload;
import com.codex.composer.internal.networking.TriggerToastPayload;
import com.codex.composer.internal.registry.ModBlockEntities;
import com.codex.composer.internal.registry.ModBlocks;

//? if minecraft: >=1.21.4 {
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.fabricmc.fabric.api.client.rendering.v1.LayeredDrawerWrapper;
import net.minecraft.client.gui.LayeredDrawer;
import com.codex.composer.internal.Composer;
//? } else {
/*import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
*///? }

public class ComposerClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PLUSH, RenderLayer.getCutout());
        BlockEntityRendererFactories.register(ModBlockEntities.PLUSH, PlushBlockEntityRenderer::new);

        //? if minecraft: >=1.21.4 {
        HudLayerRegistrationCallback.EVENT.register(w -> addLayer(w, "toasts", ToastManager.getInstance()::render));
        HudLayerRegistrationCallback.EVENT.register(w -> addLayer(w, "overlays", OverlayHandler::render));
        //? } else {
        /*HudRenderCallback.EVENT.register(ToastManager.getInstance()::render);
        HudRenderCallback.EVENT.register(OverlayHandler::render);
        *///? }

        ClientTickEvents.START_CLIENT_TICK.register(OverlayHandler::tick);

        ClearToastsPayload.registerHandler();
        ClearOverlaysPayload.registerHandler();
        TriggerToastPayload.registerHandler();
        ShowOverlayPayload.registerHandler();
    }

    //? if minecraft: >=1.21.4 {
    private void addLayer(LayeredDrawerWrapper wrapper, String name, LayeredDrawer.Layer render) {
        wrapper.addLayer(IdentifiedLayer.of(Composer.identify(name), render));
    }
    //? }
}
