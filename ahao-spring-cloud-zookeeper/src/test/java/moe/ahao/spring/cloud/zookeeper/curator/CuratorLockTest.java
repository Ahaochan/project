package moe.ahao.spring.cloud.zookeeper.curator;

import org.apache.curator.framework.recipes.locks.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

class CuratorLockTest extends CuratorBaseTest {

    @DisplayName("可重入锁测试")
    @Test
    void lock1() throws Exception {
        InterProcessLock lock = new InterProcessMutex(zk, "/ahao-lock1");
        lock.acquire();
        lock.release();
    }

    @DisplayName("非可重入锁测试")
    @Test
    void lock2() throws Exception {
        InterProcessLock lock = new InterProcessSemaphoreMutex(zk, "/ahao-lock2");
        lock.acquire();
        lock.release();
    }

    @Test
    void readWriteLock() throws Exception {
        InterProcessReadWriteLock readWriteLock = new InterProcessReadWriteLock(zk, "/ahao-rwlock");
        InterProcessLock readLock = readWriteLock.readLock();
        InterProcessLock writeLock = readWriteLock.writeLock();

        writeLock.acquire();
        readLock.acquire();
        readLock.release();
        writeLock.release();
    }

    @Test
    void multiLock() throws Exception {
        List<InterProcessLock> locks = Arrays.asList(
            new InterProcessMutex(zk, "/ahao-lock1"),
            new InterProcessMutex(zk, "/ahao-lock2"),
            new InterProcessMutex(zk, "/ahao-lock3")
        );
        InterProcessLock multiLock = new InterProcessMultiLock(locks);
        multiLock.acquire();
        multiLock.release();
    }

    @Test
    void semaphore() throws Exception {
        InterProcessSemaphoreV2 semaphore = new InterProcessSemaphoreV2(zk, "/ahao-semaphore-v2", 2);

        int size = 10;
        CountDownLatch latch = new CountDownLatch(10);
        for (int i = 0; i < size; i++) {
            new Thread(() -> {
                try {
                    System.out.println(new Date() + "：线程[" + Thread.currentThread().getName() + "]尝试获取Semaphore锁");
                    Lease lease = semaphore.acquire();
                    System.out.println(new Date() + "：线程[" + Thread.currentThread().getName() + "]成功获取到了Semaphore锁，开始工作");
                    Thread.sleep(3000);
                    semaphore.returnLease(lease);
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
}
