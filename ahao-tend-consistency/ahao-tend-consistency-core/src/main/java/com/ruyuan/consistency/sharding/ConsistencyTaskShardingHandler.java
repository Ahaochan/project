package com.ruyuan.consistency.sharding;

import com.ruyuan.consistency.config.TendConsistencyConfiguration;
import com.ruyuan.consistency.election.PeerElectionHandler;
import com.ruyuan.consistency.sharding.strategy.AverageAllocationConsistencyTaskShardingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 分片处理器
 * @author zhonghuashishan
 **/
@Component
public class ConsistencyTaskShardingHandler {


    /**
     * 一致性任务配置
     */
    @Autowired
    private TendConsistencyConfiguration tendConsistencyConfiguration;
    /**
     * 选举处理器
     */
    @Autowired
    private PeerElectionHandler peerElectionHandler;

    /**
     * 对任务做分片
     *
     * @param peersConfigList 节点信息列表
     */
    public void doTaskSharding(List<String> peersConfigList) {
        // 获取配置文件中配置的任务分片数
        Long taskShardingCount = tendConsistencyConfiguration.taskShardingCount;
        // 如果用户没有自定义任务分片类，使用内部已经定义好的分片类，进行分片
        // 具体集群里有多少个分片，你可以自己配置，自定义的，你可以有3个节点，但是配置了10个分片
        Map<String, List<Long>> taskShardingResult = AverageAllocationConsistencyTaskShardingStrategy
                .getInstance()
                .sharding(peersConfigList, taskShardingCount);
        // 分发已完成分片的事件给选举处理器
        // 把分片分配的结果，发布到事件总线里去，eventBus里去
        // 对事件总线里的事件有监听的人，都可以被你传递这个事件过去吧
        peerElectionHandler.eventBus.post(taskShardingResult);
    }

}
