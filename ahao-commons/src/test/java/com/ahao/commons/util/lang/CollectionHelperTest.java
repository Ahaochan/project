package com.ahao.commons.util.lang;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectionHelperTest {

    @Test
    public void size() {
        Collection nullC = null;
        Enumeration nullE = null;
        Map nullM = null;
        Assert.assertEquals(0, CollectionHelper.size(nullC));
        Assert.assertEquals(0, CollectionHelper.size(nullE));
        Assert.assertEquals(0, CollectionHelper.size(nullM));

        Collection c1 = Collections.singletonList("array");
        Enumeration<String> e1 = new Vector<String>(Collections.singletonList("enum")).elements();
        Map<String, String> m1 = Stream.of("map").collect(Collectors.toMap(e -> e + "key", e -> e + "value"));
        Assert.assertEquals(1, CollectionHelper.size(c1));
        Assert.assertEquals(1, CollectionHelper.size(e1));
        Assert.assertEquals(1, CollectionHelper.size(m1));
    }

    @Test
    public void add() {
        Assert.assertFalse(CollectionHelper.add(null, "1", "2"));
        Assert.assertTrue(CollectionHelper.add(Stream.of("array").collect(Collectors.toList()), "1", "2"));
        Assert.assertFalse(CollectionHelper.add(Collections.singletonList("array")));
    }

    @Test
    public void subList() {
        List<String> list = CollectionHelper.toList("1", "2", "3", "4");

        Assert.assertEquals(0, CollectionHelper.subList(null, 0, 999).size());
        Assert.assertEquals(4, CollectionHelper.subList(list, -1, 999).size());
        Assert.assertEquals(2, CollectionHelper.subList(list, 0, 2).size());
        Assert.assertEquals(2, CollectionHelper.subList(list, -1, 2).size());
        Assert.assertEquals(4, CollectionHelper.subList(list, 0, 999).size());
    }


    @Test
    public void contains() {
        List<String> list = CollectionHelper.toList("1", "2", "3", "4");

        Assert.assertFalse(CollectionHelper.contains(null, "1"));
        Assert.assertFalse(CollectionHelper.contains(list, "5"));
        Assert.assertTrue (CollectionHelper.contains(list, "1"));

        Assert.assertFalse(CollectionHelper.containAny(null, "1", null));
        Assert.assertFalse(CollectionHelper.containAny(list, "5", "6"));
        Assert.assertTrue (CollectionHelper.containAny(list, "1", "5"));
    }

}