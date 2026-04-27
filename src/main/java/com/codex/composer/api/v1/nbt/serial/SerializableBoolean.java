package com.codex.composer.api.v1.nbt.serial;

import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;
import com.codex.composer.api.v1.nbt.NbtSerializable;

public class SerializableBoolean implements Comparable<Boolean>, NbtSerializable<SerializableBoolean> {
    private boolean value;

    public SerializableBoolean(NbtCompound tag) {
        this(tag.getBoolean("value")/*? if minecraft: >=1.21.5 {*//*.orElse(false)*//*? }*/);
    }

    public SerializableBoolean(boolean b) {
        value = b;
    }

    public boolean get() {
        return value;
    }

    public void set(boolean b) {
        this.value = b;
    }

    @Override
    public int compareTo(@NotNull Boolean o) {
        return (value == o) ? 0 : (value ? 1 : -1);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        tag.putBoolean("value", value);
        return tag;
    }
}
