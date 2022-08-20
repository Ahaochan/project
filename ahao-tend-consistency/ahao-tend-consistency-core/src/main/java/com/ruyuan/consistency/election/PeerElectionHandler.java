package com.ruyuan.consistency.election;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.ruyuan.consistency.common.CommonRes;
import com.ruyuan.consistency.config.PeerNodeConfigProperties;
import com.ruyuan.consistency.config.TendConsistencyConfiguration;
import com.ruyuan.consistency.enums.PeerOpTypeEnum;
import com.ruyuan.consistency.enums.PeerTransportEnum;
import com.ruyuan.consistency.manager.TaskScheduleManager;
import com.ruyuan.consistency.remote.message.*;
import com.ruyuan.consistency.scheduler.SchedulerManager;
import com.ruyuan.consistency.sharding.ConsistencyTaskShardingContext;
import com.ruyuan.consistency.sharding.ConsistencyTaskShardingHandler;
import com.ruyuan.consistency.utils.RestTemplateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.CompletionService;
import java.util.stream.Collectors;

import static com.ruyuan.consistency.utils.NetUtils.getCurrentPeerAddress;

/**
 * 集群节点选举及任务分片锁使用的处理器
 *
 * @author zhonghuashishan
 **/
@Slf4j
@Component
public class PeerElectionHandler implements ApplicationListener<WebServerInitializedEvent>, DisposableBean {

    /**
     * 当前节点的ip地址
     */
    @Value("${server.port}")
    private int currentServerPort;
    /**
     * 集群信息配置
     */
    @Autowired
    private PeerNodeConfigProperties peerNodeConfigProperties;
    /**
     * 一致性任务调度管理器
     */
    @Autowired
    private TaskScheduleManager taskScheduleManager;
    /**
     * 用于节点启动是发送请求给其他节点，看有没有leader节点，如果有直接注册，没有则按原选举逻辑来搞
     */
    @Autowired
    private CompletionService<RegisterOrCancelResponse> peerRegisterPool;
    /**
     * 一致性任务分片处理器
     */
    @Autowired
    private ConsistencyTaskShardingHandler consistencyTaskShardingHandler;
    /**
     * 一致性任务框架配置
     */
    @Autowired
    private TendConsistencyConfiguration tendConsistencyConfiguration;
    /**
     * 调度管理器
     */
    @Autowired
    private SchedulerManager schedulerManager;
    /**
     * follower回复给leader的心跳响应表 格式： key: PeerId value: HeartbeatResponse
     * leader对应的每个follower，每次收到一个leader心跳，返回了一个响应，leader来说，他拿到的每个响应，都会根据follower peer id
     * key-value写入，覆盖
     * 对于leader来说，就可以检查到每个follower最近一次接收到心跳返回的一个响应
     */
    private Map<String, LeaderToFollowerHeartbeatResponse> heartbeatResponseTable;
    /**
     * 一致性框架集群节点的配置信息
     */
    private List<String> peersConfigList;
    /**
     * 可用于执行分片任务的实例信息 格式：key:peerId, value:ip:port
     */
    private final Map<String, String> availableShardingInstances = new HashMap<>();
    /**
     * follower发送给leader的心跳响应
     */
    private FollowerToLeaderHeartbeatResponse recentlyFollowerToLeaderHeartbeatResponse;
    /**
     * 任务分片上下文
     */
    private final ConsistencyTaskShardingContext consistencyTaskShardingContext = new ConsistencyTaskShardingContext();
    /**
     * 事件总线
     */
    public EventBus eventBus;

