package com.ahao.spring.boot.redis;

import com.ahao.spring.boot.redis.config.RedisConfig;
import com.ahao.spring.boot.redis.config.RedisExtension;
import com.ahao.spring.boot.redis.entity.User;
import com.ahao.util.spring.SpringContextHolder;
import com.ahao.util.spring.redis.RedisHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@ExtendWith(RedisExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = {RedisConfig.class, RedisAutoConfiguration.class, SpringContextHolder.class})
@ActiveProfiles("test-redis")
class RedisHelperTest {
    private static final String REDIS_KEY = "key";
    private static final String REDIS_HASH_FIELD = "field";

    @Test
    void setVoid() {
        Assertions.assertNull(RedisHelper.getObject(REDIS_KEY, void.class));
        Assertions.assertNull(RedisHelper.getObject(REDIS_KEY, Void.class));

        RedisHelper.set(REDIS_KEY, (Object) null);
        Assertions.assertNull(RedisHelper.getObject(REDIS_KEY, Object.class));
        RedisHelper.set(REDIS_KEY, "null");
        Assertions.assertEquals("null", RedisHelper.getString(REDIS_KEY));
    }

    @Test
    void setBoolean() {
        boolean data1 = true;
        RedisHelper.set(REDIS_KEY, data1);
        Assertions.assertEquals(data1, RedisHelper.getBoolean(REDIS_KEY));
        Assertions.assertEquals(Boolean.toString(data1), RedisHelper.getString(REDIS_KEY));

        Boolean data2 = false;
        RedisHelper.set(REDIS_KEY, data2);
        Assertions.assertEquals(data2, RedisHelper.getBoolean(REDIS_KEY));
        Assertions.assertEquals(data2, RedisHelper.getObject(REDIS_KEY, Boolean.class));
        Assertions.assertEquals(Boolean.toString(data2), RedisHelper.getString(REDIS_KEY));
    }

    @Test
    void setByte() {
        byte data1 = Byte.MIN_VALUE;
        RedisHelper.set(REDIS_KEY, data1);
        Assertions.assertEquals(data1, RedisHelper.getByte(REDIS_KEY).byteValue());
        Assertions.assertEquals(Byte.toString(data1), RedisHelper.getString(REDIS_KEY));

        Byte data2 = Byte.MAX_VALUE;
        RedisHelper.set(REDIS_KEY, data2);
        Assertions.assertEquals(data2, RedisHelper.getByte(REDIS_KEY));
        Assertions.assertEquals(data2, RedisHelper.getObject(REDIS_KEY,  Byte.class));
        Assertions.assertEquals(Byte.toString(data2), RedisHelper.getString(REDIS_KEY));
    }

    @Test
    void setShort() {
        short data1 = Short.MIN_VALUE;
        RedisHelper.set(REDIS_KEY, data1);
        Assertions.assertEquals(data1, RedisHelper.getShort(REDIS_KEY).shortValue());
        Assertions.assertEquals(Short.toString(data1), RedisHelper.getString(REDIS_KEY));

        Short data2 = Short.MAX_VALUE;
        RedisHelper.set(REDIS_KEY, data2);
        Assertions.assertEquals(data2, RedisHelper.getShort(REDIS_KEY));
        Assertions.assertEquals(data2, RedisHelper.getObject(REDIS_KEY, Short.class));
        Assertions.assertEquals(Short.toString(data2), RedisHelper.getString(REDIS_KEY));
    }

    @Test
    void setInteger() {
        int data1 = Integer.MIN_VALUE;
        RedisHelper.set(REDIS_KEY, data1);
        Assertions.assertEquals(data1, RedisHelper.getInteger(REDIS_KEY).intValue());
        Assertions.assertEquals(Integer.toString(data1), RedisHelper.getString(REDIS_KEY));

        Integer data2 = Integer.MAX_VALUE;
        RedisHelper.set(REDIS_KEY, data2);
        Assertions.assertEquals(data2, RedisHelper.getInteger(REDIS_KEY));
        Assertions.assertEquals(data2, RedisHelper.getObject(REDIS_KEY, Integer.class));
        Assertions.assertEquals(Integer.toString(data2), RedisHelper.getString(REDIS_KEY));
    }

