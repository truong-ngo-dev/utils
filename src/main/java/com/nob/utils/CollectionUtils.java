package com.nob.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.util.Assert;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Collection utility
 * @author Truong Ngo
 * */
public class CollectionUtils {

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


    @SuppressWarnings("unchecked")
    public static Map<String, Object> castToMap(Object o) {
        if (o == null) return null;
        if (o instanceof Map) return (Map<String, Object>) o;
        if (TypeUtils.isValueType(o.getClass())) throw new IllegalArgumentException("Unsupported type: " + o.getClass());
        return JsonUtils.fromJson(JsonUtils.toJson(o), new TypeReference<>() {});
    }
}
