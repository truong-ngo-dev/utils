package com.nob.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Collection utility
 * @author Truong Ngo
 * */
public class CollectionUtils {

    private static final Logger log = LoggerFactory.getLogger(CollectionUtils.class);

    /**
     * Check if collection is null or empty
     * @param c the collection
     * */
    public static boolean isNullOrEmpty(Collection<?> c) {
        return Objects.isNull(c) || c.isEmpty();
    }


    /**
     * Retrieves an element from a collection or an array at the specified index.
     *
     * @param o     the object to retrieve the element from; must be a collection or an array, and not null
     * @param index the index of the element to retrieve; must be non-negative
     * @return the element at the specified index
     * @throws IllegalArgumentException if the provided object is not a collection or array
     * or the provided object is null or index is negative
     * @throws IndexOutOfBoundsException if the index is out of range for the collection or array
     */
    public static Object getElement(Object o, int index) {
        Assert.isTrue(o != null, "o must be not null");
        Assert.isTrue(index >= 0, "index must be non-negative");
        if (TypeUtils.isCollectionType(o.getClass())) {
            if (Collection.class.isAssignableFrom(o.getClass())) {
                List<?> list = new ArrayList<>((Collection<?>) o);
                return list.get(index);
            } else {
                return Array.get(o, index);
            }
        }
        throw new IllegalArgumentException("Object is not a collection");
    }


    /**
     * Casts an object to a {@code Map<String, Object>} if possible.
     *
     * <p>This method attempts to convert the given object into a {@code Map<String, Object>}.
     * It follows these rules:
     * <ul>
     *     <li>If the object is {@code null}, it returns {@code null}.</li>
     *     <li>If the object is already an instance of {@code Map}, it casts and returns it.</li>
     *     <li>If the object is a value type (determined by {@code TypeUtils.isValueType}),
     *         an {@code IllegalArgumentException} is thrown.</li>
     *     <li>Otherwise, the object is converted to JSON and then deserialized back into a map.</li>
     * </ul>
     *
     * <p>This approach ensures that objects with complex structures can be transformed into a map
     * representation, which is useful in scenarios such as logging, serialization, or dynamic data handling.</p>
     *
     * @param o the object to be converted
     * @return a {@code Map<String, Object>} representation of the object, or {@code null} if the input is {@code null}
     * @throws IllegalArgumentException if the object is a value type and cannot be converted to a map
     */
    public static Map<String, Object> castToMap(Object o) {
        if (o == null) return null;
        if (o instanceof Map<?, ?> map) {
            return map.entrySet().stream().collect(Collectors.toMap(
                    Object::toString,
                    Function.identity()));
        };
        if (o instanceof String s) {
            try {
                return JsonUtils.fromJson(s, new TypeReference<>() {});
            } catch (IllegalArgumentException e) {
                log.error("Invalid json map string: {}", e.getMessage(), e);
                throw new IllegalArgumentException("Invalid json map string: '" + o + "', details: " + e.getMessage());
            }
        }
        if (TypeUtils.isValueType(o.getClass())) throw new IllegalArgumentException("Unsupported type: " + o.getClass());
        return JsonUtils.fromJson(JsonUtils.toJson(o), new TypeReference<>() {});
    }


    /**
     * Converts the provided object to a {@link Collection}.
     *
     * <p>This method checks if the provided object is an array, a {@link Collection}, or a JSON string
     * representing a collection. It performs the following operations:</p>
     * <ul>
     *     <li>If the object is an array, it converts it into a {@link List}.</li>
     *     <li>If the object is already a {@link Collection}, it creates a new {@link ArrayList} containing the elements of the collection.</li>
     *     <li>If the object is a JSON string representing a collection, it deserializes it into a {@link Collection}.</li>
     * </ul>
     *
     * <p>If the object is not one of these types, an {@link IllegalArgumentException} is thrown.</p>
     *
     * @param o the object to be converted
     * @return a {@link Collection} containing the elements of the array, collection, or deserialized from the JSON string
     * @throws IllegalArgumentException if the provided object is neither an array, a collection, nor a valid JSON string representing a collection
     * @throws NullPointerException if {@code o} is {@code null}
     */
    public static Collection<?> castToCollection(Object o) {
        if (Objects.isNull(o)) return null;
        if (o.getClass().isArray()) {
            return IntStream.range(0, Array.getLength(o))
                    .mapToObj(i -> Array.get(o, i))
                    .collect(Collectors.toList());
        }
        if (Collection.class.isAssignableFrom(o.getClass())) {
            return new ArrayList<>((Collection<?>) o);
        }
        if (o instanceof String s) {
            try {
                return JsonUtils.fromJson(s, new TypeReference<>() {});
            } catch (IllegalArgumentException e) {
                log.error("Invalid collection json string: {}", e.getMessage(), e);
                throw new IllegalArgumentException("Invalid collection json string: '" + s + "', details: " + e.getMessage());
            }
        }
        throw new IllegalArgumentException(o.getClass() + " is not a collection");
    }
}
