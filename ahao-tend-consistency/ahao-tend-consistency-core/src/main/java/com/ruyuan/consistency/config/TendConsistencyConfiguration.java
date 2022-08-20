package com.ruyuan.consistency.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 框架级配置参数
 *
 * @author zhonghuashishan
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TendConsistencyConfiguration {

    /**
     * 调度型任务线程池的核心线程数
     */
    public Integer threadCorePoolSize;
    /**
     * 调度型任务线程池的最大线程数
     */
    public Integer threadMaxPoolSize;
    /**
     * 调度型任务线程池的队列大小
     */
    public Integer threadPoolQueueSize;
    /**
     * 线程池中无任务时线程存活时间
     */
    public Long threadPoolKeepAliveTime;
    /**
     * 可选值:[SECONDS,MINUTES,HOURS,DAYS,NANOSECONDS,MICROSECONDS,MILLISECONDS] 线程池中无任务时线程存活时间单位
     */
    public String threadPoolKeepAliveTimeUnit;
    /**
     * 触发降级逻辑的阈值 任务执行次数 如果大于该值 就会进行降级
     */
    public Integer failCountThreshold;
    /**
     * 任务表是否进行分库
     */
    public Boolean taskSharded = false;
    /**
     * 这里要配置类型全路径且类要实现com.ruyuan.consistency.query.TaskTimeRangeQuery接口 如：com.xxx.TaskTimeLineQuery
     */
    private String taskScheduleTimeRangeClassName = "";
    /**
     * 生成任务表分片key的ClassName 这里要配置类型全路径且类要实现com.ruyuan.consistency.custom.shard.ShardingKeyGenerator接口
     */
    private String shardingKeyGeneratorClassName = "";
    /**
     * 集群节点的配置信息 格式: ip1:port:peerId1,ip2:port:peerId2,ip3:port:peerId3
     */
    public String peersConfig;
    /**
     * 任务分片数
     */
    public Long taskShardingCount;
    /**
     * RocksDB的存储文件夹目录
     */
    public String rocksPath;

    // ------------------------------------------------调度器相关的配置--------------------------------------------------
    /**
     * [单位秒] leader检测follower是否存活的调度器每隔多长时间执行一次检查
     */
    public Integer followerAliveCheckIntervalSeconds = 10;
    /**
     * [单位秒] leader判定follower宕机的阈值
     */
    private Integer judgeFollowerDownSecondsThreshold = 120;
    /**
     * [单位秒] follower用于检测leader是否存活的调度器每隔多长时间执行一次检查
     */
    public Integer leaderAliveCheckIntervalSeconds = 10;
    /**
     * [单位秒] follower判定leader宕机的阈值
     */
    private Integer judgeLeaderDownSecondsThreshold = 120;
    /**
     * [单位秒] leader定时发给follower的心跳的调度器，同时也会将leader对任务的分片信息发送给各个follower节点每隔多长时间执行一次
     */
    public Integer leaderToFollowerHeartbeatIntervalSeconds = 10;
    /**
     * [单位秒] follower对leader发送心跳的调度器
     */
    public Integer followerHeartbeatIntervalSeconds = 10;
    /**
     * [单位秒] 一致性框架自身的/内置的执行任务时的调度器执行任务的频率，每隔多长时间调度一次
     */
    public Integer consistencyTaskExecuteIntervalSeconds = 10;

}
