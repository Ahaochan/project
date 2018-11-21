package com.ahao.commons.util;

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
}