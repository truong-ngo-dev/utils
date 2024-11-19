package com.nob.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;

/**
 * Json utility
 * */
public class JsonUtils {

    /**
     * Default object mapper
     * */
    private static final ObjectMapper MAPPER = new ObjectMapper();


    /**
     * Convert json string to object type {@code T}
     * @param json json string
     * @param clazz class type of {@code T}
     * @param <T> object type
     * @throws JsonProcessingException if json string is invalid
     * */
    public static <T> T fromJson(String json, Class<T> clazz) throws JsonProcessingException {
        return Objects.isNull(json) ? null : MAPPER.readValue(json, clazz);
    }


    /**
     * Convert json string to object type {@code T}
     * @param json json string
     * @param type parameterize type of {@code T}
     * @param <T> object type
     * @throws JsonProcessingException if json string is invalid
     * */
    public static <T> T fromJson(String json, TypeReference<T> type) throws JsonProcessingException {
        return Objects.isNull(json) ? null : MAPPER.readValue(json, type);
    }
}
