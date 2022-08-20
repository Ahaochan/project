package moe.ahao.tend.consistency.core.adapter.subscribe;

import moe.ahao.tend.consistency.core.adapter.message.FinishTaskShardingEvent;
import moe.ahao.tend.consistency.core.election.PeerElectionHandler;
import moe.ahao.tend.consistency.core.election.entity.PeerNodeId;
import moe.ahao.tend.consistency.core.sharding.ConsistencyTaskShardingContext;
import moe.ahao.util.commons.io.JSONHelper;
import moe.ahao.util.commons.lang.CodecHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class FinishTaskShardingSubscribe {
    @Autowired
    private PeerElectionHandler peerElectionHandler;
    /**
     * 当完成任务分片的事件发生
     *
     * @param event 任务分片结果
     */
    @EventListener(FinishTaskShardingEvent.class)
    public void onFinishTaskSharding(FinishTaskShardingEvent event) {
        ConsistencyTaskShardingContext context = peerElectionHandler.getConsistencyTaskShardingContext();

        Map<PeerNodeId, List<Long>> taskShardingResult = event.getTaskShardingResult();
        context.setTaskSharingResult(taskShardingResult);
        context.setChecksum(CodecHelper.md5(JSONHelper.toString(taskShardingResult)));
    }
}