    // 很关键的一个方法，系统启动的时候，识别出来你的这个bean组件里，实现了一个指定的接口
    // 他就知道说，你想要去监听系统启动的事件，他会把一个事件传递给你
    // 系统只要一旦说 启动了，就会来触发这个方法的执行和运行，执行start，完成你的框架初始化、选举以及发起定时调度的全过程
    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        start();
    }

    @Override
    public void destroy() {
        log.info("peerId={}的节点下线", consistencyTaskShardingContext.getCurrentPeerId());
        // 构造节点下线请求，通知集群中的其他节点
        checkIsExistLeaderAndNotifyAddOrCancel(PeerOpTypeEnum.OFFLINE.getOpType());
        log.info("peerId={}的节点下线完毕", consistencyTaskShardingContext.getCurrentPeerId());
    }

    /**
     * 启动
     */
    public void start() {
        // 初始化框架
        init();
        // 选举并执行执行一致性任务的分片
        startElectionProcessAndDoTaskSharding();
        // 启动一致性任务执行引擎，定时的扫描db和本地的rocksdb里的任务，来执行了
        // aop切面里，如果说你提交任务，db写失败，就自动降级写入到rocksdb里去，系统正常在运行
        // 就会自动的去处理本地的rocksdb里的任务
        startTaskExecuteEngine();
    }

    private void init() {
        // 解析集群节点地址为list格式
        // 我们可以拿到整个你的框架嵌入的系统部署的服务器集群的地址list
        // 如果说你的框架嵌入到订单系统，你需要配置的就是订单系统部署的机器集群列表
        // 换个说法，如果你嵌入到了营销系统里去，配置的就是营销系统部署的机器集群列表list
        peersConfigList = parsePeersConfigToList(peerNodeConfigProperties.getPeersConfig());
        // 获取当前节点的peerId
        setCurrentPeerId(peersConfigList);
        // 构建可用于分片的实例map 格式：key:peerId, value:ip:port
        buildAvailableShardingInstances(peersConfigList);
        // 初始化事件总线 用于监听节点上线和下下线、分片完成等事件 详见@Subscribe
        // 基于内存里的事件总线，在系统jvm内部，基于内存实现的，大家可以基于这个事件总线，发布事件，也可以去监听订阅一个事件
        // PeerElectionHandler自己会去关注事件总线里发布出来的事件
        // 对于你来说，你关注什么事件，就得在自己的类里，实现一个方法，@Subscribe注解，那个注解标注的方法，就会负责你的事件处理
        eventBus = new EventBus(consistencyTaskShardingContext.getCurrentPeerId());
        eventBus.register(this);
    }

    /**
     * 启动选举流程并执行执行一致性任务的分片
     */
    private void startElectionProcessAndDoTaskSharding() {
        String leaderPeerId = "";

        // 检测集群中是否存在leader，如果存在则直接加入leader，不存在则选举最小节点作为leader
        // 刚开始第一次启动，集群里还没有leader存在，还是需要去进行选举的，leader选举其实很简单，leader主要扮演的角色，就是跟follower保持心跳
        // 他可以根据follower数量，分配分片数量和负责的分片号
        // 对于leader选举，没有特殊的规则再里面，默认就是按照id最小的节点来选举为一个leader就可以了
        // 没有任何一个节点，他是leader节点，此时你会收到一堆节点返回的响应

        // 每个框架实例随着系统在一台服务器上启动了之后，首先就会来干这个事儿
        // 每个节点都会发送一个online请求给其他的节点，这个操作主要是去判断说是否已经有一个leader了
        // 一开始默认情况下是不会有leader的，所以这个事情他是不会有任何的结果的

        List<RegisterOrCancelResponse> registerOrCancelResponses = checkIsExistLeaderAndNotifyAddOrCancel(PeerOpTypeEnum.ONLINE.getOpType());

        // 后启动的节点会走这里，加入leader
        if (!CollectionUtil.isEmpty(registerOrCancelResponses)) {
            RegisterOrCancelResponse registerResponse = registerOrCancelResponses.get(0);
            leaderPeerId = registerResponse.getReplyPeerId();
        }

        // 发现当前集群中没有leader
        if (StringUtils.isEmpty(leaderPeerId)) {
            // 使用最小节点id作为leader
            // 在这里大家会发起一个leader选举，选举算法非常简单，主要就是根据你的机器列表里的最小id值
            // 把id值最小的节点，选举为你的leader就可以了

            // 所有节点，但凡是启动了，在这里都会做一个事情，leader选举
            // 直接每个节点都基于自己的配置文件里的机器列表，根据id最小值，选举出一个leader来
            // 此时会基于刨除了那个宕机的leader机器以后，剩余的机器列表会发起一个选举
            // 会直接把剩下的节点里，id值最小的节点选举为新的leader
            leaderPeerId = getMinPeerIdAsLeader(peersConfigList);
        }

        if (StringUtils.isEmpty(leaderPeerId)) {
            log.warn("未找到leaderPeerId，退出选举流程");
            return;
        }
        // 将当前的leaderId设置到分片节点上下文
        consistencyTaskShardingContext.setCurrentLeaderPeerId(leaderPeerId);

        // 判断选出的leader是不是自己
        // 每个节点都会判断一下，自己是不是leader，如果说选出来的leader就是你自己的，那你就是那个id最小值的节点
        // leader就是你了
        boolean electionResult = leaderIsMySelf(leaderPeerId, availableShardingInstances);
        log.info("当前节点成为 [{}] 节点", electionResult ? "leader" : "follower");

        // 如果分片上下文中的分片结果为空 且 当前节点是leader节点
        // 如果说你自己就是一个leader这样子，此时的话呢，你就会在这里去分配一下分片
        // 分片分配给所有的节点
        if (ObjectUtil.isEmpty(consistencyTaskShardingContext.getTaskSharingResult()) && electionResult) {
            // 执行任务分片
            consistencyTaskShardingHandler.doTaskSharding(peersConfigList);
        }

        // 启动当前节点相关的所有定时调度器
        startCurrentPeerAllScheduler(electionResult);
    }

    /**
     * 检测集群中是否存在leader并通知各个节点当前节点上线或者下线的操作
     *
     * @return 结果
     */
    private List<RegisterOrCancelResponse> checkIsExistLeaderAndNotifyAddOrCancel(Integer peerOpType) {

        // 获取当前节点在集群中的唯一标识
        String currentPeerIdentify = getCurrentPeerAddress() + ":" + currentServerPort + ":" + consistencyTaskShardingContext.getCurrentPeerId();

        // 发送注册请求给除自己以外的其他节点
        // 把自己当前节点排除掉，发送注册请求，发送给其他的所有节点
        for (String peer : peersConfigList) {
            if (StringUtils.isEmpty(peer)) {
                continue;
            }
            if (peer.equals(currentPeerIdentify)) {
                continue;
            }
            // 你有几个节点，就提交多少个任务过去，对每个节点发起一个请求，默认是用http请求，基于spring mvc的controller来进行通信就可以了
            // 发送http请求的时候，RestTemplate发起请求就可以了
            // 发起请求会做一个什么呢？online，节点上线的操作，我要去通知其他所有的节点，我当前节点是上线了
            peerRegisterPool.submit(() -> {
                try {
                    return sendRegOrCancelRequest(peer, peerOpType);
                } catch (Exception e) {
                    log.error("上下线请求发生异常 {}", e.getMessage());
                    return RegisterOrCancelResponse
                            .builder()
                            .leader(false)
                            .build();
                }
            });
        }

        List<RegisterOrCancelResponse> result = new ArrayList<>(peersConfigList.size());

        try {
            // 拿到所有的结果再返回
            for (int i = 0; i < peersConfigList.size() - 1; i++) {
                RegisterOrCancelResponse registerResponse = peerRegisterPool.take().get();
                result.add(registerResponse);
            }
            // 找到是leader且注册成功的返回结果
            result = result.stream().filter(e -> !ObjectUtils.isEmpty(e) && e.isLeader()).collect(Collectors.toList());
            return result;
        } catch (Exception e) {
            log.error("获取集群中leader结果时，发生异常", e);
            return result;
        }

    }

    /**
     * 向集群中的各个节点发送节点上线/下线的请求
     *
     * @param peer       节点信息
     * @param peerOpType 集群节点操作类型
     * @return 注册请求响应信息
     */
    private RegisterOrCancelResponse sendRegOrCancelRequest(String peer, Integer peerOpType) {
        // 获取leader节点的 ip:port
        String transportAddress = peer.substring(0, peer.lastIndexOf(":"));
        // 获取请求url
        String url = String.format(PeerTransportEnum.REGISTRY_URL_TEMPLATE.getUrl(), transportAddress);
        RestTemplateUtils restTemplateUtils = new RestTemplateUtils();
        // 构造请求对象
        // 构造的其实就是你的一个http请求，封装当前你的这个节点的ip、port、id、op=online，当前节点是否为leader
        RegisterOrCancelRequest request = RegisterOrCancelRequest.builder()
                .ip(getCurrentPeerAddress())
                .port(String.valueOf(currentServerPort))
                .peerId(consistencyTaskShardingContext.getCurrentPeerId())
                .opType(peerOpType)
                .leaderOffline(consistencyTaskShardingContext.getCurrentPeerId().equals(consistencyTaskShardingContext.getCurrentLeaderPeerId()))
                .build();
        // 构造请求实体
        HttpEntity<RegisterOrCancelRequest> entity = new HttpEntity<>(request, null);
        // 构造返回值参数化类型
        ParameterizedTypeReference<CommonRes<RegisterOrCancelResponse>> parameterizedTypeReference =
                new ParameterizedTypeReference<CommonRes<RegisterOrCancelResponse>>() {
                };
        log.info("发送上线或下线请求的url为 {}", url);
        // 发送请求
        ResponseEntity<CommonRes<RegisterOrCancelResponse>> exchange = restTemplateUtils.exchange(url,
                HttpMethod.POST, entity, parameterizedTypeReference);
        // 解析结果
        if (HttpStatus.OK.value() == exchange.getStatusCode().value() && exchange.hasBody()) {
            CommonRes<RegisterOrCancelResponse> res = exchange.getBody();
            if (res == null) {
                return null;
            }
            return res.getData();
        } else {
            return null;
        }
    }

    /**
     * 启动任务执行引擎
     */
    private void startTaskExecuteEngine() {
        // 启动执行引擎
        schedulerManager.createConsistencyTaskScheduler(this::doStartTaskExecuteEngine);
    }

    /**
     * 启动当前节点所有调度器
     *
     * @param electionResult 是否为leader
     */
    private void startCurrentPeerAllScheduler(boolean electionResult) {
        // 取消所有当前节点相关的调度任务
        schedulerManager.cancelAllScheduler();

        // 如果判断当前节点是leader节点
        // 如果你是leader，你得不停的给所有的follower发送心跳，你得定时检查follower发送过来的心跳，follower是否宕机
        if (electionResult) {
            heartbeatResponseTable = new HashMap<>();
            // 创建leader发送心跳的定时调度任务 同时也会发送任务分片信息
            // 作为leader，每隔10s，要发送一下心跳消息给所有的follower这样子，这个是第一个
            schedulerManager
                    .createLeaderToFollowerHeartbeatScheduler(
                            () -> sendHeartbeatTask(availableShardingInstances,
                                    consistencyTaskShardingContext.getCurrentLeaderPeerId())
                    );
            // 创建用于检测follower是否存活的调度任务
            // 他作为leader自己，也需要创建定时任务，他要检查follower发送过来的心跳，是否过期，如果说过期了，他就要去摘除这个follower实例
            // 重新进行分片分配
            schedulerManager.createFollowerAliveCheckScheduler(this::doFollowerAliveCheck);
        }
        // 如果你是follower，你得不停的给leader发送心跳，你得定时检查leader发送给你的心跳，leader是否宕机
        else {
            // 如果是follower节点需要创建的线程以及需要初始化的对象
            recentlyFollowerToLeaderHeartbeatResponse = FollowerToLeaderHeartbeatResponse
                    .builder()
                    .success(true)
                    .replyTimestamp(System.currentTimeMillis())
                    .build();
            // 创建follower发送给leader的定时调度任务
            // 对于如果你是follower的话，你就需要定时每隔10s发送你自己的心跳给leader
            schedulerManager.createFollowerHeartbeatScheduler(this::sendFollowerHeartbeatRequest);
            // 创建用于检测leader是否宕机的定时调度任务
            // 你必须定时检查leader心跳，每隔10s检查一下，如果超过120s leader没有心跳过来，此时就认为leader宕机了，重新选举一个新leader出来
            schedulerManager.createLeaderAliveScheduler(this::doLeaderAliveCheck);
        }
    }

    /**
     * 设置当前节点在集群中的id
     *
     * @param peersConfigList 一致性任务集群配置列表
     */
    private void setCurrentPeerId(List<String> peersConfigList) {
        // 当前节点的ip+端口
        String currentPeerAddress = getCurrentPeerAddress() + ":" + currentServerPort;
        for (String peerAddress : peersConfigList) {
            if (peerAddress.contains(currentPeerAddress)) {
                String currentPeerId = peerAddress.replaceAll(currentPeerAddress + ":", "");
                consistencyTaskShardingContext.setCurrentPeerId(currentPeerId);
            }
        }
    }

    /**
     * 获取集群节点中最小id的那个节点作为作为leader
     *
     * @param peersConfigList 节点信息列表
     * @return 最小节点的id
     */
    private String getMinPeerIdAsLeader(List<String> peersConfigList) {
        // 从配置的机器列表里，找出id最小值对应的节点，作为leader就可以了
        String leaderPeerId = "";
        for (String peerInfo : peersConfigList) {
            String[] split = peerInfo.split(":");
            // 节点id
            String peerId = split[2];
            if (StringUtils.isEmpty(leaderPeerId)) {
                leaderPeerId = peerId;
            } else {
                if (Integer.parseInt(leaderPeerId) > Integer.parseInt(peerId)) {
                    leaderPeerId = peerId;
                }
            }
        }
        return leaderPeerId;
    }

    /**
     * follower检测leader是否存活
     */
    private void doLeaderAliveCheck() {
        // 每隔10s跑一次检查，如果说上一次leader心跳响应，到现在为止，超过了120s了
        // 此时就可以认为说你的leader也是宕机了
        // 如果follower超过指定阈值还无法与leader通信，判定为leader宕机重新选举，重新下发任务分片
        if (System.currentTimeMillis() - recentlyFollowerToLeaderHeartbeatResponse.getReplyTimestamp()
                > tendConsistencyConfiguration.getJudgeLeaderDownSecondsThreshold() * 1000L) {
            String currentLeaderPeerId = consistencyTaskShardingContext.getCurrentLeaderPeerId();
            String peerAddress = availableShardingInstances.get(currentLeaderPeerId);
            peersConfigList.remove(peerAddress + ":" + currentLeaderPeerId);
            // 重新选举和任务分片
            reShardTaskAndReElection();
        }
    }

    /**
     * follower向leader发送心跳请求
     */
    private void sendFollowerHeartbeatRequest() {
        // 获取leader节点的 ip:port
        String transportAddress = availableShardingInstances.get(consistencyTaskShardingContext.getCurrentLeaderPeerId());
        // 获取请求url
        String url = String.format(PeerTransportEnum.FOLLOWER_HEARTBEAT_URL_TEMPLATE.getUrl(), transportAddress);
        RestTemplateUtils restTemplateUtils = new RestTemplateUtils();
        // 构造发送给leader的心跳请求对象
        FollowerToLeaderHeartbeatRequest request = FollowerToLeaderHeartbeatRequest.builder()
                .peerId(consistencyTaskShardingContext.getCurrentPeerId())
                .build();
        // 构造请求实体
        HttpEntity<FollowerToLeaderHeartbeatRequest> entity = new HttpEntity<>(request, null);
        try {
            // 构造返回值参数化类型
            ParameterizedTypeReference<CommonRes<FollowerToLeaderHeartbeatResponse>> parameterizedTypeReference =
                    new ParameterizedTypeReference<CommonRes<FollowerToLeaderHeartbeatResponse>>() {
                    };
            // 发送请求
            ResponseEntity<CommonRes<FollowerToLeaderHeartbeatResponse>> exchange = restTemplateUtils.exchange(url,
                    HttpMethod.POST, entity, parameterizedTypeReference);
            // 解析结果
            if (HttpStatus.OK.value() == exchange.getStatusCode().value() && exchange.hasBody()) {
                CommonRes<FollowerToLeaderHeartbeatResponse> res = exchange.getBody();
                if (res == null) {
                    return;
                }
                recentlyFollowerToLeaderHeartbeatResponse = res.getData();
                log.info("follower收到leader心跳响应为 {}", JSONUtil.toJsonStr(res));
            } else {
                log.info("follower收到leader心跳响应为 {}", exchange.getStatusCodeValue());
            }
        } catch (Exception e) {
            log.error("发送心跳异常: {}", e.getMessage());
        }
    }

    /**
     * 构建可用于分片的实例map
     *
     * @param peersConfigList 集群列表
     *                        最终格式：key:peerId, value:ip:port
     */
    private void buildAvailableShardingInstances(List<String> peersConfigList) {
        for (String peerInfo : peersConfigList) {
            String[] split = peerInfo.split(":");
            // 节点ip地址
            String ip = split[0];
            // 节点端口
            String port = split[1];
            // 节点id
            String peerId = split[2];
            // 构造可用于分片集群节点的实例信息map key:peerId, value:ip:port
            availableShardingInstances.put(peerId, ip + ":" + port);
        }
    }

    /**
     * leader发送心跳包给其他节点，同时也会将分片信息发给其他节点
     *
     * @param peerInfoMap     集群地址map格式
     * @param currentLeaderId 当前leader节点的id
     */
    private void sendHeartbeatTask(Map<String, String> peerInfoMap, String currentLeaderId) {
        // 开启发送心跳给其他节点的定时任务
        log.info("进入发送心跳的定时调度任务");
        // 构造分片请求
        // leader发送心跳给follower的，taskShardingResult，分片分配结果，封装在请求里
        // 包括就是分片分配的结果，checksum校验和
        LeaderToFollowerHeartbeatRequest request = LeaderToFollowerHeartbeatRequest.builder()
                .currentLeaderId(currentLeaderId)
                .cacheSharingResult(consistencyTaskShardingContext.getTaskSharingResult())
                .checksum(consistencyTaskShardingContext.getChecksum())
                .build();
        RestTemplateUtils restTemplateUtils = new RestTemplateUtils();
        Set<String> keySet = peerInfoMap.keySet();

        // 如果说你的分片分配信息变更
        // 下一次心跳的时候，leader会通过心跳消息，把最新的分片分配信息，传递给你所有的follower节点
        // 老的follower，还是新的follower，最新的分片分配信息他都知道了

        // 发送分片上下文
        for (String key : keySet) {
            if (!key.equals(currentLeaderId)) {
                String url = peerInfoMap.get(key); // 针对你的每个follower的url地址，发送http请求，leader心跳的请求发送过去
                // 通过定时心跳的机制，就可以每隔10s，把最新的分片分配的数据带给你的follower
                doSendHeartbeatTask(restTemplateUtils, url, request);
            }
        }
    }

    /**
     * 发送心跳请求
     *
     * @param restTemplateUtils 请求工具实例
     * @param url               请求url
     * @param request           心跳请求对象
     */
    private void doSendHeartbeatTask(RestTemplateUtils restTemplateUtils, String url, LeaderToFollowerHeartbeatRequest request) {
        HttpEntity<LeaderToFollowerHeartbeatRequest> entity = new HttpEntity<>(request, null);
        try {
            // 构造返回值参数化类型
            ParameterizedTypeReference<CommonRes<LeaderToFollowerHeartbeatResponse>> parameterizedTypeReference =
                    new ParameterizedTypeReference<CommonRes<LeaderToFollowerHeartbeatResponse>>() {
                    };
            // 获取leader发送给follower的心跳请求URL
            String leaderHeartbeatUrl = String.format(PeerTransportEnum.LEADER_HEARTBEAT_URL_TEMPLATE.getUrl(), url);
            // 发送请求
            ResponseEntity<CommonRes<LeaderToFollowerHeartbeatResponse>> exchange = restTemplateUtils.exchange(leaderHeartbeatUrl,
                    HttpMethod.POST, entity, parameterizedTypeReference);
            // 解析请求结果
            if (HttpStatus.OK.value() == exchange.getStatusCode().value() && exchange.hasBody()) {
                CommonRes<LeaderToFollowerHeartbeatResponse> res = exchange.getBody();
                log.info("收到心跳响应为 {}", JSONUtil.toJsonStr(res));
                assert res != null;
                if (res.getSuccess()) {
                    LeaderToFollowerHeartbeatResponse response = res.getData();
                    heartbeatResponseTable.put(response.getResponsePeerId(), response);
                }
            } else {
                log.info("收到心跳响应为 {}", exchange.getStatusCodeValue());
            }
        } catch (Exception e) {
            log.error("发送心跳异常: {}", e.getMessage());
        }
    }

    /**
     * 如果使用kill方式杀掉进程 使用该线程 来检测follower是否存活
     * 检查follower节点心跳响应是否超过阈值
     * 如果超过阈值，则剔除该节点，然后重新对任务进行分片
     */
    private void doFollowerAliveCheck() {
        // 遍历你的心跳结果的table
        heartbeatResponseTable.forEach((peerId, leaderToFollowerHeartbeatResponse) -> {
            // 每个follower最近的一次接收到leader心跳，以及针对leader心跳返回响应 的时间
            // 当前时间，减去上一次的follower心跳时间，这个时间间隔，如果超过了，120s，就说明这个follower已经宕机了
            if (System.currentTimeMillis() - leaderToFollowerHeartbeatResponse.getLastResponseTs() >=
                    tendConsistencyConfiguration.getJudgeFollowerDownSecondsThreshold() * 1000L) {
                log.info("检测到peerId={}的节点超过给定阈值内未与leader建立通信关系，leader重新规划任务分片", peerId);
                // ip:port
                String peerAddress = availableShardingInstances.get(peerId);
                peersConfigList.remove(peerAddress + ":" + peerId);
                // 重新基于还剩下的存活 的follower列表，重新分片分配，他就会在下一次的leader发送的心跳的时候，把你的最新的分片分配的信息带过去
                consistencyTaskShardingHandler.doTaskSharding(peersConfigList);
            }
        });
    }

    /**
     * 重新划分任务分片或重新选举
     */
    private void reShardTaskAndReElection() {
        clearShardingContext();
        startElectionProcessAndDoTaskSharding();
    }

    /**
     * 解析配置文件配置的一致性任务集群地址信息为list格式
     *
     * @param peersConfig 配置信息
     * @return 集群地址列表
     */
    private List<String> parsePeersConfigToList(String peersConfig) {
        String[] splitPeers = peersConfig.split(",");
        List<String> peersConfigList = new ArrayList<>(splitPeers.length);
        peersConfigList.addAll(Arrays.asList(splitPeers));
        return peersConfigList;
    }

    /**
     * 判断自己是不是leader
     *
     * @param leaderPeerId 选举出来的leader角色的id
     * @param peerInfoMap  集群节点信息的map
     * @return 结果
     */
    private boolean leaderIsMySelf(String leaderPeerId, Map<String, String> peerInfoMap) {
        String currentPeerAddress = getCurrentPeerAddress() + ":" + this.currentServerPort;
        String leaderIpAndPort = peerInfoMap.get(leaderPeerId);
        return currentPeerAddress.equals(leaderIpAndPort);
    }

    /**
     * 启动任务执行引擎
     */
    private void doStartTaskExecuteEngine() {
        try {
            taskScheduleManager.performanceTask();
        } catch (Exception e) {
            log.error("执行任务时，发生异常", e);
        }
    }

    /**
     * 当完成任务分片的事件发生
     *
     * @param taskShardingResult 任务分片结果
     */
    @Subscribe
    public void onFinishTaskSharding(Map<String, List<Long>> taskShardingResult) {
        buildShardingContext(taskShardingResult);
    }

    /**
     * 当集群成员变更的事件发生
     *
     * @param registerResponse 节点注册响应
     */
    @Subscribe
    public void onPeerGroupChanged(RegisterOrCancelResponse registerResponse) {
        log.info("收到集群成员变更的消息 内容为:{}", JSONUtil.toJsonStr(registerResponse));
        Integer opType = registerResponse.getRegisterOrCancelRequest().getOpType();
        RegisterOrCancelRequest registerOrCancelRequest = registerResponse.getRegisterOrCancelRequest();
        // 上下线节点的唯一标识
        String peerIdentify = getNewPeerIdentify(registerOrCancelRequest);
        // 如果是leader且发送的是上线请求
        if (PeerOpTypeEnum.ONLINE.getOpType().equals(opType) && registerResponse.isLeader()) {
            log.info("进入到新节点重新注册的流程");
            if (!peersConfigList.contains(peerIdentify)) {
                peersConfigList.add(peerIdentify); // 新上线的节点，加入到你的peersConfigList里去，加入到你的集群节点列表里去
                // 任务重新分片
                consistencyTaskShardingHandler.doTaskSharding(peersConfigList);
                log.info("新增节点[{}]完毕，已启动重新分片的流程", peerIdentify);
            }
            // 如果是下线请求 且 下线的是follower 且 下线的是 接收请求的是leader节点
        } else if (PeerOpTypeEnum.OFFLINE.getOpType().equals(opType) && !registerResponse.getRegisterOrCancelRequest().isLeaderOffline() && registerResponse.isLeader()) {
            log.info("进入到follower节点下线的流程");
            if (peersConfigList.contains(peerIdentify)) {
                peersConfigList.remove(peerIdentify);
                heartbeatResponseTable.remove(registerOrCancelRequest.getPeerId());
                // 任务重新分片
                consistencyTaskShardingHandler.doTaskSharding(peersConfigList);
                log.info("删除节点[{}]完毕，已启动重新分片的流程", peerIdentify);
            }
            // 如果是下线请求且下线的是leader节点 接收请求的是follower
        } else if (PeerOpTypeEnum.OFFLINE.getOpType().equals(opType) && registerResponse.getRegisterOrCancelRequest().isLeaderOffline() && !registerResponse.isLeader()) {
            log.info("进入到leader节点下线 重新选举leader的流程");
            if (peersConfigList.contains(peerIdentify)) {
                peersConfigList.remove(peerIdentify);
                // 重新选举和任务分片
                reShardTaskAndReElection();
                log.info("leader [{}] 下线完毕，已启动重新选举和重新分片的流程", peerIdentify);
            }
        }
    }

    /**
     * 构造根据节点注册请求构造节点唯一标识
     *
     * @param registerOrCancelRequest 注册请求
     * @return 新节点的唯一标识
     */
    private String getNewPeerIdentify(RegisterOrCancelRequest registerOrCancelRequest) {
        StringJoiner newPeerIdentifyJoiner = new StringJoiner(":", "", "");
        newPeerIdentifyJoiner
                .add(registerOrCancelRequest.getIp())
                .add(registerOrCancelRequest.getPort())
                .add(registerOrCancelRequest.getPeerId());
        return newPeerIdentifyJoiner.toString();
    }

    /**
     * 当前节点的分片索引好列表
     *
     * @return 当前节点的分片索引好列表
     */
    public List<Long> getMyTaskShardIndexes() {
        return consistencyTaskShardingContext.getTaskSharingResult()
                .get(getCurrentPeerAddress() + ":" + this.currentServerPort + ":" + consistencyTaskShardingContext.getCurrentPeerId());
    }

    /**
     * 获取一致性任务分片上下文
     *
     * @return 上下文
     */
    public ConsistencyTaskShardingContext getConsistencyTaskShardingContext() {
        return consistencyTaskShardingContext;
    }

    /**
     * 根据leaderToFollowerHeartbeatRequest请求对象 设置分片上下文 follower收到leader的分片结果时，使用
     *
     * @param leaderToFollowerHeartbeatRequest leader往follower发送心跳请求
     */
    public void setConsistencyTaskShardingContext(LeaderToFollowerHeartbeatRequest leaderToFollowerHeartbeatRequest) {
        consistencyTaskShardingContext.setChecksum(leaderToFollowerHeartbeatRequest.getChecksum());
        consistencyTaskShardingContext.setTaskSharingResult(leaderToFollowerHeartbeatRequest.getCacheSharingResult());
    }

    /**
     * 构造分片上下文
     *
     * @param taskShardingResult 一致性任务分片结果
     */
    private void buildShardingContext(Map<String, List<Long>> taskShardingResult) {
        consistencyTaskShardingContext.setTaskSharingResult(taskShardingResult);
        consistencyTaskShardingContext.setChecksum(SecureUtil.md5(JSONUtil.toJsonStr(taskShardingResult)));
    }

    /**
     * 清空一致性任务上下文中的分片信息
     */
    private void clearShardingContext() {
        consistencyTaskShardingContext.setTaskSharingResult(null);
    }

}
