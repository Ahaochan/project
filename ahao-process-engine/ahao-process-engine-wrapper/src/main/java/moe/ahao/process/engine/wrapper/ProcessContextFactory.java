package moe.ahao.process.engine.wrapper;

import lombok.extern.slf4j.Slf4j;
import moe.ahao.process.engine.core.ProcessContext;
import moe.ahao.process.engine.core.definition.ProcessorDefinition;
import moe.ahao.process.engine.core.store.ProcessStateStore;
import moe.ahao.process.engine.wrapper.instance.ProcessorCreator;
import moe.ahao.process.engine.wrapper.instance.ReflectNodeInstanceCreator;
import moe.ahao.process.engine.wrapper.model.ProcessModel;
import moe.ahao.process.engine.wrapper.parse.ProcessParser;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class ProcessContextFactory {
    /**
     * 解析后的流程模型
     */
    private final Map<String, ProcessorDefinition> processorDefinitionMap = new ConcurrentHashMap<>();
    private final ProcessorCreator instanceCreator;
    private final ProcessStateStore processStateStore;

    /**
     * 生成流程的唯一ID
     */
    private final String processEngineInstanceId;
    private final AtomicLong processInstanceCounter = new AtomicLong(0);

    private volatile boolean refresh = false;

    public ProcessContextFactory(ProcessParser processParser) {
        this(processParser, new ReflectNodeInstanceCreator());
    }

    public ProcessContextFactory(ProcessParser processParser, ProcessorCreator instanceCreator) {
        this(processParser, instanceCreator, new ProcessStateStore());
    }

    public ProcessContextFactory(ProcessParser processParser, ProcessStateStore processStateStore) {
        this(processParser, new ReflectNodeInstanceCreator(), processStateStore);
    }

    public ProcessContextFactory(ProcessParser processParser, ProcessorCreator instanceCreator, ProcessStateStore processStateStore) {
        this.instanceCreator = instanceCreator;
        this.processStateStore = processStateStore;
        this.processEngineInstanceId = UUID.randomUUID().toString().replace("-", "");
        log.info("初始化ProcessContextFactory, ProcessorCreator实现类:{}, ProcessStateStore实现类:{}",
            instanceCreator != null ? instanceCreator.getClass() : "null",
            processStateStore != null ? processStateStore.getClass() : "null");
        List<ProcessModel> modelList = processParser.parse();
        this.init(modelList, false);
    }

    /**
     * 初始化构造
     *
     * @param clear 是否清除之前的流程配置
     */
    private void init(List<ProcessModel> processModels, boolean clear) {
        // 1. 根据解析出的配置Model, 构造出流程定义实例对象
        Map<String, ProcessorDefinition> processorDefinitionMap = new ConcurrentHashMap<>();
        log.info("初始化流程数量:{}", processModels.size());
        for (ProcessModel processModel : processModels) {
            log.info("初始化流程name:{}", processModel.getName());
            ProcessorDefinition processorDefinition = processModel.buildDefinition(instanceCreator);
            log.info("初始化流程name:{}, 构造流程成功：\n{}", processModel.getName(), processorDefinition.toStr());
            processorDefinitionMap.put(processorDefinition.getName(), processorDefinition);
        }
        if (clear) {
            this.processorDefinitionMap.clear();
        }
        this.processorDefinitionMap.putAll(processorDefinitionMap);
    }

    public ProcessContext getContext(String name) {
        String globalUniqueId = this.getGlobalUniqueId(name);
        // 为每个流程创建独立的ProcessContext上下文
        return this.getContext(name, globalUniqueId);
    }
    public ProcessContext getContext(String name, String globalUniqueId) {
        ProcessorDefinition processorDefinition = processorDefinitionMap.get(name);
        if (processorDefinition == null) {
            return null;
        }
        // 为每个流程创建独立的ProcessContext上下文
        return new ProcessContext(globalUniqueId, processorDefinition, processStateStore);
    }

    /**
     * 监控配置变更，重新初始化
     *
     * @param modelList 解析完毕的流程节点配置集合
     */
    public void refresh(List<ProcessModel> modelList) {
        synchronized (this) {
            this.refresh = true;
            log.info("配置发生了变更, 重新初始化各业务编排流程start");
            this.init(modelList, false);
            log.info("配置发生了变更, 重新初始化各业务编排流程success");
        }
    }

    /**
     * 监控配置变更，重新初始化
     *
     * @param modelList 解析完毕的流程节点配置集合
     * @param clear    是否清除之前的流程配置
     */
    public void refresh(List<ProcessModel> modelList, boolean clear) {
        synchronized (this) {
            this.refresh = true;
            log.info("配置发生了变更, 重新初始化各业务编排流程start");
            this.init(modelList, clear);
            log.info("配置发生了变更, 重新初始化各业务编排流程success");
        }
    }

    public boolean isRefresh() {
        return this.refresh;
    }

    public void resetRefresh() {
        this.refresh = false;
    }

    /**
     * 获取ProcessContext的全局唯一id, 由UUID和自增id和流程名称组成
     * @return 如bc1a8c8f55bb421bb8f0f410b68dafa2:1:name
     */
    private String getGlobalUniqueId(String name) {
        return processEngineInstanceId + ":" + processInstanceCounter.incrementAndGet() + ":" + name;
    }
}
