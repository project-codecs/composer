package com.codex.composer.api.v1.events;

//? if minecraft: >=1.21.3 {
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.recipe.RecipePropertySet;
import net.minecraft.recipe.ServerRecipeManager;
import net.minecraft.registry.RegistryKey;

import java.util.Map;

public class ServerRecipeManagerEvents {
    public static final Event<PropertySetRegistration> PROPERTY_SET_REGISTRATION = EventFactory.createArrayBacked(PropertySetRegistration.class, (callbacks) -> (values) -> {
        for (PropertySetRegistration callback : callbacks) {
            callback.append(values);
        }
    });

    @FunctionalInterface
    public interface PropertySetRegistration {
        void append(Map<RegistryKey<RecipePropertySet>, ServerRecipeManager.SoleIngredientGetter> values);
    }
}
//? }
