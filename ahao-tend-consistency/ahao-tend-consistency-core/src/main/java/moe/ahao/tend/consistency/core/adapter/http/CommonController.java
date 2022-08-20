package moe.ahao.tend.consistency.core.adapter.http;

import lombok.extern.slf4j.Slf4j;
import moe.ahao.domain.entity.Result;
import moe.ahao.tend.consistency.core.adapter.message.RegisterOrCancelRequest;
import moe.ahao.tend.consistency.core.adapter.message.RegisterOrCancelResponse;
import moe.ahao.tend.consistency.core.election.entity.PeerNodeId;
import moe.ahao.tend.consistency.core.infrastructure.enums.PeerOpTypeEnum;
import moe.ahao.tend.consistency.core.sharding.ConsistencyTaskShardingContext;
import moe.ahao.util.commons.io.JSONHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 公用的请求对象
 * 当我们的框架伴随着你的系统启动之后，系统一定会扫描你的框架的所有bean
 * 在这里会扫描到你对应的controller bean
 * 把他变成可以对外处理http请求的bean，这个时候我们的框架controller也可以去处理你外部的请求了
 * 框架之间的online请求，/common/registerOrCancel
 *
 **/
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    /**
     * 注册到leader节点
     *
     * @param registerOrCancelRequest 注册请求
     * @return 注册结果
     */
    @PostMapping("/registerOrCancel")
    public Result<RegisterOrCancelResponse> leaderHeartbeat(@RequestBody RegisterOrCancelRequest registerOrCancelRequest) {
        log.info("收到节点 [{}] 的请求 请求内容为:[{}]",
                PeerOpTypeEnum.ONLINE.getCode().equals(registerOrCancelRequest.getOpType()) ? "上线" : "下线",
                JSONHelper.toString(registerOrCancelRequest));

        // 分片节点上下文
        ConsistencyTaskShardingContext consistencyTaskShardingContext = ConsistencyTaskShardingContext.getInstance();
        // 当前节点的id
        PeerNodeId currentPeerId = consistencyTaskShardingContext.getCurrentPeerId();
        // 当前leader的id
        PeerNodeId currentLeaderPeerId = consistencyTaskShardingContext.getCurrentLeaderPeerId();

        boolean isLeader = false;

        // 如果说收到请求的节点，他的id和leader id，是一样的，等值匹配的
        // 收到请求的节点，自己本身就是一个leader
        // 刚开始集群第一次启动，其实这个时候没有人是leader的，此时isLeader = false
        if (currentLeaderPeerId != null && currentPeerId != null) {
            isLeader = currentLeaderPeerId.equals(currentPeerId);
        }

        // 如果当前节点是leader节点
        RegisterOrCancelResponse registerResponse = new RegisterOrCancelResponse(currentPeerId.getVal(), isLeader, registerOrCancelRequest);

        // 你作为leader，你自己内部，会去发布一个事件，当前就是有一个RegisterOrCancelResponse事件发生了
        // 有一个新的节点online上线了
        applicationEventPublisher.publishEvent(registerResponse);

        return Result.success(registerResponse);
    }

}
