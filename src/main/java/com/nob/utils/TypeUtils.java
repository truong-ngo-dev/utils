package com.nob.utils;

import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.*;

/**
 * Utility for data type
 * @author Truong Ngo
 * @version 1.0.0
 * */
public class TypeUtils {

    /**
     * Checks whether the given class represents a primitive type.
     *
     * <p>This method returns {@code true} if the specified {@code Class} object
     * represents a Java primitive type such as {@code int}, {@code double}, or {@code boolean}.
     * It does not consider wrapper classes (e.g., {@code Integer}, {@code Double}) as primitive types.</p>
     *
     * @param type the class to check
     * @return {@code true} if the given class represents a primitive type; {@code false} otherwise
     * @throws NullPointerException if {@code type} is {@code null}
     */
    public static boolean isPrimitiveType(Class<?> type) {
        return type.isPrimitive();
    }


    /**
     * Checks whether the given class is a wrapper type for a primitive type.
     *
     * <p>This method returns {@code true} if the specified {@code Class} object
     * represents one of Java's wrapper types for primitive values.
     *
     * @param type the class to check
     * @return {@code true} if the given class is a wrapper type; {@code false} otherwise
     * @throws NullPointerException if {@code type} is {@code null}
     */
    public static boolean isWrapperType(Class<?> type) {
        return
                type == Void.class || type == Character.class ||
                type == Byte.class || type == Short.class ||
                type == Integer.class || type == Long.class ||
                type == Float.class || type == Double.class;
    }


    /**
     * Determines whether the given class represents a void type.
     *
     * <p>This method checks if the specified {@code Class} object is either {@code void.class}
     * (the primitive {@code void} type) or {@code Void.class} (the wrapper class for void).</p>
     *
     * @param type the class to check
     * @return {@code true} if the given class represents a void type; {@code false} otherwise
     * @throws NullPointerException if {@code type} is {@code null}
     */
    public static boolean isVoidType(Class<?> type) {
        return (type == void.class || type == Void.class);
    }


    /**
     * Checks whether the given class represents a primitive numeric type.
     *
     * <p>This method returns {@code true} if the specified {@code Class} object
     * represents one of Java's primitive numeric types.
     *
     * @param clazz the class to check
     * @return {@code true} if the given class represents a primitive numeric type; {@code false} otherwise
     * @throws NullPointerException if {@code clazz} is {@code null}
     */
    public static boolean isPrimitiveNumberType(Class<?> clazz) {
        return
                clazz == byte.class || clazz == short.class ||
                clazz == int.class || clazz == long.class ||
                clazz == double.class || clazz == float.class;
    }


    /**
     * Checks whether the given class represents a wrapper type for a primitive numeric type.
     *
     * <p>This method returns {@code true} if the specified {@code Class} object
     * is one of Java's wrapper classes for primitive numeric types.
     *
     * @param type the class to check
     * @return {@code true} if the given class represents a wrapper numeric type; {@code false} otherwise
     * @throws NullPointerException if {@code clazz} is {@code null}
     */
    public static boolean isWrapperNumberType(Class<?> type) {
        return
                type == Byte.class || type == Short.class ||
                type == Integer.class || type == Long.class ||
                type == Double.class || type == Float.class;
    }


    /**
     * Checks whether the given class represents a big number type.
     *
     * <p>This method returns {@code true} if the specified {@code Class} object
     * represents one of Java's big number types, specifically {@code BigDecimal} or {@code BigInteger}.
     * These types are used for high-precision arithmetic with arbitrarily large numbers.</p>
     *
     * @param type the class to check
     * @return {@code true} if the given class represents a big number type; {@code false} otherwise
     * @throws NullPointerException if {@code type} is {@code null}
     */
    public static boolean isBigNumberType(Class<?> type) {
        return (type == BigDecimal.class || type == BigInteger.class);
    }


