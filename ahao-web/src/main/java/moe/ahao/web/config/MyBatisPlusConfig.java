package moe.ahao.web.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@MapperScan("moe.ahao.**.mapper")
public class MyBatisPlusConfig {
}
