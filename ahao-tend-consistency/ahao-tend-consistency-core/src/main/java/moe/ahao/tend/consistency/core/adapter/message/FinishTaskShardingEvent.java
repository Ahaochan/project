package moe.ahao.tend.consistency.core.adapter.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import moe.ahao.tend.consistency.core.election.entity.PeerNodeId;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class FinishTaskShardingEvent {
    private Map<PeerNodeId, List<Long>> taskShardingResult;
}
