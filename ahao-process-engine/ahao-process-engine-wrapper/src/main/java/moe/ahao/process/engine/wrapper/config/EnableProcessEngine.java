package moe.ahao.process.engine.wrapper.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({ProcessNodeRegistrar.class, ProcessEngineConfig.class})
public @interface EnableProcessEngine {
    /**
     * 配置文件名称
     */
    String value();
}
