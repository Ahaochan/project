package moe.ahao.spring.boot.datasources.properties;

import moe.ahao.spring.boot.datasources.strategy.LoadBalanceStrategy;
import moe.ahao.spring.boot.datasources.strategy.PollingStrategy;

/**
 * 注意要开启 @EnableAutoConfiguration 注解
 */
public class BalanceDataSourceProperties {
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
}
