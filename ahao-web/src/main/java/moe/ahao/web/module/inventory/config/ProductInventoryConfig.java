package moe.ahao.web.module.inventory.config;

import moe.ahao.web.module.inventory.request.RequestProcessorThreadPool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class ProductInventoryConfig {
    @Bean
    public RequestProcessorThreadPool requestProcessorThreadPool() {
        RequestProcessorThreadPool bean = new RequestProcessorThreadPool();
        bean.setThreadCount(10);
        bean.setBlockingQueueLength(100);
        return bean;
    }
}
