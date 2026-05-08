package com.codex.composer.internal.data.data;

import net.minecraft.registry.RegistryWrapper;
import com.codex.composer.internal.registry.ModBlocks;

import java.util.concurrent.CompletableFuture;

//? legacy {
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
//? } else {
/*import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
*///? }

public class ModBlockLootTableProvider extends /*? if legacy {*/FabricBlockLootTableProvider/*? } else {*//*FabricBlockLootSubProvider*//*? }*/ {
    //? if minecraft: <=1.20.4 {
    /*public ModBlockLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }
    *///? } else {
    public ModBlockLootTableProvider(/*? if legacy {*/FabricDataOutput/*? } else {*//*FabricPackOutput*//*? }*/ dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }
    //? }

    @Override
    public void generate() {
        addDrop(ModBlocks.PLUSH);
    }
}
