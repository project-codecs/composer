package com.codex.composer.api.v1.util.misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListBuilder<B, V> {
    private final List<V> emptyDefaults;
    private List<V> list = new ArrayList<>();
    private final B parent;

    private ListBuilder(B parent, List<V> emptyDefaults) {
        this.emptyDefaults = emptyDefaults;
        this.parent = parent;
    }

    public static <B, V> ListBuilder<B, V> of(B parent) {
        return of(parent, new ArrayList<>());
    }

    public static <B, V> ListBuilder<B, V> of(B parent, List<V> emptyDefaults) {
        return new ListBuilder<>(parent, emptyDefaults);
    }

    public ListBuilder<B, V> push(V elem) {
        this.list.add(elem);
        return this;
    }

    @SafeVarargs
    public final ListBuilder<B, V> push(V... elements) {
        this.list.addAll(Arrays.stream(elements).toList());
        return this;
    }

    public ListBuilder<B, V> pop() {
        if (!this.list.isEmpty()) {
            this.list.removeLast();
        }

        return this;
    }

    public ListBuilder<B, V> clear() {
        this.list.clear();
        return this;
    }

    public B end() {
        return this.parent;
    }

    public List<V> build() {
        return new ArrayList<>(this.list.isEmpty() ? this.emptyDefaults : this.list);
    }

    public ListBuilder<B, V> set(List<V> list) {
        this.list = list;
        return this;
    }
}
