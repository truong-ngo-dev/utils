package vn.truongngo.lib.utils.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
     *     <li>Returns the original object if it is a value type.</li>
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
        if (ClassUtils.isSimpleValueType(body.getClass())) return body;
        if (body.getClass().isArray() || Collection.class.isAssignableFrom(body.getClass())) {
            Collection<?> list = castToCollection(body);
            if (Objects.isNull(list)) return null;
            return list.stream().map(JsonUtils::sanitizeByteArray).collect(Collectors.toList());
        }
        if (Map.class.isAssignableFrom(body.getClass())) {
            Map<String, Object> map = castToMap(body);
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
        if (ClassUtils.isSimpleValueType(o.getClass())) throw new IllegalArgumentException("Unsupported type: " + o.getClass());
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
