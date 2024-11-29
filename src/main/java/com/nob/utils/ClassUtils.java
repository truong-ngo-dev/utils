package com.nob.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Class utility
 * */
@Slf4j
public class ClassUtils {

    /**
     * Prevent instantiate
     * */
    private ClassUtils() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }


    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException invalid package name
     * @throws IOException read file error
     */
    public static List<Class<?>> scanPackage(String packageName) throws IOException, ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        List<Class<?>> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return new ArrayList<>(classes);
    }


    /**
     * Utility method of {@link #scanPackage(String)} <p>
     * Recursive method used to find all classes in a given directory and sub-dirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException if package name is invalid
     */
    private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        assert files != null;
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }


    /**
     * Load all json file of specific resource folder into object type {@code T}
     * @param classPath directory from root classpath
     * @param clazz class type of {@code T}
     * @param <T> object type T
     * @return List of object type {@code T}
     * */
    public static <T> List<T> loadJson(String classPath, Class<T> clazz) {
        return loadJsonInternal(classPath, clazz, null);
    }


    /**
     * Load all json file of specific resource folder into generic parameterize object type {@code T}
     * @param classPath directory from root classpath
     * @param type generic parameterize object type of {@code T}
     * @param <T> object type T
     * @return List of object type {@code T}
     * */
    public static <T> List<T> loadJson(String classPath, TypeReference<T> type) {
        return loadJsonInternal(classPath, null, type);
    }


    /**
     * Helper method for load json file into object. If the base directory not exist or some file is not valid ignored
     * @param <T> type of object
     * @param clazz class type of object
     * @param type parameterize type of object
     * @return List of object type {@code T}
     * @see #loadJson(String, Class)
     * @see #loadJson(String, TypeReference)
     * */
    private static <T> List<T> loadJsonInternal(String classPath, Class<T> clazz, TypeReference<T> type) {
        List<T> result = new ArrayList<>();
        try {
            String path = classPath.replace('.', '/') + "/*.json";
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources(path);
            for (Resource r : resources) {
                String json = new String(Files.readAllBytes(Paths.get(r.getURI())));
                T o = Objects.nonNull(clazz) ?
                        JsonUtils.fromJson(json, clazz) :
                        JsonUtils.fromJson(json, type);
                result.add(o);
            }
        } catch (IOException e) {
            log.warn("Error occur while loading the json file on resource folder!");
        }
        return result;
    }


    /**
     * Find the getter base on field name
     * @param fieldName object field name
     * @return {@code Function} act as object getter
     * */
    public static Function<Object, Object> fieldGetter(String fieldName) {
        return o -> {
            try {
                Field field = o.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(o);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                return null;
            }
        };
    }
}
