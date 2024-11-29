package com.nob.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Mapper Utility
 * @author Truong Ngo
 * */
public class MapperUtils {

    /**
     * Default mapper
     * */
    public static ObjectMapper MAPPER = new ObjectMapper();

    static {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        MAPPER.registerModule(javaTimeModule);
    }
}
