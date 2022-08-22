package moe.ahao.tend.consistency.core.spi.shard.shardingstrategy;

import moe.ahao.tend.consistency.core.election.entity.PeerNode;
import moe.ahao.tend.consistency.core.election.entity.PeerNodeId;
import moe.ahao.tend.consistency.core.infrastructure.config.TendConsistencyConfiguration;
import moe.ahao.util.commons.io.JSONHelper;
import moe.ahao.util.commons.lang.CodecHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 分片处理器
 **/
@Component
public class ConsistencyTaskShardingHandler {
    /**
     * 一致性任务配置
     */
    @Autowired
    private TendConsistencyConfiguration tendConsistencyConfiguration;

    private final ConsistencyTaskShardingStrategy consistencyTaskShardingStrategy;
    private ConsistencyTaskShardingHandler() {
        this(AverageAllocationConsistencyTaskShardingStrategy.getInstance());
    }
    private ConsistencyTaskShardingHandler(ConsistencyTaskShardingStrategy consistencyTaskShardingStrategy) {
        this.consistencyTaskShardingStrategy = consistencyTaskShardingStrategy;
    }

    /**
     * 对任务做分片
     *
     * @param peerNodes 节点信息列表
     */
    public void doTaskSharding(List<PeerNode> peerNodes) {
        ConsistencyTaskShardingContext context = ConsistencyTaskShardingContext.getInstance();

        // 1. 获取配置文件中配置的任务分片数
        Long taskShardingCount = tendConsistencyConfiguration.getTaskShardingCount();
        // 2. 使用默认的分片策略进行分片, 每个节点分配一些任务
        Map<PeerNodeId, List<Long>> taskShardingResult = consistencyTaskShardingStrategy.sharding(peerNodes, taskShardingCount);
        // 3. 设置到context上下文里
        context.setTaskSharingResult(taskShardingResult);
        String checkSum = CodecHelper.md5(JSONHelper.toString(taskShardingResult));
        context.setChecksum(checkSum);
    }
}
