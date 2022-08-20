package moe.ahao.tend.consistency.core.election;

import lombok.extern.slf4j.Slf4j;
import moe.ahao.tend.consistency.core.adapter.message.FollowerToLeaderHeartbeatResponse;
import moe.ahao.tend.consistency.core.adapter.message.RegisterOrCancelRequest;
import moe.ahao.tend.consistency.core.adapter.message.RegisterOrCancelResponse;
import moe.ahao.tend.consistency.core.adapter.scheduler.SchedulerManager;
import moe.ahao.tend.consistency.core.election.entity.PeerNode;
import moe.ahao.tend.consistency.core.election.entity.PeerNodeId;
import moe.ahao.tend.consistency.core.infrastructure.config.TendConsistencyConfiguration;
import moe.ahao.tend.consistency.core.infrastructure.config.properties.PeerNodeConfigProperties;
import moe.ahao.tend.consistency.core.infrastructure.enums.PeerOpTypeEnum;
import moe.ahao.tend.consistency.core.infrastructure.gateway.PeerNodeGateway;
import moe.ahao.tend.consistency.core.manager.TaskScheduleManager;
import moe.ahao.tend.consistency.core.sharding.ConsistencyTaskShardingContext;
import moe.ahao.tend.consistency.core.sharding.ConsistencyTaskShardingHandler;
import moe.ahao.tend.consistency.core.utils.NetUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * 集群节点选举及任务分片锁使用的处理器
 **/
@Slf4j
@Component
public class PeerElectionHandler implements ApplicationListener<WebServerInitializedEvent>, InitializingBean, DisposableBean {
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
     * 任务分片上下文
     */
    private final ConsistencyTaskShardingContext consistencyTaskShardingContext = ConsistencyTaskShardingContext.getInstance();;

    @Autowired
    private PeerNodeGateway peerNodeGateway;

