package com.codex.composer.api.v1.datagen.ores;

import com.codex.composer.api.v1.registry.lazy.DeferredBlockRegistry;
import com.codex.composer.api.v1.registry.lazy.DeferredItemRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class OreSet {
    public enum Variant {
        RAW_MATERIAL(false),
        INGOT(false),
        NUGGET(false),
        STONE_ORE(true),
        DEEPSLATE_ORE(true),
        STORAGE_BLOCK(true),
        RAW_STORAGE_BLOCK(true);

        private final boolean block;

        Variant(boolean block) {
            this.block = block;
        }

        public boolean isBlock() {
            return block;
        }
    }

    public enum Tier {
        NONE, STONE, IRON, DIAMOND;

        public @Nullable TagKey<Block> tag() {
            return switch (this) {
                case NONE -> null;
                case STONE -> BlockTags.NEEDS_STONE_TOOL;
                case IRON -> BlockTags.NEEDS_IRON_TOOL;
                case DIAMOND -> BlockTags.NEEDS_DIAMOND_TOOL;
            };
        }
    }

    private static final Map<String, List<OreSet>> ALL = new LinkedHashMap<>();

    public static List<OreSet> allOf(String modId) {
        return Collections.unmodifiableList(ALL.getOrDefault(modId, List.of()));
    }

    public static OreSet create(String modId, String id, DeferredItemRegistry items, DeferredBlockRegistry blocks) {
        return new OreSet(modId, id, items, blocks);
    }

    private final String modId;
    private final String id;
    private final DeferredItemRegistry itemRegistry;
    private final DeferredBlockRegistry blockRegistry;

    private final Set<Variant> requested = EnumSet.noneOf(Variant.class);
    private final Map<Variant, String> nameOverrides = new EnumMap<>(Variant.class);
    private final Map<Variant, Block> blockMap = new EnumMap<>(Variant.class);
    private final Map<Variant, Item> itemMap = new EnumMap<>(Variant.class);

    private Tier tier = Tier.STONE;
    private float smeltingExperience = 0.7F;
    private int smeltingTime = 200;
    private int blastingTime = 100;
    private int minMiningXp = 0;
    private int maxMiningXp = 0;
    private boolean registered = false;

    private OreSet(String modId, String id, DeferredItemRegistry itemRegistry, DeferredBlockRegistry blockRegistry) {
        this.modId = modId;
        this.id = id;
        this.itemRegistry = itemRegistry;
        this.blockRegistry = blockRegistry;
    }

    public OreSet with(Variant... variants) {
        notLocked();
        Collections.addAll(requested, variants);
        return this;
    }

    public OreSet rawMaterial() { return with(Variant.RAW_MATERIAL); }
    public OreSet ingot() { return with(Variant.INGOT); }
    public OreSet nugget() { return with(Variant.NUGGET); }
    public OreSet stoneOre() { return with(Variant.STONE_ORE); }
    public OreSet deepslateOre() { return with(Variant.DEEPSLATE_ORE); }
    public OreSet storageBlock() { return with(Variant.STORAGE_BLOCK); }
    public OreSet rawStorageBlock() { return with(Variant.RAW_STORAGE_BLOCK); }

    public OreSet name(Variant variant, String registryName) {
        notLocked();
        nameOverrides.put(variant, registryName);
        return this;
    }

    public OreSet tier(Tier tier) {
        notLocked();
        this.tier = tier;
        return this;
    }

    public OreSet smeltingExperience(float experience) {
        notLocked();
        this.smeltingExperience = experience;
        return this;
    }

    public OreSet smeltingTime(int ticks) {
        notLocked();
        this.smeltingTime = ticks;
        return this;
    }

    public OreSet blastingTime(int ticks) {
        notLocked();
        this.blastingTime = ticks;
        return this;
    }

    public OreSet miningExperience(int min, int max) {
        notLocked();
        this.minMiningXp = min;
        this.maxMiningXp = max;
        return this;
    }

    public OreSet register() {
        notLocked();
        validate();

        for (Variant variant : Variant.values()) {
            if (requested.contains(variant)) registerVariant(variant);
        }

        registered = true;
        ALL.computeIfAbsent(modId, k -> new ArrayList<>()).add(this);
        return this;
    }

    private void validate() {
        if (requested.isEmpty()) throw new IllegalStateException("Ore set '" + id + "' has no variants.");

        boolean hasOre = requested.contains(Variant.STONE_ORE) || requested.contains(Variant.DEEPSLATE_ORE);
        if (hasOre && !requested.contains(Variant.RAW_MATERIAL) && !requested.contains(Variant.INGOT)) throw new IllegalStateException("Ore set '" + id + "' has ore blocks but nothing for them to drop. Add rawMaterial() or ingot().");
        if (requested.contains(Variant.NUGGET) && !requested.contains(Variant.INGOT)) throw new IllegalStateException("Ore set '" + id + "': nugget() needs ingot().");
        if (requested.contains(Variant.STORAGE_BLOCK) && !requested.contains(Variant.INGOT)) throw new IllegalStateException("Ore set '" + id + "': storageBlock() needs ingot().");
        if (requested.contains(Variant.RAW_STORAGE_BLOCK) && !requested.contains(Variant.RAW_MATERIAL)) throw new IllegalStateException("Ore set '" + id + "': rawStorageBlock() needs rawMaterial().");
    }


    private void registerVariant(Variant variant) {
        String name = nameOverrides.getOrDefault(variant, defaultName(variant));
        UniformIntProvider xp = UniformIntProvider.create(minMiningXp, maxMiningXp);

        switch (variant) {
            case RAW_MATERIAL, INGOT, NUGGET -> itemMap.put(variant, itemRegistry.register(name));
            //~ if 1.20.1 'xp, s' -> 's, xp' {
            case STONE_ORE -> addBlock(variant, name, s -> new ExperienceDroppingBlock(xp, s), AbstractBlock.Settings.copy(Blocks.IRON_ORE));
            case DEEPSLATE_ORE -> addBlock(variant, name, s -> new ExperienceDroppingBlock(xp, s), AbstractBlock.Settings.copy(Blocks.DEEPSLATE_IRON_ORE));
            //~}
            case STORAGE_BLOCK -> addBlock(variant, name, Block::new, AbstractBlock.Settings.copy(Blocks.IRON_BLOCK));
            case RAW_STORAGE_BLOCK -> addBlock(variant, name, Block::new, AbstractBlock.Settings.copy(Blocks.RAW_IRON_BLOCK));
        }
    }

    private void addBlock(Variant variant, String name, DeferredBlockRegistry.BlockProvider<Block> provider, AbstractBlock.Settings settings) {
        Block block = blockRegistry.register(name, provider, settings);
        itemRegistry.register(block, name);
        blockMap.put(variant, block);
    }

    private String defaultName(Variant variant) {
        return switch (variant) {
            case RAW_MATERIAL -> id + "_raw";
            case INGOT -> id + "_ingot";
            case NUGGET -> id + "_nugget";
            case STONE_ORE -> id + "_ore";
            case DEEPSLATE_ORE -> "deepslate_" + id + "_ore";
            case STORAGE_BLOCK -> id + "_block";
            case RAW_STORAGE_BLOCK -> "raw_" + id + "_block";
        };
    }

    private Identifier featureId() {
        return Identifier.of(modId, "ore_" + id);
    }

    private void notLocked() {
        if (registered) throw new IllegalStateException("Ore set '" + id + "' was already registered; it can't be modified.");
    }

    public void locked() {
        if (!registered) throw new IllegalStateException("Ore set '" + id + "' hasn't been registered yet. Call register() first.");
    }

    public String modId() { return modId; }
    public String id() { return id; }
    public Tier tier() { return tier; }
    public float smeltingExperience() { return smeltingExperience; }
    public int smeltingTime() { return smeltingTime; }
    public int blastingTime() { return blastingTime; }

    public boolean has(Variant variant) {
        return requested.contains(variant);
    }

    public Set<Variant> variants() {
        locked();
        return Collections.unmodifiableSet(requested);
    }

    public @Nullable Block block(Variant variant) {
        return blockMap.get(variant);
    }

    public @NotNull Block tBlock(Variant variant) {
        return Optional.ofNullable(block(variant)).orElseThrow();
    }

    public @Nullable Item item(Variant variant) {
        Block block = blockMap.get(variant);
        return block != null ? block.asItem() : itemMap.get(variant);
    }

    public @NotNull Item tItem(Variant variant) {
        return Optional.ofNullable(item(variant)).orElseThrow();
    }

    public List<ItemConvertible> smeltingInputs() {
        locked();
        List<ItemConvertible> inputs = new ArrayList<>();
        if (itemMap.containsKey(Variant.RAW_MATERIAL)) inputs.add(itemMap.get(Variant.RAW_MATERIAL));
        if (blockMap.containsKey(Variant.STONE_ORE)) inputs.add(blockMap.get(Variant.STONE_ORE));
        if (blockMap.containsKey(Variant.DEEPSLATE_ORE)) inputs.add(blockMap.get(Variant.DEEPSLATE_ORE));
        return inputs;
    }
}