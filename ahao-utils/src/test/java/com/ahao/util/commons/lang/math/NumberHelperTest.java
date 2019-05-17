package com.ahao.util.commons.lang.math;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import static org.junit.Assert.*;

public class NumberHelperTest {

    @Test
    public void isBetween() {
        assertFalse(NumberHelper.isBetween(0, 1, 3));
        assertTrue (NumberHelper.isBetween(1, 1, 3));
        assertTrue (NumberHelper.isBetween(2, 1, 3));
        assertTrue (NumberHelper.isBetween(3, 1, 3));
        assertFalse(NumberHelper.isBetween(4, 1, 3));
    }

    @Test
    public void between() {
        assertEquals(NumberHelper.between(0, 1, 3), 1);
        assertEquals(NumberHelper.between(1, 1, 3), 1);
        assertEquals(NumberHelper.between(2, 1, 3), 2);
        assertEquals(NumberHelper.between(3, 1, 3), 3);
        assertEquals(NumberHelper.between(4, 1, 3), 3);
    }

    @Test
    public void parseInt() {
        assertEquals(NumberHelper.parseInt(null), 0);
        assertEquals(NumberHelper.parseInt(false), 0);
        assertEquals(NumberHelper.parseInt(Boolean.FALSE), 0);
        assertEquals(NumberHelper.parseInt(true), 1);
        assertEquals(NumberHelper.parseInt(Boolean.TRUE), 1);
        assertEquals(NumberHelper.parseInt(""), 0);
        assertEquals(NumberHelper.parseInt("123"), 123);
        assertEquals(NumberHelper.parseInt("123.5"), 123);
    }

    @Test
    public void isNumber(){
        assertTrue(NumberHelper.isNumber(1));
        assertTrue(NumberHelper.isNumber(0x21));
        assertTrue(NumberHelper.isNumber(1L));
        assertTrue(NumberHelper.isNumber(1.2));
        assertTrue(NumberHelper.isNumber(1.2f));
        assertTrue(NumberHelper.isNumber(1.2d));

        assertTrue(NumberHelper.isNumber(Byte.parseByte("1")));
        assertTrue(NumberHelper.isNumber(Byte.valueOf("1")));
        assertTrue(NumberHelper.isNumber(Short.parseShort("2")));
        assertTrue(NumberHelper.isNumber(Short.valueOf("2")));
        assertTrue(NumberHelper.isNumber(Integer.parseInt("3")));
        assertTrue(NumberHelper.isNumber(Integer.valueOf("3")));
        assertTrue(NumberHelper.isNumber(Long.parseLong("4")));
        assertTrue(NumberHelper.isNumber(Long.valueOf("4")));
        assertTrue(NumberHelper.isNumber(Float.parseFloat("4")));
        assertTrue(NumberHelper.isNumber(Float.valueOf("4")));
        assertTrue(NumberHelper.isNumber(Double.parseDouble("4")));
        assertTrue(NumberHelper.isNumber(Double.valueOf("4")));

        assertTrue(NumberHelper.isNumber("1"));
        assertTrue(NumberHelper.isNumber("1.2"));
        assertTrue(NumberHelper.isNumber("012"));
        assertTrue(NumberHelper.isNumber(BigDecimal.TEN));
        assertTrue(NumberHelper.isNumber(BigInteger.TEN));

        assertFalse(NumberHelper.isNumber(null));
        assertFalse(NumberHelper.isNumber(""));
        assertFalse(NumberHelper.isNumber("a12"));
        assertFalse(NumberHelper.isNumber("1.2.3"));
        assertFalse(NumberHelper.isNumber(new Date()));
        assertFalse(NumberHelper.isNumber(new Object()));
    }
}