package com.codex.composer.api.v1.registry.lazy;

//? if minecraft: >=1.21.4 {
import com.codex.composer.api.v1.util.data.IdentifierMap;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.fabricmc.fabric.api.client.rendering.v1.LayeredDrawerWrapper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.LayeredDrawer;
import net.minecraft.client.render.RenderTickCounter;

import java.util.function.BiConsumer;

public class DeferredHudLayerRegistry extends EmptyDeferredRegistry {
    private final IdentifierMap<LayeredDrawer.Layer> layers;

    public DeferredHudLayerRegistry(String modId) {
        super(modId);
        this.layers = new IdentifierMap<>(modId);
        HudLayerRegistrationCallback.EVENT.register(this::register);
    }

    public void register(String name, LayeredDrawer.Layer render) {
        this.layers.put(name, render);
    }

    public void register(String name, BiConsumer<DrawContext, RenderTickCounter> render) {
        register(name, (LayeredDrawer.Layer) render);
    }

    private void register(LayeredDrawerWrapper wrapper) {
        layers.registerTo(wrapper::addLayer, IdentifiedLayer::of);
    }
}
//? }
