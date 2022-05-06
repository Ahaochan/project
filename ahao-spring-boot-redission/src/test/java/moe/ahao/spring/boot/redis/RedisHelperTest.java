package moe.ahao.spring.boot.redis;

import com.fasterxml.jackson.core.type.TypeReference;
import moe.ahao.embedded.EmbeddedRedisTest;
import moe.ahao.util.spring.redis.RedisHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;
import java.util.concurrent.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = Starter.class)
@ActiveProfiles("test-redis")
// @ContextConfiguration(classes = {RedisConfig.class, RedisAutoConfiguration.class, JacksonAutoConfiguration.class, SpringContextHolder.class})
class RedisHelperTest extends EmbeddedRedisTest {
    private static final String REDIS_KEY = "key";
    private static final String REDIS_HASH_FIELD = "field";

    @Test
    void setVoid() {
        Assertions.assertNull(RedisHelper.getObject(REDIS_KEY, new TypeReference<Void>() {}));
        Assertions.assertNull(RedisHelper.getObject(REDIS_KEY, new TypeReference<Void>() {}));

        RedisHelper.set(REDIS_KEY, (Object) null);
        Assertions.assertNull(RedisHelper.getObject(REDIS_KEY, new TypeReference<Object>() {}));
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
        Assertions.assertEquals(data2, RedisHelper.getObject(REDIS_KEY, new TypeReference<Boolean>() {}));
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
        Assertions.assertEquals(data2, RedisHelper.getObject(REDIS_KEY, new TypeReference<Byte>() {}));
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
        Assertions.assertEquals(data2, RedisHelper.getObject(REDIS_KEY, new TypeReference<Short>() {}));
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
        Assertions.assertEquals(data2, RedisHelper.getObject(REDIS_KEY, new TypeReference<Integer>() {}));
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
        Assertions.assertEquals(data2, RedisHelper.getObject(REDIS_KEY, new TypeReference<Long>() {}));
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
        Assertions.assertEquals(data2, RedisHelper.getObject(REDIS_KEY, new TypeReference<Float>() {}));
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
        Assertions.assertEquals(data2, RedisHelper.getObject(REDIS_KEY, new TypeReference<Double>() {}));
        Assertions.assertEquals(Double.toString(data2), RedisHelper.getString(REDIS_KEY));
    }

    @Test
    void setObject() {
        String msg = "hello_world";
        RedisHelper.set(REDIS_KEY, msg);
        Assertions.assertEquals(msg, RedisHelper.getString(REDIS_KEY));

        List<Long> longList = Arrays.asList(1L, 2L, 3L, 4L);
        RedisHelper.set(REDIS_KEY, longList);
        Assertions.assertEquals(longList, RedisHelper.getObject(REDIS_KEY, new TypeReference<List<Long>>() {}));

        Data data = new Data();
        data.id = 1L;
        data.name = "张三";
        RedisHelper.set(REDIS_KEY, data);
        Assertions.assertEquals(data, RedisHelper.getObject(REDIS_KEY, new TypeReference<Data>() {}));
    }

    @Test
    void setEx() throws Exception {
        String msg = "hello_world";
        RedisHelper.setEx(REDIS_KEY, msg, 200, TimeUnit.MILLISECONDS);
        Assertions.assertEquals(msg, RedisHelper.getString(REDIS_KEY));
        Thread.sleep(300);
        Assertions.assertNull(RedisHelper.getString(REDIS_KEY));
    }

    @Test
    void incr() throws Exception {
        int count = 1000;
        CountDownLatch latch = new CountDownLatch(count);

        // 1. 多线程并发执行
        int coreNum = Runtime.getRuntime().availableProcessors();
        ExecutorService threadPool = Executors.newFixedThreadPool(coreNum);
        for (int i = 0; i < count; i++) {
            Future<?> value = threadPool.submit(() -> {
                RedisHelper.incr(REDIS_KEY);
                latch.countDown();
            });
        }

        // 2. 等待线程执行完毕
        boolean await = latch.await(10, TimeUnit.SECONDS);
        Assertions.assertTrue(await);
        Assertions.assertEquals(Integer.valueOf(count), RedisHelper.getInteger(REDIS_KEY));
    }

