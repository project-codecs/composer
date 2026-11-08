package com.codex.composer.api.v1.datagen.lang;

import com.codex.composer.api.v1.registry.util.ComposedBlockFamily;
import net.minecraft.block.Block;
import net.minecraft.data.family.BlockFamily.Variant;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

@SuppressWarnings("UnusedReturnValue")
public final class FamilyTranslationBuilder {
    @FunctionalInterface public interface Sink {  void accept(Block block, String name, boolean hasItem); }

    private final ComposedBlockFamily family;
    private final Sink sink;
    private final Map<Variant, String> names = new EnumMap<>(Variant.class);
    private @Nullable String baseName;
    private String prefix = null, suffix = null;

    public FamilyTranslationBuilder(ComposedBlockFamily family, Sink sink) {
        this.family = family;
        this.sink = sink;
    }

    public static FamilyTranslationBuilder baseEnglish(ComposedBlockFamily family, Sink sink, String baseNamePl, String baseNameS) {
        FamilyTranslationBuilder builder = new FamilyTranslationBuilder(family, sink);
        if (family.ownsBase()) builder.base(baseNamePl);
        for (Variant variant : family.getVariants()) {
            switch (variant) {
                case BUTTON -> builder.button(baseNameS + " Button");
                case CHISELED -> builder.chiseled("Chiseled " + baseNamePl);
                case CRACKED -> builder.cracked("Cracked " + baseNamePl);
                case CUT -> builder.cut("Cut " + baseNamePl);
                case DOOR -> builder.door(baseNameS + " Door");
                case CUSTOM_FENCE -> builder.customFence(baseNameS + " Fence");
                case FENCE -> builder.fence(baseNameS + " Fence");
                case CUSTOM_FENCE_GATE -> builder.customFenceGate(baseNameS + " Fence Gate");
                case FENCE_GATE -> builder.fenceGate(baseNameS + " Fence Gate");
                case MOSAIC -> builder.mosaic(baseNameS + " Mosaic");
                case SIGN -> builder.sign(baseNameS + " Sign");
                case SLAB -> builder.slab(baseNameS + " Slab");
                case STAIRS -> builder.stairs(baseNameS + " Stairs");
                case PRESSURE_PLATE -> builder.pressurePlate(baseNameS + " Pressure Plate");
                case POLISHED -> builder.polished("Polished " + baseNamePl);
                case TRAPDOOR -> builder.trapdoor(baseNameS + " Trapdoor");
                case WALL -> builder.wall(baseNameS + " Wall");
                //? if >=26.1.2 {
                /*case BRICKS -> builder.bricks(baseNameS + " Bricks");
                case COBBLED -> builder.cobbled("Cobbled " + baseNamePl);
                case TILES -> builder.tiles(baseNameS + " Tiles");
                //? if >=26.2 {
                /^case HANGING_SIGN -> builder.hangingSign(baseNameS + " Hanging Sign");
                case CUSTOM_HANGING_SIGN -> builder.customHangingSign(baseNameS + " Hanging Sign");
                case LOG -> builder.log(baseNameS + " Log");
                case STRIPPED_LOG -> builder.strippedLog("Stripped " + baseNameS + " Log");
                case PILLAR -> builder.pillar(baseNameS + " Pillar");
                ^///? }
                *///? }
            }
        }
        return builder;
    }

    public FamilyTranslationBuilder base(String name) {
        this.baseName = name;
        return this;
    }

    public FamilyTranslationBuilder with(Variant variant, String name) {
        String val = name;
        if (prefix != null) val = prefix + " " + name;
        if (suffix != null) val += " " + suffix;
        names.put(variant, val);
        return this;
    }

