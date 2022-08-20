package com.ruyuan.consistency.remote;

import cn.hutool.json.JSONUtil;
import com.ruyuan.consistency.common.CommonError;
import com.ruyuan.consistency.common.CommonRes;
import com.ruyuan.consistency.common.EmBusinessError;
import com.ruyuan.consistency.election.PeerElectionHandler;
import com.ruyuan.consistency.remote.message.LeaderToFollowerHeartbeatRequest;
import com.ruyuan.consistency.remote.message.LeaderToFollowerHeartbeatResponse;
import com.ruyuan.consistency.sharding.ConsistencyTaskShardingContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * follower节点对外提供的接口
 *
 * @author zhonghuashishan
 **/
@Slf4j
@RestController
@RequestMapping("/follower")
public class FollowerController {

    /**
     * 选举及任务分片处理器
     */
    @Autowired
    private PeerElectionHandler peerElectionHandler;

    @PostMapping("/heartbeat")
    public CommonRes<?> leaderHeartbeat(@RequestBody LeaderToFollowerHeartbeatRequest leaderToFollowerHeartbeatRequest) {
        log.info("follower收到来自leader的心跳包 {}", JSONUtil.toJsonStr(leaderToFollowerHeartbeatRequest));
        try {
            // 获取leader传递来的，分片上下文的校验和
            String checksum = leaderToFollowerHeartbeatRequest.getChecksum();
            // 获取分片上下文
            ConsistencyTaskShardingContext consistencyTaskShardingContext = peerElectionHandler.getConsistencyTaskShardingContext();
            // 如果校验和为空或校验和与本地的不同，则进行变更
            if (StringUtils.isEmpty(consistencyTaskShardingContext.getChecksum())
                    || !checksum.equals(consistencyTaskShardingContext.getChecksum())) {
                peerElectionHandler.setConsistencyTaskShardingContext(leaderToFollowerHeartbeatRequest);
            }
            return CommonRes.success(createResponse());
        } catch (Exception e) {
            return CommonRes.fail(new CommonError(EmBusinessError.SYS_INTERNAL_SERVER_ERROR));
        }
    }

    /**
     * 创建心跳响应对象
     *
     * @return 响应
     */
    private LeaderToFollowerHeartbeatResponse createResponse() {
        return LeaderToFollowerHeartbeatResponse.builder()
                .success(true)
                .responsePeerId(peerElectionHandler.getConsistencyTaskShardingContext().getCurrentPeerId())
                .lastResponseTs(System.currentTimeMillis())
                .build();
    }


}
