package moe.ahao.spring.boot.integration.lock;


import moe.ahao.embedded.EmbeddedRedisTest;
import moe.ahao.spring.boot.Starter;
import moe.ahao.util.spring.SpringContextHolder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = {Starter.class, RedisAutoConfiguration.class, JacksonAutoConfiguration.class, SpringContextHolder.class})
@ActiveProfiles("redis")
public class RedisLockTest extends EmbeddedRedisTest {

    @Autowired
    private RedisConnectionFactory factory;

    @Test
    public void singleThread() throws Exception {
        String keyPrefix = "ahao";
        RedisLockRegistry redisLockRegistry = new RedisLockRegistry(factory, keyPrefix);

        Lock lock = redisLockRegistry.obtain("lock");
        boolean b1 = lock.tryLock(2, TimeUnit.SECONDS);
        Assertions.assertTrue(b1);

        Thread.sleep(100);
        boolean b2 = lock.tryLock(2, TimeUnit.SECONDS);
        Assertions.assertTrue(b2);

        Thread.sleep(100);
        boolean b3 = lock.tryLock(2, TimeUnit.SECONDS);
        Assertions.assertTrue(b3);

        lock.unlock();
        lock.unlock();
        lock.unlock();
        Assertions.assertThrows(IllegalStateException.class, lock::unlock); // 解锁超过加锁次数抛出异常
    }

    @Test
    public void multiThread() throws Exception {
        String keyPrefix = "ahao";
        RedisLockRegistry redisLockRegistry = new RedisLockRegistry(factory, keyPrefix);

        Lock lock = redisLockRegistry.obtain("lock");
        CountDownLatch latch = new CountDownLatch(3);

        Runnable task = () -> {
            try {
                boolean b1 = lock.tryLock(2, TimeUnit.SECONDS);
                Thread.sleep(100);
                System.out.println(b1);
                // Assertions.assertTrue(b1);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
                lock.unlock();
            }
        };

        new Thread(task).start();
        new Thread(task).start();
        new Thread(task).start();

        latch.await();
    }

}
