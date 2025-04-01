package com.nob.utils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * String utility
 * @author Truong Ngo
 * */
public class StringUtils {

    public static final String EMAIL_REGEX = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";

    public static final String ALPHANUMERIC_REGEX = "[a-zA-Z0-9]+";

    public static final String URL_REGEX_PREFIX = "^(?i)(%s):\\/\\/[\\w\\-]+(\\.[\\w\\-]+)+[/#?]?.*$";

    public static final String COLLECTION_ACCESS_REGEX = "^.*\\[(\\d+)]$";

    public static final String PATH_VARIABLE_REGEX = "\\{[^}]+}";

    public static final String PATH_VARIABLE_REGEX_W_GROUP = "\\{([^}]+)}";


    /**
     * This constant defines a mapping of Vietnamese accented characters to their unaccented equivalents.
     * <p>
     * Each entry in the array consists of:
     * <ul>
     *     <li>A regex pattern matching Vietnamese accented characters</li>
     *     <li>The corresponding unaccented character</li>
     * </ul>
     * </p>
     */
    public static final String[][] VNI_ACCENTS =
    {
            {"à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ", "a"},
            {"À|Á|Ạ|Ả|Ã|Â|Ầ|Ấ|Ậ|Ẩ|Ẫ|Ă|Ằ|Ắ|Ặ|Ẳ|Ẵ", "A"},
            {"è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ", "e"},
            {"È|É|Ẹ|Ẻ|Ẽ|Ê|Ề|Ế|Ệ|Ể|Ễ", "E"},
            {"ì|í|ị|ỉ|ĩ", "i"},
            {"Ì|Í|Ị|Ỉ|Ĩ", "I"},
            {"ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ", "o"},
            {"Ò|Ó|Ọ|Ỏ|Õ|Ô|Ồ|Ố|Ộ|Ổ|Ỗ|Ơ|Ờ|Ớ|Ợ|Ở|Ỡ", "O"},
            {"ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ", "u"},
            {"Ù|Ú|Ụ|Ủ|Ũ|Ư|Ừ|Ứ|Ự|Ử|Ữ", "U"},
            {"ỳ|ý|ỵ|ỷ|ỹ", "y"},
            {"Ỳ|Ý|Ỵ|Ỷ|Ỹ", "Y"},
            {"đ", "d"},
            {"Đ", "D"}
    };


    /**
     * Checks if a given string is null or empty after trimming whitespace.
     *
     * @param s the string to check
     * @return {@code true} if the string is null or empty after trimming, otherwise {@code false}
     */
    public static boolean isTrimmedEmpty(String s) {
        return Objects.isNull(s) || s.trim().isEmpty();
    }


    /**
     * Joins a list of elements into a single string using a specified delimiter and a mapping function.
     *
     * @param <T>       the type of elements in the list
     * @param list      the list of elements to join
     * @param delimiter the delimiter to separate each mapped element
     * @param mapper    a function that converts each element into a string
     * @return a concatenated string of mapped elements separated by the delimiter,
     *         or {@code null} if the list is null or empty
     */
    public static <T> String join(List<T> list, String delimiter, Function<T, String> mapper) {
        if (CollectionUtils.isNullOrEmpty(list)) return null;
        return list.stream().map(mapper).collect(Collectors.joining(delimiter));
    }


    /**
     * Splits a string into a list of elements using a specified delimiter and a mapping function.
     *
     * @param <T>       the type of elements in the resulting list
     * @param s         the string to be split
     * @param delimiter the delimiter used to separate the string
     * @param mapper    a function that converts each split substring into an element of type {@code T}
     * @return a list of mapped elements, or an empty list if the input string is null or empty after trimming
     */
    public static <T> List<T> split(String s, String delimiter, Function<String, T> mapper) {
        if (isTrimmedEmpty(s)) return Collections.emptyList();
        return Stream.of(s.split(delimiter)).map(mapper).collect(Collectors.toList());
    }


    /**
     * Returns the given string if it is not null or empty after trimming, otherwise returns null.
     *
     * @param s the input string
     * @return the original string if it is not empty after trimming, otherwise {@code null}
     */
    public static String sanitizeEmptyToNull(String s) {
        return isTrimmedEmpty(s) ? null : s;
    }


    /**
     * Returns the given string if it is not null or empty after trimming, otherwise returns an empty string.
     *
     * @param s the input string
     * @return the original string if it is not empty after trimming, otherwise an empty string
     */
    public static String nvl(String s) {
        return isTrimmedEmpty(s) ? "" : s;
    }


