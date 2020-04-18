package com.ahao.spring.boot.async;

import com.ahao.util.commons.lang.time.DateHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ThreadLocalTest {
    @Test
    public void threadLocal1() throws Exception {
        ThreadLocal<String> context = new ThreadLocal<>();

        Assertions.assertNull(context.get());
        String expectP = "hello " + DateHelper.getNow(DateHelper.yyyyMMdd_hhmmssSSS);
        context.set(expectP);

        Runnable runnable = () -> {
            Assertions.assertNull(context.get());
            String expect = "hello " + DateHelper.getNow(DateHelper.yyyyMMdd_hhmmssSSS);
            context.set(expect);
            String value = context.get();
            System.out.println(Thread.currentThread().getName() + " get " + value);
            Assertions.assertEquals(expect, value);
        };

        Thread thread1 = new Thread(runnable);
        thread1.start();
        thread1.join();

        Thread thread2 = new Thread(runnable);
        thread2.start();
        thread2.join();

        String value = context.get();
        System.out.println(Thread.currentThread().getName() + " get " + value);
        Assertions.assertEquals(expectP, value);
    }

    @Test
    public void threadLocal2() throws Exception {
        ThreadLocal<String> context = new ThreadLocal<>();

        String expect = "hello " + DateHelper.getNow(DateHelper.yyyyMMdd_hhmmssSSS);
        context.set(expect);
        System.out.println("[parent thread] set " + context.get());
        Assertions.assertEquals(expect, context.get());

        Thread childThread = new Thread(() -> {
            String value = context.get();
            System.out.println("[child thread] get " + value);
            Assertions.assertNotEquals(expect, value);
        });
        childThread.start();
        childThread.join();

        System.out.println("[parent thread] get " + context.get());
        Assertions.assertEquals(expect, context.get());
    }

    @Test
    public void inheritableThreadLocal() throws Exception {
        ThreadLocal<String> context = new InheritableThreadLocal<>();

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
}
