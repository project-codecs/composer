package com.codex.composer.api.v2.registry.lazy;

import com.codex.composer.api.v0.registry.lazy.AbstractItemRegisterable;
import com.codex.composer.api.v1.registry.lazy.EmptyDeferredRegistry;
import com.codex.composer.api.v1.registry.util.ComposedBlockFamily;
import com.codex.composer.api.v2.registry.lazy.struct.ComposedItemFamily;
import com.codex.composer.api.v2.registry.lazy.struct.FamilyPlacement;
import com.codex.composer.api.v2.registry.lazy.struct.ItemPlacement;
import net.minecraft.block.Block;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//? legacy {
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
//? } else
//import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;

//? if >=1.20.6
import net.minecraft.registry.RegistryKeys;

@ApiStatus.Experimental
public class DeferredItemRegistry extends EmptyDeferredRegistry implements AbstractItemRegisterable {
    private static final Logger LOGGER = LoggerFactory.getLogger("composer");

    private record Entry(Item item, int order, @Nullable Item anchor, boolean after) {}

    private static final Map<String, Map<RegistryKey<ItemGroup>, List<Entry>>> SHARED = new HashMap<>();
    private static final Set<String> HOOKED = new HashSet<>();

    private final @Nullable RegistryKey<ItemGroup> defaultGroup;
    private final Set<RegistryKey<ItemGroup>> usedGroups = new LinkedHashSet<>();
    private boolean finalized = false;

    public DeferredItemRegistry(String modId) {
        this(modId, null);
    }

    public DeferredItemRegistry(String modId, @Nullable RegistryKey<ItemGroup> defaultGroup) {
        super(modId);
        this.defaultGroup = defaultGroup;
    }

    public Item register(String name) {
        return registerInternal(name, Item::new, new Item.Settings(), ItemPlacement.inherit());
    }

    public Item register(String name, Item.Settings settings) {
        return registerInternal(name, Item::new, settings, ItemPlacement.inherit());
    }

    public <I extends Item> I register(String name, Function<Item.Settings, I> factory) {
        return registerInternal(name, factory, new Item.Settings(), ItemPlacement.inherit());
    }

    public <I extends Item> I register(String name, Function<Item.Settings, I> factory, Item.Settings settings) {
        return registerInternal(name, factory, settings, ItemPlacement.inherit());
    }

    public Item withSettings(String name, UnaryOperator<Item.Settings> settings) {
        return registerInternal(name, Item::new, settings.apply(new Item.Settings()), ItemPlacement.inherit());
    }

    public <I extends Item> I withSettings(String name, Function<Item.Settings, I> factory, UnaryOperator<Item.Settings> settings) {
        return registerInternal(name, factory, settings.apply(new Item.Settings()), ItemPlacement.inherit());
    }

    public BlockItem registerBlock(Block block, String name) {
        return registerInternal(name, s -> new BlockItem(block, s), new Item.Settings(), ItemPlacement.inherit());
    }

    public BlockItem registerBlock(Block block, String name, Item.Settings settings) {
        return registerInternal(name, s -> new BlockItem(block, s), settings, ItemPlacement.inherit());
    }

    public BlockItem registerBlock(Block block, String name, UnaryOperator<Item.Settings> settings) {
        return registerInternal(name, s -> new BlockItem(block, s), settings.apply(new Item.Settings()), ItemPlacement.inherit());
    }

    public ItemBuilder<Item> item(String name) {
        return new ItemBuilder<>(name, Item::new);
    }

    public ItemBuilder<BlockItem> blockItem(Block block, String name) {
        return new ItemBuilder<>(name, s -> new BlockItem(block, s));
    }

    public final class ItemBuilder<I extends Item> {
        private final String name;
        private final Function<Item.Settings, I> factory;
        private UnaryOperator<Item.Settings> settings = UnaryOperator.identity();
        private ItemPlacement placement = ItemPlacement.inherit();

        private ItemBuilder(String name, Function<Item.Settings, I> factory) {
            this.name = name;
            this.factory = factory;
        }

        public <J extends Item> ItemBuilder<J> factory(Function<Item.Settings, J> newFactory) {
            ItemBuilder<J> next = new ItemBuilder<>(name, newFactory);
            next.settings = this.settings;
            next.placement = this.placement;
            return next;
        }

        public ItemBuilder<I> settings(UnaryOperator<Item.Settings> op) {
            UnaryOperator<Item.Settings> previous = this.settings;
            this.settings = s -> op.apply(previous.apply(s));
            return this;
        }

        public ItemBuilder<I> group(RegistryKey<ItemGroup> group) { placement = placement.group(group); return this; }
        public ItemBuilder<I> noGroup() { placement = ItemPlacement.hidden(); return this; }
        public ItemBuilder<I> order(int order) { placement = placement.order(order); return this; }
        public ItemBuilder<I> after(Item anchor) { placement = placement.after(anchor); return this; }
        public ItemBuilder<I> before(Item anchor) { placement = placement.before(anchor); return this; }
        public ItemBuilder<I> placement(ItemPlacement placement) { this.placement = placement; return this; }

