package com.ruyuan.consistency.config;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.ruyuan.consistency.custom.query.TaskTimeRangeQuery;
import com.ruyuan.consistency.custom.shard.ShardingKeyGenerator;
import com.ruyuan.consistency.exceptions.ConsistencyException;
import com.ruyuan.consistency.utils.ReflectTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.ruyuan.consistency.utils.DefaultValueUtils.getOrDefault;

/**
 * 提供给SpringBoot的自动装配类 SPI使用
 *
 * @author zhonghuashishan
 **/
@Slf4j
@Configuration
@EnableConfigurationProperties(value = {
        TendConsistencyParallelTaskConfigProperties.class,
        TendConsistencyFallbackConfigProperties.class,
        TaskExecuteEngineConfigProperties.class,
        SchedulerConfigProperties.class,
        ShardModeConfigProperties.class,
        PeerNodeConfigProperties.class,
        RocksDBConfigProperties.class
})
public class TendConsistencyAutoConfiguration {

    /**
     * 执行调度任务的线程池的配置
     */
    @Autowired
    private TendConsistencyParallelTaskConfigProperties consistencyParallelTaskConfigProperties;
    /**
     * 降级逻辑相关参数配置
     */
    @Autowired
    private TendConsistencyFallbackConfigProperties tendConsistencyFallbackConfigProperties;
    /**
     * 数据库分片模式参数配置
     */
    @Autowired
    private ShardModeConfigProperties shardModeConfigProperties;
    /**
     * 一致性任务节点的配置信息
     */
    @Autowired
    private PeerNodeConfigProperties peerNodeConfigProperties;
    /**
     * 执行引擎相关参数
     */
    @Autowired
    private TaskExecuteEngineConfigProperties taskExecuteEngineConfigProperties;
    /**
     * RocksDB的配置信息
     */
    @Autowired
    private RocksDBConfigProperties rocksDBConfigProperties;
    /**
     * 调度器相关的配置
     */
    @Autowired
    private SchedulerConfigProperties schedulerConfigProperties;

    /**
     *
     * 框架级配置
     * spring boot自动装配，就是说去收集各种各样的参数，把参数注入到properties里去
     * 把各个properties里面的配置和信息，注入到 configuration里面去
     * 拿到一个完整的框架的配置，后续你的各种组件如果需要使用到框架的配置，就可以直接用这个configuration就可以了
     *
     * @return 配置bean
     */
    @Bean
    public TendConsistencyConfiguration tendConsistencyConfigService() {
        // 对配置进行检查
        doConfigCheck(consistencyParallelTaskConfigProperties, shardModeConfigProperties, rocksDBConfigProperties, peerNodeConfigProperties);

        return TendConsistencyConfiguration
                .builder()
                .threadCorePoolSize(getOrDefault(consistencyParallelTaskConfigProperties.getThreadCorePoolSize(), 5))
                .threadMaxPoolSize(getOrDefault(consistencyParallelTaskConfigProperties.getThreadMaxPoolSize(), 5))
                .threadPoolQueueSize(getOrDefault(consistencyParallelTaskConfigProperties.getThreadPoolQueueSize(), 100))
                .threadPoolKeepAliveTime(getOrDefault(consistencyParallelTaskConfigProperties.getThreadPoolKeepAliveTime(), 60L))
                .threadPoolKeepAliveTimeUnit(getOrDefault(consistencyParallelTaskConfigProperties.getThreadPoolKeepAliveTimeUnit(), "SECONDS"))
                .taskScheduleTimeRangeClassName(getOrDefault(consistencyParallelTaskConfigProperties.getTaskScheduleTimeRangeClassName(), ""))
                .failCountThreshold(getOrDefault(tendConsistencyFallbackConfigProperties.getFailCountThreshold(), 2))
                .taskSharded(getOrDefault(shardModeConfigProperties.getTaskSharded(), false))
                .shardingKeyGeneratorClassName(getOrDefault(shardModeConfigProperties.getShardingKeyGeneratorClassName(), ""))
                .peersConfig(peerNodeConfigProperties.getPeersConfig())
                .taskShardingCount(getTaskShardingCountOrDefault(taskExecuteEngineConfigProperties))
                .rocksPath(getOrCreate(rocksDBConfigProperties.rocksPath))
                .consistencyTaskExecuteIntervalSeconds(getOrDefault(schedulerConfigProperties.getConsistencyTaskExecuteIntervalSeconds(), 10))
                .followerAliveCheckIntervalSeconds(getOrDefault(schedulerConfigProperties.getFollowerAliveCheckIntervalSeconds(), 10))
                .followerHeartbeatIntervalSeconds(getOrDefault(schedulerConfigProperties.getFollowerHeartbeatIntervalSeconds(), 10))
                .leaderAliveCheckIntervalSeconds(getOrDefault(schedulerConfigProperties.getLeaderAliveCheckIntervalSeconds(), 10))
                .leaderToFollowerHeartbeatIntervalSeconds(getOrDefault(schedulerConfigProperties.getLeaderToFollowerHeartbeatIntervalSeconds(), 10))
                .judgeFollowerDownSecondsThreshold(getOrDefault(schedulerConfigProperties.getJudgeFollowerDownSecondsThreshold(), 120))
                .judgeLeaderDownSecondsThreshold(getOrDefault(schedulerConfigProperties.getJudgeLeaderDownSecondsThreshold(), 120))
                .build();
    }

