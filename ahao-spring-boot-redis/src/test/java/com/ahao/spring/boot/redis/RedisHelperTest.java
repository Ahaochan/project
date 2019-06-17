package com.ahao.spring.boot.redis;

import com.ahao.spring.boot.redis.config.RedisConfig;
import com.ahao.spring.boot.redis.util.RedisHelper;
import com.ahao.util.spring.SpringContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@ExtendWith(RedisExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = {RedisConfig.class, RedisAutoConfiguration.class, SpringContextHolder.class})
@ActiveProfiles("test-redis")
class RedisHelperTest {
    private static final String REDIS_KEY = "key";

    @Test
    void setVoid() throws Exception {
        Assertions.assertNull(RedisHelper.get(REDIS_KEY, void.class));
        Assertions.assertNull(RedisHelper.get(REDIS_KEY, Void.class));

        RedisHelper.set(REDIS_KEY, null);
        Assertions.assertNull(RedisHelper.get(REDIS_KEY, Object.class));
        RedisHelper.set(REDIS_KEY, "null");
        Assertions.assertEquals("null", RedisHelper.get(REDIS_KEY, String.class));
    }

    @Test
    void setBoolean() throws Exception {
        boolean data1 = true;
        RedisHelper.set(REDIS_KEY, data1);
        Assertions.assertEquals(data1, RedisHelper.get(REDIS_KEY, boolean.class));

        Boolean data2 = false;
        RedisHelper.set(REDIS_KEY, data2);
        Assertions.assertEquals(data2, RedisHelper.get(REDIS_KEY, Boolean.class));
    }

    @Test
    void setCharacter() throws Exception {
        char data1 = Character.MIN_VALUE;
        RedisHelper.set(REDIS_KEY, data1);
        Assertions.assertEquals(data1, RedisHelper.get(REDIS_KEY, char.class).charValue());

        Character data2 = Character.MAX_VALUE;
        RedisHelper.set(REDIS_KEY, data2);
        Assertions.assertEquals(data2, RedisHelper.get(REDIS_KEY, Character.class));
    }

    @Test
    void setByte() throws Exception {
        byte data1 = Byte.MIN_VALUE;
        RedisHelper.set(REDIS_KEY, data1);
        Assertions.assertEquals(data1, RedisHelper.get(REDIS_KEY, byte.class).byteValue());

        Byte data2 = Byte.MAX_VALUE;
        RedisHelper.set(REDIS_KEY, data2);
        Assertions.assertEquals(data2, RedisHelper.get(REDIS_KEY, Byte.class));
    }

    @Test
    void setShort() throws Exception {
        short data1 = Short.MIN_VALUE;
        RedisHelper.set(REDIS_KEY, data1);
        Assertions.assertEquals(data1, RedisHelper.get(REDIS_KEY, short.class).shortValue());

        Short data2 = Short.MAX_VALUE;
        RedisHelper.set(REDIS_KEY, data2);
        Assertions.assertEquals(data2, RedisHelper.get(REDIS_KEY, Short.class));
    }

    @Test
    void setInteger() throws Exception {
        int data1 = Integer.MIN_VALUE;
        RedisHelper.set(REDIS_KEY, data1);
        Assertions.assertEquals(data1, RedisHelper.get(REDIS_KEY, int.class).intValue());

        Integer data2 = Integer.MAX_VALUE;
        RedisHelper.set(REDIS_KEY, data2);
        Assertions.assertEquals(data2, RedisHelper.get(REDIS_KEY, Integer.class));
    }

    @Test
    void setLong() throws Exception {
        long data1 = Long.MIN_VALUE;
        RedisHelper.set(REDIS_KEY, data1);
        Assertions.assertEquals(data1, RedisHelper.get(REDIS_KEY, long.class).longValue());

        Long data2 = Long.MAX_VALUE;
        RedisHelper.set(REDIS_KEY, data2);
        Assertions.assertEquals(data2, RedisHelper.get(REDIS_KEY, Long.class));
    }

    @Test
    void setFloat() throws Exception {
        float data1 = Float.MIN_VALUE;
        RedisHelper.set(REDIS_KEY, data1);
        Assertions.assertEquals(data1, RedisHelper.get(REDIS_KEY, float.class).floatValue(), 3);

        Float data2 = Float.MAX_VALUE;
        RedisHelper.set(REDIS_KEY, data2);
        Assertions.assertEquals(data2, RedisHelper.get(REDIS_KEY, Float.class));
    }

    @Test
    void setDouble() throws Exception {
        double data1 = Double.MIN_VALUE;
        RedisHelper.set(REDIS_KEY, data1);
        Assertions.assertEquals(data1, RedisHelper.get(REDIS_KEY, double.class).doubleValue(), 3);

        Double data2 = Double.MAX_VALUE;
        RedisHelper.set(REDIS_KEY, data2);
        Assertions.assertEquals(data2, RedisHelper.get(REDIS_KEY, Double.class));
    }

    @Test
    void setObject() throws Exception {
        String msg = "hello_world";
        RedisHelper.set(REDIS_KEY, msg);
        Assertions.assertEquals(msg, RedisHelper.get(REDIS_KEY, String.class));

        List<Long> longList = Arrays.asList(1L, 2L, 3L, 4L);
        RedisHelper.set(REDIS_KEY, longList);
        Assertions.assertEquals(longList, RedisHelper.get(REDIS_KEY, List.class));
    }

    @Test
    void setEx() throws Exception {
        String msg = "hello_world";
        RedisHelper.setEx(REDIS_KEY, msg, 2);
        Assertions.assertEquals(msg, RedisHelper.get(REDIS_KEY, String.class));
        Thread.sleep(5000);
        Assertions.assertNull(RedisHelper.get(REDIS_KEY, String.class));
    }

    @Test
    void hset() throws Exception {
        // !!! Key 必须为 String, 因为 JSON 的 Key 只能是 String 类型 !!!
        Map<String, Object> obj = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            obj.put("key" + i, "value" + i);
        }
        for (int i = 11; i < 20; i++) {
            obj.put(String.valueOf(i), i * 10);
        }

        RedisHelper.set(REDIS_KEY, obj);
        Map<Object, Object> redisObj = RedisHelper.get(REDIS_KEY, Map.class);
        for (Map.Entry<String, Object> entry : obj.entrySet()) {
            Assertions.assertEquals(entry.getValue(), redisObj.get(entry.getKey()));
        }
    }

    @Test
    void incr() throws Exception {
        // 1. 多线程并发执行
        ExecutorService threadPool = Executors.newFixedThreadPool(1000);
        for (int i = 0; i < 1000; i++) {
            threadPool.execute(() -> RedisHelper.incr(REDIS_KEY));
        }
        // 2. 等待线程执行完毕
        threadPool.shutdown();
        threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        // 3. 取值 assert
        Assertions.assertEquals(1000, RedisHelper.getInt(REDIS_KEY));
    }

    @AfterEach
    void after() {
        RedisHelper.del(REDIS_KEY);
    }
}
