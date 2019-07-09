package com.ahao.spring.boot.rabbitmq.util;

import java.util.HashMap;
import java.util.Map;

public class ObjectPool {
    public static final String KEY_DEFAULT = "default";
    private static Map<String, String> pool = new HashMap<>();

    public static void put(String key, String value) {
        pool.put(key, value);
    }

    public static String get(String key) {
        return pool.get(key);
    }
}
