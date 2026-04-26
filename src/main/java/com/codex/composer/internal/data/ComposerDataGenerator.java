package com.codex.composer.internal.data;

import com.codex.composer.internal.data.assets.ModLanguageProvider;
import com.codex.composer.internal.data.assets.ModModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import com.codex.composer.internal.data.data.ModBlockLootTableProvider;
import com.codex.composer.internal.data.data.ModRecipeProvider;

public class ComposerDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModLanguageProvider::new);
        pack.addProvider(ModModelProvider::new);

        pack.addProvider(ModBlockLootTableProvider::new);
        pack.addProvider(ModRecipeProvider::new);
    }
}
