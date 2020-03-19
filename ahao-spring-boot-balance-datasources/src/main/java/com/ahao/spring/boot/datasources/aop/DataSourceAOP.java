package com.ahao.spring.boot.datasources.aop;

import com.ahao.spring.boot.datasources.DataSourceContextHolder;
import com.ahao.spring.boot.datasources.annotation.DataSource;
import com.ahao.spring.boot.datasources.annotation.MasterDataSource;
import com.ahao.spring.boot.datasources.annotation.SlaveDataSource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.PriorityOrdered;

@Aspect
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
public class DataSourceAOP implements PriorityOrdered {
    private static Logger logger = LoggerFactory.getLogger(DataSourceAOP.class);

    @Before("@annotation(annotation)")
    public void common(DataSource annotation) {
        logger.trace("当前数据源切换到 {} 数据库", annotation.value());
        DataSourceContextHolder.set(annotation.value());
    }

    @Before("@annotation(annotation)")
    public void master(MasterDataSource annotation) {
        logger.trace("当前数据源切换到 Master 数据库");
        DataSourceContextHolder.set("master");
    }

    @Before("@annotation(annotation)")
    public void slave(SlaveDataSource annotation) {
        logger.trace("当前数据源切换到 Slave 数据库");
        DataSourceContextHolder.set("slave");
    }

    @After("(@annotation(com.ahao.spring.boot.datasources.annotation.DataSource)||" +
        "@annotation(com.ahao.spring.boot.datasources.annotation.MasterDataSource)||" +
        "@annotation(com.ahao.spring.boot.datasources.annotation.SlaveDataSource))")
    public void after(JoinPoint point) {
        DataSourceContextHolder.clear();
    }

    @Override
    public int getOrder() {
        return 0; // 保证在 AOP 最外层
    }
}
