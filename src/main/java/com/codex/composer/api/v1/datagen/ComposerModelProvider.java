package com.codex.composer.api.v1.datagen;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import net.minecraft.block.Block;
import net.minecraft.client.render.model.json.BlockModelDefinition;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.registry.Registries;

//? if minecraft: <=1.21.3 {
/*import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateSupplier;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.item.Item;
import net.minecraft.data.client.Model;
import net.minecraft.util.Identifier;
import java.util.HashSet;
import java.util.Set;
*///? } else {
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ItemAsset;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import net.minecraft.client.data.*;
import org.apache.commons.lang3.NotImplementedException;
//? }

//? if minecraft: <=1.21.3 {
/*public abstract class ComposerModelProvider implements DataProvider {
    private ItemModelGenerator itemModelGenerator;
    private final DataOutput.PathResolver blockstatesPathResolver;
    private final DataOutput.PathResolver modelsPathResolver;

    public ComposerModelProvider(FabricDataOutput output) {
        this.blockstatesPathResolver = output.getResolver(DataOutput.OutputType.RESOURCE_PACK, "blockstates");
        this.modelsPathResolver = output.getResolver(DataOutput.OutputType.RESOURCE_PACK, "models");
    }

    public abstract void generateBlocks(BlockStateModelGenerator generator);
    public abstract void generateItems(ItemModelGenerator generator);

    public void blockItemModelFor(Item item, Block from) {
        Identifier id = Registries.BLOCK.getId(from);
        itemModelGenerator.register(item, new Model(Optional.ofNullable(Identifier.of(id.getNamespace(), "block/" + id.getPath())), Optional.empty()));
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        Map<Block, BlockStateSupplier> blockStates = new HashMap<>();
        Consumer<BlockStateSupplier> blockConsumer = blockStateSupplier -> {
            Block block = blockStateSupplier.getBlock();
            if (blockStates.put(block, blockStateSupplier) != null) {
                throw new IllegalStateException("Duplicate blockstate definition for " + block);
            }
        };

        Map<Identifier, Supplier<JsonElement>> models = new HashMap<>();
        BiConsumer<Identifier, Supplier<JsonElement>> modelConsumer = (id, supplier) -> {
            if (models.put(id, supplier) != null) {
                throw new IllegalStateException("Duplicate model definition for " + id);
            }
        };

        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        Set<Item> itemModels = new HashSet<>();
        Consumer<Item> itemConsumer = itemModels::add;

        BlockStateModelGenerator blockStateModelGenerator = new BlockStateModelGenerator(blockConsumer, modelConsumer, itemConsumer);
        itemModelGenerator = new ItemModelGenerator(modelConsumer);

        generateBlocks(blockStateModelGenerator);
        generateItems(itemModelGenerator);

        //noinspection deprecation
        return CompletableFuture.allOf(
                writeJsons(writer, blockStates, b -> blockstatesPathResolver.resolveJson(b.getRegistryEntry().registryKey().getValue())),
                writeJsons(writer, models, modelsPathResolver::resolveJson)
        );
    }

    @Override
    public String getName() {
        return "Models";
    }

    private <T> CompletableFuture<?> writeJsons(DataWriter writer, Map<T, ? extends Supplier<JsonElement>> map, Function<T, Path> pathGetter) {
        return CompletableFuture.allOf(map.entrySet().stream()
                .map(entry -> DataProvider.writeToPath(writer, entry.getValue().get(), pathGetter.apply(entry.getKey())))
                .toArray(CompletableFuture[]::new));
    }
}
*///?} else {
@SuppressWarnings("deprecation")
@Environment(EnvType.CLIENT)
public abstract class ComposerModelProvider implements DataProvider {
    private ItemModelGenerator itemModelGenerator;
    private final DataOutput.PathResolver blockstatesPathResolver;
    private final DataOutput.PathResolver itemsPathResolver;
    private final DataOutput.PathResolver modelsPathResolver;

    public ComposerModelProvider(DataOutput output) {
        this.blockstatesPathResolver = output.getResolver(DataOutput.OutputType.RESOURCE_PACK, "blockstates");
        this.itemsPathResolver = output.getResolver(DataOutput.OutputType.RESOURCE_PACK, "items");
        this.modelsPathResolver = output.getResolver(DataOutput.OutputType.RESOURCE_PACK, "models");
    }

    public abstract void generateBlocks(BlockStateModelGenerator generator);
    public abstract void generateItems(ItemModelGenerator generator);

