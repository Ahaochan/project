package moe.ahao.tend.consistency.core.adapter.subscribe;

import lombok.extern.slf4j.Slf4j;
import moe.ahao.tend.consistency.core.adapter.message.RegisterOrCancelRequest;
import moe.ahao.tend.consistency.core.adapter.message.RegisterOrCancelResponse;
import moe.ahao.tend.consistency.core.election.PeerElectionHandler;
import moe.ahao.tend.consistency.core.election.PeerNodeManager;
import moe.ahao.tend.consistency.core.election.entity.PeerNode;
import moe.ahao.tend.consistency.core.infrastructure.enums.PeerOpTypeEnum;
import moe.ahao.tend.consistency.core.sharding.ConsistencyTaskShardingHandler;
import moe.ahao.util.commons.io.JSONHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RegisterOrCancelSubscribe {
    @Autowired
    private PeerElectionHandler peerElectionHandler;
    /**
     * 一致性任务分片处理器
     */
    @Autowired
    private ConsistencyTaskShardingHandler consistencyTaskShardingHandler;

    /**
     * 当集群成员变更的事件发生
     *
     * @param registerResponse 节点注册响应
     */
    @EventListener(RegisterOrCancelResponse.class)
    public void onPeerNodesChanged(RegisterOrCancelResponse registerResponse) {
        PeerNodeManager peerNodeManager = PeerNodeManager.getInstance();

        log.info("收到集群成员变更的消息 内容为:{}", JSONHelper.toString(registerResponse));
        Integer opType = registerResponse.getRegisterOrCancelRequest().getOpType();
        RegisterOrCancelRequest registerOrCancelRequest = registerResponse.getRegisterOrCancelRequest();
        // 上下线节点的唯一标识
        PeerNode peerIdentify = this.getNewPeerIdentify(registerOrCancelRequest);
        // 如果是leader且发送的是上线请求
        List<PeerNode> peerNodeList = peerNodeManager.getPeerNodes();
        if (PeerOpTypeEnum.ONLINE.getCode().equals(opType) && registerResponse.isLeader()) {
            log.info("进入到新节点重新注册的流程");
            if (!peerNodeList.contains(peerIdentify)) {
                peerNodeList.add(peerIdentify); // 新上线的节点，加入到你的peersConfigList里去，加入到你的集群节点列表里去
                // 任务重新分片
                log.info("新增节点[{}]完毕，已启动重新分片的流程", peerIdentify);
                consistencyTaskShardingHandler.doTaskSharding(peerNodeList);

            }
            // 如果是下线请求 且 下线的是follower 且 下线的是 接收请求的是leader节点
        } else if (PeerOpTypeEnum.OFFLINE.getCode().equals(opType) && !registerResponse.getRegisterOrCancelRequest().isLeaderOffline() && registerResponse.isLeader()) {
            log.info("进入到follower节点下线的流程");
            if (peerNodeList.contains(peerIdentify)) {
                peerNodeList.remove(peerIdentify);
                peerNodeManager.getHeartbeatResponseTable().remove(peerIdentify.getId());
                // 任务重新分片
                log.info("删除节点[{}]完毕，已启动重新分片的流程", peerIdentify);
                consistencyTaskShardingHandler.doTaskSharding(peerNodeList);

            }
            // 如果是下线请求且下线的是leader节点 接收请求的是follower
        } else if (PeerOpTypeEnum.OFFLINE.getCode().equals(opType) && registerResponse.getRegisterOrCancelRequest().isLeaderOffline() && !registerResponse.isLeader()) {
            log.info("进入到leader节点下线 重新选举leader的流程");
            if (peerNodeList.contains(peerIdentify)) {
                peerNodeList.remove(peerIdentify);
                // 重新选举和任务分片
                log.info("leader [{}] 下线完毕，已启动重新选举和重新分片的流程", peerIdentify);
                peerElectionHandler.reShardTaskAndReElection();
            }
        }
    }

    /**
     * 构造根据节点注册请求构造节点唯一标识
     *
     * @param request 注册请求
     * @return 新节点的唯一标识
     */
    private PeerNode getNewPeerIdentify(RegisterOrCancelRequest request) {
        String config = request.getIp() + ":" + request.getPort() + ":" + request.getPeerId();
        return new PeerNode(config);
    }
}
