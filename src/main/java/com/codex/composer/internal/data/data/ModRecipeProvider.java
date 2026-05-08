package com.codex.composer.internal.data.data;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;

import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;

import static net.minecraft.advancement.criterion.InventoryChangedCriterion.Conditions.items;
import static net.minecraft.item.Items.*;
import static com.codex.composer.internal.registry.ModItems.PLUSHIE;

//? if minecraft: <=1.20.1 {
/*import net.minecraft.data.server.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;

import java.util.function.Consumer;
*///? } else if minecraft: <=1.21.3 {
/*import net.minecraft.data.server.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;

import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;
*///? } else {
import net.minecraft.data.recipe.CookingRecipeJsonBuilder;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.ShapedRecipeJsonBuilder;

import java.util.concurrent.CompletableFuture;
import net.minecraft.data.recipe.RecipeGenerator;
//?}

//? if minecraft: =1.21.3
//import net.minecraft.data.server.recipe.RecipeGenerator;

//? if minecraft: >=1.21.3
import net.minecraft.registry.RegistryKeys;

//? legacy {
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
//? } else
//import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;

public class ModRecipeProvider extends FabricRecipeProvider {
    //? if minecraft: <=1.20.4 {
    /*public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }
    *///? } else {
    public ModRecipeProvider(/*? if legacy {*/FabricDataOutput/*? } else {*//*FabricPackOutput*//*? }*/ output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    public String getName() {
        return "Recipes";
    }
    //?}

    @Override
    //? if minecraft: <=1.20.1 {
    /*public void generate(Consumer<RecipeJsonProvider> exporter) {
    *///? } else if minecraft: <1.21.3 {
    /*public void generate(RecipeExporter exporter) {
    *///? } else {
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
    //?}
        //? minecraft: >=1.21.3 {
        return new RecipeGenerator(registryLookup, exporter) {
            @Override
            public void generate() {
                ShapedRecipeJsonBuilder.create(registryLookup.getOrThrow(RegistryKeys.ITEM), RecipeCategory.DECORATIONS, PLUSHIE)
                        //? } else {
                        /*ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, PLUSHIE)
                         *///? }
                        .pattern("WBW")
                        .pattern("NBP")
                        .pattern("P N")
                        .input('B', BLACK_WOOL)
                        .input('P', PURPLE_WOOL)
                        .input('W', WHITE_WOOL)
                        .input('N', BROWN_WOOL)
                        .criterion("has_wool", items(BLACK_WOOL, PURPLE_WOOL, WHITE_WOOL, BROWN_WOOL))
                        .offerTo(exporter);

                CookingRecipeJsonBuilder.createCampfireCooking(
                        Ingredient.ofItems(PLUSHIE),
                        RecipeCategory.MISC,
                        /*? if minecraft: <=1.20.1 { *//*AIR*//*? } else { */PLUSHIE/*? }*/,
                        0f,
                        10
                ).criterion("has_plush", items(PLUSHIE)).offerTo(exporter, "cook_plush");

            //? if minecraft: >= 1.21.3 {
            }};
            //? }
    }
}
