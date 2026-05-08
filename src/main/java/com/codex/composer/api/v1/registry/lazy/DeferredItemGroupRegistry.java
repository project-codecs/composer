package com.codex.composer.api.v1.registry.lazy;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

//? legacy {
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
//? } else
//import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;

public class DeferredItemGroupRegistry extends EmptyDeferredRegistry {
    public DeferredItemGroupRegistry(String modId) {
        super(modId);
    }

    public RegistryKey<ItemGroup> registerItemGroup(String name, Supplier<ItemStack> iconSupplier) {
        Identifier id = Identifier.of(modId, name);
        RegistryKey<ItemGroup> key = RegistryKey.of(Registries.ITEM_GROUP.getKey(), id);

        ItemGroup group = /*? if legacy {*/FabricItemGroup/*? } else {*//*FabricCreativeModeTab*//*? }*/.builder()
                .icon(iconSupplier)
                .displayName(Text.translatable("itemGroup." + name))
                .build();

        Registry.register(Registries.ITEM_GROUP, key, group);
        return key;
    }
}
