package moe.ahao.maven.archetype.infrastructure.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@MapperScan("moe.ahao.maven.archetype.infrastructure.repository.mybatis.mapper")
public class MybatisPlusAutoConfig {
}
