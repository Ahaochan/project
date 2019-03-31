package com.ahao.redis.util;

import com.ahao.spring.util.SpringContextHolder;
import org.apache.commons.lang3.math.NumberUtils;
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

    @SuppressWarnings("unchecked")
    public static <T> T get(String key, Class<T> clazz) {
        // 1. 处理 void
        if(clazz == void.class || clazz == Void.class) {
            return null;
        }

        // 2. 从 Redis 中获取数据
        Object obj = getRedisTemplate().opsForValue().get(key);
        String string = String.valueOf(obj);

        // 3. 处理 char 类型
        if(clazz == char.class || clazz == Character.class) {
            return (T) Character.valueOf(string.charAt(0));
        }

        // 4. 处理数值类型
        if(NumberUtils.isCreatable(string)) {
            if(clazz == byte.class || clazz == Byte.class) {
                return (T) Byte.valueOf(string);
            }
            if(clazz == short.class || clazz == Short.class) {
                return (T) Short.valueOf(string);
            }
            if(clazz == int.class || clazz == Integer.class) {
                return (T) Integer.valueOf(string);
            }
            if(clazz == long.class || clazz == Long.class) {
                return (T) Long.valueOf(string);
            }
            if(clazz == float.class || clazz == Float.class) {
                return (T) Float.valueOf(string);
            }
            if(clazz == double.class || clazz == Double.class) {
                return (T) Double.valueOf(string);
            }
        }
        return (T) obj;
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
