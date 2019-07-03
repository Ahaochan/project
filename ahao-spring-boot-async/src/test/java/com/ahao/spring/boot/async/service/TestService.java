package com.ahao.spring.boot.async.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class TestService {
    public static Object value = null;

    @Async
    public void executeVoid(Object value) throws Exception {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TestService.value = value;
    }

    @Async
    public Future<Integer> executeFuture(Integer value) {
        TestService.value = value;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Integer> result = executor.submit(() -> value + 100);
        return result;
    }

    @Async
    public void executeException() {
        throw new RuntimeException("错误");
    }
}
