package moe.ahao.spring.boot.ddd.infrastructure.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@MapperScan("moe.ahao.spring.boot.ddd.infrastructure.repository.mybatis.mapper")
public class MybatisPlusAutoConfig {
}
