package moe.ahao.process.engine.wrapper.parse;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.springframework.core.env.Environment;

import java.util.Properties;

/**
 * 从Nacos读取XML格式的流程配置解析器
 */
@Slf4j
public class NacosXmlProcessParser extends StringXmlProcessParser {
    private static final String NACOS_SERVER_ADDR_KEY = "spring.cloud.nacos.config.server-addr";
    private static final String NACOS_SERVER_USERNAME_KEY = "spring.cloud.nacos.config.username";
    private static final String NACOS_SERVER_PASSWORD_KEY = "spring.cloud.nacos.config.password";
    private static final String NACOS_SERVER_GROUP_KEY = "spring.cloud.nacos.config.group";

    private final String file;
    private final Environment environment;

    public NacosXmlProcessParser(String file, Environment environment) {
        super();
        this.file = file;
        this.environment = environment;
    }


    @Override
    protected Document getDocument() throws Exception {
        // 1. 从env读取nacos配置相关
        String serverAddr = environment.getProperty(NACOS_SERVER_ADDR_KEY);
        String username = environment.getProperty(NACOS_SERVER_USERNAME_KEY);
        String password = environment.getProperty(NACOS_SERVER_PASSWORD_KEY);
        String group = environment.getProperty(NACOS_SERVER_GROUP_KEY, "DEFAULT_GROUP");
        if(StringUtils.isAnyEmpty(serverAddr, username, password, group)) {
            throw new UnsupportedOperationException(String.format("cannot read nacos config from env!!"));
        }

        // 2. 创建ConfigService
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
        properties.put(PropertyKeyConst.USERNAME, username);
        properties.put(PropertyKeyConst.PASSWORD, password);
        ConfigService configService = NacosFactory.createConfigService(properties);

        // 3. 读取nacos上的流程配置文件
        String config = configService.getConfig(file, group, 3000);
        if(null == config) {
            throw new UnsupportedOperationException(String.format("nacos cannot find this config！！，file=%s,group=%s",file,group));
        }
        log.info("read {} from nacos, the content=\n{},group={}",file,config,group);

        // 4. 解析读取到的xml
        this.setXmlString(config);
        return super.getDocument();
    }


}
