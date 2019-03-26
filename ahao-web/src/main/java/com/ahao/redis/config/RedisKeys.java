package com.ahao.redis.config;

/**
 * Redis Key 存储, 避免 Key 覆盖
 *
 * Key 统一以 : 分割, 并根据数据类型以特定字符开头
 * 1. String: s
 * 2. Hash: h
 * 3. List: l
 * 4. Set: set
 * 5. Sorted Set: z
 *
 * 比如 String 类型, s:user:1:name
 */
public interface RedisKeys {
    String SHIRO_RETRY = "s:shiro:user:%s:retry"; // 用户id
}
