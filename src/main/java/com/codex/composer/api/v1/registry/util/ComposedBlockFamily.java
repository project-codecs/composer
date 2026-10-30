package com.codex.composer.api.v1.registry.util;

import com.codex.composer.api.v1.registry.lazy.DeferredBlockRegistry;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.data.family.BlockFamily.Variant;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Pair;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public final class ComposedBlockFamily {
    public static final Set<Variant> DO_NOT_REGISTER_ITEM = Set.of(Variant.WALL_SIGN/*? if >=26.2 {*//*, Variant.WALL_HANGING_SIGN, Variant.WALL_HANGING_SIGN*//*?}*/);

    public static class BaseType {
        public static final BaseType WOOD = new BaseType(() -> Blocks.OAK_PLANKS, BlockSetType.OAK, WoodType.OAK, BlockTags.AXE_MINEABLE, 30);
        public static final BaseType NETHER_WOOD = new BaseType(() -> Blocks.CRIMSON_PLANKS, BlockSetType.CRIMSON, WoodType.CRIMSON, BlockTags.AXE_MINEABLE, 30);
        public static final BaseType BAMBOO = new BaseType(() -> Blocks.BAMBOO_PLANKS, BlockSetType.BAMBOO, WoodType.BAMBOO, BlockTags.AXE_MINEABLE, 30);
        public static final BaseType STONE = new BaseType(() -> Blocks.STONE, BlockSetType.STONE, null, BlockTags.PICKAXE_MINEABLE, 20);
        public static final BaseType DEEPSLATE = new BaseType(() -> Blocks.COBBLED_DEEPSLATE, BlockSetType.STONE, null, BlockTags.PICKAXE_MINEABLE, 20);
        public static final BaseType BRICK = new BaseType(() -> Blocks.BRICKS, BlockSetType.STONE, null, BlockTags.PICKAXE_MINEABLE, 20);

        private final Supplier<Block> template;
        private final BlockSetType blockSetType;
        private final @Nullable WoodType woodType;
        private final TagKey<Block> minableTag;
        private final int buttonPressTicks;

        public BaseType(Supplier<Block> template, BlockSetType blockSetType, @Nullable WoodType woodType, TagKey<Block> minableTag, int buttonPressTicks) {
            this.template = template;
            this.blockSetType = blockSetType;
            this.woodType = woodType;
            this.minableTag = minableTag;
            this.buttonPressTicks = buttonPressTicks;
        }
    }

    @FunctionalInterface public interface RegisterListener { void onRegistered(String name, Block block); }
    @FunctionalInterface private interface Registrar { void register(ComposedBlockFamily b); }
    @FunctionalInterface private interface Applier { void apply(BlockFamily.Builder family, ComposedBlockFamily b);}
    @FunctionalInterface private interface Tagger { void tag(ComposedBlockFamily b, BiConsumer<TagKey<Block>, Block> out); }

    private record Spec(Variant variant, Registrar registrar, Applier applier) { }

    private static final List<Spec> SPECS = new ArrayList<>();
    private static final Map<Variant, Tagger> TAGGERS = new EnumMap<>(Variant.class);

    private static void spec(Variant variant, Registrar registrar, Applier applier) {
        SPECS.add(new Spec(variant, registrar, applier));
    }

    private static void tags(Variant variant, @Nullable TagKey<Block> always, @Nullable TagKey<Block> wooden, @Nullable TagKey<Block> stone) {
        TAGGERS.put(variant, (b, out) -> {
            Block block = b.block(variant);
            if (always != null) out.accept(always, block);
            if (wooden != null && b.woodType != null) out.accept(wooden, block);
            if (stone != null && b.woodType == null && b.blockSetType == BlockSetType.STONE) out.accept(stone, block);
        });
    }

    private static void tagsWithWall(Variant variant, Variant wall, TagKey<Block> standing, TagKey<Block> wallTag) {
        TAGGERS.put(variant, (b, out) -> {
            out.accept(standing, b.block(variant));
            out.accept(wallTag, b.block(wall));
        });
    }

    static {
        spec(Variant.STAIRS, b -> b.addSuffixed(Variant.STAIRS, b.baseSettings(), s -> new StairsBlock(b.base.getDefaultState(), s)), (f, b) -> f.stairs(b.block(Variant.STAIRS)));
        spec(Variant.SLAB, b -> b.addSuffixed(Variant.SLAB, b.baseSettings(), SlabBlock::new), (f, b) -> f.slab(b.block(Variant.SLAB)));
        spec(Variant.WALL, b -> b.addSuffixed(Variant.WALL, b.baseSettings(), WallBlock::new), (f, b) -> f.wall(b.block(Variant.WALL)));
        spec(Variant.BUTTON, b -> {
            BlockSetType set = b.blockSetType;
            int ticks = b.type.buttonPressTicks;
            //~ if minecraft: <=1.20.1 'set, ticks, s' -> 's, set, ticks, b.woodType != null'
            b.addSuffixed(Variant.BUTTON, b.destroyOnPush(b.baseSettings().noCollision().strength(0.5F)), s -> new ButtonBlock(set, ticks, s));
        }, (f, b) -> f.button(b.block(Variant.BUTTON)));

        spec(Variant.PRESSURE_PLATE, b -> {
            BlockSetType set = b.blockSetType;
            //~ if minecraft: <=1.20.1 'set, s' -> 'b.woodType == null ? PressurePlateBlock.ActivationRule.EVERYTHING : PressurePlateBlock.ActivationRule.MOBS, s, set'
            b.addSuffixed(Variant.PRESSURE_PLATE, b.destroyOnPush(b.baseSettings().noCollision().strength(0.5F)), s -> new PressurePlateBlock(set, s));
        }, (f, b) -> f.pressurePlate(b.block(Variant.PRESSURE_PLATE)));

        spec(Variant.DOOR, b -> {
            BlockSetType set = b.blockSetType;
            //~ if minecraft: <=1.20.1 'set, s' -> 's, set'
            b.addSuffixed(Variant.DOOR, b.destroyOnPush(b.baseSettings().strength(3.0F).nonOpaque()), s -> new DoorBlock(set, s));
        }, (f, b) -> f.door(b.block(Variant.DOOR)));

        spec(Variant.TRAPDOOR, b -> {
            BlockSetType set = b.blockSetType;
            //~ if minecraft: <=1.20.1 'set, s' -> 's, set'
            b.addSuffixed(Variant.TRAPDOOR, b.baseSettings().strength(3.0F).nonOpaque(), s -> new TrapdoorBlock(set, s));
        }, (f, b) -> f.trapdoor(b.block(Variant.TRAPDOOR)));

        spec(Variant.FENCE, b -> b.addSuffixed(Variant.FENCE, b.baseSettings(), FenceBlock::new), (f, b) -> f.fence(b.block(Variant.FENCE)));
        spec(Variant.FENCE_GATE, b -> {
            WoodType wood = b.requireWoodType(Variant.FENCE_GATE);
            //~ if minecraft: <=1.20.1 'wood, s' -> 's, wood'
            b.addSuffixed(Variant.FENCE_GATE, b.baseSettings(), s -> new FenceGateBlock(wood, s));
        }, (f, b) -> f.fenceGate(b.block(Variant.FENCE_GATE)));

        spec(Variant.SIGN, b -> {
            WoodType wood = b.requireWoodType(Variant.SIGN);
            String signName = b.nameOf(Variant.SIGN, b.prefix + "_sign");
            //~ if minecraft: <=1.20.1 'wood, s' -> 's, wood'
            Block sign = b.registerNamed(signName, b.signSettings(), s -> new SignBlock(wood, s));
            b.blocks.put(Variant.SIGN, sign);
            //~ if minecraft: <=1.21 'lootTable(sign.getLootTableKey())' -> 'dropsLike(sign)'
            //~ if minecraft: <=1.20.1 'wood, s' -> 's, wood'
            b.wallSign = b.registerNamed(wallSignName(signName), b.signSettings().lootTable(sign.getLootTableKey()), s -> new WallSignBlock(wood, s));
            b.blocks.put(Variant.WALL_SIGN, b.wallSign);
        }, (f, b) -> f.sign(b.block(Variant.SIGN), b.wallSign));

        spec(Variant.CHISELED, b -> b.addPrefixed(Variant.CHISELED, b.baseSettings(), Block::new), (f, b) -> f.chiseled(b.block(Variant.CHISELED)));
        spec(Variant.CRACKED, b -> b.addPrefixed(Variant.CRACKED, b.baseSettings(), Block::new), (f, b) -> f.cracked(b.block(Variant.CRACKED)));
        spec(Variant.CUT, b -> b.addPrefixed(Variant.CUT, b.baseSettings(), Block::new), (f, b) -> f.cut(b.block(Variant.CUT)));
        spec(Variant.POLISHED, b -> b.addPrefixed(Variant.POLISHED, b.baseSettings(), Block::new), (f, b) -> f.polished(b.block(Variant.POLISHED)));
        spec(Variant.MOSAIC, b -> b.addSuffixed(Variant.MOSAIC, b.baseSettings(), Block::new), (f, b) -> f.mosaic(b.block(Variant.MOSAIC)));
        spec(Variant.CUSTOM_FENCE, b -> b.addSuffixed(Variant.CUSTOM_FENCE, b.baseSettings(), FenceBlock::new), (f, b) -> f.customFence(b.block(Variant.CUSTOM_FENCE)));
        spec(Variant.CUSTOM_FENCE_GATE, b -> {
            WoodType wood = b.requireWoodType(Variant.CUSTOM_FENCE_GATE);
            //~ if minecraft: <=1.20.1 'wood, s' -> 's, wood'
            b.addSuffixed(Variant.CUSTOM_FENCE_GATE, b.baseSettings(), s -> new FenceGateBlock(wood, s));
        }, (f, b) -> f.customFenceGate(b.block(Variant.CUSTOM_FENCE_GATE)));
        
        //? if >=26.1.2 {
        /*spec(Variant.BRICKS, b -> b.addSuffixed(Variant.BRICKS, b.baseSettings(), Block::new), (f, b) -> f.bricks(b.block(Variant.BRICKS)));
        spec(Variant.COBBLED, b -> b.addPrefixed(Variant.COBBLED, b.baseSettings(), Block::new), (f, b) -> f.cobbled(b.block(Variant.COBBLED)));
        spec(Variant.TILES, b -> b.addSuffixed(Variant.TILES, b.baseSettings(), Block::new), (f, b) -> f.tiles(b.block(Variant.TILES)));

        //? if >=26.2 {
        /^spec(Variant.LOG, b -> b.addSuffixed(Variant.LOG, b.baseSettings(), PillarBlock::new), (f, b) -> f.log(b.block(Variant.LOG)));
        spec(Variant.STRIPPED_LOG, b -> {
            String name = b.nameOf(Variant.STRIPPED_LOG, "stripped_" + b.prefix + "_log");
            b.blocks.put(Variant.STRIPPED_LOG, b.registerNamed(name, b.baseSettings(), PillarBlock::new));
        }, (f, b) -> f.strippedLog(b.block(Variant.STRIPPED_LOG)));

        spec(Variant.PILLAR, b -> b.addSuffixed(Variant.PILLAR, b.baseSettings(), PillarBlock::new), (f, b) -> f.pillar(b.block(Variant.PILLAR)));
        spec(Variant.HANGING_SIGN,
                b -> b.registerHangingSign(Variant.HANGING_SIGN, Variant.WALL_HANGING_SIGN),
                (f, b) -> f.hangingSign(b.block(Variant.HANGING_SIGN), b.block(Variant.WALL_HANGING_SIGN)));
        spec(Variant.CUSTOM_HANGING_SIGN,
                b -> b.registerHangingSign(Variant.CUSTOM_HANGING_SIGN, Variant.CUSTOM_WALL_HANGING_SIGN),
                (f, b) -> f.customHangingSign(b.block(Variant.CUSTOM_HANGING_SIGN), b.block(Variant.CUSTOM_WALL_HANGING_SIGN)));
        ^///?}
        *///?}

        tags(Variant.STAIRS, BlockTags.STAIRS, BlockTags.WOODEN_STAIRS, null);
        tags(Variant.SLAB, BlockTags.SLABS, BlockTags.WOODEN_SLABS, null);
        tags(Variant.WALL, BlockTags.WALLS, null, null);
        tags(Variant.BUTTON, BlockTags.BUTTONS, BlockTags.WOODEN_BUTTONS, BlockTags.STONE_BUTTONS);
        tags(Variant.PRESSURE_PLATE, BlockTags.PRESSURE_PLATES, BlockTags.WOODEN_PRESSURE_PLATES, BlockTags.STONE_PRESSURE_PLATES);
        tags(Variant.DOOR, BlockTags.DOORS, BlockTags.WOODEN_DOORS, null);
        tags(Variant.TRAPDOOR, BlockTags.TRAPDOORS, BlockTags.WOODEN_TRAPDOORS, null);
        tags(Variant.FENCE, BlockTags.FENCES, BlockTags.WOODEN_FENCES, null);
        tags(Variant.CUSTOM_FENCE, BlockTags.FENCES, BlockTags.WOODEN_FENCES, null);
        tags(Variant.FENCE_GATE, BlockTags.FENCE_GATES, null, null);
        tags(Variant.CUSTOM_FENCE_GATE, BlockTags.FENCE_GATES, null, null);
        tagsWithWall(Variant.SIGN, Variant.WALL_SIGN, BlockTags.STANDING_SIGNS, BlockTags.WALL_SIGNS);

        //? if >=26.1.2
        //tags(Variant.BRICKS, null, null, BlockTags.STONE_BRICKS);

        //? if >=26.2 {
        /*tags(Variant.LOG, BlockTags.LOGS, null, null);
        tags(Variant.STRIPPED_LOG, BlockTags.LOGS, null, null);
        tagsWithWall(Variant.HANGING_SIGN, Variant.WALL_HANGING_SIGN, BlockTags.CEILING_HANGING_SIGNS, BlockTags.WALL_HANGING_SIGNS);
        tagsWithWall(Variant.CUSTOM_HANGING_SIGN, Variant.CUSTOM_WALL_HANGING_SIGN, BlockTags.CEILING_HANGING_SIGNS, BlockTags.WALL_HANGING_SIGNS);
        *///?}
    }

    private final DeferredBlockRegistry registry;
    private final String baseName;
    private final BaseType type;
    private final @Nullable Block existingBase;

    private String prefix;
    private @Nullable Block settingsSource;
    private BlockSetType blockSetType;
    private @Nullable WoodType woodType;

    private final Set<Variant> requested = EnumSet.noneOf(Variant.class);
    private final Map<Variant, String> nameOverrides = new HashMap<>();
    private final List<RegisterListener> listeners = new ArrayList<>();

    private boolean generateModels = true;
    private boolean generateRecipes = true;
    //? if minecraft: >=26.1.2 {
    /*private boolean generateStonecutterRecipes = false;
    //? if >=26.2
    //private boolean generateCrackingRecipes = true;
    *///?}
    private @Nullable String group;
    private @Nullable String unlockCriterionName;

    private final Map<Variant, Block> blocks = new EnumMap<>(Variant.class);
    private final Set<String> usedNames = new HashSet<>();
    private Block base;
    private Block wallSign;
    private @Nullable BlockFamily.Builder result;
    private @Nullable TagKey<Block> minableTag;
    private final List<TagKey<Block>> extraTags = new ArrayList<>();

    public ComposedBlockFamily(DeferredBlockRegistry registry, String baseName, @Nullable Block existingBase, BaseType type) {
        this.registry = registry;
        this.baseName = baseName;
        this.existingBase = existingBase;
        this.type = type;
        this.prefix = derivePrefix(baseName);
        this.blockSetType = type.blockSetType;
        this.woodType = type.woodType;
    }

    public ComposedBlockFamily prefix(String prefix) {
        notLocked();
        this.prefix = prefix;
        return this;
    }

    public ComposedBlockFamily name(Variant variant, String registryName) {
        notLocked();
        nameOverrides.put(variant, registryName);
        return this;
    }

    public ComposedBlockFamily settingsFrom(Block block) {
        notLocked();
        this.settingsSource = block;
        return this;
    }

    public ComposedBlockFamily blockSetType(BlockSetType blockSetType) {
        notLocked();
        this.blockSetType = blockSetType;
        return this;
    }

    public ComposedBlockFamily woodType(WoodType woodType) {
        notLocked();
        this.woodType = woodType;
        return this;
    }

    public ComposedBlockFamily minableTag(TagKey<Block> tag) {
        notLocked();
        this.minableTag = tag;
        return this;
    }

    @SafeVarargs
    public final ComposedBlockFamily tagAll(TagKey<Block>... tags) {
        notLocked();
        Collections.addAll(extraTags, tags);
        return this;
    }

    public ComposedBlockFamily onRegister(RegisterListener listener) {
        notLocked();
        listeners.add(listener);
        return this;
    }

    public @Nullable Block get(Variant variant) {
        return blocks.get(variant);
    }

    public Block getBase() {
        locked();
        return base;
    }

    public Set<Variant> getVariants() {
        locked();
        return Collections.unmodifiableSet(blocks.keySet());
    }

    public boolean ownsBase() {
        return existingBase == null;
    }

    public void forEachTag(BiConsumer<TagKey<Block>, Block> out) {
        locked();
        TagKey<Block> mineable = minableTag != null ? minableTag : type.minableTag;

        if (existingBase == null) {
            out.accept(mineable, base);
            if (baseName.endsWith("_planks")) out.accept(BlockTags.PLANKS, base);
            for (TagKey<Block> tag : extraTags) out.accept(tag, base);
        }

        for (Variant variant : requested) {
            Tagger tagger = TAGGERS.get(variant);
            if (tagger != null) tagger.tag(this, out);
        }

        for (Block block : blocks.values()) {
            out.accept(mineable, block);
            for (TagKey<Block> tag : extraTags) out.accept(tag, block);
        }
    }

    public ComposedBlockFamily with(Variant... variants) {
        notLocked();
        for (Variant variant : variants) {
            Variant resolved = switch (variant) {
                case WALL_SIGN -> Variant.SIGN;
                //? if >=26.2 {
                /*case WALL_HANGING_SIGN -> Variant.HANGING_SIGN;
                case CUSTOM_WALL_HANGING_SIGN -> Variant.CUSTOM_HANGING_SIGN;
                *///?}
                default -> variant;
            };

            if (SPECS.stream().noneMatch(s -> s.variant() == resolved)) throw new IllegalArgumentException("Variant not supported by BlockFamilyBuilder: " + variant);
            requested.add(resolved);
        }
        return this;
    }

    public ComposedBlockFamily button() { return with(Variant.BUTTON); }
    public ComposedBlockFamily chiseled() { return with(Variant.CHISELED); }
    public ComposedBlockFamily cracked() { return with(Variant.CRACKED); }
    public ComposedBlockFamily cut() { return with(Variant.CUT); }
    public ComposedBlockFamily door() { return with(Variant.DOOR); }
    public ComposedBlockFamily fence() { return with(Variant.FENCE); }
    public ComposedBlockFamily fenceGate() { return with(Variant.FENCE_GATE); }
    public ComposedBlockFamily sign() { return with(Variant.SIGN); }
    public ComposedBlockFamily slab() { return with(Variant.SLAB); }
    public ComposedBlockFamily stairs() { return with(Variant.STAIRS); }
    public ComposedBlockFamily pressurePlate() { return with(Variant.PRESSURE_PLATE); }
    public ComposedBlockFamily polished() { return with(Variant.POLISHED); }
    public ComposedBlockFamily trapdoor() { return with(Variant.TRAPDOOR); }
    public ComposedBlockFamily wall() { return with(Variant.WALL); }
    public ComposedBlockFamily mosaic() { return with(Variant.MOSAIC); }
    public ComposedBlockFamily customFence() { return with(Variant.CUSTOM_FENCE); }
    public ComposedBlockFamily customFenceGate() { return with(Variant.CUSTOM_FENCE_GATE); }
    
    //? if >=26.1.2 {
    /*public ComposedBlockFamily bricks() { return with(Variant.BRICKS); }
    public ComposedBlockFamily cobbled() { return with(Variant.COBBLED); }
    public ComposedBlockFamily tiles() { return with(Variant.TILES); }
    //? if >=26.2 {
    /^public ComposedBlockFamily hangingSign() { return with(Variant.HANGING_SIGN); }
    public ComposedBlockFamily customHangingSign() { return with(Variant.CUSTOM_HANGING_SIGN); }
    public ComposedBlockFamily log() { return with(Variant.LOG); }
    public ComposedBlockFamily strippedLog() { return with(Variant.STRIPPED_LOG); }
    public ComposedBlockFamily pillar() { return with(Variant.PILLAR); }
    ^///? }
    *///?}

    public ComposedBlockFamily noGenerateModels() {
        notLocked();
        this.generateModels = false;
        return this;
    }

    public ComposedBlockFamily noGenerateRecipes() {
        notLocked();
        this.generateRecipes = false;
        return this;
    }

    //? if >=26.1.2 {
    /*public ComposedBlockFamily generateStonecutterRecipes() {
        notLocked();
        this.generateStonecutterRecipes = true;
        return this;
    }

    //? if 26.2 {
    /^public ComposedBlockFamily noGenerateCrackingRecipes() {
        notLocked();
        this.generateCrackingRecipes = false;
        return this;
    }
    ^///?}
    *///?}

    public ComposedBlockFamily group(String group) {
        notLocked();
        this.group = group;
        return this;
    }

    public ComposedBlockFamily unlockCriterionName(String unlockCriterionName) {
        notLocked();
        this.unlockCriterionName = unlockCriterionName;
        return this;
    }

    public BlockFamily getResult() {
        locked();
        return result.build();
    }

    public BlockFamily build() {
        notLocked();
        return runRegistration().getB().build();
    }

    public BlockFamily.Builder end() {
        notLocked();
        return runRegistration().getB();
    }

    public ComposedBlockFamily register() {
        notLocked();
        return runRegistration().getA();
    }

    private Pair<ComposedBlockFamily, BlockFamily.Builder> runRegistration() {
        if (result != null) return new Pair<>(this, result);

        base = existingBase != null ? existingBase : registerNamed(baseName, baseSettings(), Block::new);
        BlockFamily.Builder family = new BlockFamily.Builder(base);

        for (Spec spec : SPECS) {
            if (!requested.contains(spec.variant())) continue;
            spec.registrar().register(this);
            spec.applier().apply(family, this);
        }

        if (!generateModels) family.noGenerateModels();
        //~ if >=26.1.2 'noGenerateRecipes' -> 'noGenerateCraftingRecipes'
        if (!generateRecipes) family.noGenerateRecipes();
        //? if >=26.1.2 {
        /*if (generateStonecutterRecipes) family.generateStonecuttingRecipes();
        //? if >=26.2
        //if (!generateCrackingRecipes) family.noGenerateCrackingRecipes();
        *///?}
        if (group != null) family.group(group);
        if (unlockCriterionName != null) family.unlockCriterionName(unlockCriterionName);

        result = family;
        return new Pair<>(this, family);
    }

    private void notLocked() {
        if (result != null) throw new IllegalStateException("Block family '" + baseName + "' was already registered; it can't be modified.");
    }

    public void locked() {
        if (result == null) throw new IllegalStateException("Block family '" + baseName + "' hasn't been registered yet. Call register(), build() or end() first.");
    }

    private Block block(Variant variant) {
        return blocks.get(variant);
    }

    private String nameOf(Variant variant, String defaultName) {
        return nameOverrides.getOrDefault(variant, defaultName);
    }

    private void addSuffixed(Variant variant, AbstractBlock.Settings settings, DeferredBlockRegistry.BlockProvider<Block> provider) {
        blocks.put(variant, registerNamed(nameOf(variant, prefix + "_" + variant.getName()), settings, provider));
    }

    private void addPrefixed(Variant variant, AbstractBlock.Settings settings, DeferredBlockRegistry.BlockProvider<Block> provider) {
        blocks.put(variant, registerNamed(nameOf(variant, variant.getName() + "_" + baseName), settings, provider));
    }

    private Block registerNamed(String name, AbstractBlock.Settings settings, DeferredBlockRegistry.BlockProvider<Block> provider) {
        if (!usedNames.add(name)) throw new IllegalStateException("Block family '" + baseName + "' resolves two blocks to the name '" + name + "'. Use name(Variant, String) to rename one (e.g. fence() and customFence() together).");
        Block block = registry.register(name, provider, settings);
        for (RegisterListener listener : listeners) listener.onRegistered(name, block);
        return block;
    }

    private WoodType requireWoodType(Variant variant) {
        if (woodType == null) throw new IllegalStateException("Variant " + variant + " needs a WoodType, but base type " + type + " has none. Call woodType(...) on the builder.");
        return woodType;
    }

    private static String wallSignName(String signName) {
        return signName.endsWith("_sign") ? signName.substring(0, signName.length() - "_sign".length()) + "_wall_sign" : signName + "_wall";
    }

    private void registerHangingSign(Variant hanging, Variant wall) {
        WoodType wood = requireWoodType(hanging);
        String hangingName = nameOf(hanging, prefix + "_hanging_sign");
        //~ if minecraft: <=1.20.1 'wood, s' -> 's, wood'
        Block sign = registerNamed(hangingName, signSettings(), s -> new HangingSignBlock(wood, s));
        blocks.put(hanging, sign);
        //~ if minecraft: <=1.21 'lootTable(sign.getLootTableKey())' -> 'dropsLike(sign)'
        //~ if minecraft: <=1.20.1 'wood, s' -> 's, wood'
        blocks.put(wall, registerNamed(wallHangingSignName(hangingName), signSettings().lootTable(sign.getLootTableKey()), s -> new WallHangingSignBlock(wood, s)));
    }

    private static String wallHangingSignName(String name) {
        return name.endsWith("_hanging_sign") ? name.substring(0, name.length() - "_hanging_sign".length()) + "_wall_hanging_sign" : name + "_wall";
    }

    private static String derivePrefix(String baseName) {
        if (baseName.endsWith("_planks")) return baseName.substring(0, baseName.length() - "_planks".length());
        if (baseName.endsWith("_bricks")) return baseName.substring(0, baseName.length() - 1);
        return baseName;
    }

    private AbstractBlock.Settings baseSettings() {
        return AbstractBlock.Settings.copy(settingsSource != null ? settingsSource : type.template.get());
    }

    private AbstractBlock.Settings signSettings() {
        return baseSettings().noCollision().strength(1.0F);
    }

    private AbstractBlock.Settings destroyOnPush(AbstractBlock.Settings settings) {
        return settings.pistonBehavior(PistonBehavior.DESTROY);
    }
}