package moe.ahao.spring.boot.redis;

import com.ahao.util.spring.SpringContextHolder;
import com.ahao.util.spring.redis.RedisHelper;
import moe.ahao.spring.boot.redis.config.RedisConfig;
import moe.ahao.spring.boot.redis.config.RedisExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.redisson.api.*;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@ExtendWith(RedisExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = {RedisConfig.class, RedisAutoConfiguration.class, RedissonAutoConfiguration.class, SpringContextHolder.class})
@ActiveProfiles("test-redis")
class RedisHelperTest {
    private static final String REDIS_KEY = "key";

    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void lock() throws Exception {
        RLock lock = redissonClient.getLock(REDIS_KEY);

        Assertions.assertTrue(lock.tryLock(60, TimeUnit.SECONDS));
        Assertions.assertTrue(lock.tryLock(60, TimeUnit.SECONDS));

        Assertions.assertTrue(lock.forceUnlock());
        Assertions.assertFalse(lock.forceUnlock());
    }

    @Test
    public void lockTwoThread() throws Exception {
        int count = 2;
        CountDownLatch success = new CountDownLatch(count);
        CountDownLatch latch = new CountDownLatch(count);
        RLock lock = redissonClient.getLock(REDIS_KEY);

        // 1. 第一个线程获取锁
        new Thread(() -> {
            try {
                Assertions.assertTrue(lock.tryLock(0, 60, TimeUnit.SECONDS));
                Thread.sleep(1000);
                success.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
                lock.unlock();
            }
        }).start();
        // 2. 第二个线程获取不到锁
        Thread.sleep(100);
        new Thread(() -> {
            try {
                Assertions.assertFalse(lock.tryLock(0, 60, TimeUnit.SECONDS));
                Thread.sleep(1000);
                success.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        }).start();

        latch.await(10, TimeUnit.SECONDS);
        Assertions.assertEquals(latch.getCount(), success.getCount());
    }

    @Test
    public void bloomFilter() {
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter(REDIS_KEY);
        // 初始化布隆过滤器，预计统计元素数量为55000000，期望误差率为0.03
        bloomFilter.tryInit(55000000L, 0.03);

        int count = 10000;
        Set<String> strings = Stream.iterate(1, i -> i + 1).map(i -> "String" + i).limit(count).collect(Collectors.toSet());
        strings.forEach(bloomFilter::add);
        Assertions.assertEquals(count, bloomFilter.count());
        strings.forEach(s -> Assertions.assertTrue(bloomFilter.contains(s)));
    }

    @Test
    public void rateLimiter() throws Exception {
        RRateLimiter limiter = redissonClient.getRateLimiter(REDIS_KEY);
        // 初始化 最大流速 = 每1秒钟产生2个令牌
        limiter.trySetRate(RateType.OVERALL, 2, 1, RateIntervalUnit.SECONDS);

        CountDownLatch latch = new CountDownLatch(2);
        Assertions.assertTrue(limiter.tryAcquire(2,0, TimeUnit.SECONDS));
        Assertions.assertFalse(limiter.tryAcquire(1,0, TimeUnit.SECONDS));

        Thread.sleep(1000);
        Assertions.assertTrue(limiter.tryAcquire(2,0, TimeUnit.SECONDS));
        Assertions.assertFalse(limiter.tryAcquire(1,0, TimeUnit.SECONDS));

    }

    @BeforeEach
    void before() {
        RedisHelper.del(REDIS_KEY);
    }

    @AfterEach
    void after() {
        RedisHelper.del(REDIS_KEY);
    }
}
