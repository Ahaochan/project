package moe.ahao.tend.consistency.core.spi.shard.shardingstrategy;

import moe.ahao.tend.consistency.core.election.entity.PeerNode;
import moe.ahao.tend.consistency.core.election.entity.PeerNodeId;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

/**
 * 根据分片数平均分配任务的策略
 */
public final class AverageAllocationConsistencyTaskShardingStrategy implements ConsistencyTaskShardingStrategy {
    private static volatile AverageAllocationConsistencyTaskShardingStrategy instance;
    public static AverageAllocationConsistencyTaskShardingStrategy getInstance() {
        if (instance == null) {
            synchronized (AverageAllocationConsistencyTaskShardingStrategy.class) {
                if (instance == null) {
                    instance = new AverageAllocationConsistencyTaskShardingStrategy();
                }
            }
        }
        return instance;
    }

    /**
     * 任务分片.
     *
     * @param peerNodes          所有参与分片的机器（实例列表）
     * @param shardingTotalCount 分片总数
     * @return 分片结果 格式如下："192.168.0.222:8080:1":[0],"192.168.0.222:8081:2":[1],"192.168.0.222:8082:3":[2]
     */
    @Override
    public Map<PeerNodeId, List<Long>> sharding(List<PeerNode> peerNodes, long shardingTotalCount) {
        if (CollectionUtils.isEmpty(peerNodes)) {
            return Collections.emptyMap();
        }
        Map<PeerNodeId, List<Long>> result = new LinkedHashMap<>((int) shardingTotalCount, 1);
        // 1. 计算每个任务运行实例分配的平均分片数
        long perCount = shardingTotalCount / peerNodes.size();

        for (int i = 0, len = peerNodes.size(); i < len; i++) {
            PeerNode peerNode = peerNodes.get(i);

            // 2. 进行分片, 最后一个任务分配剩下的余数分片
            long end = (i != peerNodes.size() - 1) ? (i + 1) * perCount : shardingTotalCount;
            List<Long> shardingItems = new ArrayList<>((int) perCount + 1);
            for (long j = i * perCount; j < end; j++) {
                shardingItems.add(j);
            }
            result.put(peerNode.getId(), shardingItems);
        }
        return result;
    }
}
