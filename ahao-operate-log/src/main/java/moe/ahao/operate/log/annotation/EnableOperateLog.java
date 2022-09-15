package moe.ahao.operate.log.annotation;

import moe.ahao.operate.log.config.OperateLogSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启动操作日志框架注解
 * @author zhonghuashishan
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
@Import(OperateLogSelector.class)
public @interface EnableOperateLog {
}
