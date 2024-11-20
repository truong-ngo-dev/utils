package com.nob.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TypeUtils {

    /**
     * Check if given type is number or not
     * @param clazz type
     * @return true if type is number
     * @see #isPrimitiveNumber(Class)
     * @see #isWrapperNumber(Class)
     * */
    public static boolean isNumber(Class<?> clazz) {
        return isPrimitiveNumber(clazz) || isWrapperNumber(clazz);
    }


    /**
     * Utility method for {@link #isNumber(Class)}
     * <p>
     * Check if type is primitive number or not
     * @param clazz type
     * @return true if type is one of these: byte, short, int, long, float, double
     * */
    private static boolean isPrimitiveNumber(Class<?> clazz) {
        return
                clazz == byte.class ||
                clazz == short.class ||
                clazz == int.class ||
                clazz == long.class ||
                clazz == double.class ||
                clazz == float.class;
    }


    /**
     * Utility method for {@link #isNumber(Class)}
     * <p>
     * Check if type is wrapper number or not
     * @param clazz type
     * @return true if type is one of these:
     * {@link Byte}, {@link Short}, {@link Integer}, {@link Long}, {@link Float}, {@link Double},
     * {@link java.math.BigInteger}, {@link java.math.BigDecimal} and other Atomic type
     * */
    private static boolean isWrapperNumber(Class<?> clazz) {
        return Number.class.isAssignableFrom(clazz);
    }


    /**
     * Check if given type is integer
     * @param clazz type
     * @return true if type is integer
     * */
    public static boolean isInteger(Class<?> clazz) {
        return
                clazz == int.class || clazz == Integer.class ||
                clazz == long.class || clazz == Long.class ||
                clazz == short.class || clazz == Short.class ||
                clazz == byte.class || clazz == Byte.class ||
                clazz == BigInteger.class;
    }


    /**
     * Check if given type is decimal
     * @param clazz type
     * @return true if type is decimal
     * */
    public static boolean isDecimal(Class<?> clazz) {
        return
                clazz == float.class || clazz == Float.class ||
                clazz == double.class || clazz == Double.class ||
                clazz == BigDecimal.class;
    }


    /**
     * Check if given type is boolean or not
     * @param clazz type
     * @return true if type is boolean
     * */
    public static boolean isBoolean(Class<?> clazz) {
        return clazz == boolean.class || clazz == Boolean.class;
    }


    /**
     * Check if given type is string
     * @param clazz type
     * @return true if type is string
     * */
    public static boolean isString(Class<?> clazz) {
        return clazz == String.class || clazz == Character.class || clazz == char.class;
    }


    /**
     * Check if given type is one of supported date type
     * @param clazz given type
     * @return true if type is Date
     * */
    public static boolean isDate(Class<?> clazz) {
        return
                clazz == LocalDate.class ||
                clazz == LocalDateTime.class ||
                clazz == ZonedDateTime.class ||
                clazz == Instant.class ||
                clazz == Date.class;
    }


    /**
     * Check if given type is enum
     * @param clazz given type
     * @return true if type is Enum
     * */
    public static boolean isEnum(Class<?> clazz) {
        return clazz.isEnum();
    }


    /**
     * Check if type is value based
     * @param clazz given type
     * @return true if type is one of Date, Number, String, Boolean or Enum
     * */
    public static boolean isValueBased(Class<?> clazz) {
        return isNumber(clazz) || isBoolean(clazz) || isString(clazz) || isDate(clazz) || isEnum(clazz);
    }


    /**
     * Check if type is collection or not
     * @param clazz type
     * @return true is type is collection or array
     * */
    public static boolean isCollection(Class<?> clazz) {
        return clazz.isArray() || Collection.class.isAssignableFrom(clazz);
    }


    /**
     * Check if type is map
     * @param clazz type
     * @return true if type is map
     * */
    public static boolean isMap(Class<?> clazz) {
        return Map.class.isAssignableFrom(clazz);
    }


    /**
     * Get the element java type of collection, apply for object as other object property <p>
     * Don't use for {@code java.util.List<Object>}, {@code java.util.List<?>}, {@code Object[]}
     * @param clazz object type
     * @param field reflection field represent object
     * @return java type of element
     * @throws IllegalArgumentException if object is not a collection type
     * */
    public static Class<?> getElementType(Class<?> clazz, Field field) {
        if (clazz.isArray()) {
            return clazz.getComponentType();
        } else {
            field.setAccessible(true);
            ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
            return  (Class<?>) parameterizedType.getActualTypeArguments()[0];
        }
    }
}
