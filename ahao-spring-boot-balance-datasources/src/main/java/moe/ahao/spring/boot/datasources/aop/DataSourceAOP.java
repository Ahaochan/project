package moe.ahao.spring.boot.datasources.aop;

import moe.ahao.spring.boot.datasources.DataSourceContextHolder;
import moe.ahao.spring.boot.datasources.annotation.DataSource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.PriorityOrdered;

/**
 * 数据源切换AOP, 拦截 {@link DataSource} 注解
 * 使用方法, 将 {@link DataSourceAOP} 注册为 Bean 即可
 */
@Aspect
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
public class DataSourceAOP implements PriorityOrdered {
    private static Logger logger = LoggerFactory.getLogger(DataSourceAOP.class);

    @Around("@annotation(annotation)")
    public Object doAround(ProceedingJoinPoint pjp, DataSource annotation) throws Throwable {
        String oldContext = DataSourceContextHolder.get();
        String newContext = annotation.value();
        logger.trace("当前数据源 {} 切换到 {} 数据源", oldContext, newContext);
        DataSourceContextHolder.set(newContext);
        try {
            return pjp.proceed();
        } finally {
            DataSourceContextHolder.set(oldContext);
        }
    }

    @Override
    public int getOrder() {
        return 0; // 保证在 AOP 最外层
    }
}
