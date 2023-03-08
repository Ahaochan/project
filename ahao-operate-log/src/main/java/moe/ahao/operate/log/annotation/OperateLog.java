package moe.ahao.operate.log.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperateLog {
    /**
     * 成功日志模板
     */
    String success();
    /**
     * 失败日志模板
     */
    String fail() default "";

    /**
     * 控制生成操作日志的执行时机
     * true  : 在拦截业务方法执行之前
     * false : 在拦截业务方法执行之后
     */
    boolean executeBefore() default false;

    /**
     * 操作人
     */
    String operator() default "";

    /**
     * 业务对象标识
     */
    String bizNo();

    /**
     * 日志的种类
     */
    String category() default "";

    /**
     * 扩展参数，记录操作日志的修改详情
     */
    String detail() default "";

    /**
     * 记录日志的条件
     */
    String condition() default "";
}
