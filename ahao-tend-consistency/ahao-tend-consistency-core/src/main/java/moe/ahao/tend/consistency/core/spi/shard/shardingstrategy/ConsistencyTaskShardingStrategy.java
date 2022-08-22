package moe.ahao.tend.consistency.core.spi.shard.shardingstrategy;

import moe.ahao.tend.consistency.core.election.entity.PeerNode;
import moe.ahao.tend.consistency.core.election.entity.PeerNodeId;

import java.util.List;
import java.util.Map;

public interface ConsistencyTaskShardingStrategy {

    /**
     * 任务分片.
     *
     * @param taskInstances      所有参与分片的机器（实例列表）
     * @param shardingTotalCount 分片总数
     * @return 分片结果 格式如下："192.168.0.222:8080:1":[0],"192.168.0.222:8081:2":[1],"192.168.0.222:8082:3":[2]
     */
    Map<PeerNodeId, List<Long>> sharding(List<PeerNode> taskInstances, long shardingTotalCount);
}
