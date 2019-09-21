package moe.ahao.spring.boot.elasticjob.config;

import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import moe.ahao.spring.boot.elasticjob.properties.DataFlowJobProperties;
import moe.ahao.spring.boot.elasticjob.properties.ScriptJobProperties;
import moe.ahao.spring.boot.elasticjob.properties.SimpleJobProperties;
import moe.ahao.spring.boot.elasticjob.properties.base.DefaultJobProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConditionalOnProperty(value = "elastic-job.enabled", havingValue = "true")
public class ElasticJobConfig {

    @Bean(initMethod = "init", destroyMethod = "close")
    public ZookeeperRegistryCenter zookeeperRegistryCenter(ZookeeperConfiguration zkConfig) {
        return new ZookeeperRegistryCenter(zkConfig);
    }

    @Bean
    @ConfigurationProperties("elastic-job.zookeeper")
    public ZookeeperConfigurationFactoryBean zookeeperConfiguration() {
        return new ZookeeperConfigurationFactoryBean();
    }

    @Bean
    public ElasticJobRegister elasticJobRegister(ZookeeperRegistryCenter zk, AllProperties properties) {
        return new ElasticJobRegister(zk, properties);
    }

    @Configuration
    @ConfigurationProperties("elastic-job")
    public static class AllProperties {
        private DefaultJobProperties base;
        private List<SimpleJobProperties> simple = new ArrayList<>();
        private List<DataFlowJobProperties> dataFlow = new ArrayList<>();
        private List<ScriptJobProperties> script = new ArrayList<>();

        public DefaultJobProperties getBase() {
            return base;
        }

        public void setBase(DefaultJobProperties base) {
            this.base = base;
        }

        public List<SimpleJobProperties> getSimple() {
            return simple;
        }

        public void setSimple(List<SimpleJobProperties> simple) {
            this.simple = simple;
        }

        public List<DataFlowJobProperties> getDataFlow() {
            return dataFlow;
        }

        public void setDataFlow(List<DataFlowJobProperties> dataFlow) {
            this.dataFlow = dataFlow;
        }

        public List<ScriptJobProperties> getScript() {
            return script;
        }

        public void setScript(List<ScriptJobProperties> script) {
            this.script = script;
        }
    }
}