    public FamilyTranslationBuilder button(String trans) { return with(Variant.BUTTON, trans); }
    public FamilyTranslationBuilder chiseled(String trans) { return with(Variant.CHISELED, trans); }
    public FamilyTranslationBuilder cracked(String trans) { return with(Variant.CRACKED, trans); }
    public FamilyTranslationBuilder cut(String trans) { return with(Variant.CUT, trans); }
    public FamilyTranslationBuilder door(String trans) { return with(Variant.DOOR, trans); }
    public FamilyTranslationBuilder fence(String trans) { return with(Variant.FENCE, trans); }
    public FamilyTranslationBuilder fenceGate(String trans) { return with(Variant.FENCE_GATE, trans); }
    public FamilyTranslationBuilder sign(String trans) { return with(Variant.SIGN, trans); }
    public FamilyTranslationBuilder slab(String trans) { return with(Variant.SLAB, trans); }
    public FamilyTranslationBuilder stairs(String trans) { return with(Variant.STAIRS, trans); }
    public FamilyTranslationBuilder pressurePlate(String trans) { return with(Variant.PRESSURE_PLATE, trans); }
    public FamilyTranslationBuilder polished(String trans) { return with(Variant.POLISHED, trans); }
    public FamilyTranslationBuilder trapdoor(String trans) { return with(Variant.TRAPDOOR, trans); }
    public FamilyTranslationBuilder wall(String trans) { return with(Variant.WALL, trans); }
    public FamilyTranslationBuilder mosaic(String trans) { return with(Variant.MOSAIC, trans); }
    public FamilyTranslationBuilder customFence(String trans) { return with(Variant.CUSTOM_FENCE, trans); }
    public FamilyTranslationBuilder customFenceGate(String trans) { return with(Variant.CUSTOM_FENCE_GATE, trans); }

    public FamilyTranslationBuilder prefix(String prefix) { this.prefix = prefix; return this; }
    public FamilyTranslationBuilder prefix() { return prefix(null); }
    public FamilyTranslationBuilder suffix(String suffix) { this.suffix = suffix; return this; }
    public FamilyTranslationBuilder suffix() { return suffix(null); }

    //? if >=26.1.2 {
    /*public FamilyTranslationBuilder bricks(String trans) { return with(Variant.BRICKS, trans); }
    public FamilyTranslationBuilder cobbled(String trans) { return with(Variant.COBBLED, trans); }
    public FamilyTranslationBuilder tiles(String trans) { return with(Variant.TILES, trans); }
    //? if >=26.2 {
    /^public FamilyTranslationBuilder hangingSign(String trans) { return with(Variant.HANGING_SIGN, trans); }
    public FamilyTranslationBuilder customHangingSign(String trans) { return with(Variant.CUSTOM_HANGING_SIGN, trans); }
    public FamilyTranslationBuilder log(String trans) { return with(Variant.LOG, trans); }
    public FamilyTranslationBuilder strippedLog(String trans) { return with(Variant.STRIPPED_LOG, trans); }
    public FamilyTranslationBuilder pillar(String trans) { return with(Variant.PILLAR, trans); }
    ^///? }
    *///?}

    public void apply() {
        family.locked();

        if (family.ownsBase() && baseName == null) throw new IllegalStateException("Family registered its own base block but base(...) was not given a name.");
        if (!family.ownsBase() && baseName != null) throw new IllegalStateException("base(...) was given, but this family uses an existing base block that it doesn't own.");

        for (Variant variant : names.keySet()) {
            if (family.get(variant) == null) throw new IllegalStateException("Variant " + variant + " was translated but isn't registered in this family.");
        }
        for (Variant variant : family.getVariants()) {
            if (!names.containsKey(variant) && !ComposedBlockFamily.DO_NOT_REGISTER_ITEM.contains(variant)) {
                throw new IllegalStateException("No translation given for variant " + variant + ".");
            }
        }

        if (baseName != null) sink.accept(family.getBase(), baseName, true);
        names.forEach((variant, name) -> sink.accept(family.get(variant), name, !ComposedBlockFamily.DO_NOT_REGISTER_ITEM.contains(variant)));
    }
}