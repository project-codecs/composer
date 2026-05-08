package com.codex.composer.api.v1.data;

import net.minecraft.nbt.NbtCompound;

/**
 * Common interface for enum classes that implement serializers for some value type.
 * No this is not the same as {@link NbtSerializable}, that needs to be implemented
 * by the object itself.
 */
public interface EnumSerializer<T> {
    void write(NbtCompound target, T value);
    T read(NbtCompound source);
}
