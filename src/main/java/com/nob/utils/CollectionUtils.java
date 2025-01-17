package com.nob.utils;

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
        if (TypeUtils.isCollection(o.getClass())) {
            if (Collection.class.isAssignableFrom(o.getClass())) {
                List<?> list = new ArrayList<>((Collection<?>) o);
                return list.get(index);
            } else {
                return Array.get(o, index);
            }
        }
        throw new IllegalArgumentException("Object is not a collection");
    }
}
