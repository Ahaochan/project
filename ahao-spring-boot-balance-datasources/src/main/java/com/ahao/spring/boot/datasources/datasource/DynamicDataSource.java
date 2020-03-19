package com.ahao.spring.boot.datasources.datasource;

import com.ahao.spring.boot.datasources.DataSourceContextHolder;
import com.ahao.spring.boot.datasources.config.BalanceDataSourceProperties;
import com.ahao.spring.boot.datasources.repository.DataSourcePropertiesRepository;
import com.ahao.spring.boot.datasources.strategy.LoadBalanceStrategy;
import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DynamicDataSource extends AbstractRoutingDataSource implements InitializingBean, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSource.class);

    private BalanceDataSourceProperties configuration;
    private DataSourcePropertiesRepository repository;

    public DynamicDataSource(BalanceDataSourceProperties dataSourceConfiguration, DataSourcePropertiesRepository repository) {
        this.configuration = dataSourceConfiguration;
        this.repository = repository;
    }

    private Map<String, DataSource> dataSources;
    private Map<String, Map<String, DataSource>> dataSourceGroup;
    private LoadBalanceStrategy loadBalanceStrategy;

    @Override
    public void afterPropertiesSet() {
        // 1. 保证数据源属性不为空
        Map<String, DataSourceProperties> propertiesMap = repository.getDataSourceProperties();
        if (propertiesMap.size() <= 0) {
            throw new IllegalArgumentException("请确保至少有一个数据源");
        }

        // 2. 将所有数据源属性转为数据源
        dataSources = propertiesMap.entrySet().stream()
            .peek(e -> logger.info("初始化 {} 数据源", e.getKey()))
            .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().initializeDataSourceBuilder().build()));

        // 3. 数据源分组
        String split = configuration.getGroupBy();
        dataSourceGroup = dataSources.entrySet().stream()
            .collect(Collectors
                .groupingBy(e -> StringUtils.contains(e.getKey(), split) ? StringUtils.substringBefore(e.getKey(), split) : e.getKey(),
                    Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

        // 3. 初始化负载均衡策略
        logger.info("加载负载均衡策略{}", configuration.getLoadBalanceStrategy().getSimpleName());
        loadBalanceStrategy = BeanUtils.instantiateClass(configuration.getLoadBalanceStrategy());

        // 4. 判断校验
        String primary = configuration.getPrimary();
        DataSource primaryDataSource = findGroupOrOne(primary);
        Assert.notNull(dataSources, "数据源不允许为 null");
        Assert.notNull(dataSourceGroup, "数据源不允许为 null");
        Assert.notNull(primaryDataSource, "primary 数据源不允许为 null");
        Assert.notNull(loadBalanceStrategy, "负载均衡不允许为 null");
    }

    @Override
    protected DataSource determineTargetDataSource() {
        // 1. 获取 Key
        String key = DataSourceContextHolder.get();

        // 2. 返回查找的数据源
        DataSource find = findGroupOrOne(key);
        if (find != null) {
            return find;
        }

        // 3. 返回默认数据源
        DataSource primary = findGroupOrOne(configuration.getPrimary());
        if (primary != null) {
            return primary;
        }

        throw new IllegalArgumentException("找不到" + key + "数据源");
    }

    private DataSource findGroupOrOne(String key) {
        // 1. 查询某个组内的数据源, 负载均衡
        Map<String, DataSource> group = dataSourceGroup.get(key);
        if (group != null) {
            logger.debug("切换到 {} 组数据源", key);
            List<DataSource> dataSources = new ArrayList<>(group.values());
            return loadBalanceStrategy.determineDataSource(key, Collections.unmodifiableList(dataSources));
        }

        // 2. 查询准确的数据源
        DataSource dataSource = dataSources.get(key);
        if (dataSource != null) {
            logger.debug("切换到 {} 单数据源", key);
            return dataSource;
        }
        return null;
    }


    @Override
    public void destroy() throws Exception {
        logger.info("关闭数据源");
        for (Map.Entry<String, DataSource> item : dataSources.entrySet()) {
            String key = item.getKey();
            DataSource dataSource = item.getValue();
            Class<? extends DataSource> clazz = dataSource.getClass();
            if (dataSource instanceof DruidDataSource) {
                logger.info("关闭 Druid 数据源 {} 成功", key);
                ((DruidDataSource) dataSource).close();
            } else if (dataSource instanceof HikariDataSource) {
                logger.info("关闭 HikariDataSource 数据源 {} 成功", key);
                ((HikariDataSource) dataSource).close();
            } else {
                logger.info("关闭 {} 数据源 {} 失败, 暂不支持", clazz.getSimpleName(), key);
            }
        }
    }
}
