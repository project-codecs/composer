package com.codex.composer.api.v1.datagen.util;

//? if minecraft: >=1.20.4 {
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.TagBuilder;
import net.minecraft.registry.tag.TagEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.function.Function;

@SuppressWarnings({"UnusedReturnValue", "unused"})
public class TypedTagBuilder<T> {
    private final TagBuilder delegate;
    private final Function<T, Identifier> converter;

    private TypedTagBuilder(TagBuilder delegate, Function<T, Identifier> converter) {
        this.delegate = delegate;
        this.converter = converter;
    }

    public static <T> TypedTagBuilder<T> of(TagBuilder delegate, Function<T, Identifier> converter) {
        return new TypedTagBuilder<>(delegate, converter);
    }

    public static <T> TypedTagBuilder <T> of(TagBuilder delegate, Registry<T> registry) {
        return new TypedTagBuilder<>(delegate, registry::getId);
    }

    public TypedTagBuilder<T> add(TagEntry entry) {
        delegate.add(entry);
        return this;
    }

    public TypedTagBuilder<T> add(T entry) {
        return this.add(TagEntry.create(converter.apply(entry)));
    }

    @SafeVarargs
    public final TypedTagBuilder<T> add(T... entry) {
        Arrays.stream(entry).forEach(this::add);
        return this;
    }

    public TypedTagBuilder<T> addOptional(T entry) {
        return this.add(TagEntry.createOptional(converter.apply(entry)));
    }

    @SafeVarargs
    public final TypedTagBuilder<T> addOptional(T... entry) {
        Arrays.stream(entry).forEach(this::add);
        return this;
    }

    public TypedTagBuilder<T> addTag(TagKey<T> tagKey) {
        return this.add(TagEntry.createTag(tagKey.id()));
    }

    @SafeVarargs
    public final TypedTagBuilder<T> addTag(TagKey<T>... tagKeys) {
        Arrays.stream(tagKeys).forEach(this::addTag);
        return this;
    }

    public TypedTagBuilder<T> addOptionalTag(TagKey<T> tagKey) {
        return this.add(TagEntry.createOptionalTag(tagKey.id()));
    }

    @SafeVarargs
    public final TypedTagBuilder<T> addOptionalTag(TagKey<T>... tagKeys) {
        Arrays.stream(tagKeys).forEach(this::addOptionalTag);
        return this;
    }

    public TypedTagBuilder<T> add(Identifier id) {
        return this.add(TagEntry.create(id));
    }

    public TypedTagBuilder<T> addOptional(Identifier id) {
        return this.add(TagEntry.createOptional(id));
    }

    public TypedTagBuilder<T> addTag(Identifier id) {
        return this.add(TagEntry.createTag(id));
    }

    public TypedTagBuilder<T> addOptionalTag(Identifier id) {
        return this.add(TagEntry.createOptionalTag(id));
    }
}
//? }