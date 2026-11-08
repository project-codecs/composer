package com.codex.composer.api.v1.datagen.ores;

import com.codex.composer.api.v1.datagen.ores.OreSet.Variant;

import java.util.EnumMap;
import java.util.Map;

@SuppressWarnings("UnusedReturnValue")
public class OreTranslations {
    @FunctionalInterface
    public interface Sink {
        void accept(Variant variant, String name);
    }

    private final OreSet set;
    private final Sink sink;
    private final Map<Variant, String> names = new EnumMap<>(Variant.class);
    private String prefix = null, suffix = null;

    public OreTranslations(OreSet set, Sink sink) {
        this.set = set;
        this.sink = sink;
    }

    public static OreTranslations baseEnglish(OreSet set, Sink sink, String baseName) {
        OreTranslations translations = new OreTranslations(set, sink);
        for (Variant variant : set.variants()) {
            switch (variant) {
                case RAW_MATERIAL -> translations.rawMaterial("Raw " + baseName);
                case INGOT -> translations.ingot(baseName + " Ingot");
                case NUGGET -> translations.nugget(baseName + " Nugget");
                case STONE_ORE -> translations.stoneOre(baseName + " Ore");
                case DEEPSLATE_ORE -> translations.deepslateOre("Deepslate " + baseName + " Ore");
                case STORAGE_BLOCK -> translations.storageBlock(baseName + " Block");
                case RAW_STORAGE_BLOCK -> translations.rawStorageBlock("Raw " + baseName + " Block");
            }
        }
        return translations;
    }

    public OreTranslations set(Variant variant, String name) {
        String val = name;
        if (prefix != null) val = prefix + " " + name;
        if (suffix != null) val += " " + suffix;
        names.put(variant, val);
        return this;
    }

    public OreTranslations rawMaterial(String trans) { return set(Variant.RAW_MATERIAL, trans); }
    public OreTranslations ingot(String trans) { return set(Variant.INGOT, trans); }
    public OreTranslations nugget(String trans) { return set(Variant.NUGGET, trans); }
    public OreTranslations stoneOre(String trans) { return set(Variant.STONE_ORE, trans); }
    public OreTranslations deepslateOre(String trans) { return set(Variant.DEEPSLATE_ORE, trans); }
    public OreTranslations storageBlock(String trans) { return set(Variant.STORAGE_BLOCK, trans); }
    public OreTranslations rawStorageBlock(String trans) { return set(Variant.RAW_STORAGE_BLOCK, trans); }

    public OreTranslations prefix(String prefix) { this.prefix = prefix; return this; }
    public OreTranslations prefix() { return prefix(null); }
    public OreTranslations suffix(String suffix) { this.suffix = suffix; return this; }
    public OreTranslations suffix() { return suffix(null); }

    public void apply() {
        set.locked();

        for (Variant variant : names.keySet()) if (!set.has(variant)) throw new IllegalStateException("Variant " + variant + " was translated but isn't part of ore set '" + set.id() + "'.");
        for (Variant variant : set.variants()) if (!names.containsKey(variant)) throw new IllegalStateException("No translation given for variant " + variant + " of ore set '" + set.id() + "'.");

        names.forEach(sink::accept);
    }
}