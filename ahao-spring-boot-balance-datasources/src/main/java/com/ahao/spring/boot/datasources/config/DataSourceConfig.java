package com.ahao.spring.boot.datasources.config;

import com.ahao.spring.boot.datasources.aop.DataSourceAOP;
import com.ahao.spring.boot.datasources.datasource.DynamicDataSource;
import com.ahao.spring.boot.datasources.repository.DataSourcePropertiesMemoryImpl;
import com.ahao.spring.boot.datasources.repository.DataSourcePropertiesRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSourceAOP dataSourceAOP() {
        return new DataSourceAOP();
    }

    @Bean
    public BalanceDataSourceProperties balanceDataSourceProperties() {
        return new BalanceDataSourceProperties();
    }

    @Bean
    public DataSourcePropertiesRepository dataSourcePropertiesRepository() {
        return new DataSourcePropertiesMemoryImpl();
    }

    @Bean
    public DataSource dynamicDataSource(BalanceDataSourceProperties properties, DataSourcePropertiesRepository repository) {
        return new DynamicDataSource(properties, repository);
    }
}
