package moe.ahao.spring.boot.redis;

import moe.ahao.embedded.EmbeddedRedisTest;
import moe.ahao.util.spring.redis.RedisHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.RedissonMultiLock;
import org.redisson.RedissonRedLock;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = Starter.class)
@ActiveProfiles("test-redis")
// @ContextConfiguration(classes = {RedisConfig.class, RedisAutoConfiguration.class, RedissonAutoConfiguration.class, SpringContextHolder.class})
class RedissionTest extends EmbeddedRedisTest {
    private static final String REDIS_KEY = "key";

    @Autowired
    private RedissonClient redissonClient;

    @Test
    void lock() throws Exception {
        RLock lock = redissonClient.getLock(REDIS_KEY);

        Assertions.assertTrue(lock.tryLock(60, TimeUnit.SECONDS));
        Assertions.assertTrue(lock.tryLock(60, TimeUnit.SECONDS));

        Assertions.assertTrue(lock.forceUnlock());
        Assertions.assertFalse(lock.forceUnlock());
    }

    @Test
    void lockTwoThread() throws Exception {
        int count = 2;
        CountDownLatch success = new CountDownLatch(count);
        CountDownLatch latch = new CountDownLatch(count);
        RLock lock = redissonClient.getLock(REDIS_KEY);

        // 1. 第一个线程获取锁
        new Thread(() -> {
            try {
                Assertions.assertTrue(lock.tryLock(0, 60, TimeUnit.SECONDS));
                Thread.sleep(100);
                success.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
                lock.unlock();
            }
        }).start();
        // 2. 第二个线程获取不到锁
        Thread.sleep(1);
        new Thread(() -> {
            try {
                Assertions.assertFalse(lock.tryLock(0, 60, TimeUnit.SECONDS));
                Thread.sleep(100);
                success.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        }).start();

        boolean await = latch.await(10, TimeUnit.SECONDS);
        Assertions.assertTrue(await);
        Assertions.assertEquals(latch.getCount(), success.getCount());
    }

    @Test
    void multiLock() throws Exception {
        RLock lock1 = redissonClient.getLock(REDIS_KEY + 1);
        RLock lock2 = redissonClient.getLock(REDIS_KEY + 2);
        RLock lock3 = redissonClient.getLock(REDIS_KEY + 3);

        RedissonMultiLock multiLock = new RedissonMultiLock(lock1, lock2, lock3);

        Assertions.assertTrue(multiLock.tryLock(60, TimeUnit.SECONDS));
        multiLock.unlock();
    }

    @Test
    @Deprecated
    void redLock() throws Exception {
        RLock lock1 = redissonClient.getLock(REDIS_KEY + 1);
        RLock lock2 = redissonClient.getLock(REDIS_KEY + 2);
        RLock lock3 = redissonClient.getLock(REDIS_KEY + 3);

        RedissonRedLock redLock = new RedissonRedLock(lock1, lock2, lock3);

        Assertions.assertTrue(redLock.tryLock(60, TimeUnit.SECONDS));
        redLock.unlock();
    }

    @Test
    void readWriteLock() throws Exception {
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(REDIS_KEY);
        RLock readLock = readWriteLock.readLock();
        RLock writeLock = readWriteLock.writeLock();

        writeLock.lock();
        readLock.lock();
        readLock.unlock();
        writeLock.unlock();
    }

    @Test
    void semaphore() throws Exception {
        RSemaphore semaphore = redissonClient.getSemaphore(REDIS_KEY);
        semaphore.trySetPermits(3);

        int size = 10;
        CountDownLatch latch = new CountDownLatch(10);
        for (int i = 0; i < size; i++) {
            new Thread(() -> {
                try {
                    System.out.println(new Date() + "：线程[" + Thread.currentThread().getName() + "]尝试获取Semaphore锁");
                    semaphore.acquire();
                    System.out.println(new Date() + "：线程[" + Thread.currentThread().getName() + "]成功获取到了Semaphore锁，开始工作");
                    Thread.sleep(100);
                    semaphore.release();
                    System.out.println(new Date() + "：线程[" + Thread.currentThread().getName() + "]释放Semaphore锁");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            }).start();
        }
        latch.await();

        Assertions.assertEquals(0, latch.getCount());
    }

    @Test
    void countDownLatch() throws Exception {
        int size = 3;
        RCountDownLatch latch = redissonClient.getCountDownLatch(REDIS_KEY);
        latch.trySetCount(size);
        for (int i = 0; i < size; i++) {
            new Thread(() -> {
                try {
                    System.out.println(new Date() + "：线程[" + Thread.currentThread().getName() + "]开始工作");
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    System.out.println(new Date() + "：线程[" + Thread.currentThread().getName() + "]完成countDown");
                    latch.countDown();
                }
            }).start();
        }
        latch.await();

        Assertions.assertEquals(0, latch.getCount());

        Thread.sleep(1000);
    }

    @Test
    void bloomFilter() {
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
    void rateLimiter() throws Exception {
        RRateLimiter limiter = redissonClient.getRateLimiter(REDIS_KEY);
        // 初始化 最大流速 = 每1秒钟产生2个令牌
        limiter.trySetRate(RateType.OVERALL, 2, 200, RateIntervalUnit.MILLISECONDS);

        CountDownLatch latch = new CountDownLatch(2);
        Assertions.assertTrue(limiter.tryAcquire(2,0, TimeUnit.SECONDS));
        Assertions.assertFalse(limiter.tryAcquire(1,0, TimeUnit.SECONDS));

        Thread.sleep(200);
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