    /**
     * Checks whether the given class represents a standard numeric type.
     *
     * <p>This method returns {@code true} if the specified {@code Class} object
     * is either a primitive numeric type, its corresponding wrapper class, or a large number type
     * such as {@code BigDecimal} or {@code BigInteger}.
     * The recognized standard numeric types include:</p>
     *
     * <ul>
     *     <li>Primitive or Wrapper numeric types:
     *         <ul>
     *             <li>{@code byte.class} || {@code Byte.class}</li>
     *             <li>{@code short.class} || {@code Short.class}</li>
     *             <li>{@code int.class} || {@code Integer.class}</li>
     *             <li>{@code long.class} || {@code Long.class}</li>
     *             <li>{@code float.class} || {@code Float.class}</li>
     *             <li>{@code double.class} || {@code Double.class}</li>
     *         </ul>
     *     </li>
     *     <li>Big number types:
     *         <ul>
     *             <li>{@code BigDecimal.class}</li>
     *             <li>{@code BigInteger.class}</li>
     *         </ul>
     *     </li>
     * </ul>
     *
     * @param type the class to check
     * @return {@code true} if the given class represents a standard numeric type; {@code false} otherwise
     * @throws NullPointerException if {@code type} is {@code null}
     */
    public static boolean isStandardNumberType(Class<?> type) {
        return isPrimitiveNumberType(type) || isWrapperNumberType(type) || isBigNumberType(type);
    }


    /**
     * Checks whether the given class represents a numeric type.
     *
     * <p>This method returns {@code true} if the specified {@code Class} object
     * is a subclass of {@link Number}, including all numeric wrapper types,
     * {@link BigDecimal}, {@link BigInteger}, and any custom subclasses of {@code Number}
     * like {@link java.util.concurrent.atomic.AtomicInteger}, {@link java.util.concurrent.atomic.AtomicLong}...</p>
     *
     * @param type the class to check
     * @return {@code true} if the given class is a subclass of {@code Number}; {@code false} otherwise
     * @throws NullPointerException if {@code clazz} is {@code null}
     */
    public static boolean isNumberType(Class<?> type) {
        return Number.class.isAssignableFrom(type);
    }


    /**
     * Checks whether the given class represents an integer-based numeric type.
     *
     * <p>This method returns {@code true} if the specified {@code Class} object
     * represents an integer-based type, including both primitive integer types,
     * their corresponding wrapper classes, and {@link BigInteger}.</p>
     *
     * @param type the class to check
     * @return {@code true} if the given class represents an integer-based numeric type; {@code false} otherwise
     * @throws NullPointerException if {@code clazz} is {@code null}
     */
    public static boolean isIntegerNumberType(Class<?> type) {
        return
                type == int.class || type == Integer.class ||
                type == long.class || type == Long.class ||
                type == short.class || type == Short.class ||
                type == byte.class || type == Byte.class ||
                type == BigInteger.class;
    }


    /**
     * Checks whether the given class represents a decimal-based numeric type.
     *
     * <p>This method returns {@code true} if the specified {@code Class} object
     * represents a floating-point or high-precision decimal type.
     *
     * @param type the class to check
     * @return {@code true} if the given class represents a decimal-based numeric type; {@code false} otherwise
     * @throws NullPointerException if {@code clazz} is {@code null}
     */
    public static boolean isDecimalNumberType(Class<?> type) {
        return
                type == float.class || type == Float.class ||
                type == double.class || type == Double.class ||
                type == BigDecimal.class;
    }


    /**
     * Checks whether the given class represents a boolean type.
     *
     * <p>This method returns {@code true} if the specified {@code Class} object
     * represents the primitive boolean type or its corresponding wrapper class.</p>
     *
     * @param type the class to check
     * @return {@code true} if the given class represents a boolean type; {@code false} otherwise
     * @throws NullPointerException if {@code clazz} is {@code null}
     */
    public static boolean isBooleanType(Class<?> type) {
        return type == boolean.class || type == Boolean.class;
    }


    /**
     * Checks whether the given class represents a character type.
     *
     * <p>This method returns {@code true} if the specified {@code Class} object
     * represents a single character.
     *
     * @param type the class to check
     * @return {@code true} if the given class represents a character type; {@code false} otherwise
     * @throws NullPointerException if {@code type} is {@code null}
     */
    public static boolean isCharacterType(Class<?> type) {
        return type == Character.class || type == char.class;
    }


    /**
     * Checks whether the given class represents a string type.
     *
     * <p>This method returns {@code true} if the specified {@code Class} object
     * represents a string.
     *
     * @param type the class to check
     * @return {@code true} if the given class represents a string; {@code false} otherwise
     * @throws NullPointerException if {@code type} is {@code null}
     */
    public static boolean isStringType(Class<?> type) {
        return type == String.class;
    }


