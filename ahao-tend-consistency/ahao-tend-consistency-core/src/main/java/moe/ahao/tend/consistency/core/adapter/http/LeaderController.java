package moe.ahao.tend.consistency.core.adapter.http;

import lombok.extern.slf4j.Slf4j;
import moe.ahao.domain.entity.Result;
import moe.ahao.tend.consistency.core.adapter.message.FollowerToLeaderHeartbeatRequest;
import moe.ahao.tend.consistency.core.adapter.message.FollowerToLeaderHeartbeatResponse;
import moe.ahao.util.commons.io.JSONHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * leader节点提供的接口
 **/
@Slf4j
@RestController
@RequestMapping("/leader")
public class LeaderController {
    @PostMapping("/heartbeat")
    public Result<FollowerToLeaderHeartbeatResponse> followerHeartbeat(@RequestBody FollowerToLeaderHeartbeatRequest followerToLeaderHeartbeatRequest) {
        log.info("leader收到心跳请求 {}", JSONHelper.toString(followerToLeaderHeartbeatRequest));

        FollowerToLeaderHeartbeatResponse response = new FollowerToLeaderHeartbeatResponse();
        response.setSuccess(true);
        response.setReplyTimestamp(System.currentTimeMillis());
        return Result.success(response);
    }
}
