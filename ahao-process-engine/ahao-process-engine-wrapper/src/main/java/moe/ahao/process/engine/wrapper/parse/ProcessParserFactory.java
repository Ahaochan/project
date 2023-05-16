package moe.ahao.process.engine.wrapper.parse;

import moe.ahao.process.engine.wrapper.enums.XmlReadFromEnum;
import org.springframework.core.env.Environment;

/**
 * 流程解析器工厂
 */
public class ProcessParserFactory {
    private final Environment environment;

    public ProcessParserFactory(Environment environment) {
        this.environment = environment;
    }

    public ProcessParser create(String configFile, XmlReadFromEnum readFrom) {
        if (XmlReadFromEnum.NACOS.equals(readFrom)) {
            return new NacosXmlProcessParser(configFile, environment);
        } else {
            return new ClassPathXmlProcessParser(configFile);
        }
    }
}
