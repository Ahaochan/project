package com.ahao.spring.boot.async;

import com.ahao.util.commons.lang.time.DateHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ThreadLocalTest {
    @Test
    public void threadLocal() throws Exception {
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
