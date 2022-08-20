package moe.ahao.tend.consistency.core.adapter.http;

import lombok.extern.slf4j.Slf4j;
import moe.ahao.domain.entity.Result;
import moe.ahao.tend.consistency.core.adapter.message.LeaderToFollowerHeartbeatRequest;
import moe.ahao.tend.consistency.core.adapter.message.LeaderToFollowerHeartbeatResponse;
import moe.ahao.tend.consistency.core.sharding.ConsistencyTaskShardingContext;
import moe.ahao.util.commons.io.JSONHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * follower节点对外提供的接口
 **/
@Slf4j
@RestController
@RequestMapping("/follower")
public class FollowerController {
    @PostMapping("/heartbeat")
    public Result<LeaderToFollowerHeartbeatResponse> leaderHeartbeat(@RequestBody LeaderToFollowerHeartbeatRequest request) {
        log.info("follower收到来自leader的心跳包 {}", JSONHelper.toString(request));
        ConsistencyTaskShardingContext context = ConsistencyTaskShardingContext.getInstance();
        context.updateTaskSharingResult(request);

        LeaderToFollowerHeartbeatResponse response = new LeaderToFollowerHeartbeatResponse();
        response.setSuccess(true);
        response.setResponsePeerId(context.getCurrentPeerId().getVal());
        response.setLastResponseTs(System.currentTimeMillis());
        return Result.success(response);
    }
}