    private String getOrCreate(String rocksPath) {
        final File dir = new File(rocksPath);
        // 如果指定的路径是不是文件夹，而是文件
        if (!dir.isDirectory()) {
            throw new IllegalStateException("RocksDB初始化失败，请指定文件夹而非文件: " + rocksPath);
        }
        // 如果指定的RocksDB的存储目录不存在,则进行创建
        if (!dir.exists()) {
            boolean mkdirsResult = dir.mkdirs();
            if (!mkdirsResult) {
                throw new IllegalStateException("RocksDB初始化失败，创建RocksDB存储文件夹时失败: " + rocksPath);
            }
        }
        return rocksPath;
    }

    /**
     * 配置检查
     * @param consistencyParallelTaskConfigProperties 并行任务相关的配置
     * @param shardModeConfigProperties               分片模式相关配置
     * @param rocksDBConfigProperties                 rocksDB属性配置
     * @param peerNodeConfigProperties                集群节点配置的属性
     */
    private void doConfigCheck(TendConsistencyParallelTaskConfigProperties consistencyParallelTaskConfigProperties,
                               ShardModeConfigProperties shardModeConfigProperties, RocksDBConfigProperties rocksDBConfigProperties,
                               PeerNodeConfigProperties peerNodeConfigProperties) {
        TimeUnit timeUnit = null;
        if (!StringUtils.isEmpty(consistencyParallelTaskConfigProperties.getThreadPoolKeepAliveTimeUnit())) {
            try {
                timeUnit = TimeUnit.valueOf(consistencyParallelTaskConfigProperties.getThreadPoolKeepAliveTimeUnit());
            } catch (IllegalArgumentException e) {
                log.error("检查threadPoolKeepAliveTimeUnit配置时，发生异常", e);
                String errMsg = "threadPoolKeepAliveTimeUnit配置错误！注意：请在[SECONDS,MINUTES,HOURS,DAYS,NANOSECONDS,MICROSECONDS,MILLISECONDS]任选其中之一";
                throw new ConsistencyException(errMsg);
            }
        }

        if (!StringUtils.isEmpty(consistencyParallelTaskConfigProperties.getTaskScheduleTimeRangeClassName())) {
            // 校验是否存在该类
            Class<?> taskScheduleTimeRangeClass = ReflectTools.checkClassByName(consistencyParallelTaskConfigProperties.getTaskScheduleTimeRangeClassName());
            if (ObjectUtils.isEmpty(taskScheduleTimeRangeClass)) {
                String errMsg = String.format("未找到 %s 类，请检查类路径是否正确", consistencyParallelTaskConfigProperties.getTaskScheduleTimeRangeClassName());
                throw new ConsistencyException(errMsg);
            }
            // 用户自定义校验：校验是否实现了TaskTimeRangeQuery接口
            boolean result = ReflectTools.isRealizeTargetInterface(taskScheduleTimeRangeClass,
                    TaskTimeRangeQuery.class.getName());
            if (!result) {
                String errMsg = String.format("%s 类，未实现TaskTimeRangeQuery接口", consistencyParallelTaskConfigProperties.getTaskScheduleTimeRangeClassName());
                throw new ConsistencyException(errMsg);
            }
        }

        if (!StringUtils.isEmpty(shardModeConfigProperties.getShardingKeyGeneratorClassName())) {
            // 校验是否存在该类
            Class<?> shardingKeyGeneratorClass = ReflectTools.checkClassByName(shardModeConfigProperties.getShardingKeyGeneratorClassName());
            if (ObjectUtils.isEmpty(shardingKeyGeneratorClass)) {
                String errMsg = String.format("未找到 %s 类，请检查类路径是否正确", shardModeConfigProperties.getShardingKeyGeneratorClassName());
                throw new ConsistencyException(errMsg);
            }
            // 用户自定义校验：校验是否实现了ShardingKeyGenerator接口
            boolean result = ReflectTools.isRealizeTargetInterface(shardingKeyGeneratorClass,
                    ShardingKeyGenerator.class.getName());
            if (!result) {
                String errMsg = String.format("%s 类，未实现ShardingKeyGenerator接口", shardModeConfigProperties.getShardingKeyGeneratorClassName());
                throw new ConsistencyException(errMsg);
            }
        }

        if (StringUtils.isEmpty(rocksDBConfigProperties.rocksPath)) {
            throw new ConsistencyException("请指定RocksDB文件存储的路径，配置文件中的配置项为：ruyuan.tend.consistency.rocksdb.rocks-path");
        }

        final File dir = new File(rocksDBConfigProperties.rocksPath);
        // 如果指定的路径是不是文件夹，而是文件
        if (!dir.isDirectory()) {
            throw new ConsistencyException("检查RocksDB存储路径时，发现您指定的不是文件夹而是文件: " + rocksDBConfigProperties.rocksPath);
        }
        // 如果指定的RocksDB的存储目录不存在,则进行创建
        if (!dir.exists()) {
            boolean mkdirsResult = dir.mkdirs();
            if (!mkdirsResult) {
                throw new ConsistencyException("初始化RocksDB文件存储路径时，发生异常，请检查您配置的路径是否有访问权限或其他问题: " + rocksDBConfigProperties.rocksPath);
            }
        }

        if (StringUtils.isEmpty(peerNodeConfigProperties.getPeersConfig())) {
            throw new ConsistencyException("未配置集群节点的配置信息。格式: ip1:port:id1,ip2:port:id2,ip3:port:id3，配置项为：" +
                    "ruyuan.tend.consistency.peers.peers-config");
        }

        // 如果集群节点配置了，但是集群节点id中有重复项
        if (peersConfigIsDuplicate()) {
            throw new ConsistencyException("检查集群节点的配置信息时，发现有重复项，请仔细核对，不允许有重复的节点信息，您配置的" +
                    "内容为" + peerNodeConfigProperties.getPeersConfig());
        }

        if (peerIpAndPortIsDuplicate()) {
            throw new ConsistencyException("检查集群节点的配置信息时，发现有重复的[ip:port]，请仔细核对，不允许有重复的节点信息，您配置的" +
                    "内容为" + peerNodeConfigProperties.getPeersConfig());
        }

        // 检查是否有
        if (peerIdIsDuplicate()) {
            throw new ConsistencyException("检查集群节点的配置信息时，发现[节点id]中重复项，请仔细核对，不允许有重复的节点信息，您配置的" +
                    "内容为" + peerNodeConfigProperties.getPeersConfig());
        }

    }

