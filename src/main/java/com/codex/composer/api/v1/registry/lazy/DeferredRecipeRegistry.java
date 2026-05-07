package com.codex.composer.api.v1.registry.lazy;

import com.codex.composer.api.v1.registry.lazy.struct.RecipeTypeEntry;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

//? if minecraft: >=1.21.3 {
import net.minecraft.recipe.RecipePropertySet;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.registry.RegistryKey;
//? }

public class DeferredRecipeRegistry extends EmptyDeferredRegistry {
    public DeferredRecipeRegistry(String modId) {
        super(modId);
    }

    public <T extends Recipe<?>> RecipeTypeEntry<T> register(String name, RecipeSerializer<T> serializer) {
        Identifier id = Identifier.of(modId, name);

        RecipeType<T> type = Registry.register(
                Registries.RECIPE_TYPE,
                id,
                new RecipeType<T>() {
                    @Override
                    public String toString() {
                        return id.toString();
                    }
                }
        );

        RecipeSerializer<T> recipeSerializer = Registry.register(
                Registries.RECIPE_SERIALIZER,
                id,
                serializer
        );

        return new RecipeTypeEntry<>(type, recipeSerializer);
    }

    //? if minecraft: >=1.21.3 {
    public RecipeBookCategory registerCategory(String name) {
        return Registry.register(
                Registries.RECIPE_BOOK_CATEGORY,
                Identifier.of(modId, name),
                new RecipeBookCategory()
        );
    }

    public RegistryKey<RecipePropertySet> registerPropertySet(String name) {
        return RegistryKey.of(RecipePropertySet.REGISTRY, Identifier.of(modId, name));
    }
    //? }
}
