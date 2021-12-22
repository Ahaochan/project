package moe.ahao.spring.boot.atomikos;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.mysql.cj.jdbc.MysqlXADataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.boot.jta.atomikos.AtomikosXADataSourceWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.transaction.UserTransaction;

@Configuration(proxyBeanMethods = false)
public class AtomikosConfig {
    public static final String TX_MANAGER = "tx_manager";

    @Bean(TX_MANAGER)
    @Primary
    // transactionManager属性要和这里的bean名称保持一致
    // @Transactional(transactionManager = TX_MANAGER, rollbackFor = Exception.class)
    public JtaTransactionManager transactionManager() {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        UserTransaction userTransaction = new UserTransactionImp();
        return new JtaTransactionManager(userTransaction, userTransactionManager);
    }

    @Configuration
    public static class SpringDatasourceConfig {
        @Bean
        @Primary // DataSourceInitializerInvoker会报错
        @ConfigurationProperties("spring.datasource")
        public DataSourceProperties springDatasourceProperties() {
            return new DataSourceProperties();
        }

        @Bean
        public AtomikosDataSourceBean springDataSource() throws Exception {
            MysqlXADataSource dataSource = springDatasourceProperties().initializeDataSourceBuilder()
                .type(MysqlXADataSource.class).build();
            return new AtomikosXADataSourceWrapper().wrapDataSource(dataSource);
        }
    }
}
