package moe.ahao.spring.boot.datasources.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariDataSource;
import moe.ahao.spring.boot.datasources.DataSourceContextHolder;
import moe.ahao.spring.boot.datasources.properties.BalanceDataSourceProperties;
import moe.ahao.spring.boot.datasources.properties.ExDataSourceProperties;
import moe.ahao.spring.boot.datasources.repository.DataSourcePropertiesRepository;
import moe.ahao.spring.boot.datasources.strategy.LoadBalanceStrategy;
import moe.ahao.util.commons.lang.reflect.ReflectHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DynamicDataSource extends AbstractRoutingDataSource implements InitializingBean, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSource.class);

    private BalanceDataSourceProperties configuration;
    private DataSourcePropertiesRepository repository;

    public DynamicDataSource(BalanceDataSourceProperties dataSourceConfiguration, DataSourcePropertiesRepository repository) {
        this.configuration = dataSourceConfiguration;
        this.repository = repository;
    }

    private Map<String, DataSource> dataSources = new ConcurrentHashMap<>();
    private Map<String, Map<String, DataSource>> dataSourceGroup = new ConcurrentHashMap<>();
    private LoadBalanceStrategy loadBalanceStrategy;

    @Override
    public void afterPropertiesSet() {
        // 1. 保证数据源属性不为空
        Map<String, ExDataSourceProperties> propertiesMap = repository.getDataSourceProperties();
        if (propertiesMap.size() <= 0) {
            logger.error("请确保至少有一个数据源");
            throw new IllegalArgumentException("请确保至少有一个数据源");
        }

        // 2. 初始化数据源
        propertiesMap.forEach(this::setDataSource);

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

        String errorMsg = "找不到" + key + "数据源";
        logger.error(errorMsg);
        throw new IllegalArgumentException(errorMsg);
    }

    public void setDataSource(String key, ExDataSourceProperties properties) {
        // 1. 参数校验
        if (StringUtils.isBlank(key) || properties == null) {
            String message = "数据源 key:" + key + " 配置错误, 请 debug 调试!";
            logger.warn(message);
            throw new IllegalArgumentException(message);
        }

        // 2. 初始化基本信息
        DataSource dataSource = DataSourceBuilder.create()
            .type(properties.getType())
            .url(properties.getUrl())
            .username(properties.getUsername())
            .password(properties.getPassword())
            .driverClassName(properties.getDriverClassName())
            .build();

        // 3. 初始化定制信息
        Map<String, Object> externalProperties = properties.getExternalProperties();
        externalProperties.forEach((k, v) -> ReflectHelper.setValue(dataSource, k, v));

        // 4. 数据源加入
        dataSources.put(key, dataSource);

        // 5. 数据源分组
        String split = configuration.getGroupBy();
        String groupName = StringUtils.contains(key, split) ? StringUtils.substringBefore(key, split) : key;
        Map<String, DataSource> groupMap = dataSourceGroup.get(groupName);
        if(groupMap == null) {
            groupMap = new ConcurrentHashMap<>();
        }
        groupMap.put(key, dataSource);
        dataSourceGroup.put(groupName, groupMap);

        logger.debug("数据源 key:{} 添加成功", key);
    }

    public DataSource removeDataSource(String key){
        // 1. 参数校验
        if (StringUtils.isBlank(key)) {
            String message = "数据源 key:" + key + " 删除失败, 请 debug 调试!";
            logger.warn(message);
            throw new IllegalArgumentException(message);
        }

        // 4. 数据源加入
        DataSource dataSource = dataSources.remove(key);

        // 5. 数据源分组
        String split = configuration.getGroupBy();
        String groupName = StringUtils.contains(key, split) ? StringUtils.substringBefore(key, split) : key;
        Map<String, DataSource> groupMap = dataSourceGroup.get(groupName);
        if(groupMap == null) {
            groupMap = new ConcurrentHashMap<>();
        }
        groupMap.remove(key, dataSource);
        dataSourceGroup.put(groupName, groupMap);

        logger.debug("数据源 key:{} 删除成功", key);
        return dataSource;
    }

    private DataSource findGroupOrOne(String key) {
        if(StringUtils.isBlank(key)) {
            return null; // fix NPE
        }

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
