package com.codex.composer.api.v1.registry.lazy;

import net.minecraft.util.Identifier;
import com.codex.composer.api.v1.tooltips.DynamicTooltip;
import com.codex.composer.api.v1.tooltips.DynamicTooltipRegistry;

import java.util.function.Supplier;

public class DeferredDynamicTooltipRegistry extends EmptyDeferredRegistry {
    private final DynamicTooltipRegistry reg = DynamicTooltipRegistry.getInstance();

    public DeferredDynamicTooltipRegistry(String modId) {
        super(modId);
    }

    public <T extends DynamicTooltip> T register(String name, T tooltip) {
        return reg.register(Identifier.of(modId, name), tooltip);
    }

    public <T extends DynamicTooltip> T add(String name, Supplier<T> tooltip) {
        return register(name, tooltip.get());
    }
}
