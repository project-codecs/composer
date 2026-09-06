package com.codex.composer.api.v1.registry.util;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.recipe.Ingredient;

//? if minecraft: <=1.20.4 {
/*import net.minecraft.registry.tag.TagKey;
*///? } else {
import net.minecraft.registry.entry.RegistryEntry;
//?}

public class BrewingRecipeUtils {
    //? if minecraft: <=1.20.4 {
    /*public static void register(Potion input, Item item, Potion output) {
        BrewingRecipeRegistry.registerPotionRecipe(input, item, output);
    }

    public static void register(Potion input, Ingredient ingredient, Potion output) {
        BrewingRecipeRegistry.POTION_RECIPES.add(new BrewingRecipeRegistry.Recipe<>(input, ingredient, output));
    }

    public static void register(Potion input, TagKey<Item> tag, Potion output) {
        register(input, Ingredient.fromTag(tag), output);
    }

    public static void awkward(Item item, Potion output) {
        register(Potions.AWKWARD, item, output);
    }

    public static void awkward(Ingredient ingredient, Potion output) {
        register(Potions.AWKWARD, ingredient, output);
    }
    *///?} else {
    public static void register(RegistryEntry<Potion> input, Item item, RegistryEntry<Potion> output) {
        BrewingRecipeRegistry.Builder builder = new BrewingRecipeRegistry.Builder(null);
        builder.registerPotionRecipe(input, item, output);
    }

    public static void register(Potion input, Item item, Potion output) {
        register(RegistryEntry.of(input), item, RegistryEntry.of(output));
    }

    public static void register(Potion input, Ingredient ingredient, Potion output) {
        register(RegistryEntry.of(input), ingredient, RegistryEntry.of(output));
    }

    public static void register(Potion input, Item item, RegistryEntry<Potion> output) {
        register(RegistryEntry.of(input), item, output);
    }

    public static void register(Potion input, Ingredient ingredient, RegistryEntry<Potion> output) {
        register(RegistryEntry.of(input), ingredient, output);
    }



    public static void register(RegistryEntry<Potion> input, Ingredient ingredient, RegistryEntry<Potion> output) {
        BrewingRecipeRegistry.Builder builder = new BrewingRecipeRegistry.Builder(null);
        builder.registerPotionRecipe(input, ingredient, output);
    }

    public static void awkward(Item item, Potion output) {
        register(Potions.AWKWARD, item, RegistryEntry.of(output));
    }

    public static void awkward(Ingredient ingredient, Potion output) {
        register(Potions.AWKWARD, ingredient, RegistryEntry.of(output));
    }
    //?}

    public static void redstone(Potion base, Potion extended) {
        register(base, Items./*? if minecraft: >=26.2 { *//*REDSTONE_WIRE*//*? } else { */REDSTONE/*? }*/, extended);
    }

    public static void glowstone(Potion base, Potion stronger) {
        register(base, Items.GLOWSTONE_DUST, stronger);
    }

    public static void corrupt(Potion base, Potion corrupted) {
        register(base, Items.FERMENTED_SPIDER_EYE, corrupted);
    }

    public static void chain(Item item, Potion normal, Potion longVariant, Potion strongVariant) {
        register(normal, item, longVariant);
        register(strongVariant, item, longVariant);
    }

    public static void standard(Potion baseNormal, Potion baseLong, Potion baseStrong, Item catalyst) {
        awkward(catalyst, baseNormal);
        redstone(baseNormal, baseLong);
        glowstone(baseNormal, baseStrong);
    }

    public static void autoSplashLingering(Potion base, Potion splash, Potion lingering) {
        register(base, Items.GUNPOWDER, splash);
        register(splash, Items.DRAGON_BREATH, lingering);
    }
}