    // 很关键的一个方法，系统启动的时候，识别出来你的这个bean组件里，实现了一个指定的接口
    // 他就知道说，你想要去监听系统启动的事件，他会把一个事件传递给你
    // 系统只要一旦说 启动了，就会来触发这个方法的执行和运行，执行start，完成你的框架初始化、选举以及发起定时调度的全过程
    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        this.start();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.start();
    }

    @Override
    public void destroy() {
        log.info("peerId={}的节点下线", consistencyTaskShardingContext.getCurrentPeerId());
        // 构造节点下线请求，通知集群中的其他节点
        this.checkIsExistLeaderAndNotifyAddOrCancel(PeerOpTypeEnum.OFFLINE);
        log.info("peerId={}的节点下线完毕", consistencyTaskShardingContext.getCurrentPeerId());
    }

    /**
     * 启动
     */
    public void start() {
        // 初始化框架
        this.init();
        // 选举并执行执行一致性任务的分片
        this.startElectionProcessAndDoTaskSharding();
        // 启动一致性任务执行引擎，定时的扫描db和本地的rocksdb里的任务，来执行了
        // aop切面里，如果说你提交任务，db写失败，就自动降级写入到rocksdb里去，系统正常在运行
        // 就会自动的去处理本地的rocksdb里的任务
        // 启动执行引擎
        schedulerManager.createConsistencyTaskScheduler(this::doStartTaskExecuteEngine);
    }

    private void init() {
        // 1. 解析集群的地址信息, TODO 改造成从注册中心获取
        List<PeerNode> peerNodes = peerNodeConfigProperties.parsePeerNodes();
        PeerNodeManager peerNodeManager = PeerNodeManager.getInstance();
        peerNodeManager.initPeerNodes(peerNodes);
        // 2.
        String currentPeerAddress = NetUtils.getCurrentPeerAddress() + ":" + currentServerPort;
        for (PeerNode peerNode : peerNodes) {
            // 获取当前节点的peerId
            if (peerNode.getAddress().equals(currentPeerAddress)) {
                consistencyTaskShardingContext.setCurrentPeerId(peerNode.getId());
            }
        }
    }

    /**
     * 启动选举流程并执行执行一致性任务的分片
     */
    private void startElectionProcessAndDoTaskSharding() {
        PeerNodeId leaderPeerId = null;

        // 检测集群中是否存在leader，如果存在则直接加入leader，不存在则选举最小节点作为leader
        // 刚开始第一次启动，集群里还没有leader存在，还是需要去进行选举的，leader选举其实很简单，leader主要扮演的角色，就是跟follower保持心跳
        // 他可以根据follower数量，分配分片数量和负责的分片号
        // 对于leader选举，没有特殊的规则再里面，默认就是按照id最小的节点来选举为一个leader就可以了
        // 没有任何一个节点，他是leader节点，此时你会收到一堆节点返回的响应

        // 每个框架实例随着系统在一台服务器上启动了之后，首先就会来干这个事儿
        // 每个节点都会发送一个online请求给其他的节点，这个操作主要是去判断说是否已经有一个leader了
        // 一开始默认情况下是不会有leader的，所以这个事情他是不会有任何的结果的

        // 1. 发送上线请求给集群内的其他节点
        List<RegisterOrCancelResponse> registerOrCancelResponses = this.checkIsExistLeaderAndNotifyAddOrCancel(PeerOpTypeEnum.ONLINE);
        // 2. 记录leader节点的id
        if (CollectionUtils.isNotEmpty(registerOrCancelResponses)) {
            RegisterOrCancelResponse registerResponse = registerOrCancelResponses.get(0);
            leaderPeerId = new PeerNodeId(registerResponse.getReplyPeerId());
        }
        // 3. 如果当前集群中没有leader, 就选举一个作为leader, 现在是取最小节点id作为leader
        if (leaderPeerId == null) {
            leaderPeerId = PeerNodeManager.getInstance().getPeerNodes().stream()
                .min(Comparator.comparing(o -> o.getId().getVal()))
                .map(PeerNode::getId)
                .orElse(null);
        }
        if (leaderPeerId == null) {
            log.warn("未找到leaderPeerId，退出选举流程");
            return;
        }
        // 4. 将当前的leaderId设置到分片节点上下文
        consistencyTaskShardingContext.setCurrentLeaderPeerId(leaderPeerId);

        String currentPeerAddress = NetUtils.getCurrentPeerAddress() + ":" + this.currentServerPort;
        PeerNode leaderPeerNode = PeerNodeManager.getInstance().getAvailableShardingInstances().get(leaderPeerId);
        boolean isLeader = currentPeerAddress.equals(leaderPeerNode.getAddress());
        log.info("当前节点成为 [{}] 节点", isLeader ? "leader" : "follower");

        // 5. 如果还没有进行过分片, 并且当前节点是leader, 那么leader节点就需要进行任务分片
        if (MapUtils.isEmpty(consistencyTaskShardingContext.getTaskSharingResult()) && isLeader) {
            consistencyTaskShardingHandler.doTaskSharding(PeerNodeManager.getInstance().getPeerNodes());
        }

        // 6. 启动当前节点相关的所有定时调度器
        this.startScheduler(isLeader);
    }

    /**
     * 检测集群中是否存在leader并通知各个节点当前节点上线或者下线的操作
     *
     * @return 结果
     */
    private List<RegisterOrCancelResponse> checkIsExistLeaderAndNotifyAddOrCancel(PeerOpTypeEnum peerOpType) {
        // 1. 获取当前节点在集群中的唯一标识
        String currentPeerIdentify = NetUtils.getCurrentPeerAddress() + ":" + currentServerPort + ":" + consistencyTaskShardingContext.getCurrentPeerId();

        // 2. 发送注册请求给除自己以外的其他节点
        List<Future<RegisterOrCancelResponse>> futures = new ArrayList<>();
        for (PeerNode peer : PeerNodeManager.getInstance().getPeerNodes()) {
            // 2.1. 排除掉自己
            if (peer.toString().equals(currentPeerIdentify)) {
                continue;
            }
            // 构造请求对象
            // 构造的其实就是你的一个http请求，封装当前你的这个节点的ip、port、id、op=online，当前节点是否为leader
            RegisterOrCancelRequest request = RegisterOrCancelRequest.builder()
                .ip(NetUtils.getCurrentPeerAddress())
                .port(String.valueOf(currentServerPort))
                .peerId(consistencyTaskShardingContext.getCurrentPeerId().getVal())
                .opType(peerOpType.getCode())
                .leaderOffline(consistencyTaskShardingContext.getCurrentPeerId().equals(consistencyTaskShardingContext.getCurrentLeaderPeerId()))
                .build();

            // 2.2. 发送http请求给其他节点, 通知其他节点当前节点上线了
            Future<RegisterOrCancelResponse> future = peerNodeGateway.registerAsync(peer, request);
            futures.add(future);
        }
        List<RegisterOrCancelResponse> result = new ArrayList<>(PeerNodeManager.getInstance().getPeerNodes().size());
        try {
            // 拿到所有的结果再返回
            for (int i = 0; i < futures.size() - 1; i++) {
                RegisterOrCancelResponse registerResponse = futures.get(i).get();
                result.add(registerResponse);
            }
            // 找到是leader且注册成功的返回结果
            result = result.stream().filter(Objects::nonNull).filter(RegisterOrCancelResponse::isLeader).collect(Collectors.toList());
            return result;
        } catch (Exception e) {
            log.error("获取集群中leader结果时，发生异常", e);
            return result;
        }
    }

    /**
     * 启动当前节点所有调度器
     *
     * @param isLeader 是否为leader
     */
    private void startScheduler(boolean isLeader) {
        // 1. 取消当前节点所有的调度任务
        schedulerManager.cancelAllScheduler();

        // 如果判断当前节点是leader节点
        // 如果你是leader，你得不停的给所有的follower发送心跳，你得定时检查follower发送过来的心跳，follower是否宕机
        if (isLeader) {
            // 创建leader发送心跳的定时调度任务 同时也会发送任务分片信息
            // 作为leader，每隔10s，要发送一下心跳消息给所有的follower这样子，这个是第一个
            schedulerManager.startLeaderToFollowerHeartbeatScheduler();
            // 创建用于检测follower是否存活的调度任务
            // 他作为leader自己，也需要创建定时任务，他要检查follower发送过来的心跳，是否过期，如果说过期了，他就要去摘除这个follower实例
            // 重新进行分片分配
            schedulerManager.startFollowerAliveCheckScheduler(this::doFollowerAliveCheck);
        }
        // 如果你是follower，你得不停的给leader发送心跳，你得定时检查leader发送给你的心跳，leader是否宕机
        else {
            // 如果是follower节点需要创建的线程以及需要初始化的对象
            PeerNodeManager.getInstance().setRecentlyFollowerToLeaderHeartbeatResponse(FollowerToLeaderHeartbeatResponse
                .builder()
                .success(true)
                .replyTimestamp(System.currentTimeMillis())
                .build());
            // 创建follower发送给leader的定时调度任务
            // 对于如果你是follower的话，你就需要定时每隔10s发送你自己的心跳给leader
            schedulerManager.startFollowerHeartbeatScheduler();
            // 创建用于检测leader是否宕机的定时调度任务
            // 你必须定时检查leader心跳，每隔10s检查一下，如果超过120s leader没有心跳过来，此时就认为leader宕机了，重新选举一个新leader出来
            schedulerManager.startLeaderAliveScheduler(this::doLeaderAliveCheck);
        }
    }

    /**
     * follower检测leader是否存活
     */
    private void doLeaderAliveCheck() {
        // 每隔10s跑一次检查，如果说上一次leader心跳响应，到现在为止，超过了120s了
        // 此时就可以认为说你的leader也是宕机了
        // 如果follower超过指定阈值还无法与leader通信，判定为leader宕机重新选举，重新下发任务分片
        if (System.currentTimeMillis() - PeerNodeManager.getInstance().getRecentlyFollowerToLeaderHeartbeatResponse().getReplyTimestamp()
            > tendConsistencyConfiguration.getJudgeLeaderDownSecondsThreshold() * 1000L) {
            PeerNodeId currentLeaderPeerId = consistencyTaskShardingContext.getCurrentLeaderPeerId();
            PeerNode peerNode = PeerNodeManager.getInstance().getAvailableShardingInstances().get(currentLeaderPeerId);
            PeerNodeManager.getInstance().getPeerNodes().remove(peerNode);
            // 重新选举和任务分片
            reShardTaskAndReElection();
        }
    }

    /**
     * 如果使用kill方式杀掉进程 使用该线程 来检测follower是否存活
     * 检查follower节点心跳响应是否超过阈值
     * 如果超过阈值，则剔除该节点，然后重新对任务进行分片
     */
    private void doFollowerAliveCheck() {
        // 遍历你的心跳结果的table
        PeerNodeManager.getInstance().getHeartbeatResponseTable().forEach((peerId, leaderToFollowerHeartbeatResponse) -> {
            // 每个follower最近的一次接收到leader心跳，以及针对leader心跳返回响应 的时间
            // 当前时间，减去上一次的follower心跳时间，这个时间间隔，如果超过了，120s，就说明这个follower已经宕机了
            if (System.currentTimeMillis() - leaderToFollowerHeartbeatResponse.getLastResponseTs() >=
                tendConsistencyConfiguration.getJudgeFollowerDownSecondsThreshold() * 1000L) {
                log.info("检测到peerId={}的节点超过给定阈值内未与leader建立通信关系，leader重新规划任务分片", peerId);
                // ip:port
                PeerNode peerAddress = PeerNodeManager.getInstance().getAvailableShardingInstances().get(peerId);
                PeerNodeManager.getInstance().getPeerNodes().remove(peerAddress);
                // 重新基于还剩下的存活 的follower列表，重新分片分配，他就会在下一次的leader发送的心跳的时候，把你的最新的分片分配的信息带过去
                consistencyTaskShardingHandler.doTaskSharding(PeerNodeManager.getInstance().getPeerNodes());
            }
        });
    }

    /**
     * 重新划分任务分片或重新选举
     */
    public void reShardTaskAndReElection() {
        // 清空一致性任务上下文中的分片信息
        consistencyTaskShardingContext.setTaskSharingResult(null);
        startElectionProcessAndDoTaskSharding();
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
     * 当前节点的分片索引好列表
     *
     * @return 当前节点的分片索引好列表
     */
    public List<Long> getMyTaskShardIndexes() {
        return consistencyTaskShardingContext.getTaskSharingResult()
            .get(NetUtils.getCurrentPeerAddress() + ":" + this.currentServerPort + ":" + consistencyTaskShardingContext.getCurrentPeerId());
    }

    /**
     * 获取一致性任务分片上下文
     *
     * @return 上下文
     */
    public ConsistencyTaskShardingContext getConsistencyTaskShardingContext() {
        return consistencyTaskShardingContext;
    }
}
