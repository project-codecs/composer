package com.codex.composer.api.v1.util.data;

import com.google.gson.JsonObject;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import com.codex.composer.api.v1.nbt.JsonSerializable;
import com.codex.composer.api.v1.nbt.NbtSerializable;

public class SerializableIdentifier implements Comparable<SerializableIdentifier>, NbtSerializable<SerializableIdentifier>, JsonSerializable<SerializableIdentifier> {
    private final Identifier wrapped;

    public SerializableIdentifier(String namespace, String path) {
        this.wrapped = Identifier.of(namespace, path);
    }

    public SerializableIdentifier(NbtCompound tag) {
        this(tag.getString("namespace")/*? if minecraft: >=1.21.5 {*/.orElse("")/*? }*/, tag.getString("path")/*? if minecraft: >=1.21.5 {*/.orElse("")/*? }*/);
    }

    public SerializableIdentifier(JsonObject object) {
        this(object.get("namespace").getAsString(), object.get("path").getAsString());
    }

    public SerializableIdentifier(Identifier identifier) {
        this(identifier.getNamespace(), identifier.getPath());
    }

    public static SerializableIdentifier of(Identifier identifier) {
        return new SerializableIdentifier(identifier);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        tag.putString("namespace", namespace());
        tag.putString("path", path());
        return tag;
    }

    @Override
    public JsonObject write(JsonObject object) {
        object.addProperty("namespace", namespace());
        object.addProperty("path", path());
        return object;
    }

    public Identifier ident() {
        return wrapped;
    }

    public String path() {
        return wrapped.getPath();
    }

    public String namespace() {
        return wrapped.getNamespace();
    }

    public int compareTo(SerializableIdentifier identifier) {
        int i = path().compareTo(identifier.path());
        if (i == 0) {
            i = namespace().compareTo(identifier.namespace());
        }

        return i;
    }
}
