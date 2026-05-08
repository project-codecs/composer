package com.codex.composer.internal.data.assets;

import com.codex.composer.api.v1.datagen.ComposerModelProvider;
import com.codex.composer.internal.registry.ModBlocks;
import com.codex.composer.internal.registry.ModItems;

//? if minecraft: <=1.21.3 {
/*import net.minecraft.data.client.*;
*///? } else
import net.minecraft.client.data.*;

//? legacy {
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
//? } else
//import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;

public class ModModelProvider extends ComposerModelProvider {
    public ModModelProvider(/*? if legacy {*/FabricDataOutput/*? } else {*//*FabricPackOutput*//*? }*/ output) {
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
