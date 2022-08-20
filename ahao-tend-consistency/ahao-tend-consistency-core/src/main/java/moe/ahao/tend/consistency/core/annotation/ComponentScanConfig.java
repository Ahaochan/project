package moe.ahao.tend.consistency.core.annotation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 组件扫描配置
 *
 * @author zhonghuashishan
 */
@Configuration
@ComponentScan(value = {"com.ruyuan.consistency"})
@MapperScan(basePackages = {"com.ruyuan.consistency.mapper"})
public class ComponentScanConfig {


}
