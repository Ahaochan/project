package moe.ahao.tend.consistency.sharding;

import moe.ahao.tend.consistency.core.election.entity.PeerNode;
import moe.ahao.tend.consistency.core.election.entity.PeerNodeId;
import moe.ahao.tend.consistency.core.sharding.strategy.AverageAllocationConsistencyTaskShardingStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AverageAllocationConsistencyTaskShardingStrategyTest {

    @Test
    public void test1() {
        List<PeerNode> peerNodes = Arrays.asList(
            new PeerNode("192.168.1.1:8080:1"),
            new PeerNode("192.168.1.1:8080:2"),
            new PeerNode("192.168.1.1:8080:3")
        );
        int shardTotalCount = 10;

        this.test(peerNodes, shardTotalCount);
    }

    public void test(List<PeerNode> peerNodes, int shardTotalCount) {
        int peerNodeCount = peerNodes.size();
        Map<PeerNodeId, List<Long>> map = AverageAllocationConsistencyTaskShardingStrategy.getInstance().sharding(peerNodes, shardTotalCount);
        System.out.println(map);
        List<List<Long>> values = new ArrayList<>(map.values());
        for (int i = 0, size = values.size(); i < size; i++) {
            if(i != size - 1) {
                Assertions.assertEquals(shardTotalCount / peerNodeCount, values.get(i).size());
            } else {
                Assertions.assertEquals(shardTotalCount / peerNodeCount + shardTotalCount % peerNodeCount, values.get(i).size());
            }
        }
    }
}
