package moe.ahao.unit.test.annotation;

import java.lang.annotation.*;


/**
 * 标识一个junit单测方法是一个标准单测,运行完毕会自动清除数据，恢复现场
 */
@Target (ElementType.METHOD)
@Retention (RetentionPolicy.RUNTIME)
@Documented
public @interface StdUt {
}
