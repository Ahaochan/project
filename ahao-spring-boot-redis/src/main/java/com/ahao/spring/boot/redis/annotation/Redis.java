package com.ahao.spring.boot.redis.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Redis {
    int REDIS = 1, DB = 2;

    int action() default REDIS;     // 数据来源
    String key() default "";        // 指定 key, 默认使用 包名.类名(参数1,参数2...) 作为 key
    int ttl() default 24 * 60 * 60; // 缓存时间, 不允许无限缓存
    boolean zip() default false;    // TODO 是否压缩数据
}
