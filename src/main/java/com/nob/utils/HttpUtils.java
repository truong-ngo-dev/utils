package com.nob.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.HandlerMapping;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HttpUtils {

    /**
     * Parses a query string into a map of key-value pairs.
     * <p>
     * This method takes a URL-encoded query string, decodes it, and splits it into key-value pairs,
     * handling multiple values for the same key by storing them in a list.
     * </p>
     *
     * @param queryString The query string to parse, typically found in the URL after the '?' character or value of form-urlencoded.
     *                    It can contain key-value pairs separated by '&' and '='. Example: "key1=value1&key2=value2&key1=value3".
     * @return A map where each key corresponds to a query parameter, and the value is a list of strings
     *         representing the associated values for that key. If a parameter has no value, an empty string
     *         is used as the value.
     */
    public static Map<String, List<String>> parseQueryString(String queryString) {
        Map<String, List<String>> result = new LinkedHashMap<>();
        if (Objects.nonNull(queryString)) {
            String decodeString = URLDecoder.decode(queryString, StandardCharsets.UTF_8);
            String[] params = decodeString.split("&");
            for (String param : params) {
                String[] pair = param.split("=", 2);
                String key = pair[0];
                String value = pair.length > 1 ? pair[1] : null;
                result.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
            }
        }
        return result;
    }


    /**
     * Retrieves the headers from an HTTP request as a map of header names to their corresponding values.
     * <p>
     * This method iterates over all header names in the HTTP request and collects the values for each header.
     * Multiple values for a header (such as repeated "Cookie" or "Accept-Encoding" headers) are collected
     * in a list.
     * </p>
     *
     * @param request The {@link HttpServletRequest} from which headers are to be retrieved.
     *                This object contains all the header information from the HTTP request.
     * @return A map where the keys are the header names, and the values are lists of strings representing
     *         the corresponding header values. If a header has multiple values, they will appear in the list.
     *         If a header has a single value, the list will contain just one element.
     */
    public static Map<String, List<String>> getHeaders(HttpServletRequest request) {
        Map<String, List<String>> headers = new LinkedHashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, Collections.list(request.getHeaders(headerName)));
        }
        return headers;
    }


    /**
     * Retrieves path variables from the HTTP request.
     * <p>
     * This method retrieves the path variables that are typically extracted from the URL template
     * when a request is matched to a handler method in a Spring MVC controller. The path variables
     * are stored as a map with variable names as keys and their corresponding values as the map's values.
     * </p>
     *
     * @param request The {@link HttpServletRequest} from which the path variables are retrieved.
     *                This should be the request object that contains the path variables set by the
     *                framework (e.g., Spring MVC).
     * @return A map of path variables, where the keys are the variable names and the values are the
     *         corresponding values extracted from the URI template. If no path variables exist, this
     *         may return `null` or an empty map, depending on the implementation.
     * @throws ClassCastException if the attribute in the request is not of type {@link Map}.
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getPathVariables(HttpServletRequest request) {
        return (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
    }
}
