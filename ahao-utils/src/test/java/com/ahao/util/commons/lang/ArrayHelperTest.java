package com.ahao.util.commons.lang;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ArrayHelperTest {

    @Test
    public void testToArray() {
        List<String> list = new ArrayList<>();
        for(int i = 0; i < 100; i++){
            list.add("测试:"+i);
        }
        String[] array = ArrayHelper.toArray(list);
        for(int i = 0; i < 100; i++){
            Assert.assertTrue(("测试:"+i).equals(array[i]));
        }

        String[] subArray = ArrayHelper.toArray(list, 10, 5);
        for(int i = 0; i < 5; i++){
            Assert.assertTrue(("测试:"+(10+i)).equals(subArray[i]));
        }

//        Integer[] intArray = ArrayHelper.toArray(0,1,2,3,4,5,6,7,8,9);
//        for(int i = 0; i <= 9; i++){
//            Assert.assertTrue(i == intArray[i]);
//        }
    }

    @Test
    public void testLength() {
        Assert.assertEquals(10, ArrayHelper.length(0,1,2,3,4,5,6,7,8,9));
        Assert.assertEquals(2, ArrayHelper.length(null, null));
        Assert.assertEquals(1, ArrayHelper.length(1));
        Assert.assertEquals(0, ArrayHelper.length(null));
        Assert.assertEquals(0, ArrayHelper.length());
    }
}