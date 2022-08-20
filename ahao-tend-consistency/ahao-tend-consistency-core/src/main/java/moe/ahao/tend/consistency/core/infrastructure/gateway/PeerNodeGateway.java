package moe.ahao.tend.consistency.core.infrastructure.gateway;

import lombok.extern.slf4j.Slf4j;
import moe.ahao.domain.entity.Result;
import moe.ahao.tend.consistency.core.adapter.message.*;
import moe.ahao.tend.consistency.core.election.entity.PeerNode;
import moe.ahao.tend.consistency.core.infrastructure.enums.PeerTransportEnum;
import moe.ahao.util.commons.io.JSONHelper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class PeerNodeGateway {
    private final RestTemplate restTemplate;
    private final ThreadPoolExecutor threadPoolExecutor;

    public PeerNodeGateway() {
        restTemplate = new RestTemplate();

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(600000);
        factory.setReadTimeout(20000);
        restTemplate.setRequestFactory(factory);

        threadPoolExecutor = new ThreadPoolExecutor(
            5,
            5,
            60,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(50),
            new ThreadFactory() {
                private final AtomicInteger threadIndex = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "peerNodeGatewayThread" + this.threadIndex.incrementAndGet());
                }
            }
        );
    }

    public RegisterOrCancelResponse register(PeerNode peerNode, RegisterOrCancelRequest request) {
        String url = String.format(PeerTransportEnum.REGISTRY_URL_TEMPLATE.getUrl(), peerNode.getAddress());
        return this.post(url, request);
    }

    public Future<RegisterOrCancelResponse> registerAsync(PeerNode peerNode, RegisterOrCancelRequest request) {
        return threadPoolExecutor.submit(() -> {
            try {
                return this.register(peerNode, request);
            } catch (Exception e) {
                log.error("最终一致性集群发起内部通信, 请求url:{}", peerNode, e);
                return new RegisterOrCancelResponse(false);
            }
        });
    }

    public FollowerToLeaderHeartbeatResponse sendFollowerHeartbeatRequest(PeerNode peerNode, FollowerToLeaderHeartbeatRequest request) {
        String url = String.format(PeerTransportEnum.FOLLOWER_HEARTBEAT_URL_TEMPLATE.getUrl(), peerNode.getAddress());
        return this.post(url, request);
    }

    public LeaderToFollowerHeartbeatResponse doSendHeartbeatTask(PeerNode peerNode, LeaderToFollowerHeartbeatRequest request) {
        String url = String.format(PeerTransportEnum.LEADER_HEARTBEAT_URL_TEMPLATE.getUrl(), peerNode.getAddress());
        return this.post(url, request);
    }

    public <Req, Resp> Resp post(String url, Req request) {
        try {
            // 1. 请求体
            HttpEntity<Req> entity = new HttpEntity<>(request, null);
            // 2. 发送请求
            log.info("最终一致性集群发起内部通信, 请求url:{}, 请求参数:{}", url, JSONHelper.toString(request));
            ResponseEntity<Result<Resp>> response = restTemplate.exchange(url, HttpMethod.POST,
                entity, new ParameterizedTypeReference<Result<Resp>>() {
                });
            log.info("最终一致性集群发起内部通信, 请求url:{}, 响应结果:{}", url, response);
            // 3. 解析结果
            if (HttpStatus.OK.value() != response.getStatusCode().value()) {
                return null;
            }
            Result<Resp> res = response.getBody();
            if (res == null || res.getCode() != Result.SUCCESS) {
                return null;
            }
            return res.getObj();
        } catch (Exception e) {
            log.error("最终一致性集群发起内部通信, 请求url:{}, 发生异常", url, e);
            return null;
        }
    }
}
