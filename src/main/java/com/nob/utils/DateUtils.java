package com.nob.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

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
     * Check if given type is one of supported date type
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

    /**
     * Convert supported type date object to {@code Instant}
     * @param o the date object
     * @param zoneId zone id
     * @return value as {@code Instant}
     * @throws IllegalArgumentException if date type id not one of supported type date
     * */
    public static Instant toInstant(Object o, ZoneId zoneId) {
        if (Objects.isNull(o)) return null;
        if (o instanceof Instant) return (Instant) o;
        if (o instanceof Date) return ((Date) o).toInstant();
        if (o instanceof LocalDate) return ((LocalDate) o).atStartOfDay(zoneId).toInstant();
        if (o instanceof LocalDateTime) return ((LocalDateTime) o).atZone(zoneId).toInstant();
        if (o instanceof ZonedDateTime) return ((ZonedDateTime) o).toInstant();
        throw new IllegalArgumentException("Unsupported target type: " + o.getClass());
    }

    /**
     * Convert supported type date object with UTC zone to {@code Instant}
     * @param o the date object
     * @return value as {@code Instant}
     * @throws IllegalArgumentException if date type id not one of supported type date
     * */
    public static Instant toInstantUTC(Object o) {
        return toInstant(o, ZoneOffset.UTC);
    }

    /**
     * Convert supported type date object with system zone to {@code Instant}
     * @param o the date object
     * @return value as {@code Instant}
     * @throws IllegalArgumentException if date type id not one of supported type date
     * */
    public static Instant toInstantSystem(Object o) {
        return toInstant(o, ZoneId.systemDefault());
    }

    /**
     * Compare to date object
     * @param v1 left-hand side date object
     * @param v2 right-hand side date object
     * @return compare result
     * */
    @SuppressWarnings("all")
    public static int compare(Object v1, Object v2) {
        Objects.requireNonNull(v1);
        Objects.requireNonNull(v2);
        Instant date1 = toInstant(v1, ZoneId.systemDefault());
        Instant date2 = toInstant(v2, ZoneId.systemDefault());
        return date1.compareTo(date2);
    }
}
