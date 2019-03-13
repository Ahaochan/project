package com.ahao.jdbc.datasource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {
    @Value("${ahao.datasource.type}")
    private Class<? extends DataSource> dataSourceType;

    @Bean(name = "masterDataSource")
    @ConfigurationProperties(prefix = "ahao.datasource.master")
    public DataSource masterDateSource() {
        return DataSourceBuilder.create().type(dataSourceType).build();
    }

    @Bean(name = "slave1DataSource")
    @ConfigurationProperties(prefix = "ahao.datasource.slave1")
    public DataSource slave1DateSource() {
        return DataSourceBuilder.create().type(dataSourceType).build();
    }

    @Bean(name = "slave2DataSource")
    @ConfigurationProperties(prefix = "ahao.datasource.slave2")
    public DataSource slave2DateSource() {
        return DataSourceBuilder.create().type(dataSourceType).build();
    }
}
