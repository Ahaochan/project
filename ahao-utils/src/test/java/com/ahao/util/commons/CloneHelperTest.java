package com.ahao.util.commons;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class CloneHelperTest {

    @Test
    public void testDateClone() {
        Date now = new Date();
        Date clone = CloneHelper.clone(now);
        Assert.assertTrue(now != clone);
        Assert.assertTrue(now.getTime() == clone.getTime());
    }

    @Test
    public void testNullClone() {
        Object null1 = null;
        Object null2 = CloneHelper.clone(null1);
        Assert.assertTrue(null1 == null2);
    }

    @Test
    public void testStringClone() {
        String str1 = "hello";
        String str2 = CloneHelper.clone(str1);
        Assert.assertTrue(str1 != str2);

        boolean eq = true;
        char[] char1 = str1.toCharArray(), char2 = str2.toCharArray();
        for(int i = 0, len = char1.length; i < len && eq; i++) {
            if(char1[i] != char2[i]) {
                eq = false;
            }
        }
        Assert.assertTrue(eq);
    }


}