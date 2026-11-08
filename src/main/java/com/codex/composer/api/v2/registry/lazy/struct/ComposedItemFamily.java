package com.codex.composer.api.v2.registry.lazy.struct;

import com.codex.composer.api.v1.registry.util.ComposedBlockFamily;
import net.minecraft.data.family.BlockFamily.Variant;
import net.minecraft.item.BlockItem;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ComposedItemFamily {
    private final ComposedBlockFamily blockFamily;
    private final Map<Variant, BlockItem> items = new EnumMap<>(Variant.class);
    private @Nullable BlockItem base;
    private boolean bound = false;

    private ComposedItemFamily(ComposedBlockFamily blockFamily) {
        this.blockFamily = blockFamily;
    }

    public static ComposedItemFamily of(ComposedBlockFamily family) {
        return new ComposedItemFamily(family);
    }

    @ApiStatus.Internal
    public ComposedItemFamily bind(@Nullable BlockItem base, Map<Variant, BlockItem> items) {
        if (bound) throw new IllegalStateException("Item family for '" + describe() + "' is already bound.");
        this.base = base;
        this.items.putAll(items);
        this.bound = true;
        return this;
    }

    public ComposedBlockFamily getBlockFamily() {
        return blockFamily;
    }

    public boolean ownsBase() {
        return blockFamily.ownsBase();
    }

    public @Nullable BlockItem base() {
        bound();
        return base;
    }

    public @NotNull BlockItem getBase() {
        bound();
        if (base == null) throw new IllegalStateException("Item family for '" + describe() + "' does not own it's base!");
        return base;
    }

    public @Nullable BlockItem get(Variant variant) {
        bound();
        return items.get(variant);
    }

    public BlockItem require(Variant variant) {
        BlockItem item = get(variant);
        if (item == null) throw new IllegalStateException("Item family for '" + describe() + "' has no item for variant " + variant + ". Did you forget to request it on the ComposedBlockFamily?");
        return item;
    }

    public boolean has(Variant variant) {
        return get(variant) != null;
    }

    public Set<Variant> getVariants() {
        bound();
        return Collections.unmodifiableSet(items.keySet());
    }

    public List<BlockItem> all() {
        bound();
        List<BlockItem> list = new ArrayList<>(items.size() + 1);
        if (base != null) list.add(base);
        list.addAll(items.values());
        return Collections.unmodifiableList(list);
    }

    public void forEach(BiConsumer<Variant, BlockItem> consumer) {
        bound();
        items.forEach(consumer);
    }

    public Function<Variant, BlockItem> asFunction() {
        bound();
        return variant -> variant == null ? base : items.get(variant);
    }

    public @Nullable BlockItem button() { return get(Variant.BUTTON); }
    public @NotNull BlockItem getButton() { return require(Variant.BUTTON); }
    public @Nullable BlockItem chiseled() { return get(Variant.CHISELED); }
    public @NotNull BlockItem getChiseled() { return require(Variant.CHISELED); }
    public @Nullable BlockItem cracked() { return get(Variant.CRACKED); }
    public @NotNull BlockItem getCracked() { return require(Variant.CRACKED); }
    public @Nullable BlockItem cut() { return get(Variant.CUT); }
    public @NotNull BlockItem getCut() { return require(Variant.CUT); }
    public @Nullable BlockItem door() { return get(Variant.DOOR); }
    public @NotNull BlockItem getDoor() { return require(Variant.DOOR); }
    public @Nullable BlockItem fence() { return get(Variant.FENCE); }
    public @NotNull BlockItem getFence() { return require(Variant.FENCE); }
    public @Nullable BlockItem fenceGate() { return get(Variant.FENCE_GATE); }
    public @NotNull BlockItem getFenceGate() { return require(Variant.FENCE_GATE); }
    public @Nullable BlockItem sign() { return get(Variant.SIGN); }
    public @NotNull BlockItem getSign() { return require(Variant.SIGN); }
    public @Nullable BlockItem slab() { return get(Variant.SLAB); }
    public @NotNull BlockItem getSlab() { return require(Variant.SLAB); }
    public @Nullable BlockItem stairs() { return get(Variant.STAIRS); }
    public @NotNull BlockItem getStairs() { return require(Variant.STAIRS); }
    public @Nullable BlockItem pressurePlate() { return get(Variant.PRESSURE_PLATE); }
    public @NotNull BlockItem getPressurePlate() { return require(Variant.PRESSURE_PLATE); }
    public @Nullable BlockItem polished() { return get(Variant.POLISHED); }
    public @NotNull BlockItem getPolished() { return require(Variant.POLISHED); }
    public @Nullable BlockItem trapdoor() { return get(Variant.TRAPDOOR); }
    public @NotNull BlockItem getTrapdoor() { return require(Variant.TRAPDOOR); }
    public @Nullable BlockItem wall() { return get(Variant.WALL); }
    public @NotNull BlockItem getWall() { return require(Variant.WALL); }
    public @Nullable BlockItem mosaic() { return get(Variant.MOSAIC); }
    public @NotNull BlockItem getMosaic() { return require(Variant.MOSAIC); }
    public @Nullable BlockItem customFence() { return get(Variant.CUSTOM_FENCE); }
    public @NotNull BlockItem getCustomFence() { return require(Variant.CUSTOM_FENCE); }
    public @Nullable BlockItem customFenceGate() { return get(Variant.CUSTOM_FENCE_GATE); }
    public @NotNull BlockItem getCustomFenceGate() { return require(Variant.CUSTOM_FENCE_GATE); }

    //? if >=26.1.2 {
    /*public @Nullable BlockItem bricks() { return get(Variant.BRICKS); }
    public @NotNull BlockItem getBricks() { return require(Variant.BRICKS); }
    public @Nullable BlockItem cobbled() { return get(Variant.COBBLED); }
    public @NotNull BlockItem getCobbled() { return require(Variant.COBBLED); }
    public @Nullable BlockItem tiles() { return get(Variant.TILES); }
    public @NotNull BlockItem getTiles() { return require(Variant.TILES); }
    //? if >=26.2 {
    /^public @Nullable BlockItem hangingSign() { return get(Variant.HANGING_SIGN); }
    public @NotNull BlockItem getHangingSign() { return require(Variant.HANGING_SIGN); }
    public @Nullable BlockItem customHangingSign() { return get(Variant.CUSTOM_HANGING_SIGN); }
    public @NotNull BlockItem getCustomHangingSign() { return require(Variant.CUSTOM_HANGING_SIGN); }
    public @Nullable BlockItem log() { return get(Variant.LOG); }
    public @NotNull BlockItem getLog() { return require(Variant.LOG); }
    public @Nullable BlockItem strippedLog() { return get(Variant.STRIPPED_LOG); }
    public @NotNull BlockItem getStrippedLog() { return require(Variant.STRIPPED_LOG); }
    public @Nullable BlockItem pillar() { return get(Variant.PILLAR); }
    public @NotNull BlockItem getPillar() { return require(Variant.PILLAR); }
    ^///? }
    *///?}

    private void bound() {
        if (!bound) throw new IllegalStateException("Item family for '" + describe() + "' hasn't been registered yet. Pass it to a DeferredItemRegistry first.");
    }

    private String describe() {
        return blockFamily.ownsBase() && blockFamily.isRegistered() ? String.valueOf(blockFamily.getBase()) : "<unregistered family>";
    }
}