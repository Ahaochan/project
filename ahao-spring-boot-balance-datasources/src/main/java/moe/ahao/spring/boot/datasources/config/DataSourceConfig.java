package moe.ahao.spring.boot.datasources.config;

import moe.ahao.spring.boot.datasources.datasource.DynamicDataSource;
import moe.ahao.spring.boot.datasources.properties.BalanceDataSourceProperties;
import moe.ahao.spring.boot.datasources.repository.DataSourcePropertiesRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
@ConditionalOnProperty(prefix = "spring.datasource.balance", value = "primary")
public class DataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.balance")
    @ConditionalOnMissingBean
    public BalanceDataSourceProperties balanceDataSourceProperties() {
        return new BalanceDataSourceProperties();
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public DataSource dynamicDataSource(BalanceDataSourceProperties properties, DataSourcePropertiesRepository repository) {
        return new DynamicDataSource(properties, repository);
    }

    @Bean
    @ConditionalOnMissingBean
    public PlatformTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