    /**
     * Check if given string is email
     * @param s given string
     * @return true if string is email
     * */
    public static boolean isEmail(String s) {
        return Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE).matcher(s).matches();
    }


    /**
     * Check if given string is alphanumeric
     * @param s given string
     * @return true if string is alphanumeric
     * */
    public static boolean isAlphanumeric(String s) {
        return s.matches(ALPHANUMERIC_REGEX);
    }


    /**
     * Check if given string is url
     * @param s given string
     * @param schemes allow schemes (http, https, ftp...)
     * @return true if string is url base on allow schemes
     **/
    public static boolean isUrl(String s, List<String> schemes) {
        String scheme = String.join("|", schemes);
        String regex = String.format(URL_REGEX_PREFIX, scheme);
        return s.matches(regex);
    }


    /**
     * Capitalizes the first letter of the given string.
     * <p>
     * This method returns a new string with the first letter of the input string
     * converted to uppercase. The rest of the string remains unchanged. If the
     * input string is {@code null} or empty, it will return the string as-is.
     * </p>
     *
     * @param s The input string to be processed. Can be {@code null} or empty.
     * @return A new string with the first letter capitalized. If the input string
     *         is {@code null} or empty, the method returns the input string as-is.
     */
    public static String capitalizeFirstLetter(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }


    /**
     * Extracts the index value from a string that contains square brackets enclosing an integer {@code ([i])}.
     * The method expects the string to contain a single pair of square brackets, and the contents within
     * the brackets should be a valid integer.
     * <p>Example usage:</p>
     * <blockquote>
     * <pre>
     *     String s1 = "array[5]";
     *     int index1 = extractIndex(s1); // Returns: 5
     *
     *     String s2 = "list[42]";
     *     int index2 = extractIndex(s2); // Returns: 42
     *
     *     String s3 = "invalid[abc]";
     *     try {
     *         int index3 = extractIndex(s3); // Throws IllegalArgumentException
     *     } catch (IllegalArgumentException e) {
     *         System.out.println(e.getMessage()); // Output: Invalid index: abc
     *     }
     * </pre>
     * </blockquote>
     * @param expression an array access expression, which is a string containing square brackets
     *                   enclosing an integer (e.g., "array[5]", "list[42]").
     * @return the integer value inside the square brackets, representing the index for accessing an element
     * @throws IllegalArgumentException if the input string does not contain a valid expression with square brackets,
     *                                  or if the value inside the brackets is not a valid integer
     */
    public static int extractIndex(String expression) {
        if (expression.contains("[") && expression.contains("]")) {
            int startIndex = expression.indexOf("[") + 1;
            int endIndex = expression.indexOf("]");
            if (startIndex < endIndex) {
                String index = expression.substring(startIndex, endIndex);
                try {
                    return Integer.parseInt(index);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid index: " + index);
                }
            } else {
                throw new IllegalArgumentException("Invalid expression: " + expression);
            }
        }
        throw new IllegalArgumentException("Invalid expression: " + expression);
    }


    /**
     * Extracts the array or collection identifier (the part before the square brackets) from a string.
     * The method expects the string to contain an expression with square brackets enclosing an index,
     * and it returns the identifier (the part before the brackets).
     *
     * <p>Example usage:</p>
     * <blockquote>
     * <pre>
     *     String expression1 = "array[5]";
     *     String identifier1 = extractArrayIdentifier(expression1); // Returns: "array"
     *
     *     String expression2 = "list[42]";
     *     String identifier2 = extractArrayIdentifier(expression2); // Returns: "list"
     *
     *     String expression3 = "invalid[abc]";
     *     try {
     *         String identifier3 = extractArrayIdentifier(expression3); // Throws IllegalArgumentException
     *     } catch (IllegalArgumentException e) {
     *         System.out.println(e.getMessage()); // Output: Invalid expression: invalid[abc]
     *     }
     * </pre>
     * </blockquote>
     * @param expression a string representing an array or collection access expression, which contains
     *                   an identifier (e.g., "array" or "list") followed by square brackets enclosing an index
     *                   (e.g., "array[5]", "list[42]").
     * @return the identifier part of the array or collection access expression (e.g., "array", "list")
     * @throws IllegalArgumentException if the input string does not contain valid square brackets,
     *                                  or if the format is incorrect (e.g., brackets are misplaced)
     */
    public static String extractArrayIdentifier(String expression) {
        if (expression.contains("[") && expression.contains("]")) {
            int s = expression.indexOf("[") + 1;
            int e = expression.indexOf("]");
            if (s > e) throw new IllegalArgumentException("Invalid expression: " + expression);
            int endIndex = s - 1;
            return expression.substring(0, endIndex);
        }
        throw new IllegalArgumentException("Invalid expression: " + expression);
    }


    /**
     * Checks if the given string is a valid array access expression.
     * A valid array access expression contains square brackets enclosing an integer, and optionally,
     * can include an identifier (such as "array[0]" or "[0]").
     *
     * <blockquote>
     * <pre>
     *     String expression1 = "array[5]";
     *     boolean isValid1 = isArrayAccessExpression(expression1); // Returns: true
     *
     *     String expression2 = "[42]";
     *     boolean isValid2 = isArrayAccessExpression(expression2); // Returns: true
     *
     *     String expression3 = "invalid[abc]";
     *     boolean isValid3 = isArrayAccessExpression(expression3); // Returns: false
     * </pre>
     * </blockquote>
     *
     * @param expression the string to check if it represents a valid array access expression
     * @return true if the string is a valid array access expression, false otherwise
     */
    public static boolean isArrayAccessExpression(String expression) {
        if (expression.contains("[") && expression.contains("]") && expression.endsWith("]")) {
            int startIndex = expression.indexOf("[");
            int endIndex = expression.indexOf("]");
            if (startIndex < endIndex) {
                String insideBrackets = expression.substring(startIndex + 1, endIndex);
                try {
                    Integer.parseInt(insideBrackets);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }
        return false;
    }


    /**
     * Checks if the actual URL path matches the specified template path.
     * Converts path variables in the template to a regex pattern for matching.
     *
     * @param template The template path containing optional path variables.
     * @param actualPath The actual path to compare against the template.
     * @return {@code true} if the actual path matches the template; {@code false} otherwise.
     */
    public static boolean matchUrlPath(String template, String actualPath) {
        String regex = template.replaceAll(PATH_VARIABLE_REGEX_W_GROUP, "(?<$1>[^/]+)");
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(actualPath);
        return matcher.matches();
    }


    /**
     * Determines if the given template contains path variables based on a predefined regex pattern.
     *
     * @param template The template path to check for path variables.
     * @return {@code true} if the template contains path variables; {@code false} otherwise.
     */
    public static boolean hasPathVariable(String template) {
        return template.matches(".*\\{[^}]+}.*");
    }


    /**
     * Replaces all whitespace sequences in the given string with the specified replacement string.
     * Trims leading and trailing whitespace before performing the replacement.
     *
     * @param s           the input string
     * @param replacement the string to replace whitespace sequences with
     * @return the modified string with whitespace replaced, or {@code null} if the input string is {@code null}
     */
    public static String replaceWhitespace(String s, String replacement) {
        return s == null ? null : s.trim().replaceAll("\\s+", replacement);
    }


    /**
     * Removes Vietnamese accents from the given text by replacing accented characters with their unaccented equivalents.
     * <p>
     * This method uses predefined regex patterns to match and replace Vietnamese characters.
     * </p>
     *
     * @param text The input string that may contain Vietnamese accented characters.
     * @return A new string with all Vietnamese accents removed. Returns {@code null} if the input is {@code null}.
     */
    public static String removeVNAccent(String text) {
        if (text == null) {
            return null;
        }
        String rs = null;
        for (String[] pair : VNI_ACCENTS) {
            rs = text.replaceAll(pair[0], pair[1]);
        }
        return rs;
    }


    /**
     * Checks if the given string represents a false-like value.
     * A string is considered false if it is null, empty after trimming,
     * or equals "false", "null", or "undefined" (case-sensitive).
     *
     * @param s the input string
     * @return {@code true} if the string is null, empty after trimming, or matches "false", "null", or "undefined"; otherwise {@code false}
     */
    public static boolean isFalsyString(String s) {
        return isTrimmedEmpty(s) || s.equals("false") || s.equals("null") || s.equals("undefined");
    }


    /**
     * Checks whether the given string represents a single character.
     *
     * <p>This method returns {@code true} if the input string is not {@code null}
     * and contains exactly one character.
     * @param str the string to check
     * @return {@code true} if the given string represents a single character; {@code false} otherwise
     */
    public static boolean isCharacter(String str) {
        return str != null && str.length() == 1;
    }


    /**
     * Converts a given string to a {@code Character}.
     *
     * <p>If the string is {@code null}, this method returns {@code null}.
     * If the string contains exactly one character, it returns that character.
     * Otherwise, an {@code IllegalArgumentException} is thrown.
     *
     * @param str the input string
     * @return the corresponding {@code Character} if the string contains exactly one character, otherwise {@code null}
     * @throws IllegalArgumentException if the string contains more than one character
     */
    public static Character castToChar(String str) {
        if (str == null) return null;
        if (isCharacter(str)) return str.charAt(0);
        throw new IllegalArgumentException("Invalid character: " + str);
    }
}
