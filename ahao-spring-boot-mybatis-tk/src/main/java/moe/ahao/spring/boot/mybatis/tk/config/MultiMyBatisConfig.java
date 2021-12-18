package moe.ahao.spring.boot.mybatis.tk.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.StringUtils;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;

@Configuration(proxyBeanMethods = false)
public class MultiMyBatisConfig {
    @Configuration
    @MapperScan(value = "moe.ahao.spring.boot.**.mapper", sqlSessionFactoryRef = SpringMybatisConfig.SQL_SESSION_FACTORY_REF)
    public static class SpringMybatisConfig {
        public static final String SQL_SESSION_FACTORY_REF = "springSqlSessionFactory";

        @Bean
        @Primary // DataSourceInitializerInvoker会报错
        @ConfigurationProperties("spring.datasource")
        public DataSourceProperties springDatasourceProperties() {
            return new DataSourceProperties();
        }

        @Bean
        public DataSource springDatasource() {
            DataSourceProperties prop = springDatasourceProperties();
            HikariDataSource dataSource = prop.initializeDataSourceBuilder().type(HikariDataSource.class).build();
            if (StringUtils.hasText(prop.getName())) {
                dataSource.setPoolName(prop.getName());
            }
            return dataSource;
        }

        @Bean(name = SQL_SESSION_FACTORY_REF)
        public SqlSessionFactory springSqlSessionFactory() throws Exception {
            SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
            factory.setDataSource(springDatasource());
            return factory.getObject();
        }
    }
}
