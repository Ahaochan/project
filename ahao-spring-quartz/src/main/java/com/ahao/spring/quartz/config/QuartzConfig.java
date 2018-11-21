package com.ahao.spring.quartz.config;

import com.ahao.spring.beans.AutowiringSpringBeanJobFactory;
import com.ahao.spring.quartz.config.properties.QuartzProperties;
import com.ahao.spring.util.SpringContextHolder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.text.ParseException;
import java.util.Properties;

@Configuration
@DependsOn("com.ahao.spring.util.SpringContextHolder")
@ConditionalOnProperty(name = "spring.quartz.enabled", havingValue = "true")
public class QuartzConfig {
    private static final Logger logger = LoggerFactory.getLogger(QuartzConfig.class);

    @Autowired
    private QuartzProperties quartzProperties;

    @Bean
    @Lazy
    public SchedulerFactoryBean quartzScheduler(ApplicationContext applicationContext) throws Exception {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setSchedulerName("solr-quartz-scheduler");

        // 1. 初始化 quartz.properties 配置
        factory.setQuartzProperties(quartzProperties());
        factory.afterPropertiesSet();

        // 2. 自定义 Job Factroy, 允许使用 @Autowired 进行注入
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        factory.setJobFactory(jobFactory);

        // 3. 配置 触发器
        Trigger[] triggers = quartzProperties.getCronTask().entrySet().stream()
                .peek(e -> logger.debug("创建定时任务{}, cron:{}", e.getKey(), e.getValue()))
                .map(e -> createTrigger(e.getKey(), e.getValue()))
                .toArray(Trigger[]::new);
        factory.setTriggers(triggers);

        return factory;
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        Properties properties = propertiesFactoryBean.getObject();
        return properties;
    }

    private Trigger createTrigger(String beanName, String cronExpression) {
        try {
            // 1. 创建 jobDetail
            MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
            jobDetail.setTargetObject(SpringContextHolder.getBean(beanName));
            jobDetail.setTargetMethod(quartzProperties.getInvokeMethod());
            jobDetail.setConcurrent(false);
            jobDetail.afterPropertiesSet();

            // 2. 创建 cronTrigger, 注入 jobDetail
            CronTriggerFactoryBean cronTrigger = new CronTriggerFactoryBean();
            cronTrigger.setJobDetail(jobDetail.getObject());
            cronTrigger.setCronExpression(cronExpression);
            cronTrigger.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
            cronTrigger.afterPropertiesSet();

            return cronTrigger.getObject();
        } catch (NoSuchMethodException | ParseException | ClassNotFoundException e) {
            logger.error("创建Trigger错误!", e);
        }
        return null;
    }

}
