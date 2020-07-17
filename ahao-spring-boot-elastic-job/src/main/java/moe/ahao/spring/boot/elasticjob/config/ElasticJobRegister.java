package moe.ahao.spring.boot.elasticjob.config;

import com.ahao.util.commons.lang.reflect.ClassHelper;
import com.ahao.util.spring.SpringContextHolder;
import com.dangdang.ddframe.job.api.ElasticJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import moe.ahao.spring.boot.elasticjob.job.capable.ElasticJobListenerCapable;
import moe.ahao.spring.boot.elasticjob.job.capable.JobEventTraceDataSourceCapable;
import moe.ahao.spring.boot.elasticjob.properties.base.BaseJobProperties;
import moe.ahao.spring.boot.elasticjob.properties.base.DefaultJobProperties;
import moe.ahao.spring.boot.elasticjob.properties.base.ElasticAllJobProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ElasticJobRegister implements InitializingBean, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(ElasticJobRegister.class);

    private ZookeeperRegistryCenter zk;
    private ApplicationContext ctx;
    private ElasticAllJobProperties jobConfig;

    public ElasticJobRegister(ZookeeperRegistryCenter zk, ElasticAllJobProperties jobConfig) {
        this.zk = zk;
        this.jobConfig = jobConfig;
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        this.ctx = ctx;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        DefaultJobProperties defaultProperties = jobConfig.getBase();

        jobConfig.getSimple().forEach(p -> init(defaultProperties, p));
        jobConfig.getDataFlow().forEach(p -> init(defaultProperties, p));
        jobConfig.getScript().forEach(p -> init(defaultProperties, p));
    }

    private void init(DefaultJobProperties defaultProperties, BaseJobProperties jobProperties) {
        String beanName = jobProperties.getBeanName();
        ElasticJob elasticJob = StringUtils.isEmpty(beanName) ? null : SpringContextHolder.getBean(jobProperties.getBeanName());
        Class<?> clazz = elasticJob == null ? null : elasticJob.getClass();

        JobCoreConfiguration coreConfig = jobProperties.generateJobCoreConfig(defaultProperties);
        JobTypeConfiguration typeConfig = jobProperties.generateJobTypeConfig(coreConfig, clazz);
        LiteJobConfiguration liteConfig = jobProperties.generateLiteJobConfig(defaultProperties, typeConfig);

        registerSpringJobScheduler(defaultProperties, jobProperties, liteConfig, elasticJob);
    }

    private void registerSpringJobScheduler(DefaultJobProperties defaultProperties, BaseJobProperties jobProperties,
                     LiteJobConfiguration liteConfig, ElasticJob job) {
        // 1. 创建 SpringJobScheduler
        BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(SpringJobScheduler.class);
        factory.setScope(BeanDefinition.SCOPE_PROTOTYPE);

        // 2. 传入构造器所需参数
        factory.addConstructorArgValue(job);
        factory.addConstructorArgValue(zk);
        factory.addConstructorArgValue(liteConfig);
        if (defaultProperties.isJobEventTraceEnabled() || Objects.equals(jobProperties.getJobEventTraceEnabled(), true)) {
            if(!ClassHelper.isSubClass(job, JobEventTraceDataSourceCapable.class)) {
                throw new ClassCastException(job.getClass().getName() + "未实现 JobEventTraceDataSourceCapable 接口");
            }
            JobEventTraceDataSourceCapable capable = (JobEventTraceDataSourceCapable) job;
            DataSource dataSource = capable.getJobEventTraceDataSource();
            if(dataSource == null) {
                throw new IllegalArgumentException("事件追踪数据源为空! http://elasticjob.io/docs/elastic-job-lite/02-guide/event-trace/");
            }
            BeanDefinitionBuilder rdbFactory = BeanDefinitionBuilder.rootBeanDefinition(JobEventRdbConfiguration.class);
            rdbFactory.addConstructorArgValue(dataSource);
            factory.addConstructorArgValue(rdbFactory.getBeanDefinition());
        }
        if(ClassHelper.isSubClass(job, ElasticJobListenerCapable.class)) {
            ElasticJobListenerCapable capable = (ElasticJobListenerCapable) job;
            List<ElasticJobListener> listeners = capable.getElasticJobListeners();
            factory.addConstructorArgValue(listeners);
        } else {
            factory.addConstructorArgValue(Collections.emptyList());
        }

        // 3. 注册到 Spring Application Context
        String jobName = jobProperties.getJobName();
        String beanName = jobName + "SpringJobScheduler";
        this.registerBean(beanName, factory.getBeanDefinition());

        // 4. 启动定时任务
        SpringJobScheduler springJobScheduler = SpringContextHolder.getBean(beanName);
        springJobScheduler.init();
    }

    private void registerBean(String beanName, AbstractBeanDefinition beanDefinition) {
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) ctx.getAutowireCapableBeanFactory();

        if(StringUtils.isBlank(beanName)) {
            BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinition, registry);
        } else {
            registry.registerBeanDefinition(beanName, beanDefinition);
        }
    }
}
