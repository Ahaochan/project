package moe.ahao.spring.boot.bytetcc;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 按请求粒度负载均衡(使用MongoDB存储事务日志):需引入SpringBootConfiguration; <br />
 * 按事务粒度负载均衡(使用文件系统存储事务日志):需引入SpringBootSecondaryConfiguration;
 */
@Import(SpringBootSecondaryConfiguration.class)
@Configuration(proxyBeanMethods = false)
public class ByteTCCConfig {
}
