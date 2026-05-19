/*
 * Created by Truong Ngo (2026).
 */
package vn.truongngo.lib.utils.lang;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ObjectUtilsTest {

    // ========================================================================
    // NULL SAFETY & DEFAULTS
    // ========================================================================

    @Test
    @SuppressWarnings("all")
    void testDefaultIfNull() {
        assertEquals("default", ObjectUtils.defaultIfNull(null, "default"));
        assertEquals("value", ObjectUtils.defaultIfNull("value", "default"));
        assertNull(ObjectUtils.defaultIfNull(null, null));
    }

    @Test
    void testGetIfNull() {
        assertEquals("expensive", ObjectUtils.getIfNull(null, () -> "expensive"));
        assertEquals("value", ObjectUtils.getIfNull("value", () -> "expensive"));
        assertNull(ObjectUtils.getIfNull(null, null));
    }

    @Test
    void testFirstNonNull() {
        assertEquals("A", ObjectUtils.firstNonNull(null, null, "A", "B"));
        assertEquals("A", ObjectUtils.firstNonNull("A", "B"));
        assertNull(ObjectUtils.firstNonNull(null, null));
        assertNull(ObjectUtils.firstNonNull());
    }

    @Test
    @SuppressWarnings("all")
    void testAllNull() {
        assertTrue(ObjectUtils.allNull((Object[]) null));
        assertTrue(ObjectUtils.allNull(null, null));
        assertFalse(ObjectUtils.allNull(null, "A"));
        assertFalse(ObjectUtils.allNull("A", "B"));
    }

    @Test
    @SuppressWarnings("all")
    void testAnyNull() {
        assertTrue(ObjectUtils.anyNull((Object[]) null));
        assertTrue(ObjectUtils.anyNull(null, "A"));
        assertTrue(ObjectUtils.anyNull("A", null));
        assertFalse(ObjectUtils.anyNull("A", "B"));
    }

    @Test
    @SuppressWarnings("all")
    void testAllNotNull() {
        assertTrue(ObjectUtils.allNotNull("A", "B"));
        assertFalse(ObjectUtils.allNotNull("A", null));
        assertFalse(ObjectUtils.allNotNull(null, null));
        assertFalse(ObjectUtils.allNotNull((Object[]) null));
    }

    // ========================================================================
    // EMPTINESS CHECKS
    // ========================================================================

    @Test
    void testIsEmpty() {
        // Null
        assertTrue(ObjectUtils.isEmpty(null));

        // String / CharSequence
        assertTrue(ObjectUtils.isEmpty(""));
        assertTrue(ObjectUtils.isEmpty(new StringBuilder("")));
        assertFalse(ObjectUtils.isEmpty(" "));
        assertFalse(ObjectUtils.isEmpty("abc"));

        // Array
        assertTrue(ObjectUtils.isEmpty(new int[]{}));
        assertTrue(ObjectUtils.isEmpty(new String[]{}));
        assertFalse(ObjectUtils.isEmpty(new int[]{1}));
        assertFalse(ObjectUtils.isEmpty(new String[]{"a"}));

        // Collection
        assertTrue(ObjectUtils.isEmpty(Collections.emptyList()));
        assertTrue(ObjectUtils.isEmpty(new ArrayList<>()));
        assertFalse(ObjectUtils.isEmpty(Collections.singletonList("a")));

        // Map
        assertTrue(ObjectUtils.isEmpty(Collections.emptyMap()));
        assertTrue(ObjectUtils.isEmpty(new HashMap<>()));
        assertFalse(ObjectUtils.isEmpty(Collections.singletonMap("k", "v")));

        // Optional
        assertTrue(ObjectUtils.isEmpty(Optional.empty()));
        assertFalse(ObjectUtils.isEmpty(Optional.of("a")));

        // Other objects
        assertFalse(ObjectUtils.isEmpty(new Object()));
        assertFalse(ObjectUtils.isEmpty(123));
    }

    @Test
    void testIsNotEmpty() {
        assertFalse(ObjectUtils.isNotEmpty(null));
        assertFalse(ObjectUtils.isNotEmpty(""));
        assertTrue(ObjectUtils.isNotEmpty("abc"));
    }

    // ========================================================================
    // EQUALITY & ARRAYS
    // ========================================================================

    @Test
    void testNullSafeEquals() {
        // Basic objects
        assertTrue(ObjectUtils.nullSafeEquals(null, null));
        assertFalse(ObjectUtils.nullSafeEquals(null, "A"));
        assertFalse(ObjectUtils.nullSafeEquals("A", null));
        assertTrue(ObjectUtils.nullSafeEquals("A", "A"));
        assertFalse(ObjectUtils.nullSafeEquals("A", "B"));

        // Arrays (Deep Equals)
        assertTrue(ObjectUtils.nullSafeEquals(new int[]{1, 2}, new int[]{1, 2}));
        assertFalse(ObjectUtils.nullSafeEquals(new int[]{1, 2}, new int[]{1, 3}));
        assertTrue(ObjectUtils.nullSafeEquals(new String[]{"a", "b"}, new String[]{"a", "b"}));
        
        // Mixed types
        assertFalse(ObjectUtils.nullSafeEquals(new int[]{1}, new long[]{1}));
        assertFalse(ObjectUtils.nullSafeEquals("A", new String[]{"A"}));
    }

    @Test
    void testCompare() {
        // Null handling
        assertEquals(0, ObjectUtils.compare(null, null));
        assertEquals(-1, ObjectUtils.compare(null, "A")); // Default: null is smaller
        assertEquals(1, ObjectUtils.compare("A", null));

        // Null greater
        assertEquals(1, ObjectUtils.compare(null, "A", true));
        assertEquals(-1, ObjectUtils.compare("A", null, true));

        // Values
        assertEquals(0, ObjectUtils.compare("A", "A"));
        assertTrue(ObjectUtils.compare("A", "B") < 0);
        assertTrue(ObjectUtils.compare("B", "A") > 0);
    }

    @Test
    void testIsArray() {
        assertTrue(ObjectUtils.isArray(new int[]{}));
        assertTrue(ObjectUtils.isArray(new String[]{}));
        assertFalse(ObjectUtils.isArray("string"));
        assertFalse(ObjectUtils.isArray(null));
        assertFalse(ObjectUtils.isArray(Collections.emptyList()));
    }

    // ========================================================================
    // IDENTITY & DEBUGGING
    // ========================================================================

    @Test
    void testIdentityToString() {
        assertNull(ObjectUtils.identityToString(null));
        
        Object obj = new Object();
        String expected = obj.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(obj));
        assertEquals(expected, ObjectUtils.identityToString(obj));
    }

    @Test
    void testNullSafeToString() {
        assertEquals("null", ObjectUtils.nullSafeToString(null));
        assertEquals("abc", ObjectUtils.nullSafeToString("abc"));
        assertEquals("123", ObjectUtils.nullSafeToString(123));

        // Arrays
        assertEquals("[1, 2]", ObjectUtils.nullSafeToString(new int[]{1, 2}));
        assertEquals("[a, b]", ObjectUtils.nullSafeToString(new char[]{'a', 'b'}));
        assertEquals("[true, false]", ObjectUtils.nullSafeToString(new boolean[]{true, false}));
        assertEquals("[A, B]", ObjectUtils.nullSafeToString(new String[]{"A", "B"}));
        
        // Nested Arrays
        assertEquals("[[1], [2]]", ObjectUtils.nullSafeToString(new int[][]{{1}, {2}}));
    }
}