    /**
     * Checks whether the given class represents a standard date or time type.
     *
     * <p>This method returns {@code true} if the specified {@code Class} object
     * represents a commonly used date or time type in Java, including:</p>
     *
     * <ul>
     *     <li>{@link java.time.LocalDate} - A date without time-zone</li>
     *     <li>{@link java.time.LocalDateTime} - A date-time without time-zone</li>
     *     <li>{@link java.time.ZonedDateTime} - A date-time with time-zone</li>
     *     <li>{@link java.time.Instant} - A timestamp representing an instant in time</li>
     *     <li>{@link java.util.Date} - A legacy date representation</li>
     * </ul>
     *
     * @param type the class to check
     * @return {@code true} if the given class represents a standard date or time type; {@code false} otherwise
     * @throws NullPointerException if {@code clazz} is {@code null}
     */
    public static boolean isStandardDateType(Class<?> type) {
        return
                type == LocalDate.class ||
                type == LocalDateTime.class ||
                type == ZonedDateTime.class ||
                type == Instant.class ||
                type == Date.class;
    }


    /**
     * Checks whether the given class represents a legacy date type.
     *
     * <p>This method returns {@code true} if the specified {@code Class} object
     * is a subclass of {@link java.util.Date}, including {@link java.sql.Date},
     * {@link java.sql.Timestamp}, and {@link java.sql.Time}.</p>
     *
     * @param type the class to check
     * @return {@code true} if the given class is a subclass of {@code java.util.Date}; {@code false} otherwise
     * @throws NullPointerException if {@code type} is {@code null}
     */
    public static boolean isLegacyDateType(Class<?> type) {
        return Date.class.isAssignableFrom(type);
    }


    /**
     * Checks whether the given class represents a Java Time API date or time type.
     *
     * <p>This method returns {@code true} if the specified {@code Class} object
     * is a subclass of {@link java.time.temporal.Temporal}, including:</p>
     *
     * <ul>
     *     <li>{@link java.time.LocalDate}</li>
     *     <li>{@link java.time.LocalDateTime}</li>
     *     <li>{@link java.time.ZonedDateTime}</li>
     *     <li>{@link java.time.Instant}</li>
     *     <li>{@link java.time.OffsetDateTime}</li>
     *     <li>{@link java.time.OffsetTime}</li>
     *     <li>{@code etc...}</li>
     * </ul>
     *
     * @param type the class to check
     * @return {@code true} if the given class is a subclass of {@code java.time.temporal.Temporal}; {@code false} otherwise
     * @throws NullPointerException if {@code type} is {@code null}
     */
    public static boolean isJavaTimeDateType(Class<?> type) {
        return Temporal.class.isAssignableFrom(type);
    }


    /**
     * Checks whether the given class represents any type of date or time.
     *
     * <p>This method returns {@code true} if the specified {@code Class} object
     * is either a legacy date type (e.g., {@link java.util.Date}, {@link java.sql.Date})
     * or a Java Time API date or time type (e.g., {@link java.time.LocalDate}, {@link java.time.Instant}).</p>
     *
     * @param type the class to check
     * @return {@code true} if the given class is a legacy or Java Time date type; {@code false} otherwise
     * @throws NullPointerException if {@code type} is {@code null}
     */
    public static boolean isDateType(Class<?> type) {
        return isLegacyDateType(type) || isJavaTimeDateType(type);
    }


    /**
     * Checks whether the given class represents an enumeration type.
     *
     * <p>This method returns {@code true} if the specified {@code Class} object
     * is an {@code enum} type.</p>
     *
     * @param type the class to check
     * @return {@code true} if the given class is an enumeration type; {@code false} otherwise
     * @throws NullPointerException if {@code clazz} is {@code null}
     */
    public static boolean isEnumType(Class<?> type) {
        return type.isEnum();
    }


    /**
     * Checks whether the given class represents a standard value-based type.
     *
     * <p>A standard value-based type is a type commonly used for representing
     * immutable values, including:</p>
     *
     * <ul>
     *     <li>Void type: {@code void.class}, {@code Void.class}</li>
     *     <li>Standard numeric types (see {@code isStandardNumberType})</li>
     *     <li>Boolean types: {@code boolean.class}, {@code Boolean.class}</li>
     *     <li>String and character types: {@code String.class}, {@code char.class}, {@code Character.class}</li>
     *     <li>Standard date/time types (see {@code isStandardDateType})</li>
     *     <li>Enumeration types</li>
     * </ul>
     *
     * @param type the class to check
     * @return {@code true} if the given class represents a standard value-based type; {@code false} otherwise
     * @throws NullPointerException if {@code clazz} is {@code null}
     */
    public static boolean isStandardValueType(Class<?> type) {
        return
                isVoidType(type) ||
                isStandardNumberType(type) ||
                isBooleanType(type) ||
                isCharacterType(type) ||
                isStringType(type) ||
                isStandardDateType(type) ||
                isEnumType(type);
    }


