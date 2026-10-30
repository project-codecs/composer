package com.codex.composer.api.v1.datagen;

import com.codex.composer.api.v1.registry.util.ComposedBlockFamily;
import net.minecraft.block.Block;
import net.minecraft.data.DataOutput;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

//? if <=26.1.2 {
import net.minecraft.data/*? if <=1.21.3 {*//*.server*//*?}*/.tag.ValueLookupTagProvider;
//? } else {
//import net.minecraft.data.tag.TagProvider;
//? }

public abstract class ComposerBlockTagProvider extends /*~ if minecraft: <=26.1.2 'TagProvider' -> 'ValueLookupTagProvider' {*/ValueLookupTagProvider/*~}*/<Block> {
    protected ComposerBlockTagProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.BLOCK, registriesFuture/*? if <=26.1.2 {*/, block -> Registries.BLOCK.getKey(block).orElseThrow()/*?}*/);
    }

    protected void family(ComposedBlockFamily family) {
        family.forEachTag((tag, block) -> getTagBuilder(tag).add(Registries.BLOCK.getId(block)));
    }
}