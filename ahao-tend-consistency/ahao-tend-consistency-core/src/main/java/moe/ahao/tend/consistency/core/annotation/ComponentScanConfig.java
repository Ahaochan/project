package moe.ahao.tend.consistency.core.annotation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 组件扫描配置
 */
@Configuration
@ComponentScan(value = {"moe.ahao.tend.consistency"})
@MapperScan(basePackages = {"moe.ahao.tend.consistency.core.infrastructure.repository.impl.mybatis.mapper"})
public class ComponentScanConfig {
}
