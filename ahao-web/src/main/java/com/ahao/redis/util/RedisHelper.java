package com.ahao.redis.util;

import com.ahao.spring.util.SpringContextHolder;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;

public class RedisHelper {
    public static RedisTemplate<String, Object> getRedisTemplate() {
        return SpringContextHolder.getBean("redisTemplate");
    }

    public static Boolean del(String key) {
        return getRedisTemplate().delete(key);
    }
    public static Long dels(String... keys) {
        return getRedisTemplate().delete(Arrays.asList(keys));
    }

    public static <T> T get(String key) {
        return (T) getRedisTemplate().opsForValue().get(key);
    }
    public static int getInt(String key) {
        Object value = getRedisTemplate().opsForValue().get(key);
        return value == null ? 0 : Integer.parseInt(value.toString());
    }
    public static long getLong(String key) {
        Object value = getRedisTemplate().opsForValue().get(key);
        return value == null ? 0 : Long.parseLong(value.toString());
    }

    public static void set(String key, Object value) {
        getRedisTemplate().opsForValue().set(key, value);
    }

    public static Long incr(String key) {
        return getRedisTemplate().opsForValue().increment(key);
    }
}
