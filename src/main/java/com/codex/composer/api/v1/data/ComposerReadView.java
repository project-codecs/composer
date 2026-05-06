package com.codex.composer.api.v1.data;

//? if minecraft: >=1.21.6 {
/*import com.mojang.serialization.Codec;
import net.minecraft.storage.ReadView;
import net.minecraft.util.Identifier;

import java.util.*;

public class ComposerReadView {
    private final ReadView delegate;

    private ComposerReadView(ReadView delegate) {
        this.delegate = delegate;
    }

    public static ComposerReadView begin(ReadView view) {
        return new ComposerReadView(view);
    }

    public <T> List<T> getList(String key, Codec<T> codec) {
        List<T> list = new ArrayList<>();
        delegate.getListReadView(key).stream().map(view -> view.read("value", codec)).filter(Optional::isPresent).map(Optional::get).forEach(list::add);
        return list;
    }

    public <T> Map<String, T> getMap(String key, Codec<T> codec) {
        Map<String, T> map = new HashMap<>();
        delegate.getListReadView(key).stream().forEach(view -> {
            Optional<String> k = view.getOptionalString("k");
            Optional<T> value = view.read("v", codec);
            if (k.isPresent() && value.isPresent()) map.put(k.get(), value.get());
        });
        return map;
    }

    public Optional<Identifier> getIdentifier(String key) {
        return Optional.ofNullable(Identifier.tryParse(delegate.getString(key, "")));
    }

    public ReadView end() {
        return delegate;
    }

    // Alias
    public ReadView release() {
        return end();
    }
}
*///? }