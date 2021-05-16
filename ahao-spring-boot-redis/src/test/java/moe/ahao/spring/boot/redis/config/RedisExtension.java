package moe.ahao.spring.boot.redis.config;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import redis.embedded.RedisServer;

public class RedisExtension extends SpringExtension {
    private static RedisServer redisServer;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        redisServer = RedisServer.builder()
            .port(6379)
            .setting("maxmemory 128M")
            .build();
        redisServer.start();
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        redisServer.stop();
    }
}
