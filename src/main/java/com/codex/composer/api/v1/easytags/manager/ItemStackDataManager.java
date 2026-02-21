package com.codex.composer.api.v1.easytags.manager;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import com.codex.composer.api.v1.nbt.NbtSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utility class for managing serialization and deserialization of custom
 * item stack data.
 * <p> Provides static methods to:
 * <ul>
 *     <li>Register deserializers for specific classes.</li>
 *     <li>Save {@link NbtSerializable} data to an {@link ItemStack}.</li>
 *     <li>Retrieve previously saved data from an {@link ItemStack}.</li>
 * </ul>
 */
public class ItemStackDataManager {
    private static final Map<Class<?>, Function<NbtCompound, ?>> deserializers = new HashMap<>();

    /**
     * Registers a deserializer function for a given class type.
     *
     * @param clazz        the class type to register
     * @param deserializer the function that converts an {@link NbtCompound} into an instance of the class
     * @param <T>          the type of the class
     */
    public static <T> void register(Class<T> clazz, Function<NbtCompound, T> deserializer) {
        deserializers.put(clazz, deserializer);
    }

    /**
     * Retrieves deserialized data of the specified class from an {@link NbtCompound}.
     *
     * @param tag   the tag to read from
     * @param clazz the class type of the data
     * @param <T>   the type of the data
     * @return an instance of the requested data type, or null if no data is present
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(NbtCompound tag, Class<T> clazz) {
        return tag.isEmpty() ? null : ((Function<NbtCompound, T>) deserializers.get(clazz)).apply(tag.getCompound(clazz.getSimpleName()));
    }

    /**
     * Retrieves deserialized data of the specified class from an {@link ItemStack}.
     *
     * @param stack the {@link ItemStack} to read from
     * @param clazz the class type of the data
     * @param <T>   the type of the data
     * @return an instance of the requested data type, or null no data is present
     */
    public static <T> T get(ItemStack stack, Class<T> clazz) {
        //? if minecraft: <=1.20.4 {
        /*return get(stack.getOrCreateNbt(), clazz);
        *///? } else {
        return null; // TODO: Implement this to work with the new system
        //? }
    }

    /**
     * Saves {@link NbtSerializable} data into the given {@link ItemStack}.
     * <p>
     * The data is stored using the simple class name as the key in the item's NBT compound.
     * </p>
     *
     * @param stack the item stack to save data into
     * @param data  the serializable data to save
     * @param <T>   the type of the data
     */
    public static <T extends NbtSerializable<T>> void save(ItemStack stack, T data) {
        //? if minecraft: <=1.20.4 {
        /*NbtCompound root = stack.getOrCreateNbt();
        save(root, data);
        stack.setNbt(root);
        *///? }

        // TODO: Implement this to work with the new system
    }

    /**
     * Saves {@link NbtSerializable} data into the given {@link NbtCompound}.
     * <p>
     * The data is stored using the simple class name as the key in the provided tag.
     * </p>
     *
     * @param tag  the nbt tag to save data into
     * @param data the serializable data to save
     * @param <T>  the type of the data
     */
    public static <T extends NbtSerializable<T>> void save(NbtCompound tag, T data) {
        tag.put(data.getClass().getSimpleName(), data.writeNbt());
    }
}
