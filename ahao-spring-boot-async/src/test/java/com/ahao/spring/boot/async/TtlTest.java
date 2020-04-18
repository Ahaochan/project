package com.ahao.spring.boot.async;

import com.ahao.util.commons.lang.time.DateHelper;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlCallable;
import com.alibaba.ttl.TtlRunnable;
import com.alibaba.ttl.threadpool.TtlExecutors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TtlTest {
    /**
     * 简单使用
     * copy from https://github.com/alibaba/transmittable-thread-local/blob/master/src/test/java/com/alibaba/demo/ttl/SimpleDemo.kt
     *
     * @see <a href="https://github.com/alibaba/transmittable-thread-local#1-%E7%AE%80%E5%8D%95%E4%BD%BF%E7%94%A8">简单使用</a>
     */
    @Test
    public void simple() throws Exception {
        TransmittableThreadLocal<String> context = new TransmittableThreadLocal<>();

        String expect = "hello " + DateHelper.getNow(DateHelper.yyyyMMdd_hhmmssSSS);
        context.set(expect);
        System.out.println("[parent thread] set " + context.get());
        Assertions.assertEquals(expect, context.get());

        Thread childThread = new Thread(() -> {
            String value = context.get();
            System.out.println("[child thread] get " + value);
            Assertions.assertEquals(expect, value);
        });
        childThread.start();
        childThread.join();

        System.out.println("[parent thread] get " + context.get());
        Assertions.assertEquals(expect, context.get());
    }

    /**
     * 修饰Runnable和Callable
     * copy from https://github.com/alibaba/transmittable-thread-local/blob/master/src/test/java/com/alibaba/demo/ttl/TtlWrapperDemo.kt
     *
     * @see <a href="https://github.com/alibaba/transmittable-thread-local#21-%E4%BF%AE%E9%A5%B0runnable%E5%92%8Ccallable">修饰Runnable和Callable</a>
     */
    @Test
    public void wrapper() throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();
        TransmittableThreadLocal<String> context = new TransmittableThreadLocal<>();

        String expect = "hello " + DateHelper.getNow(DateHelper.yyyyMMdd_hhmmssSSS);
        context.set(expect);
        System.out.println("[parent thread] set " + context.get());
        Assertions.assertEquals(expect, context.get());

        /////////////////////////////////////
        // Runnable / TtlRunnable
        /////////////////////////////////////
        Runnable task = () -> {
            System.out.println("[child thread] get " + context.get() + " in Runnable");
            Assertions.assertEquals(expect, context.get());
        };
        TtlRunnable ttlRunnable = TtlRunnable.get(task);
        executorService.submit(ttlRunnable).get();

        /////////////////////////////////////
        // Callable / TtlCallable
        /////////////////////////////////////
        Callable<Integer> call = () -> {
            System.out.println("[child thread] get " + context.get() + " in Callable");
            Assertions.assertEquals(expect, context.get());
            return 42;
        };
        TtlCallable ttlCallable = TtlCallable.get(call);
        executorService.submit(ttlCallable).get();

        /////////////////////////////////////
        // cleanup
        /////////////////////////////////////
        executorService.shutdown();
    }

    /**
     * 修饰线程池
     * copy from https://github.com/alibaba/transmittable-thread-local/blob/master/src/test/java/com/alibaba/demo/ttl/TtlExecutorWrapperDemo.kt
     * @see <a href="https://github.com/alibaba/transmittable-thread-local#22-%E4%BF%AE%E9%A5%B0%E7%BA%BF%E7%A8%8B%E6%B1%A0">修饰线程池</a>
     */
    @Test
    public void executor() throws Exception {
        ExecutorService ttlExecutorService = TtlExecutors.getTtlExecutorService(Executors.newCachedThreadPool());
        TransmittableThreadLocal<String> context = new TransmittableThreadLocal<>();

        String expect = "hello " + DateHelper.getNow(DateHelper.yyyyMMdd_hhmmssSSS);
        context.set(expect);
        System.out.println("[parent thread] set " + context.get());
        Assertions.assertEquals(expect, context.get());

        /////////////////////////////////////
        // Runnable
        /////////////////////////////////////
        Runnable task = () -> {
            System.out.println("[child thread] get " + context.get() + " in Runnable");
            Assertions.assertEquals(expect, context.get());
        };
        ttlExecutorService.submit(task).get();

        /////////////////////////////////////
        // Callable
        /////////////////////////////////////
        Callable<Integer> call = () -> {
            System.out.println("[child thread] get " + context.get() + " in Callable");
            Assertions.assertEquals(expect, context.get());
            return 42;
        };
        ttlExecutorService.submit(call).get();

        /////////////////////////////////////
        // cleanup
        /////////////////////////////////////
        ttlExecutorService.shutdown();
    }

    /**
     * TODO 使用Java Agent来修饰JDK线程池实现类
     * copy from https://github.com/alibaba/transmittable-thread-local/blob/master/src/test/java/com/alibaba/demo/ttl/agent/AgentDemo.kt
     * @see <a href="https://github.com/alibaba/transmittable-thread-local#23-%E4%BD%BF%E7%94%A8java-agent%E6%9D%A5%E4%BF%AE%E9%A5%B0jdk%E7%BA%BF%E7%A8%8B%E6%B1%A0%E5%AE%9E%E7%8E%B0%E7%B1%BB">使用Java Agent来修饰JDK线程池实现类</a>
     */
    public void agent() {
    }
}
