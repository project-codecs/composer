package com.codex.composer.api.v1.item.group;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;

public class ItemGroupModifications {
    @Deprecated(forRemoval = true, since = "3.2.1")
    public static void register(RegistryKey<ItemGroup> key, Item after, ItemStack... stacks) {
        ItemGroupEvents.modifyEntriesEvent(key).register(group -> group.addAfter(after, stacks));
    }
}
