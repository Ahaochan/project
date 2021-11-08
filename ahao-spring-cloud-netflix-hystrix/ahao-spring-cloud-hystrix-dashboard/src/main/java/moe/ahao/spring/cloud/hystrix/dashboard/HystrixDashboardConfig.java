package moe.ahao.spring.cloud.hystrix.dashboard;

import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableHystrixDashboard
public class HystrixDashboardConfig {
}
