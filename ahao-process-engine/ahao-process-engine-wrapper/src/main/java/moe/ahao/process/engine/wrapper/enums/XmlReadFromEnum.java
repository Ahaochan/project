package moe.ahao.process.engine.wrapper.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * xml流程配置文件从哪读取
 */
@Getter
@AllArgsConstructor
public enum XmlReadFromEnum {
    CLASSPATH("本地classpath"),
    NACOS("nacos注册中");
    private final String desc;
}
