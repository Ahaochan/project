package com.ahao.spring.boot.redis;

import org.junit.rules.ExternalResource;
import redis.embedded.RedisServer;

public class RedisExternalResource extends ExternalResource {
    private static RedisServer redisServer;

    @Override
    protected void before() {
        redisServer = RedisServer.builder()
                .port(6379)
                .setting("maxmemory 128M")
                .build();
        redisServer.start();
    }

    @Override
    protected void after() {
        redisServer.stop();
    }
}
