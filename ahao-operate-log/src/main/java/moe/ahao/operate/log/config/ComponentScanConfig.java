package moe.ahao.operate.log.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * 组件扫描组件
 * @author zhonghuashishan
 * @version 1.0
 */
@Component
@ComponentScan(value = {"com.ruyuan.operatelog"})
public class ComponentScanConfig {
}
