package com.nob.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Objects;

/**
 * Utility class for handling various date types. The following classes are supported:
 * <ul>
 *     <li>{@link java.util.Date}</li>
 *     <li>{@link java.time.LocalDate}</li>
 *     <li>{@link java.time.LocalDateTime}</li>
 *     <li>{@link java.time.ZonedDateTime}</li>
 *     <li>{@link java.time.Instant}</li>
 * </ul>
 * Provide utility methods for converting, parsing, and manipulating date objects.
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
     * Parses a string into a date object of the specified type {@code T}.
     * <p>
     * This method supports parsing strings in ISO-8601 format for the following target types:
     *
     * <p><strong>Example Usage:</strong></p>
     * <pre>{@code
     * String isoDate = "2023-12-31";
     * LocalDate date = DateUtils.parseDate(isoDate, LocalDate.class, ZoneId.of("UTC"));
     * System.out.println(dateTime); // Output: 2023-12-31
     * }</pre>
     *
     * @param <T>        The type of the desired date object.
     * @param s          The string representing the date (in ISO-8601 format).
     * @param targetType The target class type for the parsed date (e.g., {@code LocalDate.class}).
     * @param zoneId     The {@link ZoneId} to apply if the target type requires time-zone information.
     * @return A parsed date object of the specified type {@code T}.
     *
     * @throws IllegalArgumentException if the string cannot be parsed or the target type is unsupported.
     */
    public static <T> T parseDate(String s, Class<T> targetType, ZoneId zoneId)  {
        try {
            if (targetType == LocalDate.class) {
                return targetType.cast(LocalDate.parse(s));
            } else if (targetType == LocalDateTime.class) {
                return targetType.cast(LocalDateTime.parse(s));
            } else if (targetType == Instant.class) {
                LocalDateTime dateTime = LocalDateTime.parse(s);
                return targetType.cast(dateTime.atZone(zoneId).toInstant());
            } else if (targetType == Date.class) {
                LocalDateTime dateTime = LocalDateTime.parse(s);
                Instant instant = dateTime.atZone(zoneId).toInstant();
                return targetType.cast(Date.from(instant));
            } else if (targetType == ZonedDateTime.class) {
                LocalDateTime dateTime = LocalDateTime.parse(s);
                return targetType.cast(dateTime.atZone(zoneId));
            } else {
                throw new IllegalArgumentException("Unsupported target type: " + targetType);
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + s, e);
        }
    }


    /**
     * Parses a given string into a date object of the specified type {@code T}.
     * <p><strong>Example Usage:</strong></p>
     * <pre>{@code
     * String dateString = "2025-01-10T15:30:00";
     * String pattern = "yyyy-MM-dd'T'HH:mm:ss";
     * ZoneId zoneId = ZoneId.of("UTC");

     * Instant instant = DateUtils.parseDate(dateString, pattern, Instant.class, zoneId);
     * System.out.println("Instant: " + instant); // Output: 2025-01-10T15:30:00Z
     * }</pre>
     *
     * @param <T> The type of the desired date object.
     * @param s The string representing the date.
     * @param pattern The pattern of the date string.
     * @param targetType The target class type for the parsed date.
     * @param zoneId The {@link ZoneId} to apply if the target type requires time-zone information.
     * @return A parsed date object of the specified type {@code T}.
     * @throws IllegalArgumentException if the string cannot be parsed, the pattern is invalid, or the target type is unsupported.
     */
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
     * Parses a given string in ISO-8601 format into a date object of the specified type {@code T}, using UTC+0 as the default time zone.
     * <p>
     * This method serves as a simplified wrapper for {@link #parseDate(String, Class, ZoneId)} with the time zone set to UTC.
     * It supports the same set of date types as the underlying {@code parseDate} method.
     *
     * @param <T>        The type of the desired date object.
     * @param s          A string representing the date in ISO-8601 format.
     * @param targetType The target class type for the parsed date.
     * @return A parsed date object of the specified type {@code T}.
     * @throws IllegalArgumentException if the string format is invalid or the target type is unsupported.
     */
    public static <T> T parseDateWithUTC(String s, Class<T> targetType) {
        ZoneId zoneId = ZoneOffset.UTC;
        return parseDate(s, targetType, zoneId);
    }


    /**
     * Parses a given string into a date object of type {@code T} using the specified pattern and UTC+0 as the time zone.
     * <p>
     * This method acts as a simplified wrapper for {@link #parseDate(String, String, Class, ZoneId)}, with the time zone set to UTC.
     * It supports parsing strings into the same set of date types as the underlying {@code parseDate} method.
     *
     * @param <T>        The type of the desired date object.
     * @param s          A string representing the date to be parsed.
     * @param pattern    The date pattern to parse the string.
     * @param targetType The target class type for the parsed date (e.g., {@code LocalDate.class}).
     * @return A parsed date object of the specified type {@code T}.
     * @throws IllegalArgumentException if the string format, pattern, or target type is invalid.
     */
    public static <T> T parseDateWithUTC(String s, String pattern, Class<T> targetType) {
        ZoneId zoneId = ZoneOffset.UTC;
        return parseDate(s, pattern, targetType, zoneId);
    }


    /**
     * Parses a given string into a date object of type {@code T} using the system default time zone.
     * <p>
     * This method acts as a simplified wrapper for {@link #parseDate(String, Class, ZoneId)},
     * with the time zone set to the system default zone.
     * </p>
     *
     * @param <T>        The type of the desired date object.
     * @param s          A string representing the date in ISO-8601 format.
     * @param targetType The target class type for the parsed date (e.g., {@code LocalDate.class}).
     * @return A parsed date object of the specified type {@code T}.
     * @throws IllegalArgumentException if the string format, date pattern, or target type is invalid.
     */
    public static <T> T parseDateWithSystemZone(String s, Class<T> targetType) {
        ZoneId zoneId = ZoneId.systemDefault();
        return parseDate(s, targetType, zoneId);
    }


    /**
     * Parses a given string into a date object of type {@code T} using the system default time zone and the provided date pattern.
     * <p>
     * This method acts as a simplified wrapper for {@link #parseDate(String, String, Class, ZoneId)},
     * with the time zone set to the system default zone.
     * </p>
     *
     * @param <T>        The type of the desired date object.
     * @param s          A string representing the date to be parsed.
     * @param pattern    The date pattern to parse the string.
     * @param targetType The target class type for the parsed date (e.g., {@code LocalDate.class}).
     * @return A parsed date object of the specified type {@code T}.
     * @throws IllegalArgumentException if the string format, date pattern, or target type is invalid.
     */
    public static <T> T parseDateWithSystemZone(String s, String pattern, Class<T> targetType) {
        ZoneId zoneId = ZoneId.systemDefault();
        return parseDate(s, pattern, targetType, zoneId);
    }


    /**
     * Parse a given timestamp into date type {@code T} with given zone id.
     * <p>
     * This method parses a timestamp (in milliseconds since the Unix epoch) into different date types
     * based on the specified target type and zone ID.
     * </p>
     *
     * <p><strong>Example usage:</strong></p>
     * <pre>{@code
     * long timestamp = 1672531199000L; // Represents 2023-01-01T00:00:00.000Z
     * LocalDate localDate = DateParser.parseDate(timestamp, LocalDate.class, ZoneId.of("America/New_York"));
     * System.out.println("LocalDate: " + localDate); // Output: 2022-12-31
     * }</pre>
     *
     * @param <T> The type of the desired date object.
     * @param timestamp The timestamp in milliseconds since the epoch.
     * @param targetType The target class type for the parsed date (e.g., {@code LocalDate.class}).
     * @param zoneId The {@link ZoneId} to apply.
     * @return A parsed date object of the specified type {@code T}.
     * @throws IllegalArgumentException if the timestamp or date type is invalid.
     */
    public static <T> T parseDate(Long timestamp, Class<T> targetType, ZoneId zoneId) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        if (targetType == Date.class) return targetType.cast(Date.from(instant));
        if (targetType == Instant.class) return targetType.cast(instant);
        if (targetType == LocalDate.class) return targetType.cast(instant.atZone(zoneId).toLocalDate());
        if (targetType == LocalDateTime.class) return targetType.cast(instant.atZone(zoneId).toLocalDateTime());
        if (targetType == ZonedDateTime.class) return targetType.cast(instant.atZone(zoneId));
        throw new IllegalArgumentException("Unsupported target type: " + targetType);
    }


    /**
     * Parse a given timestamp into date type {@code T} with UTC+0 zone.
     * <p>
     * This method parses a timestamp (in milliseconds since the Unix epoch) into a date object of
     * the specified target type, with the UTC time zone.
     * </p>
     *
     * @param <T> The type of the desired date object.
     * @param timestamp The timestamp in milliseconds since the epoch.
     * @param targetType The target class type for the parsed date (e.g., {@code LocalDate.class}).
     * @return A parsed date object of the specified type {@code T}.
     * @throws IllegalArgumentException if the timestamp or date type is invalid.
     */
    public static <T> T parseDateWithUTC(Long timestamp, Class<T> targetType) {
        return parseDate(timestamp, targetType, ZoneOffset.UTC);
    }


    /**
     * Parse a given timestamp into date type {@code T} with system default zone.
     * <p>
     * This method parses a timestamp (in milliseconds since the Unix epoch) into a date object of
     * the specified target type, with the system's default time zone.
     * </p>
     *
     * @param <T> The type of the desired date object.
     * @param timestamp The timestamp in milliseconds since the epoch.
     * @param targetType The target class type for the parsed date (e.g., {@code LocalDate.class}).
     * @return A parsed date object of the specified type {@code T}.
     * @throws IllegalArgumentException if the timestamp or date type is invalid.
     */
    public static <T> T parseDateWithSystemZone(Long timestamp, Class<T> targetType) {
        return parseDate(timestamp, targetType, ZoneId.systemDefault());
    }


    /**
     * Convert a supported date object to {@code Instant}.
     * <p>
     * This method converts various types of date objects to {@code Instant}
     * by adjusting them according to the provided {@code ZoneId}.
     * </p>
     * <p><strong>Example Usage:</strong></p>
     * <pre>{@code
     * LocalDateTime localDateTime = LocalDateTime.of(2023, 1, 10, 15, 30);
     * ZoneId zoneId = ZoneId.of("UTC");
     * Instant instant = DateUtils.toInstant(localDateTime, zoneId);
     * System.out.println(instant); // Output: 2023-01-10T15:30:00Z
     * }</pre>
     *
     * @param o the date object to be converted.
     * @param zoneId the {@link ZoneId} to apply when converting the date.
     * @return the corresponding {@link Instant}.
     * @throws IllegalArgumentException if the provided object is not a supported date type.
     */
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
     * Convert a supported date object with UTC zone to {@code Instant}.
     * <p>
     * This method converts various types of date objects to {@code Instant},
     * adjusting them to UTC (Coordinated Universal Time).
     * </p>
     *
     * @param o the date object to be converted.
     * @return the corresponding {@link Instant} in UTC.
     * @throws IllegalArgumentException if the provided object is not a supported date type.
     */
    public static Instant toInstantUTC(Object o) {
        return toInstant(o, ZoneOffset.UTC);
    }


    /**
     * Convert a supported date object with system zone to {@code Instant}.
     * <p>
     * This method converts various types of date objects to {@code Instant},
     * adjusting them to the system's default time zone.
     * </p>
     *
     * @param o the date object to be converted.
     * @return the corresponding {@link Instant} in the system's default time zone.
     * @throws IllegalArgumentException if the provided object is not a supported date type.
     */
    public static Instant toInstantSystem(Object o) {
        return toInstant(o, ZoneId.systemDefault());
    }


    /**
     * Convert an {@code Instant} to a date object of the specified target type,
     * considering the provided time zone.
     *
     * @param instant    the {@link Instant} to be converted.
     * @param zoneId     the time zone to apply when converting.
     * @param targetType the desired target type for the date object.
     * @return the corresponding date object of the specified type.
     * @throws IllegalArgumentException if the target type is not a supported date type.
     */
    public static Object fromInstant(Instant instant, ZoneId zoneId, Class<?> targetType) {
        if (Objects.isNull(instant)) return null;
        if (targetType == Date.class) return Date.from(instant);
        if (targetType == Instant.class) return instant;
        if (targetType == LocalDate.class) return instant.atZone(zoneId).toLocalDate();
        if (targetType == LocalDateTime.class) return instant.atZone(zoneId).toLocalDateTime();
        if (targetType == ZonedDateTime.class) return instant.atZone(zoneId);
        throw new IllegalArgumentException("Unsupported target type: " + targetType);
    }


    /**
     * Convert an {@code Instant} to a date object in UTC+0 zone,
     * based on the specified target type.
     *
     * @param instant    the {@link Instant} to be converted.
     * @param targetType the desired target type for the date object.
     * @return the corresponding date object in UTC+0 zone.
     * @throws IllegalArgumentException if the target type is not a supported date type.
     */
    public static Object fromInstantUTC(Instant instant, Class<?> targetType) {
        ZoneId zoneId = ZoneOffset.UTC;
        return fromInstant(instant, zoneId, targetType);
    }


    /**
     * Convert an {@code Instant} to a date object with the system's default time zone,
     * based on the specified target type.
     *
     * @param instant    the {@link Instant} to be converted.
     * @param targetType the desired target type for the date object.
     * @return the corresponding date object in the system's default time zone.
     * @throws IllegalArgumentException if the target type is not a supported date type.
     */
    public static Object fromInstantSystemZone(Instant instant, Class<?> targetType) {
        ZoneId zoneId = ZoneId.systemDefault();
        return fromInstant(instant, zoneId, targetType);
    }


    /**
     * Compares two date objects to determine their relative order.
     * The comparison is done by converting both date objects to {@link Instant} using the system's default time zone.
     *
     * @param lhs the left-hand side date object.
     * @param rhs the right-hand side date object.
     * @return a negative integer, zero, or a positive integer as the first date is earlier than, equal to, or later than the second date.
     * @throws NullPointerException if either of the provided date objects is {@code null}.
     * @throws IllegalArgumentException if either of the provided date objects is of an unsupported type.
     */
    @SuppressWarnings("all")
    public static int compare(Object lhs, Object rhs) {
        Objects.requireNonNull(lhs);
        Objects.requireNonNull(rhs);
        Instant date1 = toInstant(lhs, ZoneId.systemDefault());
        Instant date2 = toInstant(rhs, ZoneId.systemDefault());
        return date1.compareTo(date2);
    }
}
