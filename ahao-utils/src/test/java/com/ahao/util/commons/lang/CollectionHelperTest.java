package com.ahao.util.commons.lang;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectionHelperTest {
    @Test
    public void add() {
        Assert.assertFalse(CollectionHelper.add(null, "1", "2"));
        Assert.assertTrue(CollectionHelper.add(Stream.of("array").collect(Collectors.toList()), "1", "2"));
        Assert.assertFalse(CollectionHelper.add(Collections.singletonList("array")));
    }

    @Test
    public void addToLinkedHashMap() {
        LinkedHashMap<Integer, Integer> map = new LinkedHashMap<>();
        for (int i = 0; i < 10; i++) {
            map.put(i, i + 1000);
        }
        CollectionHelper.add(map, 2, 888, 8888);
        CollectionHelper.add(map, -2, 999, 999);
        int i = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getKey() == 888) {
                Assert.assertEquals(2, i);
            }
            if (entry.getKey() == 999) {
                Assert.assertEquals(map.size() - 2, i);
            }
            i++;
        }
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


    @Test
    public void retain() {
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            final int j = i;
            list.add(Stream.of("1", "2", "3").collect(Collectors.toMap(s -> "key" + s, s -> "value" + s + j)));
        }
        Assert.assertEquals(0, CollectionHelper.retain(null).size());

        List<Map<String, String>> retain = CollectionHelper.retain(list, "key1", "key3");
        for (Map<String, String> item : retain) {
            Assert.assertEquals(2, item.keySet().size());
            Assert.assertTrue(item.keySet().contains("key1"));
            Assert.assertFalse(item.keySet().contains("key2"));
            Assert.assertTrue(item.keySet().contains("key3"));
        }
    }

    @Test
    public void getFirst() {
        List<String> list = CollectionHelper.toList("1", "2", "3", "4");

        Assert.assertNull(CollectionHelper.getFirst(null));
        Assert.assertEquals("1", CollectionHelper.getFirst(list));
    }
}