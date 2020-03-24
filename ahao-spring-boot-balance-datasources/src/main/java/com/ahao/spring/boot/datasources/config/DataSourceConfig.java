package com.ahao.spring.boot.datasources.config;

import com.ahao.spring.boot.datasources.aop.DataSourceAOP;
import com.ahao.spring.boot.datasources.datasource.DynamicDataSource;
import com.ahao.spring.boot.datasources.repository.DataSourcePropertiesRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@ConditionalOnBean(DataSourcePropertiesRepository.class)
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
    @Primary
    public DataSource dynamicDataSource(BalanceDataSourceProperties properties, DataSourcePropertiesRepository repository) {
        return new DynamicDataSource(properties, repository);
    }

    @Bean
    public PlatformTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