    /**
     * 获取执行引擎分片数
     * @param properties 执行引擎配置
     * @return 任务执行时，要分片的数量
     */
    private Long getTaskShardingCountOrDefault(TaskExecuteEngineConfigProperties properties) {
        if (!ObjectUtil.isEmpty(properties.getTaskShardingCount())) {
            return properties.getTaskShardingCount();
        } else if (!StringUtils.isEmpty(peerNodeConfigProperties.getPeersConfig())) {
            return (long) peerNodeConfigProperties.getPeersConfig().split(",").length;
        } else {
            // 如果任务分片数没有设置 且 一致性框架的集群节点也没有设置 那么就将分片数设置为1
            return 1L;
        }
    }

    /**
     * 检查集群节点的配置是否有重复
     * @return 是否有重复
     */
    private boolean peersConfigIsDuplicate() {
        // 解析为list格式
        List<String> peersConfigList = parsePeersConfigToList(peerNodeConfigProperties.getPeersConfig());
        // 去重
        List<String> distinctPeersConfigList = peersConfigList.stream().distinct().collect(Collectors.toList());

        return peersConfigList.size() != distinctPeersConfigList.size();
    }

    /**
     * 解析为list格式
     * @param peersConfig 解析为list格式
     * @return 解析为list格式
     */
    private List<String> parsePeersConfigToList(String peersConfig) {
        String[] splitPeers = peersConfig.split(",");
        List<String> peersConfigList = new ArrayList<>(splitPeers.length);
        peersConfigList.addAll(Arrays.asList(splitPeers));
        return peersConfigList;
    }

    /**
     * 检查集群节点的配置中是否有ip:port的重复项
     * @return 是否有ip:port的重复项
     */
    private boolean peerIpAndPortIsDuplicate() {
        List<String> peersConfigList = parsePeersConfigToList(peerNodeConfigProperties.getPeersConfig());

        List<String> peerIpAndPortList = new ArrayList<>(peersConfigList.size());
        for (String peer : peersConfigList) {
            String peerIpAndPort = peer.substring(0, peer.lastIndexOf(":"));
            peerIpAndPortList.add(peerIpAndPort);
        }

        // 去重
        List<String> distinctPeerIpAndPortList = peerIpAndPortList.stream().distinct().collect(Collectors.toList());

        return peerIpAndPortList.size() != distinctPeerIpAndPortList.size();
    }

    /**
     * 检查集群节点的id是否有重复
     * @return 集群节点id是否有重复
     */
    private boolean peerIdIsDuplicate() {
        // 解析为list格式
        List<String> peersConfigList = parsePeersConfigToList(peerNodeConfigProperties.getPeersConfig());

        List<Integer> peerIdList = new ArrayList<>(peersConfigList.size());
        for (String peer : peersConfigList) {
            String peerId = peer.substring(peer.lastIndexOf(":") + 1);
            if (!NumberUtil.isNumber(peerId)) {
                throw new ConsistencyException("检查集群节点的配置信息时，发现[节点id]中存在非数字项，请仔细核对，不允许有重复的节点信息，您配置的" +
                        "内容为" + peerNodeConfigProperties.getPeersConfig());
            }
            peerIdList.add(Integer.parseInt(peerId));
        }

        // 去重
        List<Integer> distinctPeerIdList = peerIdList.stream().distinct().collect(Collectors.toList());

        return peerIdList.size() != distinctPeerIdList.size();
    }

}