    /**
     * Checks whether the given class represents a simple value type.
     *
     * <p>This method uses {@link ClassUtils#isSimpleValueType(Class)} to determine if the specified
     * class represents a simple value type, which includes primitive types, their wrappers,
     * {@link String}, {@link Boolean}, {@link Character}, and other basic types.</p>
     *
     * @param type the class to check
     * @return {@code true} if the given class represents a simple value type; {@code false} otherwise
     * @throws NullPointerException if {@code clazz} is {@code null}
     */
    public static boolean isValueType(Class<?> type) {
        return ClassUtils.isSimpleValueType(type);
    }


    /**
     * Checks whether the given type is a collection or array.
     *
     * <p>This method returns {@code true} if the specified {@code Class} object represents a collection
     * (e.g., {@link java.util.List}, {@link java.util.Set}, etc.) or an array type. It uses
     * {@link Class#isArray()} and {@link java.util.Collection} to determine if the class is a collection or array.</p>
     *
     * @param clazz the class to check
     * @return {@code true} if the given class is a collection or an array; {@code false} otherwise
     * @throws NullPointerException if {@code clazz} is {@code null}
     */
    public static boolean isCollectionType(Class<?> clazz) {
        return clazz.isArray() || Collection.class.isAssignableFrom(clazz);
    }


    /**
     * Checks whether the given type is a map.
     *
     * <p>This method returns {@code true} if the specified {@code Class} object represents a map
     * (e.g., {@link java.util.Map}, {@link java.util.HashMap}, etc.). It uses
     * {@link java.util.Map} to determine if the class is a map.</p>
     *
     * @param clazz the class to check
     * @return {@code true} if the given class is a map; {@code false} otherwise
     * @throws NullPointerException if {@code clazz} is {@code null}
     */
    public static boolean isMapType(Class<?> clazz) {
        return Map.class.isAssignableFrom(clazz);
    }


    /**
     * Gets the element type's of a collection or array. This is useful for determining the type of elements
     * contained within collections or arrays, especially when working with reflection and generic types.
     *
     * <p>Note: This method should not be used for types like {@code java.util.List<Object>},
     * {@code java.util.List<?>}, or {@code Object[]} as they are not directly applicable for type retrieval.</p>
     *
     * <p>The method works as follows:</p>
     * <ul>
     *     <li>If the given class represents an array, it returns the component type of the array.</li>
     *     <li>If the given class represents a generic collection type, it retrieves the actual type argument
     *         (i.e., the element type) from the field's generic type using reflection.</li>
     * </ul>
     *
     * <blockquote><pre>
     *     // Example 1: For array type
     *     Class<?> elementType1 = getElementType(String[].class, someField);  // returns String.class
     *
     *     // Example 2: For List type
     *     Class<?> elementType2 = getElementType(List.class, someField);  // returns String.class for List<String>
     * </pre></blockquote>
     *
     * @param clazz the class representing the object type (collection or array)
     * @param field the field in the object representing the collection
     * @return the Java type of the element (component type for arrays or actual type argument for collections)
     * @throws IllegalArgumentException if the provided object type is not a collection or array
     * @throws NullPointerException if {@code clazz} or {@code field} is {@code null}
     */
    public static Class<?> resolveCollectionElementType(Class<?> clazz, Field field) {
        if (clazz.isArray()) {
            return clazz.getComponentType();
        } else {
            field.setAccessible(true);
            ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
            return (Class<?>) parameterizedType.getActualTypeArguments()[0];
        }
    }


