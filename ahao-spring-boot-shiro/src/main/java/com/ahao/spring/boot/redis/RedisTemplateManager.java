package com.ahao.spring.boot.redis;

import org.crazycake.shiro.IRedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RedisTemplateManager implements IRedisManager, InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(RedisTemplateManager.class);

    private RedisConnectionFactory redisConnectionFactory;
    private RedisTemplate<byte[], byte[]> redisTemplate;
    public RedisTemplateManager(RedisConnectionFactory redisConnectionFactory) {
       this.redisConnectionFactory = redisConnectionFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 1. 初始化 RedisTemplate
        redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 2. 关闭序列化, 使用原生 byte[]
        redisTemplate.setEnableDefaultSerializer(false);
        redisTemplate.setKeySerializer(null);
        redisTemplate.setValueSerializer(null);
        redisTemplate.setHashKeySerializer(null);
        redisTemplate.setHashValueSerializer(null);

        // 3. 后处理
        redisTemplate.afterPropertiesSet();
    }

    @Override
    public byte[] get(byte[] key) {
        if (key == null) {
            return null;
        }
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public byte[] set(byte[] key, byte[] value, int expire) {
        if (key == null) {
            return null;
        }
        redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
        return value;
    }

    @Override
    public void del(byte[] key) {
        if (key == null) {
            return;
        }
        redisTemplate.delete(key);
    }

    @Override
    public Long dbSize(byte[] pattern) {
        Long dbSize = redisTemplate.execute(RedisServerCommands::dbSize);
        return dbSize == null ? 0 : dbSize;
    }

    @Override
    public Set<byte[]> keys(byte[] pattern) {
        ScanOptions options = ScanOptions.scanOptions().match(new String(pattern, StandardCharsets.UTF_8)).count(100).build();

        try (Cursor<byte[]> cursor = redisTemplate.executeWithStickyConnection(connection -> connection.scan(options))) {
            if (cursor == null) {
                return Collections.emptySet();
            }

            Set<byte[]> keys = new HashSet<>();
            while (cursor.hasNext()) {
                byte[] key = cursor.next();
                keys.add(key);
            }
            return keys;
        } catch (IOException e) {
            logger.error("get redis keys failed", e);
        }
        return Collections.emptySet();
    }
}
