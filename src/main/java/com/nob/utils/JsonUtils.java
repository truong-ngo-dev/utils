package com.nob.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Json utility
 * @author Truong Ngo
 * @version 1.0.0
 * */
public class JsonUtils {

    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);

    /**
     * Default object mapper
     * */
    private static final ObjectMapper MAPPER = new ObjectMapper();


    static {
        MAPPER.findAndRegisterModules();
    }


    /**
     * Converts a JSON string into an object of the specified type {@code T}.
     *
     * @param json  The JSON string to be converted.
     * @param clazz The class type of {@code T}.
     * @param <T>   The target object type.
     * @return An object of type {@code T} parsed from the JSON string, or {@code null} if the input is {@code null}.
     * @throws IllegalArgumentException If the JSON string is invalid or cannot be deserialized.
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return Objects.isNull(json) ? null : MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse json string: {}, cause: {}", json, e.getMessage(), e);
            throw new IllegalArgumentException("Failed to parse json string: " + json + ", cause: " + e.getMessage(), e);
        }
    }


    /**
     * Converts a JSON string into an object of the specified parameterized type {@code T}.
     *
     * @param json The JSON string to be converted.
     * @param type The parameterized type reference of {@code T}.
     * @param <T>  The target object type.
     * @return An object of type {@code T} parsed from the JSON string, or {@code null} if the input is {@code null}.
     * @throws IllegalArgumentException If the JSON string is invalid or cannot be deserialized.
     */
    public static <T> T fromJson(String json, TypeReference<T> type) {
        try {
            return Objects.isNull(json) ? null : MAPPER.readValue(json, type);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse json string: {}, cause: {}", json, e.getMessage(), e);
            throw new IllegalArgumentException("Failed to parse json string: " + json + ", cause: " + e.getMessage(), e);
        }
    }


    /**
     * Serializes an object into a JSON string.
     *
     * @param obj The object to be serialized.
     * @return A JSON string representation of the object.
     * @throws IllegalArgumentException If serialization fails.
     */
    public static String toJson(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Failed to stringify json object: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Failed to stringify json object: " + e.getMessage(), e);
        }
    }


    /**
     * Sanitizes the given object by replacing byte arrays (`byte[]`) with a placeholder message, usually use for logging.
     *
     * <p>This method recursively processes the input object and:
     * <ul>
     *     <li>Returns {@code null} if the input is {@code null}.</li>
     *     <li>Replaces {@code byte[]} instances with the string {@code "Binary data omitted"}.</li>
     *     <li>Returns the original object if it is a value type (determined by {@link  TypeUtils#isValueType(Class)}).</li>
     *     <li>Recursively processes collections by applying the same sanitization to their elements.</li>
     *     <li>Processes maps by replacing any byte arrays found in their values.</li>
     *     <li>If the object is neither a collection nor a map, attempts to convert it into a map
     *         and sanitize the resulting key-value pairs.</li>
     *     <li>If conversion to a map fails, returns the original object.</li>
     * </ul>
     *
     * <p>Example usage:</p>
     * <blockquote><pre>
     *     byte[] data = new byte[]{1, 2, 3};
     *     Object sanitized = sanitizeByteArray(data);
     *     System.out.println(sanitized); // Outputs: "Binary data"
     * </pre></blockquote>
     *
     * @param body the object to be sanitized
     * @return the sanitized object with binary data replaced, or the original object if no modifications are needed
     * @throws IllegalArgumentException if an error occurs during object conversion
     */
    public static Object sanitizeByteArray(Object body) {
        if (Objects.isNull(body)) return null;
        if (body instanceof byte[]) return "Binary data";
        if (TypeUtils.isValueType(body.getClass())) return body;
        if (TypeUtils.isCollectionType(body.getClass())) {
            Collection<?> list = CollectionUtils.castToCollection(body);
            if (Objects.isNull(list)) return null;
            return list.stream().map(JsonUtils::sanitizeByteArray).collect(Collectors.toList());
        }
        if (TypeUtils.isMapType(body.getClass())) {
            Map<String, Object> map = CollectionUtils.castToMap(body);
            map.replaceAll((key, value) -> {
                if (value instanceof byte[]) return "Binary data";
                return sanitizeByteArray(value);
            });
        }
        try {
            Map<String, Object> map = MAPPER.convertValue(body, new TypeReference<>() {});
            map.replaceAll((key, value) -> {
                if (value instanceof byte[]) return "Binary data";
                return sanitizeByteArray(value);
            });
            return map;
        } catch (IllegalArgumentException e) {
            log.error("Error sanitizing byte array fields: {}", e.getMessage(), e);
            return body;
        }
    }
}
