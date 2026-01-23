package com.codex.composer.api.v1.registry.lazy;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import org.jetbrains.annotations.NotNull;

//? if minecraft: >=1.20.6
import net.minecraft.registry.RegistryKeys;

public class DeferredItemRegistry extends EmptyDeferredRegistry {
    private final RegistryKey<ItemGroup> itemGroupKey;
    private final List<Item> registeredItems = new ArrayList<>();

    public DeferredItemRegistry(String modId, RegistryKey<ItemGroup> itemGroupKey) {
        super(modId);
        this.itemGroupKey = itemGroupKey;
    }

    public Item register(String name) {
        return register(name, Item::new, new Item.Settings(), true);
    }

    public Item register(String name, UnaryOperator<Item.Settings> settingsBuilder) {
        return register(name, Item::new, settingsBuilder.apply(new Item.Settings()), true);
    }

    public <I extends Item> I register(String name, Function<Item.Settings, I> provider) {
        return register(name, provider, new Item.Settings(), true);
    }

    public <I extends Item> I register(String name, Function<Item.Settings, I> provider,
                                       UnaryOperator<Item.Settings> settingsBuilder) {
        return register(name, provider, settingsBuilder.apply(new Item.Settings()), true);
    }

    public <I extends Item, S extends Item.Settings> I register(String name, Function<S, I> provider, S settings) {
        return register(name, provider, settings, true);
    }

    public <B extends Block> BlockItem register(B block, String name) {
        return register(block, name, new Item.Settings());
    }

    public <B extends Block> BlockItem register(B block, String name, boolean addToGroup) {
        return register(block, name, new Item.Settings(), addToGroup);
    }

    public <B extends Block> BlockItem register(B block, String name, UnaryOperator<Item.Settings> settingsBuilder) {
        return register(block, name, settingsBuilder, true);
    }

    public <B extends Block> BlockItem register(B block, String name, UnaryOperator<Item.Settings> settingsBuilder, boolean addToGroup) {
        Item.Settings settings = settingsBuilder.apply(new Item.Settings());
        return register(block, name, settings, addToGroup);
    }

    public <B extends Block, S extends Item.Settings> BlockItem register(B block, String name, S settings) {
        return register(block, name, settings, true);
    }

    public <B extends Block, S extends Item.Settings> BlockItem register(B block, String name, S settings, boolean addToGroup) {
        return register(name, s -> new BlockItem(block, s), settings, addToGroup);
    }

    //? if minecraft: >=1.21.3
    @SuppressWarnings("unchecked")
    private <I extends Item, S extends Item.Settings> I register(String name, @NotNull Function<S, I> provider, @NotNull S settings, boolean addToGroup) {
        Identifier id = Identifier.of(modId, name);

        S finalSettings = settings;
        //? if minecraft: >=1.21.3
        finalSettings = (S) finalSettings.registryKey(RegistryKey.of(RegistryKeys.ITEM, id));

        I item = provider.apply(finalSettings);
        I registered = Registry.register(Registries.ITEM, id, item);

        if (addToGroup) registeredItems.add(registered);
        return registered;
    }

    public void finalizeRegistration() {
        if (itemGroupKey != null) {
            ItemGroupEvents.modifyEntriesEvent(itemGroupKey)
                    .register(entries -> registeredItems.forEach(item -> entries.add(item.getDefaultStack())));
        }
    }
}