    @Test
    void setLong() {
        long data1 = Long.MIN_VALUE;
        RedisHelper.set(REDIS_KEY, data1);
        Assertions.assertEquals(data1, RedisHelper.getLong(REDIS_KEY).longValue());
        Assertions.assertEquals(Long.toString(data1), RedisHelper.getString(REDIS_KEY));

        Long data2 = Long.MAX_VALUE;
        RedisHelper.set(REDIS_KEY, data2);
        Assertions.assertEquals(data2, RedisHelper.getLong(REDIS_KEY));
        Assertions.assertEquals(data2, RedisHelper.getObject(REDIS_KEY, Long.class));
        Assertions.assertEquals(Long.toString(data2), RedisHelper.getString(REDIS_KEY));
    }

    @Test
    void setFloat() {
        float data1 = Float.MIN_VALUE;
        RedisHelper.set(REDIS_KEY, data1);
        Assertions.assertEquals(data1, RedisHelper.getFloat(REDIS_KEY).floatValue(), 3);
        Assertions.assertEquals(Float.toString(data1), RedisHelper.getString(REDIS_KEY));

        Float data2 = Float.MAX_VALUE;
        RedisHelper.set(REDIS_KEY, data2);
        Assertions.assertEquals(data2, RedisHelper.getFloat(REDIS_KEY));
        Assertions.assertEquals(data2, RedisHelper.getObject(REDIS_KEY, Float.class));
        Assertions.assertEquals(Float.toString(data2), RedisHelper.getString(REDIS_KEY));
    }

    @Test
    void setDouble() {
        double data1 = Double.MIN_VALUE;
        RedisHelper.set(REDIS_KEY, data1);
        Assertions.assertEquals(data1, RedisHelper.getDouble(REDIS_KEY).doubleValue(), 3);
        Assertions.assertEquals(Double.toString(data1), RedisHelper.getString(REDIS_KEY));

        Double data2 = Double.MAX_VALUE;
        RedisHelper.set(REDIS_KEY, data2);
        Assertions.assertEquals(data2, RedisHelper.getDouble(REDIS_KEY));
        Assertions.assertEquals(data2, RedisHelper.getObject(REDIS_KEY, Double.class));
        Assertions.assertEquals(Double.toString(data2), RedisHelper.getString(REDIS_KEY));
    }

    @Test
    void setObject() {
        String msg = "hello_world";
        RedisHelper.set(REDIS_KEY, msg);
        Assertions.assertEquals(msg, RedisHelper.getString(REDIS_KEY));
        Assertions.assertEquals(msg, RedisHelper.getObject(REDIS_KEY, String.class));

        List<Long> longList = Arrays.asList(1L, 2L, 3L, 4L);
        RedisHelper.set(REDIS_KEY, longList);
        Assertions.assertEquals(longList, RedisHelper.getObject(REDIS_KEY, List.class));

        User user = new User(1, "张三");
        RedisHelper.set(REDIS_KEY, user);
        Assertions.assertEquals(user, RedisHelper.getObject(REDIS_KEY, User.class));
    }

    @Test
    void setEx() throws Exception {
        String msg = "hello_world";
        RedisHelper.setEx(REDIS_KEY, msg, 2);
        Assertions.assertEquals(msg, RedisHelper.getString(REDIS_KEY));
        Thread.sleep(5000);
        Assertions.assertNull(RedisHelper.getString(REDIS_KEY));
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
        Assertions.assertEquals(1000, RedisHelper.getInteger(REDIS_KEY).intValue());
    }

    @AfterEach
    void after() {
        RedisHelper.del(REDIS_KEY);
    }
}
