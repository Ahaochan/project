package moe.ahao.operate.log.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 * @author zhonghuashishan
 * @version 1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface OperateLog {

    /**
     * 日志模板
     */
    String content();

    /**
     * 操作人，目前必须是SpEL表达式
     */
    String operator() default "";

    /**
     * 业务no，目前必须是SpEL表达式，必填
     */
    String bizNo();
}
