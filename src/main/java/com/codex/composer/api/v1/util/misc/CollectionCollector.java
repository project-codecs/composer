package com.codex.composer.api.v1.util.misc;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class CollectionCollector {
    public static <T> List<T> toList(Supplier<Integer> size, Function<Integer, T> getter) {
        return toCollection(ArrayList::new, size, getter);
    }

    public static <T> List<T> toUnmodifiableList(Supplier<Integer> size, Function<Integer, T> getter) {
        return List.copyOf(toList(size, getter));
    }

    public static <T> Set<T> toSet(Supplier<Integer> size, Function<Integer, T> getter) {
        return toCollection(HashSet::new, size, getter);
    }

    public static <T> Set<T> toUnmodifiableSet(Supplier<Integer> size, Function<Integer, T> getter) {
        return Set.copyOf(toSet(size, getter));
    }

    public static <T, C extends Collection<T>> C toCollection(Supplier<C> collectionFactory, Supplier<Integer> size, Function<Integer, T> getter) {
        C collection = collectionFactory.get();
        for (int i = 0; i < size.get(); i++) {
            collection.add(getter.apply(i));
        }
        return collection;
    }

    public static <T> Collection<T> toUnmodifiableCollection(Supplier<Integer> size, Function<Integer, T> getter) {
        return Collections.unmodifiableCollection(toCollection(ArrayList::new, size, getter));
    }

    public static <T> Stream<T> stream(Supplier<Integer> size, Function<Integer, T> getter) {
        return toList(size, getter).stream();
    }
}