    public void blockItemModelFor(Item item, Block from) {
        Identifier id = Registries.BLOCK.getId(from);
        itemModelGenerator.register(item, new Model(Optional.of(Identifier.of(id.getNamespace(), "block/" + id.getPath())), Optional.empty()));
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        BlockStateSuppliers blockStateSuppliers = new BlockStateSuppliers();
        ItemAssets itemAssets = new ItemAssets();
        ModelSuppliers modelSuppliers = new ModelSuppliers();

        BlockStateModelGenerator blockGenerator = new BlockStateModelGenerator(blockStateSuppliers, itemAssets, modelSuppliers);
        itemModelGenerator = new ItemModelGenerator(itemAssets, modelSuppliers);

        generateBlocks(blockGenerator);
        generateItems(itemModelGenerator);

        return CompletableFuture.allOf(
                blockStateSuppliers.writeAllToPath(writer, blockstatesPathResolver),
                modelSuppliers.writeAllToPath(writer, modelsPathResolver),
                itemAssets.writeAllToPath(writer, itemsPathResolver)
        );
    }

    @Override
    public String getName() {
        return "Models";
    }

    @Environment(EnvType.CLIENT)
    public static class ModelSuppliers implements BiConsumer<Identifier, ModelSupplier> {
        private final Map<Identifier, ModelSupplier> modelSuppliers = new HashMap<>();

        ModelSuppliers() {
        }

        public void accept(Identifier identifier, ModelSupplier modelSupplier) {
            Supplier<JsonElement> supplier = this.modelSuppliers.put(identifier, modelSupplier);
            if (supplier != null) {
                throw new IllegalStateException("Duplicate model definition for " + identifier);
            }
        }

        public CompletableFuture<?> writeAllToPath(DataWriter writer, DataOutput.PathResolver pathResolver) {
            Objects.requireNonNull(pathResolver);
            return ComposerModelProvider.writeAllToPath(writer, pathResolver::resolveJson, this.modelSuppliers);
        }
    }

    @Environment(EnvType.CLIENT)
    public static class BlockStateSuppliers implements Consumer</*? if minecraft: <=1.21.4 { *//*BlockStateSupplier*//*? } else {*/BlockModelDefinitionCreator/*?}*/> {
        private final Map<Block, /*? if minecraft: <=1.21.4 { *//*BlockStateSupplier*//*? } else {*/BlockModelDefinitionCreator/*?}*/> blockStateSuppliers = new HashMap<>();

        BlockStateSuppliers() {
        }

        public void accept(/*? if minecraft: <=1.21.4 { *//*BlockStateSupplier*//*? } else {*/BlockModelDefinitionCreator/*?}*/ blockStateSupplier) {
            Block block = blockStateSupplier.getBlock();
            /*? if minecraft: <=1.21.4 { *//*BlockStateSupplier*//*? } else {*/BlockModelDefinitionCreator/*?}*/ blockStateSupplier2 = this.blockStateSuppliers.put(block, blockStateSupplier);
            if (blockStateSupplier2 != null) {
                throw new IllegalStateException("Duplicate blockstate definition for " + block);
            }
        }

        //? if minecraft: <=1.21.4 {
        /*public CompletableFuture<?> writeAllToPath(DataWriter writer, DataOutput.PathResolver pathResolver) {
            return ComposerModelProvider.writeAllToPath(writer, (block) -> pathResolver.resolveJson(block.getRegistryEntry().registryKey().getValue()), this.blockStateSuppliers);
        }
        *///? } else {
        public CompletableFuture<?> writeAllToPath(DataWriter dataWriter, DataOutput.PathResolver pathResolver) {
            Map<Block, BlockModelDefinition> map = Maps.transformValues(this.blockStateSuppliers, BlockModelDefinitionCreator::createBlockModelDefinition);
            Function<Block, Path> function = (block) -> pathResolver.resolveJson(block.getRegistryEntry().registryKey().getValue());
            return DataProvider.writeAllToPath(dataWriter, BlockModelDefinition.CODEC, function, map);
        }
        //? }
    }

    @Environment(EnvType.CLIENT)
    public static class ItemAssets implements ItemModelOutput {
        private final Map<Item, ItemAsset> itemAssets = new HashMap<>();

        ItemAssets() {
        }

        public void accept(Item item, ItemModel.Unbaked model) {
            this.accept(item, new ItemAsset(model, ItemAsset.Properties.DEFAULT));
        }

        private void accept(Item item, ItemAsset asset) {
            ItemAsset itemAsset = this.itemAssets.put(item, asset);
            if (itemAsset != null) {
                throw new IllegalStateException("Duplicate item model definition for " + item);
            }
        }

        public void acceptAlias(Item base, Item alias) {
            throw new NotImplementedException();
        }

        public CompletableFuture<?> writeAllToPath(DataWriter writer, DataOutput.PathResolver pathResolver) {
            return DataProvider.writeAllToPath(writer, ItemAsset.CODEC, (item) -> pathResolver.resolveJson(item.getRegistryEntry().registryKey().getValue()), this.itemAssets);
        }
    }

    protected static <T> CompletableFuture<?> writeAllToPath(DataWriter writer, Function<T, Path> pathResolver, Map<T, ? extends Supplier<JsonElement>> idsToValues) {
        return DataProvider.writeAllToPath(writer, Supplier::get, pathResolver, idsToValues);
    }
}
//?}
