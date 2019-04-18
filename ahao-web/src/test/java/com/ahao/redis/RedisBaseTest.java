package com.ahao.redis;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import redis.embedded.RedisServer;

import java.io.IOException;

public class RedisBaseTest {
    private static RedisServer redisServer;

    @BeforeClass
    public static void start() throws IOException {
        redisServer = RedisServer.builder()
                .port(6379)
                .setting("maxmemory 128M")
                .build();
        redisServer.start();
    }

    @AfterClass
    public static void stop() throws IOException {
        redisServer.stop();
    }
}
