package com.codex.composer.api.v1.datagen.automata;

import com.codex.composer.api.v1.datagen.ComposerBlockTagProvider;
import com.codex.composer.api.v1.datagen.ComposerModelProvider;
import com.codex.composer.api.v1.datagen.ores.OreSet;
import com.codex.composer.api.v1.datagen.ores.OreSet.Variant;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

//? if >=1.21
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;

//? if >=26.1.2
//import net.minecraft.recipe.book.CookingRecipeCategory;

//? if >=1.21.3 {
import net.minecraft.data./*? if <=1.21.3 {*//*server.*//*?}*/recipe.RecipeGenerator;
import net.minecraft.data./*? if <=1.21.3 {*//*server.*//*?}*/recipe.RecipeExporter;
//?} else {
/*import net.minecraft.data.server.recipe.RecipeProvider;
//~ if <=1.20.1 'RecipeExporter' -> 'RecipeJsonProvider'
import net.minecraft.data.server.recipe.RecipeExporter;
*///?}

//? if <=1.20.1 {
/*import java.util.function.Consumer;
*///?} else {
import com.codex.composer.api.v1.datagen.util.TypedTagBuilder;
//? }

//? if >=1.21.4 {
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;
//?} else {
/*import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
*///?}

public class OreSetDataProviders {
    public static void generateFor(ComposerDataPack pack) {
        generateFor(pack, true);
    }

    public static void generateFor(ComposerDataPack pack, boolean blockTags) {
        pack.addProvider(OreRecipes::new);
        pack.addProvider(OreModels::new);
        pack.addProvider(OreLoot::new);
        if (blockTags) pack.addProvider(OreBlockTags::new);
    }

    private static String path(ItemConvertible item) {
        return Registries.ITEM.getId(item.asItem()).getPath();
    }

    //? if >=1.21.3 {
    public static void recipes(RecipeGenerator generator, OreSet set) {
        //?} else if >1.20.1 {
        /*public static void recipes(RecipeExporter generator, OreSet set) {
         *///?} else {
        /*public static void recipes(Consumer<RecipeJsonProvider> generator, OreSet set) {
         *///?}
        Item raw = set.item(Variant.RAW_MATERIAL);
        Item ingot = set.item(Variant.INGOT);
        Item nugget = set.item(Variant.NUGGET);
        Block storage = set.block(Variant.STORAGE_BLOCK);
        Block rawStorage = set.block(Variant.RAW_STORAGE_BLOCK);

        //~ if <=1.21 'generator.' -> 'RecipeProvider.' {
        if (ingot != null) {
            String ingotPath = path(ingot);

            List<ItemConvertible> inputs = set.smeltingInputs();
            if (!inputs.isEmpty()) {
                generator./*~ if >=26.1.2 'offerSmelting' -> 'offerSmeltingRecipe' {*/offerSmelting/*~}*/(/*? if <=1.21 {*//*generator, *//*?}*/inputs, RecipeCategory.MISC, /*? if >=26.1.2 {*//*CookingRecipeCategory.MISC, *//*?}*/ingot, set.smeltingExperience(), set.smeltingTime(), ingotPath);
                generator./*~ if >=26.1.2 'offerBlasting' -> 'offerBlastingRecipe' {*/offerBlasting/*~}*/(/*? if <=1.21 {*//*generator, *//*?}*/inputs, RecipeCategory.MISC, /*? if >=26.1.2 {*//*CookingRecipeCategory.MISC, *//*?}*/ingot, set.smeltingExperience(), set.blastingTime(), ingotPath);
            }

            if (storage != null) generator.offerReversibleCompactingRecipesWithReverseRecipeGroup(/*? if <=1.21 {*//*generator, *//*?}*/RecipeCategory.MISC, ingot, RecipeCategory.BUILDING_BLOCKS, storage, ingotPath + "_from_" + path(storage), ingotPath);
            if (nugget != null) generator.offerReversibleCompactingRecipesWithCompactingRecipeGroup(/*? if <=1.21 {*//*generator, *//*?}*/RecipeCategory.MISC, nugget, RecipeCategory.MISC, ingot, ingotPath + "_from_nuggets", ingotPath);
        }

        if (raw != null && rawStorage != null) {
            generator.offerReversibleCompactingRecipes(/*? if <=1.21 {*//*generator, *//*?}*/RecipeCategory.MISC, raw, RecipeCategory.BUILDING_BLOCKS, rawStorage);
        }
        //~}
    }

    private static final class OreRecipes extends FabricRecipeProvider {
        private final String modId;

        OreRecipes(FabricDataOutput output/*? if >1.20.4 {*/, CompletableFuture<RegistryWrapper.WrapperLookup> lookup/*?}*/) {
            super(output/*? if >1.20.4 {*/, lookup/*?}*/);
            this.modId = output.getModId();
        }

