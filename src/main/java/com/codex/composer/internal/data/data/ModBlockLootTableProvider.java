package com.codex.composer.internal.data.data;

import net.minecraft.registry.RegistryWrapper;
import com.codex.composer.internal.registry.ModBlocks;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

public class ModBlockLootTableProvider extends FabricBlockLootTableProvider {
    //? if minecraft: <=1.20.4 {
    /*public ModBlockLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }
    *///? } else {
    public ModBlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }
    //? }

    @Override
    public void generate() {
        addDrop(ModBlocks.PLUSH);
    }
}
