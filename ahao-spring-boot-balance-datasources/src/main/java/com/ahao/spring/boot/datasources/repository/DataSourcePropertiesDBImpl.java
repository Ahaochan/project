package com.ahao.spring.boot.datasources.repository;

import com.ahao.spring.boot.datasources.properties.BalanceDataSourceProperties;
import com.ahao.spring.boot.datasources.properties.ExDataSourceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public abstract class DataSourcePropertiesDBImpl implements DataSourcePropertiesRepository, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(DataSourcePropertiesDBImpl.class);

    private String applicationName = "";
    private BalanceDataSourceProperties tenantDataSourceProperties;
    private ExDataSourceProperties configDataSourceProperties;

    public DataSourcePropertiesDBImpl(BalanceDataSourceProperties balanceDataSourceProperties, ExDataSourceProperties configDataSourceProperties) {
        this.tenantDataSourceProperties = balanceDataSourceProperties;
        this.configDataSourceProperties = configDataSourceProperties;
    }

    @Override
    public Map<String, ExDataSourceProperties> getDataSourceProperties() {
        // 1. 从配置文件初始化配置数据库的数据源
        String url = configDataSourceProperties.getUrl();
        String username = configDataSourceProperties.getUsername();
        String password = configDataSourceProperties.getPassword();
        String sql = this.querySQL();

        // 2. 使用原生JDBC连接, 解析数据
        Map<String, ExDataSourceProperties> map = new HashMap<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);) {
            try (ResultSet rs = statement.executeQuery();) {
                int i = 0;
                while (rs.next()) {
                    String key = this.initKey(rs, i);
                    ExDataSourceProperties properties = this.initProperties(rs, i);
                    map.put(key, properties);
                    i++;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("初始化数据源错误", e);
        }

        // 3. 配置库也加入结果集,标识主库是什么库（租户库，support库，或其他库）
        String primary = tenantDataSourceProperties.getPrimary();
        map.put(primary, configDataSourceProperties);
        logger.debug("初始化配置库:{}", primary);
        return map;
    }

    protected abstract String initKey(ResultSet rs, int index) throws SQLException;
    protected abstract ExDataSourceProperties initProperties(ResultSet rs, int index) throws SQLException;
    protected abstract String querySQL();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationName = applicationContext.getEnvironment().getProperty("spring.application.name");
        Assert.hasLength(this.applicationName, "applicationName:" + applicationName + " 不能为空");
    }
}
