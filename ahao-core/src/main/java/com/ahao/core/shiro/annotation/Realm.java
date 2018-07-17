package com.ahao.core.shiro.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by Ahaochan on 2017/9/21.
 * Shiro Realm的注解, 复制自{@link org.springframework.stereotype.Service}
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Realm {
    String value() default "";
}

