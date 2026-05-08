package com.codex.composer.mixin.impl.misc;

import org.spongepowered.asm.mixin.Mixin;

//? if minecraft: >=1.21.3 {
import com.codex.composer.api.v1.events.ServerRecipeManagerEvents;
import net.minecraft.recipe.ServerRecipeManager;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.recipe.RecipePropertySet;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.injection.At;

import java.util.*;
@Mixin(ServerRecipeManager.class)
//? } else {
/*import net.minecraft.recipe.RecipeManager;
*///? }

//? if minecraft: <=1.21
//@Mixin(RecipeManager.class)
public class ServerRecipeManagerMixin {
    //? if minecraft: >=1.21.3 {
    @ModifyExpressionValue(method = "initialize", at = @At(value = "INVOKE", target = "Ljava/util/Map;entrySet()Ljava/util/Set;"))
    private static Set<Map.Entry<RegistryKey<RecipePropertySet>, ServerRecipeManager.SoleIngredientGetter>> ss$addKilnRecipePropertySet(Set<Map.Entry<RegistryKey<RecipePropertySet>, ServerRecipeManager.SoleIngredientGetter>> original) {
        Set<Map.Entry<RegistryKey<RecipePropertySet>, ServerRecipeManager.SoleIngredientGetter>> mutable = new HashSet<>(original);
        HashMap<RegistryKey<RecipePropertySet>, ServerRecipeManager.SoleIngredientGetter> map = new HashMap<>();
        ServerRecipeManagerEvents.PROPERTY_SET_REGISTRATION.invoker().append(map);
        mutable.addAll(map.entrySet());
        return Collections.unmodifiableSet(mutable);
    }
    //? }
}