        public I register() {
            return registerInternal(name, factory, settings.apply(new Item.Settings()), placement);
        }
    }

    public ComposedItemFamily register(ComposedBlockFamily family) {
        return register(family, FamilyPlacement.create());
    }

    public ComposedItemFamily register(ComposedBlockFamily family, boolean addToGroup) {
        return register(family, addToGroup ? FamilyPlacement.create() : FamilyPlacement.hidden());
    }

    public ComposedItemFamily register(ComposedBlockFamily family, FamilyPlacement placement) {
        family.locked();
        if (family.itemFamily() != null) throw new IllegalStateException("Items for this block family were already registered.");

        EnumMap<BlockFamily.Variant, BlockItem> map = new EnumMap<>(BlockFamily.Variant.class);
        BlockItem base = family.ownsBase() ? registerFamilyBlock(family.getBase(), placement.forBase()) : null;

        family.getVariants().stream()
                .filter(Predicate.not(ComposedBlockFamily.DO_NOT_REGISTER_ITEM::contains))
                .forEach(variant -> map.put(variant, registerFamilyBlock(family.get(variant), placement.forVariant(variant))));

        return family.bindItemFamily(ComposedItemFamily.of(family).bind(base, map));
    }

    private BlockItem registerFamilyBlock(Block block, ItemPlacement placement) {
        Identifier id = Registries.BLOCK.getId(block);
        if (!Objects.equals(id.getNamespace(), modId)) throw new IllegalStateException("Mismatched mod id! Expected " + modId + " but got " + id);
        return registerInternal(id.getPath(), s -> new BlockItem(block, s), new Item.Settings(), placement);
    }

    private <I extends Item> I registerInternal(String name, @NotNull Function<Item.Settings, I> factory, @NotNull Item.Settings settings, @NotNull ItemPlacement placement) {
        if (finalized) throw new IllegalStateException("Registry already finalized, cannot register " + name);
        Identifier id = id(name);

        //? if >=1.21.3
        settings = settings.registryKey(RegistryKey.of(RegistryKeys.ITEM, id));
        I registered = Registry.register(Registries.ITEM, id, factory.apply(settings));
        RegistryKey<ItemGroup> group = placement.isHidden() ? null : placement.group() != null ? placement.group() : defaultGroup;

        if (group != null) {
            usedGroups.add(group);
            SHARED.computeIfAbsent(modId, k -> new HashMap<>())
                    .computeIfAbsent(group, k -> new ArrayList<>())
                    .add(new Entry(registered, placement.order(), placement.anchor(), placement.after()));
        }

        return registered;
    }

    public void finalizeRegistration() {
        if (finalized) return;
        finalized = true;

        for (RegistryKey<ItemGroup> group : usedGroups) {
            if (!HOOKED.add(modId + "/" + group.getValue())) continue;

            /*? if legacy {*/ItemGroupEvents.modifyEntriesEvent/*? } else {*//*CreativeModeTabEvents.modifyOutputEvent*//*? }*/(group)
                    .register(entries -> arrange(SHARED.get(modId).get(group), group).forEach(item -> entries.add(item.getDefaultStack())));
        }
    }

    private List<Item> arrange(List<Entry> entries, RegistryKey<ItemGroup> group) {
        List<Entry> result = new ArrayList<>();
        List<Entry> pending = new ArrayList<>();
        for (Entry entry : entries) (entry.anchor() == null ? result : pending).add(entry);
        result.sort(Comparator.comparingInt(Entry::order));

        boolean progress = true;
        while (!pending.isEmpty() && progress) {
            progress = false;
            for (Iterator<Entry> it = pending.iterator(); it.hasNext(); ) {
                Entry entry = it.next();
                int index = indexOf(result, entry.anchor());
                if (index < 0) continue;

                int pos = index;
                if (entry.after()) {
                    pos = index + 1;
                    while (pos < result.size() && result.get(pos).anchor() == entry.anchor() && result.get(pos).after()) pos++;
                }

                result.add(pos, entry);
                it.remove();
                progress = true;
            }
        }

        for (Entry entry : pending) {
            LOGGER.warn("[{}] Item {} is anchored to {} which is not in creative tab {}; appending at the end.",
                    modId, Registries.ITEM.getId(entry.item()), Registries.ITEM.getId(entry.anchor()), group.getValue());
            result.add(entry);
        }

        return result.stream().map(Entry::item).toList();
    }

    private static int indexOf(List<Entry> list, Item item) {
        for (int i = 0; i < list.size(); i++) if (list.get(i).item() == item) return i;
        return -1;
    }
}