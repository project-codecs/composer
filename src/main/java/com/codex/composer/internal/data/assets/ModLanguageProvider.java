package com.codex.composer.internal.data.assets;

import com.codex.composer.api.v1.datagen.lang.ComposerMultiLanguageProvider;
import com.codex.composer.internal.data.assets.languages.ModEnglishProvider;
import com.codex.composer.internal.data.assets.languages.ModHungarianProvider;
import com.codex.composer.internal.data.assets.languages.ModLolcatProvider;
import com.codex.composer.internal.data.assets.languages.ModShakespeareanProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

//? legacy {
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
//? } else
//import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;

public class ModLanguageProvider extends ComposerMultiLanguageProvider {
    public ModLanguageProvider(/*? if legacy {*/FabricDataOutput/*? } else {*//*FabricPackOutput*//*? }*/ dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    protected void init(LanguagePack pack) {
        pack.addProvider("en_us", ModEnglishProvider::new, true);
        pack.addProvider("lol_us", ModLolcatProvider::new);
        pack.addProvider("enws", ModShakespeareanProvider::new);
        pack.addProvider("hu_hu", ModHungarianProvider::new);
    }
}
