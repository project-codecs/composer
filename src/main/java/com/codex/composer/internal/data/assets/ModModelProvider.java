package com.codex.composer.internal.data.assets;

import com.codex.composer.api.v1.datagen.ComposerModelProvider;
import com.codex.composer.internal.registry.ModBlocks;
import com.codex.composer.internal.registry.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

//? if minecraft: <=1.21.3
//import net.minecraft.data.client.*;

//? if minecraft: >=1.21.4
import net.minecraft.client.data.*;

public class ModModelProvider extends ComposerModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlocks(BlockStateModelGenerator generator) {

    }

    @Override
    public void generateItems(ItemModelGenerator generator) {
        blockItemModelFor(ModItems.PLUSHIE, ModBlocks.PLUSH);
    }
}
