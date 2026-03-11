package com.codex.composer.api.v1.item.group;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemGroupModifications {
    private static final Map<RegistryKey<ItemGroup>, List<Pair<Item, ItemStack[]>>> modifications = new HashMap<>();

    public static void register(RegistryKey<ItemGroup> key, Item after, ItemStack... stacks) {
        if (modifications.containsKey(key)) {
            var items = modifications.get(key);
            items.add(new Pair<>(after, stacks));
            modifications.put(key, items);
        } else {
            modifications.put(
                    key,
                    List.of(new Pair<>(after, stacks))
            );
        }
    }

    public static List<Pair<Item, ItemStack[]>> getFor(RegistryKey<ItemGroup> group) {
        return modifications.get(group);
    }
}
