package com.ahao.spring.boot.datasources.config;

import com.ahao.spring.boot.datasources.strategy.LoadBalanceStrategy;
import com.ahao.spring.boot.datasources.strategy.PollingStrategy;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 注意要开启 @EnableAutoConfiguration 注解
 */
@Component
@ConfigurationProperties("spring.datasource.dynamic")
public class DataSourceConfiguration {
    /**
     * 主库名称
     */
    private String primary = "primary";

    /**
     * 分隔符
     */
    private String groupBy = "_";

    /**
     * 负载均衡策略
     */
    private Class<? extends LoadBalanceStrategy> loadBalanceStrategy = PollingStrategy.class;

    /**
     * 数据库属性
     */
    private Map<String, DataSourceProperties> datasource;

    // ================================== Getter And Setter ========================================

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public Class<? extends LoadBalanceStrategy> getLoadBalanceStrategy() {
        return loadBalanceStrategy;
    }

    public void setLoadBalanceStrategy(Class<? extends LoadBalanceStrategy> loadBalanceStrategy) {
        this.loadBalanceStrategy = loadBalanceStrategy;
    }

    public Map<String,DataSourceProperties> getDatasource() {
        return datasource;
    }

    public void setDatasource(Map<String, DataSourceProperties> datasource) {
        this.datasource = datasource;
    }
}
