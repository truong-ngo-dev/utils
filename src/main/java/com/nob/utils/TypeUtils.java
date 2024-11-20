package com.nob.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;

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
}