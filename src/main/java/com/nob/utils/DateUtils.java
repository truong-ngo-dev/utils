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


    /**
     * Retrieves the year from an object that can be converted to a {@link LocalDate}.
     * <p>
     * This method converts the provided object into an {@link Instant} using the {@link #toInstantSystem(Object)}
     * method, then converts that {@link Instant} into a {@link LocalDate} based on the system's default time zone.
     * It returns the year part of the resulting {@link LocalDate}.
     * </p>
     *
     * <p>
     * For example, if the object represents a date corresponding to {@code 2025-01-13}, this method
     * will return {@code 2025}.
     * </p>
     *
     * @param o the object to extract the year from, which will be converted to an {@link Instant}
     *          and then to a {@link LocalDate}
     * @return the year as an {@link Integer}
     * @throws DateTimeException if the object cannot be converted into an {@link Instant}
     */
    public static Integer getYear(Object o) {
        Instant instant = toInstantSystem(o);
        LocalDate date = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        return date.getYear();
    }


    /**
     * Retrieves the current year.
     * <p>
     * This method returns the current year based on the system's default time zone.
     * It uses {@link java.time.LocalDate} to extract the year from the current date.
     * </p>
     *
     * <p>
     * For example, if the current date is {@code 2025-01-13}, this method will return
     * {@code 2025}.
     * </p>
     *
     * @return the current year as an {@link Integer}
     */
    public static Integer getCurrentYear() {
        return java.time.LocalDate.now().getYear();
    }


    /**
     * Retrieves the month from an object that can be converted to a {@link LocalDate}.
     * <p>
     * This method converts the provided object into an {@link Instant} using the {@link #toInstantSystem(Object)}
     * method, then converts that {@link Instant} into a {@link LocalDate} based on the system's default time zone.
     * It returns the month part of the resulting {@link LocalDate} as an integer, where {@code 1} represents January
     * and {@code 12} represents December.
     * </p>
     *
     * <p>
     * For example, if the object represents a date corresponding to {@code 2025-01-13}, this method will return
     * {@code 1} (for January).
     * </p>
     *
     * @param o the object to extract the month from, which will be converted to an {@link Instant}
     *          and then to a {@link LocalDate}
     * @return the month as an {@link Integer}, where {@code 1} is January and {@code 12} is December
     * @throws DateTimeException if the object cannot be converted into an {@link Instant}
     */
    public static Integer getMonth(Object o) {
        Instant instant = toInstantSystem(o);
        LocalDate date = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        return date.getMonthValue();
    }


    /**
     * Retrieves the current month.
     * <p>
     * This method returns the current month based on the system's default time zone.
     * It uses {@link java.time.LocalDate} to extract the month from the current date.
     * The month is represented as an integer, with {@code 1} for January and {@code 12} for December.
     * </p>
     *
     * <p>
     * For example, if the current date is {@code 2025-01-13}, this method will return
     * {@code 1} (for January).
     * </p>
     *
     * @return the current month as an {@link Integer}, where {@code 1} is January and
     *         {@code 12} is December
     */
    public static Integer getCurrentMonth() {
        return java.time.LocalDate.now().getMonthValue();
    }


    /**
     * Retrieves the day of the month from an object that can be converted to a {@link LocalDate}.
     * <p>
     * This method converts the provided object into an {@link Instant} using the {@link #toInstantSystem(Object)}
     * method, then converts that {@link Instant} into a {@link LocalDate} based on the system's default time zone.
     * It returns the day of the month as an integer.
     * </p>
     *
     * <p>
     * For example, if the object represents a date corresponding to {@code 2025-01-13}, this method will return
     * {@code 13}.
     * </p>
     *
     * @param o the object to extract the day from, which will be converted to an {@link Instant}
     *          and then to a {@link LocalDate}
     * @return the day of the month as an {@link Integer}
     * @throws DateTimeException if the object cannot be converted into an {@link Instant}
     */
    public static Integer getDay(Object o) {
        Instant instant = toInstantSystem(o);
        LocalDate date = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        return date.getDayOfMonth();
    }


    /**
     * Retrieves the current day of the month.
     * <p>
     * This method returns the day of the month based on the system's default time zone.
     * It uses {@link java.time.LocalDate} to extract the day from the current date.
     * The day is represented as an integer, where the value ranges from {@code 1} to {@code 31}
     * depending on the month.
     * </p>
     *
     * <p>
     * For example, if the current date is {@code 2025-01-13}, this method will return
     * {@code 13}.
     * </p>
     *
     * @return the current day of the month as an {@link Integer}
     */
    public static Integer getCurrentDay() {
        return java.time.LocalDate.now().getDayOfMonth();
    }


    /**
     * Retrieves the day of the week from an object that can be converted to a {@link LocalDate}.
     * <p>
     * This method converts the provided object into an {@link Instant} using the {@link #toInstantSystem(Object)}
     * method, then converts that {@link Instant} into a {@link LocalDate} based on the system's default time zone.
     * It returns the day of the week as a {@link DayOfWeek} value.
     * </p>
     *
     * <p>
     * For example, if the object represents a date corresponding to {@code 2025-01-13} (a Monday),
     * this method will return {@link DayOfWeek#MONDAY}.
     * </p>
     *
     * @param o the object to extract the day of the week from, which will be converted to an {@link Instant}
     *          and then to a {@link LocalDate}
     * @return the day of the week as a {@link DayOfWeek}
     * @throws DateTimeException if the object cannot be converted into an {@link Instant}
     */
    public static DayOfWeek getDayOfWeek(Object o) {
        Instant instant = toInstantSystem(o);
        LocalDate date = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        return date.getDayOfWeek();
    }


    /**
     * Retrieves the current day of the week.
     * <p>
     * This method uses {@link java.time.LocalDate} to obtain the current date, then returns the day of the week
     * as a {@link DayOfWeek} value (e.g., {@link DayOfWeek#MONDAY}, {@link DayOfWeek#TUESDAY}, etc.).
     * </p>
     *
     * <p>
     * For example, if today is Monday, the method will return {@code MONDAY}.
     * </p>
     *
     * @return the current day of the week as a {@link DayOfWeek}
     */
    public static DayOfWeek getCurrentDayOfWeek() {
        return java.time.LocalDate.now().getDayOfWeek();
    }


    /**
     * Retrieves the Vietnamese name of a given {@link DayOfWeek}.
     * <p>
     * This method takes a {@link DayOfWeek} value and returns its corresponding name in Vietnamese.
     * </p>
     *
     * <p>
     * For example, if the input is {@link DayOfWeek#MONDAY}, the method will return {@code "Thứ Hai"}.
     * </p>
     *
     * @param dayOfWeek the {@link DayOfWeek} to convert into Vietnamese
     * @return the Vietnamese name of the day of the week
     */
    public static String getDayOfWeekInVietnamese(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> "Thứ Hai";
            case TUESDAY -> "Thứ Ba";
            case WEDNESDAY -> "Thứ Tư";
            case THURSDAY -> "Thứ Năm";
            case FRIDAY -> "Thứ Sáu";
            case SATURDAY -> "Thứ Bảy";
            case SUNDAY -> "Chủ Nhật";
        };
    }
}