    /**
     * Retrieves the element type's of a collection parameter in a method annotated with a specific annotation.
     *
     * <p>This method scans the parameters of the provided {@code method} to find the one annotated with the given
     * annotation {@code annotationClass}. If such a parameter exists and is of a collection type (e.g., {@link java.util.List},
     * {@link java.util.Set}), it will return the generic type of the elements contained within the collection.
     * If the parameter is an array, it will return the component type of the array.</p>
     *
     * <p>If no parameter is annotated with the specified annotation, or if the annotated parameter is not a collection or array,
     * the method will return {@code null}.</p>
     *
     * <blockquote><pre>
     *     // Example usage:
     *     Method method = someObject.getClass().getMethod("someMethod", List.class);
     *     Class<?> elementType = getParameterGenericElementType(method, SomeAnnotation.class);
     *     // If 'someMethod' has a parameter annotated with @SomeAnnotation and is a List<String>,
     *     // elementType will be String.class.
     * </pre></blockquote>
     *
     * @param method the method whose parameters to check
     * @param annotationClass the annotation class to search for on the method parameters
     * @param <A> the type of the annotation
     * @return the generic type of the collection's elements, or {@code null} if no annotated parameter is found
     *         or the parameter is not a collection or array
     * @throws NullPointerException if {@code method} or {@code annotationClass} is {@code null}
     *
     * @author Truong Ngo
     * @version 1.0.0
     */
    public static <A extends Annotation> Class<?> getAnnotatedParameterElementType (Method method, Class<A> annotationClass) {
        Parameter parameter = null;
        Parameter[] parameters = method.getParameters();
        for (Parameter p : parameters) {
            if (p.isAnnotationPresent(annotationClass)) {
                parameter = p;
            }
        }

        if (Objects.nonNull(parameter)) {
            if (parameter.getType().isArray()) {
                return parameter.getType().getComponentType();
            }
            if (Collection.class.isAssignableFrom(parameter.getType())) {
                ParameterizedType parameterizedType = (ParameterizedType) parameter.getParameterizedType();
                return (Class<?>) parameterizedType.getActualTypeArguments()[0];
            }
        }
        return null;
    }


    /**
     * Converts a given string value to the specified type.
     *
     * <p>This method attempts to cast the provided string to the desired target type, which is specified by
     * the {@code clazz} parameter. The method checks several possible types, including numeric types,
     * string types, character types, boolean types, enums, and standard date types. If the type is not
     * recognized, it attempts to parse the value as JSON.</p>
     *
     * <p>Supported conversions include:</p>
     * <ul>
     *     <li>If the target type is a standard numeric type (e.g., {@code Integer}, {@code Double}), the
     *         value is parsed as a number.</li>
     *     <li>If the target type is a string, the value is returned as is.</li>
     *     <li>If the target type is a character, the value is parsed as a single character.</li>
     *     <li>If the target type is a boolean, the value is parsed using {@code Boolean.parseBoolean()}.</li>
     *     <li>If the target type is an enum, the value is converted to the corresponding enum constant.</li>
     *     <li>If the target type is a standard date type (e.g., {@code LocalDate}, {@code Date}), the value is
     *         parsed using a date utility.</li>
     *     <li>For unsupported types, the value is attempted to be parsed as JSON using {@code JsonUtils.fromJson()}.</li>
     * </ul>
     *
     * <blockquote><pre>
     *     cast("123", Integer.class);  // returns 123
     *     cast("true", Boolean.class);  // returns true
     *     cast("A", Character.class);   // returns 'A'
     *     cast("2025-04-01", LocalDate.class);  // returns LocalDate of April 1, 2025
     * </pre></blockquote>
     *
     * @param value the string value to be converted
     * @param clazz the target class type to which the value should be converted
     * @param <T> the type of the result
     * @return the converted value, or {@code null} if the input string is {@code null}
     * @throws IllegalArgumentException if the string value cannot be converted to the specified type
     * @throws NullPointerException if the {@code clazz} is {@code null}
     */
    @SuppressWarnings("all")
    public static <T> T cast(String value, Class<T> clazz) {
        if (Objects.isNull(value)) return null;
        if (isStandardNumberType(clazz)) return clazz.cast(NumberUtils.toNumber(clazz, value));
        if (isStringType(clazz)) return clazz.cast(value);
        if (isCharacterType(clazz)) return clazz.cast(StringUtils.castToChar(value));
        if (isBooleanType(clazz)) return clazz.cast(Boolean.parseBoolean(value));
        if (isEnumType(clazz)) return clazz.cast(Enum.valueOf((Class<Enum>) clazz, value));
        if (isStandardDateType(clazz)) return clazz.cast(DateUtils.parseWithSystemZone(value, clazz));
        return JsonUtils.fromJson(value, clazz);
    }
}
