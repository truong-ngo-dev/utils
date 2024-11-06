package com.nob.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

/**
 * Utility for date type, all of those class below will be treated as date type:
 * <ul>
 *     <li>{@link java.util.Date}</li>
 *     <li>{@link java.time.LocalDate}</li>
 *     <li>{@link java.time.LocalDateTime}</li>
 *     <li>{@link java.time.ZonedDateTime}</li>
 *     <li>{@link java.time.Instant}</li>
 * </ul>
 * Provide utility method for converting and manipulating date type.
 * @author Truong Ngo
 * */
public class DateUtils {

    /**
     * Prevent instantiate
     * */
    private DateUtils() {
        throw new UnsupportedOperationException("Cannot be instantiated!");
    }

    /**
     * Parse a given string into date type {@code T}
     * @param s a string represent date
     * @param pattern date pattern
     * @param targetType type of date
     * @param zoneId the time zone required
     * @return date object of type {@code T}
     * @throws IllegalArgumentException if the string or format or date type is invalid
     * */
    public static <T> T parseDate(String s, String pattern, Class<T> targetType, ZoneId zoneId)  {
        DateTimeFormatter formatter;
        try {
            formatter = DateTimeFormatter.ofPattern(pattern);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid pattern: " + pattern, e);
        }
        try {
            if (targetType == LocalDate.class) {
                return targetType.cast(LocalDate.parse(s, formatter));
            } else if (targetType == LocalDateTime.class) {
                return targetType.cast(LocalDateTime.parse(s, formatter));
            } else if (targetType == Instant.class) {
                LocalDateTime dateTime = LocalDateTime.parse(s, formatter);
                return targetType.cast(dateTime.atZone(zoneId).toInstant());
            } else if (targetType == Date.class) {
                LocalDateTime dateTime = LocalDateTime.parse(s, formatter);
                Instant instant = dateTime.atZone(zoneId).toInstant();
                return targetType.cast(Date.from(instant));
            } else if (targetType == ZonedDateTime.class) {
                LocalDateTime dateTime = LocalDateTime.parse(s, formatter);
                return targetType.cast(dateTime.atZone(zoneId));
            } else {
                throw new IllegalArgumentException("Unsupported target type: " + targetType);
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + s, e);
        }
    }

    /**
     * Parse a given string into date type {@code T} with UTC+0 zone
     * @param s a string represent date
     * @param pattern date pattern
     * @param targetType type of date
     * @return date object of type {@code T}
     * @throws IllegalArgumentException if the string or format or date type is invalid
     * */
    public static <T> T parseDateWithUTCZone(String s, String pattern, Class<T> targetType) {
        ZoneId zoneId = ZoneId.of("UTC");
        return parseDate(s, pattern, targetType, zoneId);
    }

    /**
     * Parse a given string into date type {@code T} with system zone
     * @param s a string represent date
     * @param pattern date pattern
     * @param targetType type of date
     * @return date object of type {@code T}
     * @throws IllegalArgumentException if the string or format or date type is invalid
     * */
    public static <T> T parseDateWithSystemZone(String s, String pattern, Class<T> targetType) {
        ZoneId zoneId = ZoneId.systemDefault();
        return parseDate(s, pattern, targetType, zoneId);
    }

    /**
     * Parse a given timestamp into date type {@code T}
     * @param timestamp timestamp as millisecond
     * @param targetType type of date
     * @return date object of type {@code T}
     * @throws IllegalArgumentException if the string or format or date type is invalid
     * */
    public static <T> T parseDate(Long timestamp, Class<T> targetType) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        if (targetType == Date.class) {
            return targetType.cast(Date.from(instant));
        } else if (targetType == Instant.class) {
            return targetType.cast(instant);
        } else if (targetType == LocalDate.class) {
            return targetType.cast(instant.atZone(ZoneId.systemDefault()).toLocalDate());
        } else if (targetType == LocalDateTime.class) {
            return targetType.cast(instant.atZone(ZoneId.systemDefault()).toLocalDateTime());
        } else if (targetType == ZonedDateTime.class) {
            return targetType.cast(instant.atZone(ZoneId.systemDefault()));
        } else {
            throw new IllegalArgumentException("Unsupported target type: " + targetType);
        }
    }

    /**
     * Check if given type is Date type
     * @param clazz given type
     * @return true if type is Date
     * */
    public static boolean isDate(Class<?> clazz) {
        return
                clazz == LocalDate.class ||
                        clazz == LocalDateTime.class ||
                        clazz == ZonedDateTime.class ||
                        clazz == Instant.class ||
                        clazz == Date.class;
    }
}
