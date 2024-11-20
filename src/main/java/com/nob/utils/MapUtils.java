package com.nob.utils;

import java.util.Map;
import java.util.stream.Collectors;

public class MapUtils {

    /**
     * Append prefix for key of given map
     * @param map the map
     * @param prefix prefix string
     * @return new map with appended prefix key
     * */
    public static Map<String, Object> appendKeyPrefix(Map<String, Object> map, String prefix) {
        return map.entrySet().stream().collect(Collectors.toMap(entry -> prefix + entry.getKey(), Map.Entry::getValue));
    }
}
