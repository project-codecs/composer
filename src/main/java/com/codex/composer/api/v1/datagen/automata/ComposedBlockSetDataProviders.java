package com.codex.composer.api.v1.datagen.automata;

import com.codex.composer.api.v1.datagen.ComposerBlockTagProvider;
import com.codex.composer.api.v1.datagen.ComposerModelProvider;
import com.codex.composer.api.v1.registry.util.ComposedBlockFamily;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

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

public class ComposedBlockSetDataProviders {
    public static void generateFor(ComposerDataPack pack) {
        generateFor(pack, true);
    }

    public static void generateFor(ComposerDataPack pack, boolean blockTags) {
        pack.addProvider(BlockSetRecipes::new);
        pack.addProvider(BlockSetModels::new);
        pack.addProvider(BlockSetLoot::new);
        if (blockTags) pack.addProvider(BlockSetTags::new);
    }

    private static String path(ItemConvertible item) {
        return Registries.ITEM.getId(item.asItem()).getPath();
    }

    //~ if <=1.21 'RecipeGenerator' -> 'RecipeExporter'
    //~ if <=1.20.1 'RecipeGenerator' -> 'Consumer<RecipeJsonProvider>'
    public static void recipes(RecipeGenerator generator, ComposedBlockFamily set) {
        set.generateRecipes(generator);
    }

    private static final class BlockSetRecipes extends FabricRecipeProvider {
        private final String modId;

        BlockSetRecipes(FabricDataOutput output/*? if >1.20.4 {*/, CompletableFuture<RegistryWrapper.WrapperLookup> lookup/*?}*/) {
            super(output/*? if >1.20.4 {*/, lookup/*?}*/);
            this.modId = output.getModId();
        }

        //? if >=1.21.3 {
        @Override
        protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registries, RecipeExporter exporter) {
            return new RecipeGenerator(registries, exporter) {
                @Override
                public void generate() {
                    for (ComposedBlockFamily family : ComposedBlockFamily.allOf(modId)) recipes(this, family);
                }
            };
        }
        //?} else if >1.20.1 {
        /*@Override
        public void generate(RecipeExporter exporter) {
            for (ComposedBlockFamily family : ComposedBlockFamily.allOf(modId)) recipes(exporter, family);
        }
        *///?} else {
        /*@Override
        public void generate(Consumer<RecipeJsonProvider> exporter) {
            for (ComposedBlockFamily family : ComposedBlockFamily.allOf(modId)) recipes(exporter, family);
        }
        *///?}

        @Override
        public String getName() {
            return "Composed Block Set Recipes";
        }
    }

    private static final class BlockSetTags extends ComposerBlockTagProvider {
        private final String modId;

        BlockSetTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> lookup) {
            super(output, lookup);
            this.modId = output.getModId();
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup lookup) {
            for (ComposedBlockFamily family : ComposedBlockFamily.allOf(modId)) family(family);
        }

        @Override
        public String getName() {
            return "Composed Block Set Block Tags";
        }
    }

    private static final class BlockSetModels extends ComposerModelProvider {
        private final String modId;

        BlockSetModels(FabricDataOutput output) {
            super(output);
            this.modId = output.getModId();
        }

        @Override
        public void generateBlocks(BlockStateModelGenerator generator) {
            for (ComposedBlockFamily family : ComposedBlockFamily.allOf(modId)) cubeAllBlockFamily(generator, family);
        }

        @Override
        public void generateItems(ItemModelGenerator generator) {
            for (ComposedBlockFamily family : ComposedBlockFamily.allOf(modId)) blockItemModelsFor(Objects.requireNonNull(family.itemFamily()));
        }

        @Override
        public String getName() {
            return "Composed Block Set Models";
        }
    }

    private static final class BlockSetLoot extends FabricBlockLootTableProvider {
        private final String modId;

        BlockSetLoot(FabricDataOutput output/*? if >1.20.4 {*/, CompletableFuture<RegistryWrapper.WrapperLookup> lookup/*?}*/) {
            super(output/*? if >1.20.4 {*/, lookup/*?}*/);
            this.modId = output.getModId();
        }

        @Override
        public void generate() {
            for (ComposedBlockFamily family : ComposedBlockFamily.allOf(modId)) {
                if (family.ownsBase()) addDrop(family.getBase());
                family.getVariants().stream().map(family::get).filter(Objects::nonNull).forEach(this::addDrop);
            }
        }

        @Override
        public String getName() {
            return "Composed Block Set Loot Tables";
        }
    }
}