package com.codex.composer.api.v1.data;

//? if minecraft: >=1.21.6 {
/*import com.mojang.serialization.Codec;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

public class ComposerWriteView {
    private final WriteView delegate;

    private ComposerWriteView(WriteView delegate) {
        this.delegate = delegate;
    }

    public static ComposerWriteView begin(WriteView view) {
        return new ComposerWriteView(view);
    }

    public <T> ComposerWriteView putList(String key, Codec<T> codec, List<T> list) {
        WriteView.ListView view = delegate.getList(key);
        list.forEach(e -> view.add().put("value", codec, e));
        return this;
    }

    public <T> ComposerWriteView putMap(String key, Codec<T> codec, Map<String, T> map) {
        WriteView.ListView view = delegate.getList(key);
        map.forEach((k, v) -> {
            WriteView row = view.add();
            row.putString("k", k);
            row.put("v", codec, v);
        });
        return this;
    }

    public ComposerWriteView putIdentifier(String key, Identifier id) {
        delegate.putString(key, id.toString());
        return this;
    }

    public WriteView end() {
        return delegate;
    }

    // Alias
    public WriteView release() {
        return end();
    }
}
*///? }
