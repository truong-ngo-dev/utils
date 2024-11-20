package com.nob.utils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * String utility
 * @author Truong Ngo
 * */
public class StringUtils {

    /**
     * Check if given string is empty or not after trim()
     * @param s the string
     * */
    public static boolean isTrimEmpty(String s) {
        return Objects.isNull(s) || s.trim().isEmpty();
    }


    /**
     * Join the object's collection into string
     * @param list the collection
     * @param delimiter delimiter between element
     * @param mapper map object of T to string
     * @param <T> generic type of object
     * */
    public static <T> String join(List<T> list, String delimiter, Function<T, String> mapper) {
        if (CollectionUtils.isNullOrEmpty(list)) return null;
        return list.stream().map(mapper).collect(Collectors.joining(delimiter));
    }


    /**
     * Split string into collection of object
     * @param s the string
     * @param delimiter delimiter between element
     * @param mapper map string to object of T
     * @param <T> generic type of object
     * */
    public static <T> List<T> split(String s, String delimiter, Function<String, T> mapper) {
        if (isTrimEmpty(s)) return Collections.emptyList();
        return Stream.of(s.split(delimiter)).map(mapper).collect(Collectors.toList());
    }
}
