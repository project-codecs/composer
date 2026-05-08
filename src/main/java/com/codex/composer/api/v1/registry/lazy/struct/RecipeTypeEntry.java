package com.codex.composer.api.v1.registry.lazy.struct;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;

public class RecipeTypeEntry<T extends Recipe<?>> {
    public final RecipeType<T> type;
    public final RecipeSerializer<T> serializer;

    public RecipeTypeEntry(RecipeType<T> type, RecipeSerializer<T> serializer) {
        this.type = type;
        this.serializer = serializer;
    }
}
