package com.codex.composer.api.v1.registry.lazy;

//? if minecraft: >=1.21.4 <=1.21.5 {
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
//? } else if minecraft: >=1.21.6 {
/*import com.codex.composer.api.v1.util.data.IdentifierMap;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

import java.util.function.BiConsumer;

public class DeferredHudLayerRegistry extends EmptyDeferredRegistry {
    private final IdentifierMap<HudElement> first;
    private final IdentifierMap<HudElement> last;

    public DeferredHudLayerRegistry(String modId) {
        super(modId);
        first = new IdentifierMap<>(modId);
        last = new IdentifierMap<>(modId);
    }

    public void registerFirst(String name, HudElement element) {
        first.put(name, element);
    }

    public void registerFirst(String name, BiConsumer<DrawContext, RenderTickCounter> render) {
        registerFirst(name, (HudElement) render);
    }

    public void registerLast(String name, HudElement element) {
        last.put(name, element);
    }

    public void registerLast(String name, BiConsumer<DrawContext, RenderTickCounter> render) {
        registerLast(name, (HudElement) render);
    }

    public void finish() {
        first.registerTo(HudElementRegistry::addFirst);
        last.registerTo(HudElementRegistry::addLast);
    }
}
*///? }
