package moe.ahao.process.engine.wrapper.schedule;

import lombok.extern.slf4j.Slf4j;
import moe.ahao.process.engine.core.ProcessContext;
import moe.ahao.process.engine.core.enums.ProcessStatusEnum;
import moe.ahao.process.engine.core.store.ProcessStateStore;
import moe.ahao.process.engine.wrapper.ProcessContextFactory;
import moe.ahao.util.commons.io.JSONHelper;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.DisposableBean;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ProcessPlaybackTask implements Runnable, DisposableBean {
    private final ScheduledThreadPoolExecutor scheduledThreadPool;

    private final ProcessContextFactory processContextFactory;
    private final ProcessStateStore processStateStore;
    private final RedissonClient redissonClient;

    public ProcessPlaybackTask(ProcessContextFactory processContextFactory, ProcessStateStore processStateStore, RedissonClient redissonClient) {
        this.scheduledThreadPool = new ScheduledThreadPoolExecutor(1);
        this.scheduledThreadPool.scheduleAtFixedRate(this, 10L, 60, TimeUnit.SECONDS);

        this.processContextFactory = processContextFactory;
        this.processStateStore = processStateStore;
        this.redissonClient = redissonClient;
    }

    @Override
    public void destroy() throws Exception {
        scheduledThreadPool.shutdown();
    }

    @Override
    public void run() {
        // 1. 查出5分钟里还没跑完的流程
        Collection<String> keys = processStateStore.pollUnCompletedProcess(5, TimeUnit.MINUTES, processContextFactory.isRefresh());
        if (keys.isEmpty()) {
            return;
        }
        for (String key : keys) {
            // 2. 尝试对该流程加锁, 避免分布式多实例对同一个流程进行重放
            RLock lock = redissonClient.getLock(key);
            boolean locked = lock.tryLock();
            if (!locked) {
                continue;
            }
            try {
                Map<String, String> metadata = processStateStore.getMap(key);
                String state = metadata.get(ProcessStateStore.META_CURRENT_EXECUTE_STATE_KEY);
                String processName = metadata.get(ProcessStateStore.META_PROCESS_NAME_KEY);
                String currentNode = metadata.get(ProcessStateStore.META_CURRENT_EXECUTE_NODE_KEY);
                // 3. 创建流程上下文
                ProcessContext context = processContextFactory.getContext(processName, key);
                // 4. 为流程上下文初始化上下文参数
                for (Map.Entry<String, String> entry : metadata.entrySet()) {
                    if (!entry.getKey().startsWith(ProcessStateStore.META_PROCESS_PARAM_KEY)) {
                        continue;
                    }
                    String paramsKey = entry.getKey();
                    String[] split = paramsKey.split("#");
                    String paramKey = split[1];
                    String paramClassName = split[2];
                    String value = entry.getValue();
                    Class<?> clazz;
                    try {
                        clazz = Class.forName(paramClassName);
                    } catch (Exception e) {
                        try {
                            clazz = Class.forName(getInnerClassName(paramClassName));
                        } catch (Exception e1) {
                            log.error("反序列化流程参数失败：", e1);
                            return;
                        }
                    }
                    Object o = JSONHelper.parse(value, clazz);
                    context.set(paramKey, o);
                }
                // 5. 如果流程在正向执行阶段中断了, 直接start即可
                if (ProcessStatusEnum.PROCESS.getName().equals(state)) {
                    log.info("发现一个被中断的流程，开始重新执行流程，name={}, 从节点 {} 继续流程，参数：{}", processName, currentNode, JSONHelper.toString(context.params()));
                    context.start(currentNode);
                }
                // 6. 如果流畅在回滚阶段中断了, 就从记录的节点开始回滚
                else if (ProcessStatusEnum.ROLLBACK.getName().equals(state)) {
                    log.info("发现一个回滚过程中断的流程，开始重新执行回滚，name={}, 从节点 {} 开始回滚", processName, currentNode);
                    context.rollbackFrom(currentNode);
                }
            } finally {
                lock.unlock();
            }
        }
    }

    private String getInnerClassName(String className) {
        String[] split = className.split("\\.");
        String[] temp = new String[split.length - 1];
        System.arraycopy(split, 0, temp, 0, temp.length);
        String join = String.join(".", temp);
        return join + "$" + split[split.length - 1];
    }
}
