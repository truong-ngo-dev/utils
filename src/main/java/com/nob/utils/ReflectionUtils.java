package com.nob.utils;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

public class ReflectionUtils {


    /**
     * Retrieve a field by its name, including fields from superclasses (excluding Object).
     *
     * @param clazz     The class to start searching from.
     * @param fieldName The name of the field to retrieve.
     * @return an {@link Optional} containing the Field, or an empty Optional if the field is not found.
     * @throws IllegalArgumentException if clazz or fieldName is null.
     */
    public static Optional<Field> getField(Class<?> clazz, String fieldName) {
        if (Objects.isNull(clazz) || Objects.isNull(fieldName)) {
            throw new IllegalArgumentException("Class and field name must not be null");
        }
        Class<?> currentClass = clazz;
        while (Objects.nonNull(currentClass) && !currentClass.equals(Object.class)) {
            try {
                return Optional.of(currentClass.getDeclaredField(fieldName));
            } catch (NoSuchFieldException e) {
                currentClass = currentClass.getSuperclass();
            }
        }
        return Optional.empty();
    }


    /**
     * Retrieve the value of a field from an object.
     *
     * @param obj       The object to retrieve the field value from.
     * @param fieldName The name of the field.
     * @return an {@link Optional} containing the value of the field, or an empty Optional if the field is not found or inaccessible.
     * @throws IllegalArgumentException if obj or fieldName is null.
     * @throws IllegalStateException if field is not accessible
     */
    public static Object getFieldValue(Object obj, String fieldName) {
        if (Objects.isNull(obj) || Objects.isNull(fieldName)) {
            throw new IllegalArgumentException("Object and field name must not be null");
        }
        Class<?> clazz = obj.getClass();
        Field field = getField(clazz, fieldName).orElseThrow(() -> new IllegalArgumentException("Field " + fieldName + " not found"));
        field.setAccessible(true);
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Unable to access field: " + fieldName);
        }
    }
}
