package com.ruyuan.consistency.sharding.strategy;

import com.ruyuan.consistency.custom.shard.SnowflakeShardingKeyGenerator;

import java.util.*;

/**
 * 根据分片数平均分配任务的策略
 */
public final class AverageAllocationConsistencyTaskShardingStrategy implements ConsistencyTaskShardingStrategy {

    private static volatile AverageAllocationConsistencyTaskShardingStrategy instance;

    /**
     * 任务分片.
     *
     * @param taskInstances      所有参与分片的机器（实例列表）
     * @param shardingTotalCount 分片总数
     * @return 分片结果 格式如下："192.168.0.222:8080:1":[0],"192.168.0.222:8081:2":[1],"192.168.0.222:8082:3":[2]
     */
    @Override
    public Map<String, List<Long>> sharding(List<String> taskInstances, long shardingTotalCount) {
        // 不存在任务运行节点
        if (taskInstances.isEmpty()) {
            return Collections.emptyMap();
        }
        // 分配能被整除的部分
        Map<String, List<Long>> result = shardingInteger(taskInstances, shardingTotalCount);
        // 分配不能被整除的部分
        addRemainder(taskInstances, shardingTotalCount, result);
        return result;
    }

    /**
     * 任务整数部分分片
     * @param taskInstances      所有参与分片的单元列表
     * @param shardingTotalCount 分片总数
     * @return 分片结果
     */
    private Map<String, List<Long>> shardingInteger(final List<String> taskInstances, final long shardingTotalCount) {
        Map<String, List<Long>> result = new LinkedHashMap<>((int)shardingTotalCount, 1);
        // 每个任务运行实例分配的平均分片数
        long itemCountPerSharding = shardingTotalCount / taskInstances.size();
        long count = 0;
        for (String each : taskInstances) {
            List<Long> shardingItems = new ArrayList<>((int)itemCountPerSharding + 1);
            // 顺序向下平均分配任务
            for (long i = count * itemCountPerSharding; i < (count + 1) * itemCountPerSharding; i++) {
                shardingItems.add(i);
            }
            result.put(each, shardingItems);
            count++;
        }
        return result;
    }

    /**
     * 任务余数部分分片
     * @param taskInstances       所有参与分片的单元列表
     * @param shardingTotalCount  分片总数
     * @param shardingResults     整除部分分片结果
     */
    private static void addRemainder(final List<String> taskInstances, final Long shardingTotalCount, final Map<String, List<Long>> shardingResults) {
        // 余数
        long remainder = shardingTotalCount % taskInstances.size();
        long count = 0L;
        for (Map.Entry<String, List<Long>> entry : shardingResults.entrySet()) {
            if (count < remainder) {
                entry.getValue().add(shardingTotalCount / taskInstances.size() * taskInstances.size() + count);
            }
            count++;
        }
    }

    /**
     * 获取单例对象
     *
     * @return 单例对象
     */
    public static AverageAllocationConsistencyTaskShardingStrategy getInstance() {
        if (instance == null) {
            synchronized (SnowflakeShardingKeyGenerator.class) {
                if (instance == null) {
                    instance = new AverageAllocationConsistencyTaskShardingStrategy();
                }
            }
        }
        return instance;
    }

}