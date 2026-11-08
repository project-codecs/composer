package com.codex.composer.api.v2.registry.lazy.struct;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.RegistryKey;
import org.jetbrains.annotations.Nullable;

public record ItemPlacement(@Nullable RegistryKey<ItemGroup> group, boolean isHidden, int order, @Nullable Item anchor, boolean after) {
    private static final ItemPlacement INHERIT = new ItemPlacement(null, false, 0, null, false);

    public static ItemPlacement inherit() { return INHERIT; }
    public static ItemPlacement in(RegistryKey<ItemGroup> group) { return INHERIT.group(group); }
    public static ItemPlacement hidden() { return new ItemPlacement(null, true, 0, null, false); }

    public ItemPlacement group(RegistryKey<ItemGroup> group) { return new ItemPlacement(group, false, order, anchor, after); }
    public ItemPlacement order(int order) { return new ItemPlacement(group, isHidden, order, null, false); }
    public ItemPlacement after(Item anchor) { return new ItemPlacement(group, isHidden, order, anchor, true); }
    public ItemPlacement before(Item anchor) { return new ItemPlacement(group, isHidden, order, anchor, false); }
}