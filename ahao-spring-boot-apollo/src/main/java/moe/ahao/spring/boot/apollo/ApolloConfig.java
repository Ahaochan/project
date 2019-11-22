package moe.ahao.spring.boot.apollo;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@EnableApolloConfig
@Configuration
public class ApolloConfig implements ApplicationContextAware, InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(ApolloConfig.class);

    private ApplicationContext applicationContext;

    @Value("${apollo.bootstrap.namespaces}")
    private String[] namespaces;

    @Override
    public void afterPropertiesSet() throws Exception {
        ConfigChangeListener configChangeListener = changeEvent -> {
            logger.info("修改了namespace:{}", changeEvent.getNamespace());
            for (String key : changeEvent.changedKeys()) {
                ConfigChange change = changeEvent.getChange(key);
                logger.info("改变值 - {}", change.toString());
            }

            // 更新相应的bean的属性值，主要是存在@ConfigurationProperties注解的bean
            applicationContext.publishEvent(new EnvironmentChangeEvent(changeEvent.changedKeys()));
        };

        for (String namespace : namespaces) {
            Config config = ConfigService.getConfig(namespace);
            config.addChangeListener(configChangeListener);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
