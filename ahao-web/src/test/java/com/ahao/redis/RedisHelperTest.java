package com.ahao.redis;

import com.ahao.redis.config.RedisConfig;
import com.ahao.redis.util.RedisHelper;
import com.ahao.spring.util.SpringContextHolder;
import org.junit.After;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = {RedisConfig.class, RedisAutoConfiguration.class, SpringContextHolder.class})
//@ContextConfiguration(classes = {SpringContextHolder.class})
@ActiveProfiles("test-redis")
public class RedisHelperTest {
    private static final String REDIS_KEY = "key";

    @ClassRule
    public static final RedisExternalResource redis = new RedisExternalResource();

    @Test
    public void setVoid() throws Exception {
        assertNull(RedisHelper.get(REDIS_KEY, void.class));
        assertNull(RedisHelper.get(REDIS_KEY, Void.class));

        RedisHelper.set(REDIS_KEY, null);
        assertNull(RedisHelper.get(REDIS_KEY, Object.class));
        RedisHelper.set(REDIS_KEY, "null");
        assertEquals("null", RedisHelper.get(REDIS_KEY, String.class));
    }

    @Test
    public void setBoolean() throws Exception {
        boolean data1 = true;
        RedisHelper.set(REDIS_KEY, data1);
        assertEquals(data1, RedisHelper.get(REDIS_KEY, boolean.class));

        Boolean data2 = false;
        RedisHelper.set(REDIS_KEY, data2);
        assertEquals(data2, RedisHelper.get(REDIS_KEY, Boolean.class));
    }

    @Test
    public void setCharacter() throws Exception {
        char data1 = Character.MIN_VALUE;
        RedisHelper.set(REDIS_KEY, data1);
        assertEquals(data1, RedisHelper.get(REDIS_KEY, char.class).charValue());

        Character data2 = Character.MAX_VALUE;
        RedisHelper.set(REDIS_KEY, data2);
        assertEquals(data2, RedisHelper.get(REDIS_KEY, Character.class));
    }

    @Test
    public void setByte() throws Exception {
        byte data1 = Byte.MIN_VALUE;
        RedisHelper.set(REDIS_KEY, data1);
        assertEquals(data1, RedisHelper.get(REDIS_KEY, byte.class).byteValue());

        Byte data2 = Byte.MAX_VALUE;
        RedisHelper.set(REDIS_KEY, data2);
        assertEquals(data2, RedisHelper.get(REDIS_KEY, Byte.class));
    }

    @Test
    public void setShort() throws Exception {
        short data1 = Short.MIN_VALUE;
        RedisHelper.set(REDIS_KEY, data1);
        assertEquals(data1, RedisHelper.get(REDIS_KEY, short.class).shortValue());

        Short data2 = Short.MAX_VALUE;
        RedisHelper.set(REDIS_KEY, data2);
        assertEquals(data2, RedisHelper.get(REDIS_KEY, Short.class));
    }

    @Test
    public void setInteger() throws Exception {
        int data1 = Integer.MIN_VALUE;
        RedisHelper.set(REDIS_KEY, data1);
        assertEquals(data1, RedisHelper.get(REDIS_KEY, int.class).intValue());

        Integer data2 = Integer.MAX_VALUE;
        RedisHelper.set(REDIS_KEY, data2);
        assertEquals(data2, RedisHelper.get(REDIS_KEY, Integer.class));
    }

    @Test
    public void setLong() throws Exception {
        long data1 = Long.MIN_VALUE;
        RedisHelper.set(REDIS_KEY, data1);
        assertEquals(data1, RedisHelper.get(REDIS_KEY, long.class).longValue());

        Long data2 = Long.MAX_VALUE;
        RedisHelper.set(REDIS_KEY, data2);
        assertEquals(data2, RedisHelper.get(REDIS_KEY, Long.class));
    }

    @Test
    public void setFloat() throws Exception {
        float data1 = Float.MIN_VALUE;
        RedisHelper.set(REDIS_KEY, data1);
        assertEquals(data1, RedisHelper.get(REDIS_KEY, float.class).floatValue(), 3);

        Float data2 = Float.MAX_VALUE;
        RedisHelper.set(REDIS_KEY, data2);
        assertEquals(data2, RedisHelper.get(REDIS_KEY, Float.class));
    }

    @Test
    public void setDouble() throws Exception {
        double data1 = Double.MIN_VALUE;
        RedisHelper.set(REDIS_KEY, data1);
        assertEquals(data1, RedisHelper.get(REDIS_KEY, double.class).doubleValue(), 3);

        Double data2 = Double.MAX_VALUE;
        RedisHelper.set(REDIS_KEY, data2);
        assertEquals(data2, RedisHelper.get(REDIS_KEY, Double.class));
    }

    @Test
    public void setObject() throws Exception {
        String msg = "hello_world";
        RedisHelper.set(REDIS_KEY, msg);
        assertEquals(msg, RedisHelper.get(REDIS_KEY, String.class));

        List<Long> longList = Arrays.asList(1L, 2L, 3L, 4L);
        RedisHelper.set(REDIS_KEY, longList);
        assertEquals(longList, RedisHelper.get(REDIS_KEY, List.class));
    }

    @Test
    public void setEx() throws Exception {
        String msg = "hello_world";
        RedisHelper.setEx(REDIS_KEY, msg, 2);
        Assert.assertEquals(msg, RedisHelper.get(REDIS_KEY, String.class));
        Thread.sleep(5000);
        Assert.assertNull(RedisHelper.get(REDIS_KEY, String.class));
    }

    @Test
    public void hset() throws Exception {
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
            assertEquals(entry.getValue(), redisObj.get(entry.getKey()));
        }
    }

    @Test
    public void incr() throws Exception {
        // 1. 多线程并发执行
        ExecutorService threadPool = Executors.newFixedThreadPool(1000);
        for (int i = 0; i < 1000; i++) {
            threadPool.execute(() -> RedisHelper.incr(REDIS_KEY));
        }
        // 2. 等待线程执行完毕
        threadPool.shutdown();
        threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        // 3. 取值 assert
        Assert.assertEquals(1000, RedisHelper.getInt(REDIS_KEY));
    }

    @After
    public void after() {
        RedisHelper.del(REDIS_KEY);
    }
}