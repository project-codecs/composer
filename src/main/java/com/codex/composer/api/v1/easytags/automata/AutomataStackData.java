package com.codex.composer.api.v1.easytags.automata;

import org.jetbrains.annotations.NotNull;
import com.codex.composer.api.v1.easytags.annotation.DefaultSerialize;
import com.codex.composer.api.v1.easytags.annotation.Serialize;
import com.codex.composer.api.v1.easytags.annotation.Transient;
import com.codex.composer.api.v1.easytags.data.ComposerStackData;
import com.codex.composer.api.v1.easytags.exception.AutomataSerializationException;
import com.codex.composer.api.v1.easytags.registry.AutomataSerializable;
import com.codex.composer.api.v1.easytags.registry.AutomataSerializers;
import com.codex.composer.api.v1.nbt.ComposerCompound;

import java.lang.reflect.Field;

/**
 * Abstract base class for item stack data that supports automatic serialization and deserialization
 * of its fields to and from {@link ComposerCompound} NBT data.
 * <p> Features:
 * <ul>
 *     <li>Automatic serialization of fields marked with {@link Serialize}.</li>
 *     <li>Optional {@link DefaultSerialize} at the class level to serialize all fields by default.</li>
 *     <li>Fields marked with {@link Transient} are always ignored.</li>
 *     <li>Delegates actual reading/writing to registered {@link AutomataSerializable} implementations.</li>
 * </ul>
 * <p>
 * Subclasses should declare fields to serialize either individually with {@link Serialize} or by using
 * {@link DefaultSerialize} at the class level. Custom types must have a corresponding serializer registered
 * in {@link AutomataSerializers}.
 * </p>
 *
 * @param <T> The concrete subclass type for fluent API support
 */
@SuppressWarnings("unchecked")
public abstract class AutomataStackData<T extends AutomataStackData<T>> implements ComposerStackData<T> {

    /**
     * Serializes this instance's annotated fields into the provided {@link ComposerCompound}.
     * <p>
     * Only fields explicitly annotated with {@link Serialize} or included by {@link DefaultSerialize} are written.
     * Fields marked as {@link Transient} are skipped. The actual serialization is delegated to
     * the corresponding {@link AutomataSerializable} for each field type.
     * </p>
     *
     * @param tag the {@link ComposerCompound} to write data into
     * @return the same {@link ComposerCompound} containing serialized data
     * @throws AutomataSerializationException if a field cannot be serialized or no serializer is registered
     */
    @Override
    public ComposerCompound writeNbt(@NotNull ComposerCompound tag) {
        boolean serializeAll = getClass().isAnnotationPresent(DefaultSerialize.class)
                && getClass().getAnnotation(DefaultSerialize.class).value();

        for (Field field : getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Transient.class) && field.getAnnotation(Transient.class).value()) continue;

            Serialize annotation = field.getAnnotation(Serialize.class);
            if (annotation == null && !serializeAll) continue;

            field.setAccessible(true);
            String key = (annotation != null && !annotation.key().isEmpty()) ? annotation.key() : field.getName();

            try {
                Object value = field.get(this);
                if (value == null) continue;

                Class<?> fieldClass = value.getClass();
                AutomataSerializable<Object> serializer = (AutomataSerializable<Object>) AutomataSerializers.get(fieldClass);
                serializer.write(tag, key, value);

            } catch (Exception e) {
                throw new AutomataSerializationException("Failed to serialize field: " + field.getName(), e);
            }
        }

        return tag;
    }

    /**
     * Deserializes data from the provided {@link ComposerCompound} into this instance's fields.
     * <p>
     * Only fields explicitly annotated with {@link Serialize} or included by {@link DefaultSerialize} are read.
     * Fields marked as {@link Transient} are skipped. The actual deserialization is delegated to
     * the corresponding {@link AutomataSerializable} for each field type.
     * </p>
     *
     * @param tag the {@link ComposerCompound} containing serialized data
     * @throws AutomataSerializationException if a field cannot be deserialized or no serializer is registered
     */
    public void readNbt(@NotNull ComposerCompound tag) {
        boolean serializeAll = getClass().isAnnotationPresent(DefaultSerialize.class)
                && getClass().getAnnotation(DefaultSerialize.class).value();

        for (Field field : getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Transient.class) && field.getAnnotation(Transient.class).value()) continue;

            Serialize annotation = field.getAnnotation(Serialize.class);
            if (annotation == null && !serializeAll) continue;

            field.setAccessible(true);
            String key = (annotation != null && !annotation.key().isEmpty()) ? annotation.key() : field.getName();

            try {
                Class<?> fieldClass = field.getType();
                AutomataSerializable<Object> serializer = (AutomataSerializable<Object>) AutomataSerializers.get(fieldClass);
                Object value = serializer.read(tag, key);
                field.set(this, value);

            } catch (Exception e) {
                throw new AutomataSerializationException("Failed to deserialize field: " + field.getName(), e);
            }
        }
    }
}
