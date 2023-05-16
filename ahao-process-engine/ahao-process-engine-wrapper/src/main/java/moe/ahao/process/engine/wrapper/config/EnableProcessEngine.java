package moe.ahao.process.engine.wrapper.config;

import moe.ahao.process.engine.wrapper.enums.XmlReadFromEnum;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({ProcessNodeRegistrar.class, ProcessEngineConfig.class, ProcessScheduleConfig.class})
public @interface EnableProcessEngine {
    String VALUE_KEY = "value";
    String READ_FROM_KEY = "readFrom";

    /**
     * 配置文件名称
     */
    String value();

    /**
     * 从哪加载配置文件
     */
    XmlReadFromEnum readFrom() default XmlReadFromEnum.CLASSPATH;
}
