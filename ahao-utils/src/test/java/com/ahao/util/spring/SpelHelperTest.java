package com.ahao.util.spring;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class SpelHelperTest {

    @Test
    public void simple() {
        Map<String, Object> args = new HashMap<>();
        args.put("a", "aaa");
        args.put("b", 123);
        args.put("c", "ccc");
        String parse = SpelHelper.parseString("#{#a}_#{#b}_#{#c}_hello_world!", args);
        Assert.assertEquals("aaa_123_ccc_hello_world!", parse);
    }

    @Test
    public void map() {
        Map<String, Object> args = new HashMap<>();
        Map<String, String> map1 = new HashMap<>();
        map1.put("ak1", "av1");
        map1.put("ak2", "av2");
        args.put("a", map1);
        String parse = SpelHelper.parseString("#{#a[ak1]}_#{#a[ak2]}", args);
        Assert.assertEquals("av1_av2", parse);
    }

    @Test
    public void object() {
        Map<String, Object> args = new HashMap<>();
        args.put("a", new BigDecimal("456"));
        String parse = SpelHelper.parseString("#{#a.intValue()}_#{#a.ZERO}", args);
        Assert.assertEquals("456_0", parse);
    }

    @Test
    public void method() {
        String parse = SpelHelper.parseString("random number is #{T(java.lang.Math).random()}", null);
        parse = parse.replace("random number is ", "");
        double number = Double.parseDouble(parse);
        Assert.assertTrue(number > 0);
        Assert.assertTrue(number < 1);
    }
}