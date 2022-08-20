package com.ruyuan.consistency.config;

import com.ruyuan.consistency.model.ConsistencyTaskInstance;
import com.ruyuan.consistency.remote.message.RegisterOrCancelResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 框架线程池相关的配置
 *
 * @author zhonghuashishan
 */
@Component
public class ThreadPoolConfig {

    /**
     * 一致性任务线程名称前缀
     */
    private static final String CONSISTENCY_TASK_THREAD_POOL_PREFIX = "CTThreadPool_";
    /**
     * 用于节点新增和取消的线程池名称前缀
     */
    private static final String PEER_REGISTER_THREAD_POOL_PREFIX = "PeerAddOrCancelPool_";

    /**
     * 告警线程名称的前缀
     */
    private static final String ALERT_THREAD_POOL_PREFIX = "AlertThreadPool_";

    /**
     * 获取框架级的配置
     */
    @Autowired
    private TendConsistencyConfiguration tendConsistencyConfiguration;

    /**
     * 一致性任务执行的并行任务执行线程池
     *
     * @return 并行任务线程池
     */
    @Bean
    public CompletionService<ConsistencyTaskInstance> consistencyTaskPool() {
        LinkedBlockingQueue<Runnable> asyncConsistencyTaskThreadPoolQueue =
                new LinkedBlockingQueue<>(tendConsistencyConfiguration.getThreadPoolQueueSize());
        ThreadPoolExecutor asyncReleaseResourceExecutorPool = new ThreadPoolExecutor(
                tendConsistencyConfiguration.getThreadCorePoolSize(),
                tendConsistencyConfiguration.getThreadCorePoolSize(),
                tendConsistencyConfiguration.getThreadPoolKeepAliveTime(),
                TimeUnit.valueOf(tendConsistencyConfiguration.getThreadPoolKeepAliveTimeUnit()),
                asyncConsistencyTaskThreadPoolQueue,
                createThreadFactory(CONSISTENCY_TASK_THREAD_POOL_PREFIX)
        );
        return new ExecutorCompletionService<>(asyncReleaseResourceExecutorPool);
    }

    @Bean
    public CompletionService<RegisterOrCancelResponse> peerRegisterPool() {
        LinkedBlockingQueue<Runnable> asyncConsistencyTaskThreadPoolQueue =
                new LinkedBlockingQueue<>(50);
        ThreadPoolExecutor asyncReleaseResourceExecutorPool = new ThreadPoolExecutor(
                5,
                5,
                60,
                TimeUnit.SECONDS,
                asyncConsistencyTaskThreadPoolQueue,
                createThreadFactory(PEER_REGISTER_THREAD_POOL_PREFIX)
        );
        return new ExecutorCompletionService<>(asyncReleaseResourceExecutorPool);
    }

    /**
     * 用于告警通知的线程池
     *
     * @return 并行任务线程池
     */
    @Bean
    public ThreadPoolExecutor alertNoticePool() {
        LinkedBlockingQueue<Runnable> asyncAlertNoticeThreadPoolQueue =
                new LinkedBlockingQueue<>(100);
        return new ThreadPoolExecutor(
                3,
                5,
                60,
                TimeUnit.SECONDS,
                asyncAlertNoticeThreadPoolQueue,
                createThreadFactory(ALERT_THREAD_POOL_PREFIX)
        );
    }

    /**
     * 创建线程池工厂
     *
     * @param threadPoolPrefix 线程池前缀
     * @return 线程池工厂
     */
    private ThreadFactory createThreadFactory(String threadPoolPrefix) {
        return new ThreadFactory() {
            private AtomicInteger threadIndex = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, threadPoolPrefix + this.threadIndex.incrementAndGet());
            }
        };
    }

}
