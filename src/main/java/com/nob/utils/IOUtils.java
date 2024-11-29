package com.nob.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * IO Utility
 * @author Truong Ngo
 * */
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
            ObjectMapper mapper = MapperUtils.MAPPER;
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
            ObjectMapper mapper = MapperUtils.MAPPER;
            return mapper.readValue(resource.getInputStream(), type);
        } catch (IOException e) {
            return null;
        }
    }

}
