package com.codex.composer.api.v1.registry.lazy;

import net.minecraft.fluid.Fluid;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class DeferredFluidRegistry extends EmptyDeferredRegistry {
    public DeferredFluidRegistry(String modId) {
        super(modId);
    }

    public <T extends Fluid> T register(String name, T fluid) {
        return Registry.register(Registries.FLUID, id(name), fluid);
    }
}
