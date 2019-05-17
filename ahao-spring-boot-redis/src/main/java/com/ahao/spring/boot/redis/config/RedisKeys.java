package com.ahao.spring.boot.redis.config;

import com.ahao.spring.boot.redis.aop.RedisCacheAOP;

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
 * 6. Cache: c    {@link RedisConfig}
 * 7. Function: f {@link RedisCacheAOP}
 *
 * 比如 String 类型, s:user:1:name
 */
public interface RedisKeys {
    String PREFIX_S   = "s:"; // 字符串Key的前缀
    String PREFIX_H   = "h:"; // Hash的Key的前缀
    String PREFIX_L   = "l:"; // List的Key的前缀
    String PREFIX_SET = "set:"; // Set的Key的前缀
    String PREFIX_Z   = "z:"; // Sorted Set的Key的前缀
    String PREFIX_C   = "c:"; // 缓存管理器生成的key
    String PREFIX_F   = "f:"; // 方法拦截生成的key
}
