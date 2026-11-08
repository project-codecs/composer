package com.codex.composer.api.v2.registry.lazy.struct;

import net.minecraft.data.family.BlockFamily.Variant;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.RegistryKey;

import java.util.EnumMap;
import java.util.Map;

public class FamilyPlacement {
    private ItemPlacement all = ItemPlacement.inherit();
    private ItemPlacement base = null;
    private final Map<Variant, ItemPlacement> variants = new EnumMap<>(Variant.class);

    private FamilyPlacement() { }

    public static FamilyPlacement create() { return new FamilyPlacement(); }
    public static FamilyPlacement in(RegistryKey<ItemGroup> group) { return new FamilyPlacement().all(ItemPlacement.in(group)); }
    public static FamilyPlacement hidden() { return new FamilyPlacement().all(ItemPlacement.hidden()); }

    public FamilyPlacement all(ItemPlacement placement) { this.all = placement; return this; }
    public FamilyPlacement base(ItemPlacement placement) { this.base = placement; return this; }
    public FamilyPlacement variant(Variant variant, ItemPlacement placement) { variants.put(variant, placement); return this; }

    public ItemPlacement forBase() { return base != null ? base : all; }
    public ItemPlacement forVariant(Variant variant) { return variants.getOrDefault(variant, all); }
}