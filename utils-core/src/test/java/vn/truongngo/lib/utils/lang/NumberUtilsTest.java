/*
 * Created by Truong Ngo (2026).
 */
package vn.truongngo.lib.utils.lang;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class NumberUtilsTest {

    // ========================================================================
    // CLASSIFICATION
    // ========================================================================

    @Test
    void testIsIntegerType() {
        assertTrue(NumberUtils.isIntegerType(Integer.class));
        assertTrue(NumberUtils.isIntegerType(int.class));
        assertTrue(NumberUtils.isIntegerType(Long.class));
        assertTrue(NumberUtils.isIntegerType(BigInteger.class));
        assertFalse(NumberUtils.isIntegerType(Double.class));
        assertFalse(NumberUtils.isIntegerType(String.class));
    }

    @Test
    void testIsDecimalType() {
        assertTrue(NumberUtils.isDecimalType(Double.class));
        assertTrue(NumberUtils.isDecimalType(float.class));
        assertTrue(NumberUtils.isDecimalType(BigDecimal.class));
        assertFalse(NumberUtils.isDecimalType(Integer.class));
    }

    @Test
    void testIsExactPrecision() {
        assertTrue(NumberUtils.isExactPrecision(Integer.class));
        assertTrue(NumberUtils.isExactPrecision(BigDecimal.class));
        assertFalse(NumberUtils.isExactPrecision(Double.class));
        assertFalse(NumberUtils.isExactPrecision(Float.class));
    }

    @Test
    void testIsFloatingPointType() {
        assertTrue(NumberUtils.isFloatingPointType(Double.class));
        assertTrue(NumberUtils.isFloatingPointType(float.class));
        assertFalse(NumberUtils.isFloatingPointType(BigDecimal.class));
        assertFalse(NumberUtils.isFloatingPointType(Integer.class));
    }

    @Test
    void testIsLargeNumber() {
        assertTrue(NumberUtils.isLargeNumber(BigInteger.class));
        assertTrue(NumberUtils.isLargeNumber(BigDecimal.class));
        assertFalse(NumberUtils.isLargeNumber(Long.class));
    }

    @Test
    void testIsBitwiseType() {
        assertTrue(NumberUtils.isBitwiseType(Integer.class));
        assertTrue(NumberUtils.isBitwiseType(Long.class));
        assertFalse(NumberUtils.isBitwiseType(BigInteger.class)); // BigInteger is integer but handled differently
        assertFalse(NumberUtils.isBitwiseType(Double.class));
    }

    // ========================================================================
    // SYSTEM IDENTIFICATION
    // ========================================================================

    @Test
    void testIsHexadecimal() {
        assertTrue(NumberUtils.isHexadecimal("0x1A"));
        assertTrue(NumberUtils.isHexadecimal("0X1a"));
        assertTrue(NumberUtils.isHexadecimal("#FF"));
        assertTrue(NumberUtils.isHexadecimal("-0x1"));
        assertFalse(NumberUtils.isHexadecimal("123"));
        assertFalse(NumberUtils.isHexadecimal("0xG"));
        assertFalse(NumberUtils.isHexadecimal(null));
    }

    @Test
    void testIsOctal() {
        assertTrue(NumberUtils.isOctal("0123"));
        assertTrue(NumberUtils.isOctal("-07"));
        assertFalse(NumberUtils.isOctal("123")); // No leading 0
        assertFalse(NumberUtils.isOctal("08")); // 8 is not octal
        assertFalse(NumberUtils.isOctal("0x1")); // Hex
        assertFalse(NumberUtils.isOctal(null));
    }

    @Test
    void testIsScientificNotation() {
        assertTrue(NumberUtils.isScientificNotation("1.2e3"));
        assertTrue(NumberUtils.isScientificNotation("-1E-10"));
        assertFalse(NumberUtils.isScientificNotation("123"));
        assertFalse(NumberUtils.isScientificNotation("e3"));
        assertFalse(NumberUtils.isScientificNotation(null));
    }

    @Test
    void testIsParsable() {
        assertTrue(NumberUtils.isParsable("123"));
        assertTrue(NumberUtils.isParsable("-12.3"));
        assertFalse(NumberUtils.isParsable("123L")); // Suffix not allowed in parsable
        assertFalse(NumberUtils.isParsable("0x1"));
        assertFalse(NumberUtils.isParsable(null));
    }

    @Test
    void testIsCreatable() {
        assertTrue(NumberUtils.isCreatable("123"));
        assertTrue(NumberUtils.isCreatable("123L"));
        assertTrue(NumberUtils.isCreatable("0xAB"));
        assertTrue(NumberUtils.isCreatable("1.2e3"));
        assertFalse(NumberUtils.isCreatable("abc"));
        assertFalse(NumberUtils.isCreatable(null));
    }

    // ========================================================================
    // VALIDATION
    // ========================================================================

    @Test
    void testIsZero() {
        assertTrue(NumberUtils.isZero(0));
        assertTrue(NumberUtils.isZero(0.0));
        assertTrue(NumberUtils.isZero(BigInteger.ZERO));
        assertTrue(NumberUtils.isZero(BigDecimal.ZERO));
        assertFalse(NumberUtils.isZero(1));
        assertFalse(NumberUtils.isZero(null));
    }

    @Test
    void testIsPositive() {
        assertTrue(NumberUtils.isPositive(1));
        assertTrue(NumberUtils.isPositive(0.1));
        assertFalse(NumberUtils.isPositive(0));
        assertFalse(NumberUtils.isPositive(-1));
        assertFalse(NumberUtils.isPositive(null));
    }

    @Test
    void testIsValidNumber() {
        assertTrue(NumberUtils.isValidNumber(10));
        assertTrue(NumberUtils.isValidNumber(10.5));
        assertFalse(NumberUtils.isValidNumber(Double.NaN));
        assertFalse(NumberUtils.isValidNumber(Double.POSITIVE_INFINITY));
        assertFalse(NumberUtils.isValidNumber(Float.NEGATIVE_INFINITY));
        assertFalse(NumberUtils.isValidNumber(null));
    }

    // ========================================================================
    // CONVERSION
    // ========================================================================

    @Test
    void testConvertNumber() {
        assertEquals(10, NumberUtils.convertNumber(10L, Integer.class));
        assertEquals(10L, NumberUtils.convertNumber(10, Long.class));
        assertEquals(10.5, NumberUtils.convertNumber(10.5f, Double.class), 0.0001);
        assertEquals(BigInteger.TEN, NumberUtils.convertNumber(10, BigInteger.class));
        
        // Overflow check
        assertThrows(ArithmeticException.class, () -> NumberUtils.convertNumber(Long.MAX_VALUE, Integer.class));
    }

    @Test
    void testDecode() {
        assertEquals(16, NumberUtils.decode("0x10", Integer.class));
        assertEquals(255, NumberUtils.decode("#FF", Integer.class));
        assertEquals(10, NumberUtils.decode("012", Integer.class)); // Octal
        assertEquals(123, NumberUtils.decode("123", Integer.class));
        assertNull(NumberUtils.decode(null, Integer.class));
    }

    @Test
    void testParse() {
        // Flexible mode (default)
        assertEquals(123L, NumberUtils.parse("123L", Long.class));
        assertEquals(123, NumberUtils.parse("123", Integer.class));
        assertEquals(1.23, NumberUtils.parse("1.23", Double.class), 0.0001);
        
        // Strict mode
        assertEquals(123, NumberUtils.parse("123", Integer.class, true));
        assertThrows(IllegalArgumentException.class, () -> NumberUtils.parse("123L", Long.class, true));
        assertThrows(IllegalArgumentException.class, () -> NumberUtils.parse("0x1", Integer.class, true)); // Hex not allowed in strict decimal
    }

    // ========================================================================
    // MATH HELPERS
    // ========================================================================

    @Test
    void testCompare() {
        assertEquals(0, NumberUtils.compare(10, 10));
        assertEquals(-1, NumberUtils.compare(10, 20));
        assertEquals(1, NumberUtils.compare(20, 10));
        
        // Mixed types
        assertEquals(0, NumberUtils.compare(10, 10.0));
        assertEquals(-1, NumberUtils.compare(10, 10.1));
    }

    @Test
    void testMax() {
        assertEquals(5, NumberUtils.max(1, 5, 3));
        assertEquals(10.5, NumberUtils.max(1.5, 10.5, 5.5));
        assertNull(NumberUtils.max());
    }

    @Test
    void testIsEven() {
        assertTrue(NumberUtils.isEven(2));
        assertTrue(NumberUtils.isEven(0));
        assertTrue(NumberUtils.isEven(-2));
        assertFalse(NumberUtils.isEven(1));
        assertFalse(NumberUtils.isEven(3));
        
        // BigInteger
        assertTrue(NumberUtils.isEven(BigInteger.valueOf(100)));
        assertFalse(NumberUtils.isEven(BigInteger.valueOf(101)));
    }
}
