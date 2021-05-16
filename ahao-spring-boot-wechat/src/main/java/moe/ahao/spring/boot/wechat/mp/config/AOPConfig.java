package moe.ahao.spring.boot.wechat.mp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
public class AOPConfig {
}
