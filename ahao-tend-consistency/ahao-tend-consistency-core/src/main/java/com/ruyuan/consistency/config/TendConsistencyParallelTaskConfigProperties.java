package com.ruyuan.consistency.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 控制同时可以有几个线程进行任务的执行的配置类
 *
 * @author zhonghuashishan
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "ruyuan.tend.consistency.parallel.pool")
public class TendConsistencyParallelTaskConfigProperties {

    /**
     * 调度型任务线程池的核心线程数
     */
    public Integer threadCorePoolSize = 5;
    /**
     * 调度型任务线程池的最大线程数
     */
    public Integer threadMaxPoolSize = 5;
    /**
     * 调度型任务线程池的队列大小
     */
    public Integer threadPoolQueueSize = 100;
    /**
     * 线程池中无任务时线程存活时间
     */
    public Long threadPoolKeepAliveTime = 60L;
    /**
     * 可选值:[SECONDS,MINUTES,HOURS,DAYS,NANOSECONDS,MICROSECONDS,MILLISECONDS] 线程池中无任务时线程存活时间单位
     */
    public String threadPoolKeepAliveTimeUnit = "SECONDS";
    /**
     * 这里要配置类型全路径且类要实现com.ruyuan.consistency.custom.query.TaskTimeRangeQuery接口 如：com.xxx.TaskTimeLineQuery
     */
    private String taskScheduleTimeRangeClassName = "";

}
