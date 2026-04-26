package com.codex.composer.api.v1.util.builder;

import com.codex.composer.api.v1.util.exception.NotAllFieldsFilledException;

import java.lang.reflect.Field;

public class BuilderFields {
    /**
     * Verifies that all non-@Nullable fields of the given object are non-null.
     * Throws an IllegalStateException if any field is null.
     */
    public static void verify(Object obj) {
        if (obj == null) throw new IllegalArgumentException("Cannot verify null object");

        Class<?> clazz = obj.getClass();

        while (clazz != Object.class) { // also check superclasses
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Optional.class)) continue;

                field.setAccessible(true);
                try {
                    Object value = field.get(obj);
                    if (value == null) {
                        throw new NotAllFieldsFilledException("Field '" + field.getName() +
                                "' in " + obj.getClass().getSimpleName() + " is null but not @Nullable");
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to access field " + field.getName(), e);
                }
            }
            clazz = clazz.getSuperclass();
        }
    }
}
