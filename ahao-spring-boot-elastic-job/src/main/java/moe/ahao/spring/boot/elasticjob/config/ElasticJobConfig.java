package moe.ahao.spring.boot.elasticjob.config;

import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import moe.ahao.spring.boot.elasticjob.properties.base.ElasticAllJobProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "elastic-job.enabled", havingValue = "true")
public class ElasticJobConfig {

    @Bean(initMethod = "init", destroyMethod = "close")
    public ZookeeperRegistryCenter zookeeperRegistryCenter(ZookeeperConfiguration zkConfig) {
        return new ZookeeperRegistryCenter(zkConfig);
    }

    @Bean
    @ConfigurationProperties(prefix = "elastic-job.zookeeper")
    public ZookeeperConfigurationFactoryBean zookeeperConfiguration() {
        return new ZookeeperConfigurationFactoryBean();
    }

    @Bean
    public ElasticJobRegister elasticJobRegister(ZookeeperRegistryCenter zk, ElasticAllJobProperties properties) {
        return new ElasticJobRegister(zk, properties);
    }
}
