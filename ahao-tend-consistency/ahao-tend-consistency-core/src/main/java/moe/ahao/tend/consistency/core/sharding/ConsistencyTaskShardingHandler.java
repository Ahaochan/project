package moe.ahao.tend.consistency.core.sharding;

import moe.ahao.tend.consistency.core.adapter.message.FinishTaskShardingEvent;
import moe.ahao.tend.consistency.core.election.entity.PeerNode;
import moe.ahao.tend.consistency.core.election.entity.PeerNodeId;
import moe.ahao.tend.consistency.core.infrastructure.config.TendConsistencyConfiguration;
import moe.ahao.tend.consistency.core.sharding.strategy.AverageAllocationConsistencyTaskShardingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 分片处理器
 **/
@Component
public class ConsistencyTaskShardingHandler {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    /**
     * 一致性任务配置
     */
    @Autowired
    private TendConsistencyConfiguration tendConsistencyConfiguration;

    /**
     * 对任务做分片
     *
     * @param peerNodes 节点信息列表
     */
    public void doTaskSharding(List<PeerNode> peerNodes) {
        // 1. 获取配置文件中配置的任务分片数
        Long taskShardingCount = tendConsistencyConfiguration.getTaskShardingCount();
        // 2. 使用默认的分片策略进行分片, 每个节点分配一些任务
        Map<PeerNodeId, List<Long>> taskShardingResult = AverageAllocationConsistencyTaskShardingStrategy
            .getInstance()
            .sharding(peerNodes, taskShardingCount);
        // 3. 分片结果通过事件发布出去
        applicationEventPublisher.publishEvent(new FinishTaskShardingEvent(taskShardingResult));
    }
}
