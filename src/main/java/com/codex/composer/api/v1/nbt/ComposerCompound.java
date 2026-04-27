package com.codex.composer.api.v1.nbt;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.*;
import java.util.function.Function;

//? if minecraft: <=1.21.4
import com.google.common.collect.Maps;

/**
 * A helper wrapper around {@link NbtCompound} with fluent and type-safe methods
 * for serializing and deserializing {@link NbtSerializable} objects, collections,
 * and maps.
 */
public class ComposerCompound /*? if minecraft: <=1.21.4 {*/extends NbtCompound/*? }*/ implements Cloneable {
    //? if minecraft: <=1.21.4 {
    public ComposerCompound(Map<String, NbtElement> entries) {
        super(entries);
    }

    public ComposerCompound() {
        this(Maps.newHashMap());
    }//? } else {
    /*private final NbtCompound delegate;

    public ComposerCompound(NbtCompound of) {
        delegate = of;
    }

    public ComposerCompound() {
        this(new NbtCompound());
    }
    *///? }

    public NbtCompound asVanilla() {
        //? if minecraft: <=1.21.4
        return this;

        //? if minecraft: >=1.21.5
        //return delegate;
    }

    public static ComposerCompound copy(NbtCompound tag) {
        //? if minecraft: <=1.21.4 {
        ComposerCompound nbt = new ComposerCompound();
        tag.getKeys().forEach(key -> nbt.put(key, tag.get(key)));
        return nbt;
        //? } else {
        /*return new ComposerCompound(tag);
        *///? }
    }

    @Override
    public ComposerCompound clone() {
        try {
            ComposerCompound clone = (ComposerCompound) super.clone();
            /*? if minecraft: >=1.21.5 {*//*delegate.*//*? }*/getKeys().forEach(key -> clone./*? if minecraft: >=1.21.5 {*//*delegate.*//*? }*/put(key, /*? if minecraft: >=1.21.5 {*//*delegate.*//*? }*/get(key)));
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public <T extends NbtSerializable<?>> void putList(String key, Collection<T> list) {
        NbtList tagList = new NbtList();
        list.forEach(e -> tagList.add(e.writeNbt()));
        /*? if minecraft: >=1.21.5 {*//*delegate.*//*? }*/put(key, tagList);
    }

    public <T extends NbtSerializable<T>> ComposerCompound putListFluent(String key, Collection<T> list) {
        putList(key, list);
        return this;
    }

    public <T extends NbtSerializable<T>> List<T> getList(String key, Function<NbtCompound, T> factory) {
        NbtList tagList = /*? if minecraft: >=1.21.5 {*//*delegate.*//*? }*/getList(key/*? if minecraft: <=1.21.4 {*/, NbtElement.COMPOUND_TYPE/*? }*/)/*? if minecraft: >=1.21.5 {*//*.orElse(new NbtList())*//*? }*/;
        List<T> list = new ArrayList<>();
        for (int i = 0; i < tagList.size(); i++) {
            list.add(factory.apply(tagList.getCompound(i)/*? if minecraft: >=1.21.5 {*//*.orElse(new NbtCompound())*//*? }*/));
        }
        return list;
    }

    public <T extends NbtSerializable<T>> List<T> getListOrDefault(String key, Function<NbtCompound, T> factory) {
        if (!/*? if minecraft: >=1.21.5 {*//*delegate.*//*? }*/contains(key/*? if minecraft: <=1.21.4 {*/, NbtElement.LIST_TYPE/*? }*/)) return new ArrayList<>();
        return getList(key, factory);
    }

    public void putSerializable(String key, NbtSerializable<?> value) {
        /*? if minecraft: >=1.21.5 {*//*delegate.*//*? }*/put(key, value.writeNbt());
    }

    public <T extends NbtSerializable<T>> T getSerializable(String key, Function<NbtCompound, T> factory) {
        return factory.apply(/*? if minecraft: >=1.21.5 {*//*delegate.*//*? }*/getCompound(key)/*? if minecraft: >=1.21.5 {*//*.orElse(new NbtCompound())*//*? }*/);
    }

    public <T extends NbtSerializable<T>> Optional<T> getOptional(String key, Function<NbtCompound, T> factory) {
        return /*? if minecraft: >=1.21.5 {*//*delegate.*//*? }*/contains(key/*? if minecraft: <=1.21.4 {*/, NbtElement.COMPOUND_TYPE/*? }*/)
                ? Optional.of(getSerializable(key, factory))
                : Optional.empty();
    }

    public <T extends NbtSerializable<?>> void putMap(String key, Map<String, T> map) {
        NbtList list = new NbtList();
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            ComposerCompound tag = new ComposerCompound();
            tag./*? if minecraft: >=1.21.5 {*//*delegate.*//*? }*/putString("key", entry.getKey());
            tag.putSerializable("value", (NbtSerializable<?>) entry.getValue());
            list.add(tag/*? if minecraft: >=1.21.5 {*//*.delegate*//*? }*/);
        }
        /*? if minecraft: >=1.21.5 {*//*delegate.*//*? }*/put(key, list);
    }

    public <T extends NbtSerializable<T>> Map<String, T> getMap(String key, Function<NbtCompound, T> factory) {
        NbtList values = /*? if minecraft: >=1.21.5 {*//*delegate.*//*? }*/getList(key/*? if minecraft: <=1.21.4 {*/, NbtElement.COMPOUND_TYPE/*? }*/)/*? if minecraft: >=1.21.5 {*//*.orElse(new NbtList())*//*? }*/;
        Map<String, T> map = new HashMap<>();
        for (NbtElement value : values) {
            if (value instanceof NbtCompound tag) {
                map.put(tag.getString("key")/*? if minecraft: >=1.21.5 {*//*.orElse("")*//*? }*/, factory.apply(tag.getCompound("value")/*? if minecraft: >=1.21.5 {*//*.orElse(new NbtCompound())*//*? }*/));
            }
        }
        return map;
    }
}
