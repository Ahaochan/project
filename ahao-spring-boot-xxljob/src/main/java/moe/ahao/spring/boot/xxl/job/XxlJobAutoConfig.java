package moe.ahao.spring.boot.xxl.job;

import com.xxl.job.core.executor.XxlJobExecutor;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import moe.ahao.spring.boot.xxl.job.properties.XxlJobProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
@ConditionalOnClass(XxlJobExecutor.class)
public class XxlJobAutoConfig {
    private Logger logger = LoggerFactory.getLogger(XxlJobAutoConfig.class);

    @Bean
    @ConfigurationProperties("xxl.job")
    public XxlJobProperties xxlJobProperties() {
        return new XxlJobProperties();
    }


    @Bean
    public XxlJobSpringExecutor xxlJobExecutor(XxlJobProperties properties) {
        Optional<XxlJobProperties> optional = Optional.of(properties);
        logger.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        optional.map(XxlJobProperties::getAdminAddresses).ifPresent(xxlJobSpringExecutor::setAdminAddresses);
        optional.map(XxlJobProperties::getAppName).ifPresent(xxlJobSpringExecutor::setAppname);
        optional.map(XxlJobProperties::getAddress).ifPresent(xxlJobSpringExecutor::setAddress);
        optional.map(XxlJobProperties::getIp).ifPresent(xxlJobSpringExecutor::setIp);
        optional.map(XxlJobProperties::getPort).ifPresent(xxlJobSpringExecutor::setPort);
        optional.map(XxlJobProperties::getAccessToken).ifPresent(xxlJobSpringExecutor::setAccessToken);
        optional.map(XxlJobProperties::getLogPath).ifPresent(xxlJobSpringExecutor::setLogPath);
        optional.map(XxlJobProperties::getLogRetentionDays).ifPresent(xxlJobSpringExecutor::setLogRetentionDays);
        return xxlJobSpringExecutor;
    }
}
