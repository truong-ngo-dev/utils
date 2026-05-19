package vn.truongngo.lib.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import vn.truongngo.lib.utils.serialization.JsonUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * IO Utility
 * @author Truong Ngo
 * */
@Slf4j
public class IOUtils {

    /**
     * Read resource file to object {@code T}
     * @param classPath path to resource
     * @param clazz desire type
     * @param <T> generic desire type
     * @return object of type {@code T} or null if resource not found or error occur
     * */
    public static <T> T readFile(String classPath, Class<T> clazz) {
        try {
            ClassPathResource resource = new ClassPathResource(classPath);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(resource.getInputStream(), clazz);
        } catch (IOException e) {
            return null;
        }
    }


    /**
     * Read resource file to object {@code T}
     * @param classPath path to resource
     * @param type desire parameterized type
     * @param <T> generic desire type
     * @return object of type {@code T} or null if resource not found or error occur
     * */
    public static <T> T readFile(String classPath, TypeReference<T> type) {
        try {
            ClassPathResource resource = new ClassPathResource(classPath);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(resource.getInputStream(), type);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Loads all JSON files from a classpath directory into a list of objects.
     *
     * @param classPath the directory path under classpath (must not be {@code null})
     * @param clazz     the target class type (must not be {@code null})
     * @param <T>       the type of each object
     * @return list of deserialized objects (never {@code null})
     */
    public static <T> List<T> loadJson(String classPath, Class<T> clazz) {
        return loadJsonInternal(classPath, clazz, null);
    }

    /**
     * Loads all JSON files from a classpath directory into a list of objects of the specified parameterized type.
     *
     * @param classPath the directory path under classpath (must not be {@code null})
     * @param type      the generic parameterized type (must not be {@code null})
     * @param <T>       the type of each object
     * @return list of deserialized objects (never {@code null})
     */
    public static <T> List<T> loadJson(String classPath, TypeReference<T> type) {
        return loadJsonInternal(classPath, null, type);
    }

    private static <T> List<T> loadJsonInternal(String classPath, Class<T> clazz, TypeReference<T> type) {
        List<T> result = new ArrayList<>();
        try {
            String pattern = classPath.replace('.', '/') + "/*.json";
            Resource[] resources = new PathMatchingResourcePatternResolver().getResources(pattern);
            for (Resource r : resources) {
                try {
                    String json = new String(Files.readAllBytes(Paths.get(r.getURI())));
                    result.add(Objects.nonNull(clazz) ? JsonUtils.fromJson(json, clazz) : JsonUtils.fromJson(json, type));
                } catch (Exception ignored) {}
            }
        } catch (IOException e) {
            log.warn("Failed to load JSON files from classpath: {}", classPath);
        }
        return result;
    }

}