    @Test
    void hincrBy() throws Exception {
        int count = 1000;
        CountDownLatch latch = new CountDownLatch(count);

        // 1. 多线程并发执行
        int coreNum = Runtime.getRuntime().availableProcessors();
        ExecutorService threadPool = Executors.newFixedThreadPool(coreNum);
        for (int i = 0; i < count; i++) {
            threadPool.execute(() -> {
                RedisHelper.hincrBy(REDIS_KEY, REDIS_HASH_FIELD, 1);
                latch.countDown();
            });
        }
        // 2. 等待线程执行完毕
        boolean await = latch.await(10, TimeUnit.SECONDS);
        Assertions.assertTrue(await);
        Assertions.assertEquals(Integer.valueOf(count), RedisHelper.hgetInteger(REDIS_KEY, REDIS_HASH_FIELD));
    }

    @Test
    void keys() {
        int size = 100;
        for (int i = 0; i < size; i++) {
            RedisHelper.set(REDIS_KEY+i, "value"+i);
        }

        Set<String> keys = RedisHelper.keys(REDIS_KEY+"*");
        keys.forEach(System.out::println);
        Assertions.assertEquals(size, keys.size());
        Assertions.assertEquals(Long.valueOf(size), RedisHelper.dbSize());

        for (int i = 0; i < size; i++) {
            RedisHelper.del(REDIS_KEY+i);
        }
    }

    @Test
    void scan() {
        int size = 100;
        for (int i = 0; i < size; i++) {
            RedisHelper.set(REDIS_KEY+i, "value"+i);
        }

        RedisHelper.scan(REDIS_KEY+"*", System.out::println);

        for (int i = 0; i < size; i++) {
            RedisHelper.del(REDIS_KEY+i);
        }
    }

    @Test
    public void lock() throws Exception {
        String unionId1 = UUID.randomUUID().toString();
        String unionId2 = UUID.randomUUID().toString();

        Assertions.assertTrue(RedisHelper.lock(REDIS_KEY, unionId1));
        Assertions.assertFalse(RedisHelper.lock(REDIS_KEY, unionId1));
        Assertions.assertFalse(RedisHelper.lock(REDIS_KEY, unionId2));

        Assertions.assertFalse(RedisHelper.unlock(REDIS_KEY, unionId2));
        Assertions.assertTrue(RedisHelper.unlock(REDIS_KEY, unionId1));
        Assertions.assertFalse(RedisHelper.unlock(REDIS_KEY, unionId1));
    }

    @Test
    public void expire() throws Exception{
        String value = "value";
        RedisHelper.set(REDIS_KEY, value);
        Assertions.assertEquals(value, RedisHelper.getString(REDIS_KEY));

        RedisHelper.expire(REDIS_KEY, 200, TimeUnit.MILLISECONDS);
        Assertions.assertEquals(value, RedisHelper.getString(REDIS_KEY));
        Thread.sleep(200);
        Assertions.assertNull(RedisHelper.getString(REDIS_KEY));

        for (int i = 0; i < 100; i++) {
            RedisHelper.incrEx(REDIS_KEY, 1000, TimeUnit.MILLISECONDS);
        }
        Assertions.assertEquals(100, RedisHelper.getInteger(REDIS_KEY).intValue());
        Thread.sleep(1000);
        Assertions.assertNull(RedisHelper.getInteger(REDIS_KEY));
    }


    @BeforeEach
    void before() {
        RedisHelper.del(REDIS_KEY);
    }

    @AfterEach
    void after() {
        RedisHelper.del(REDIS_KEY);
    }

    static class Data {
        Long id;
        String name;

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Data data = (Data) o;
            return Objects.equals(id, data.id) && Objects.equals(name, data.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name);
        }
    }
}
