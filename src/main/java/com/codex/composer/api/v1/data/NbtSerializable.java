package com.codex.composer.api.v1.data;

//? if minecraft: <=1.21.5 {
import net.minecraft.nbt.NbtCompound;

/**
 * Base interface for objects that can be serialized to NBT.
 * <p>
 * Implementing classes provide logic to write their state into an {@link NbtCompound}.
 * A default method is provided to create a new {@link NbtCompound} and write into it.
 * </p>
 *
 * @param <T> the concrete type of the implementing class
 */
public interface NbtSerializable<T extends NbtSerializable<T>> {

    /**
     * Writes this object's state into the provided {@link NbtCompound}.
     *
     * @param tag the {@link NbtCompound} to write data into
     * @return the same {@link NbtCompound} containing serialized data
     */
    NbtCompound writeNbt(NbtCompound tag);

    default NbtCompound writeNbt() {
        return writeNbt(new NbtCompound());
    }
}
//? }