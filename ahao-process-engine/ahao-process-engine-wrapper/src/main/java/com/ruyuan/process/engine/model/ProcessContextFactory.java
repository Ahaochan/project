package com.ruyuan.process.engine.model;

import com.ruyuan.process.engine.instance.ProcessorInstanceCreator;
import com.ruyuan.process.engine.instance.ReflectNodeInstanceCreator;
import com.ruyuan.process.engine.node.ProcessorDefinition;
import com.ruyuan.process.engine.process.ProcessContext;
import com.ruyuan.process.engine.store.ProcessStateStore;
import lombok.extern.slf4j.Slf4j;
import moe.ahao.util.commons.io.JSONHelper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Slf4j
public class ProcessContextFactory {

    public static final ProcessorInstanceCreator DEFAULT_INSTANCE_CREATOR = new ReflectNodeInstanceCreator();
    private List<ProcessModel> modelList;
    private final Map<String, ProcessorDefinition> processorDefinitionMap = new ConcurrentHashMap<>();
    private final ProcessorInstanceCreator instanceCreator;
    private final ProcessStateStore processStateStore;
    private final AtomicLong processInstanceCounter = new AtomicLong(0);
    private final String processEngineInstanceId;
    private AtomicBoolean scheduleStarted = new AtomicBoolean(false);
    private final ScheduledThreadPoolExecutor scheduledThreadPool = new ScheduledThreadPoolExecutor(1);
    private volatile boolean refresh = false;

    public ProcessContextFactory(List<ProcessModel> modeList) throws Exception {
        this(modeList, DEFAULT_INSTANCE_CREATOR);
    }

    public ProcessContextFactory(List<ProcessModel> modeList, ProcessorInstanceCreator instanceCreator) throws Exception {
        this(modeList, instanceCreator, null);
    }

    public ProcessContextFactory(List<ProcessModel> modeList, ProcessStateStore processStateStore) throws Exception {
        this(modeList, DEFAULT_INSTANCE_CREATOR, processStateStore);
    }

    public ProcessContextFactory(List<ProcessModel> modeList, ProcessorInstanceCreator instanceCreator,
                                 ProcessStateStore processStateStore) throws Exception {
        this.modelList = modeList;
        this.instanceCreator = instanceCreator;
        this.processStateStore = processStateStore;
        this.processEngineInstanceId = UUID.randomUUID().toString().replace("-", "");
        init();
    }

    private void init() throws Exception {
        for (ProcessModel processModel : modelList) {
            processModel.check();
        }
        for (ProcessModel processModel : modelList) {
            ProcessorDefinition processorDefinition = processModel.build(instanceCreator);
            log.info("构造流程成功：\n{}", processorDefinition.toStr());
            processorDefinitionMap.put(processorDefinition.getName(), processorDefinition);
        }
        if (processStateStore != null) {
            if (scheduleStarted.compareAndSet(false, true)) {
                scheduledThreadPool.scheduleAtFixedRate(new ProcessPlaybackTask(processStateStore, this), 0L, 60,
                        TimeUnit.SECONDS);
            }
        }
    }

    public ProcessContext getContext(String name) {
        ProcessorDefinition processorDefinition = processorDefinitionMap.get(name);
        if (processorDefinition == null) {
            throw new IllegalArgumentException("流程不存在");
        }
        String globalUniqueId = processEngineInstanceId + ":" + processInstanceCounter.incrementAndGet();
        return new ProcessContext(globalUniqueId, processorDefinition, processStateStore);
    }

    public void refresh(List<ProcessModel> modeList) throws Exception {
        synchronized (this) {
            this.modelList = modeList;
            this.refresh = false;
            init();
        }
    }

    public boolean isRefresh() {
        return refresh;
    }

    private static class ProcessPlaybackTask implements Runnable {
        private final ProcessContextFactory processContextFactory;
        private final ProcessStateStore processStateStore;

        public ProcessPlaybackTask(ProcessStateStore processStateStore, ProcessContextFactory processContextFactory) {
            this.processStateStore = processStateStore;
            this.processContextFactory = processContextFactory;
        }

        @Override
        public void run() {
            Collection<String> keys = processStateStore.pollUnCompletedProcess(5, TimeUnit.MINUTES,
                    processContextFactory.isRefresh());
            if (keys == null || keys.isEmpty()) {
                return;
            }
            for (String key : keys) {
                // 尝试对该任务进行加锁，避免分布式多实例对同一个流程进行重放
                boolean lock = processStateStore.lock(key);
                if (!lock) {
                    continue;
                }
                try {
                    Map<String, String> metadata = processStateStore.getMap(key);
                    String state = metadata.get(ProcessContext.META_CURRENT_EXECUTE_STATE_KEY);
                    String processName = metadata.get(ProcessContext.META_PROCESS_NAME_KEY);
                    String currentNode = metadata.get(ProcessContext.META_CURRENT_EXECUTE_NODE_KEY);
                    ProcessContext context = processContextFactory.getContext(processName);
                    for (Map.Entry<String, String> entry : metadata.entrySet()) {
                        if (!entry.getKey().startsWith(ProcessContext.META_PROCESS_PARAM_KEY)) {
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
                    if (ProcessContext.EXECUTE_STATE_INVOKE.equals(state)) {
                        // 流程在执行阶段中断了
                        log.info("发现一个被中断的流程，开始重新执行流程，name={}, 从节点 {} 继续流程，参数：{}", processName, currentNode,
                            JSONHelper.toString(context.params()));
                        context.start(currentNode, key);
                    } else if (ProcessContext.EXECUTE_STATE_ROLLBACK.equals(state)) {
                        // 流程在回滚阶段中断了
                        log.info("发现一个回滚过程中断的流程，开始重新执行回滚，name={}, 从节点 {} 开始回滚", processName, currentNode);
                        context.rollbackFrom(key, currentNode);
                    }
                } finally {
                    processStateStore.unlock(key);
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
}
