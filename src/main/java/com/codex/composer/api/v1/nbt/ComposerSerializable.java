package com.codex.composer.api.v1.nbt;

import net.minecraft.nbt.NbtCompound;

/**
 * Extends {@link NbtSerializable} with support for {@link ComposerCompound}.
 * <p>
 * Provides a default implementation for {@link #writeNbt(NbtCompound)} that
 * delegates to {@link #writeNbt(ComposerCompound)}.
 * </p>
 *
 * @param <T> Type of the serializable object
 */
public interface ComposerSerializable<T extends NbtSerializable<T>> extends NbtSerializable<T> {

    @Override
    default NbtCompound writeNbt(NbtCompound tag) {
        return writeNbt(ComposerCompound.copy(tag)).asVanilla();
    }

    /**
     * Writes this object to a {@link ComposerCompound}.
     *
     * @param tag The compound to write into
     * @return The same compound, populated with serialized data
     */
    ComposerCompound writeNbt(ComposerCompound tag);
}