        //? if >=1.21.3 {
        @Override
        protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registries, RecipeExporter exporter) {
            return new RecipeGenerator(registries, exporter) {
                @Override
                public void generate() {
                    for (OreSet set : OreSet.allOf(modId)) recipes(this, set);
                }
            };
        }
        //?} else if >1.20.1 {
        /*@Override
        public void generate(RecipeExporter exporter) {
            for (OreSet set : OreSet.allOf(modId)) recipes(exporter, set);
        }
        *///?} else {
        /*@Override
        public void generate(Consumer<RecipeJsonProvider> exporter) {
            for (OreSet set : OreSet.allOf(modId)) recipes(exporter, set);
        }
        *///?}

        @Override
        public String getName() {
            return "Ore Set Recipes";
        }
    }

    public static void tags(OreSet set, BiConsumer<TagKey<Block>, Block> out) {
        TagKey<Block> tier = set.tier().tag();

        for (Variant variant : set.variants()) {
            if (!variant.isBlock()) continue;
            Block block = set.block(variant);

            out.accept(BlockTags.PICKAXE_MINEABLE, block);
            if (tier != null) out.accept(tier, block);

            switch (variant) {
                case STONE_ORE, DEEPSLATE_ORE -> {
                    //? if >=1.21
                    out.accept(ConventionalBlockTags.ORES, block);

                    //? if >=1.21.4 {
                    out.accept(ConventionalBlockTags.ORE_RATES_SINGULAR, block);
                    out.accept(variant == Variant.STONE_ORE ? ConventionalBlockTags.ORES_IN_GROUND_STONE : ConventionalBlockTags.ORES_IN_GROUND_DEEPSLATE, block);
                    //? }

                    out.accept(BlockTags.SNAPS_GOAT_HORN, block);
                    out.accept(BlockTags.OVERWORLD_CARVER_REPLACEABLES, block);
                }
                case STORAGE_BLOCK, RAW_STORAGE_BLOCK -> {
                    //? if >=1.21
                    out.accept(ConventionalBlockTags.STORAGE_BLOCKS, block);
                }
                default -> { }
            }
        }
    }

    private static final class OreBlockTags extends ComposerBlockTagProvider {
        private final String modId;

        OreBlockTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> lookup) {
            super(output, lookup);
            this.modId = output.getModId();
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup lookup) {
            //? if >1.20.1 {
            for (OreSet set : OreSet.allOf(modId)) tags(set, (tag, block) -> TypedTagBuilder.of(getTagBuilder(tag), Registries.BLOCK).add(block));
            //? } else {
//            for (OreSet set : OreSet.allOf(modId)) tags(set, (tag, block) -> getOrCreateTagBuilder(tag).add(block));
            //? }
        }

        @Override
        public String getName() {
            return "Ore Set Block Tags";
        }
    }

    private static final class OreModels extends ComposerModelProvider {
        private final String modId;

        OreModels(FabricDataOutput output) {
            super(output);
            this.modId = output.getModId();
        }

        @Override
        public void generateBlocks(BlockStateModelGenerator generator) {
            for (OreSet set : OreSet.allOf(modId)) {
                for (Variant variant : set.variants()) {
                    if (variant.isBlock()) generator.registerSimpleCubeAll(set.block(variant));
                }
            }
        }

        @Override
        public void generateItems(ItemModelGenerator generator) {
            for (OreSet set : OreSet.allOf(modId)) {
                for (Variant variant : set.variants()) {
                    if (!variant.isBlock()) generator.register(set.item(variant), Models.GENERATED);
                    else blockItemModelFor(set.item(variant), set.block(variant));
                }
            }
        }

        @Override
        public String getName() {
            return "Ore Set Models";
        }
    }

    private static final class OreLoot extends FabricBlockLootTableProvider {
        private final String modId;

        OreLoot(FabricDataOutput output/*? if >1.20.4 {*/, CompletableFuture<RegistryWrapper.WrapperLookup> lookup/*?}*/) {
            super(output/*? if >1.20.4 {*/, lookup/*?}*/);
            this.modId = output.getModId();
        }

        @Override
        public void generate() {
            for (OreSet set : OreSet.allOf(modId)) {
                Item drop = set.has(Variant.RAW_MATERIAL) ? set.item(Variant.RAW_MATERIAL) : set.item(Variant.INGOT);

                for (Variant variant : set.variants()) {
                    if (!variant.isBlock()) continue;
                    Block block = set.block(variant);

                    if (variant == Variant.STONE_ORE || variant == Variant.DEEPSLATE_ORE) addDrop(block, oreDrops(block, drop));
                    else addDrop(block);
                }
            }
        }

        @Override
        public String getName() {
            return "Ore Set Loot Tables";
        }
    }
}