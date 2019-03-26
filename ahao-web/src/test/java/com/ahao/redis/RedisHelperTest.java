package com.ahao.redis;

import com.ahao.redis.config.RedisConfig;
import com.ahao.redis.util.RedisHelper;
import com.ahao.spring.util.SpringContextHolder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = {RedisConfig.class, RedisAutoConfiguration.class, SpringContextHolder.class})
@ActiveProfiles("test-redis")
public class RedisHelperTest {
    private static final String REDIS_KEY = "key";

    @Test
    public void set() throws Exception {
        long now = System.currentTimeMillis();
        RedisHelper.set(REDIS_KEY, now);
        assertEquals(now, RedisHelper.getLong(REDIS_KEY));
        RedisHelper.set(REDIS_KEY, null);
        assertEquals((Object) null, RedisHelper.get(REDIS_KEY));
        RedisHelper.set(REDIS_KEY, "null");
        assertEquals("null", RedisHelper.get(REDIS_KEY));
    }

    @Test
    public void hset() throws Exception {
        Map<String, String> obj = new HashMap<>();
        for(int i = 0; i < 100; i++) {
            obj.put("key"+i, "value"+i);
        }

        RedisHelper.set(REDIS_KEY, obj);
        Map<String, String> value = RedisHelper.get(REDIS_KEY);

        for (Map.Entry<String, String> entry : obj.entrySet()) {
            assertEquals(entry.getValue(), value.get(entry.getKey()));
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