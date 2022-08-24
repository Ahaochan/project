package com.ruyuan.process.engine.process;

import com.ruyuan.process.engine.enums.InvokeMethod;
import com.ruyuan.process.engine.node.ProcessorDefinition;
import com.ruyuan.process.engine.node.ProcessorNode;
import com.ruyuan.process.engine.store.ProcessStateStore;
import com.ruyuan.process.engine.utils.ProcessorUtils;
import lombok.extern.slf4j.Slf4j;
import moe.ahao.util.commons.io.JSONHelper;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * 流程上下文
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Slf4j
public class ProcessContext {

    public static final String META_CURRENT_EXECUTE_STATE_KEY = "current-execute-state";
    public static final String META_CURRENT_EXECUTE_NODE_KEY = "current-execute-node";
    public static final String META_PROCESS_NAME_KEY = "process-name";
    public static final String META_PROCESS_PARAM_KEY = "process-param";
    public static final String EXECUTE_STATE_INVOKE = "invoke";
    public static final String EXECUTE_STATE_ROLLBACK = "rollback";
    private final Map<String, Object> params = new HashMap<>();

    private final ProcessorDefinition processorDefinition;

    private final Stack<RollbackProcessor> rollbackProcessors = new Stack<>();
    private final String globalUniqueId;
    private ProcessStateStore processStateStore;

    public ProcessContext(String globalUniqueId, ProcessorDefinition processorDefinition) {
        this(globalUniqueId, processorDefinition, null);
    }

    public ProcessContext(String globalUniqueId, ProcessorDefinition processorDefinition,
                          ProcessStateStore processStateStore) {
        this.processorDefinition = processorDefinition;
        this.processStateStore = processStateStore;
        this.globalUniqueId = globalUniqueId;
    }

    public void set(String key, Object value) {
        if(key.contains("#")){
            // 如果带有#，会一致性机制数据异常
            throw new RuntimeException("参数名字不合法");
        }
        params.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) params.get(key);
    }

    /**
     * 启动
     */
    public void start() {
        start(null, null);
    }

    /**
     * 指定从流程的某个节点开始执行
     *
     * @param startNode 开始执行节点ID
     */
    public void start(String startNode, String key) {
        recordProcessMetaData(key);
        process(processorDefinition.getFirst(), true, startNode, key);
    }

    private void process(ProcessorNode node, boolean isMainThread, String startNode, String key) {
        Processor processor = node.getProcessor();
        if (node.getName().equals(startNode)) {
            // 如果当前节点是指定的开始节点，则从该节点开始执行
            startNode = null;
        }
        try {
            if (processor instanceof RollbackProcessor) {
                rollbackProcessors.push((RollbackProcessor) processor);
            }
            if (startNode == null) {
                updateMetadata(key, META_CURRENT_EXECUTE_NODE_KEY, node.getName());
                processor.process(this);
            }
        } catch (Exception e) {
            executeRollback(key, null);
            processor.caughtException(this, e);
            // 流程出现问题的时候，需要将异常往外抛，让业务方处理.
            throw e;
        }
        ProcessorNode nextNode = findNextNode(node);
        if (nextNode == null) {
            if (isMainThread) {
                // 如果是主线程的流程到达了最后一个节点，则标记整个流程已经执行完毕，把元数据清除
                clearMetadata(key);
            }
            return;
        }

        InvokeMethod invokeMethod = node.getInvokeMethod();
        if (invokeMethod.equals(InvokeMethod.SYNC)) {
            process(nextNode, isMainThread, startNode, key);
        } else {
            String finalStartNode = startNode;
            ProcessorUtils.executeAsync(() -> process(nextNode, false, finalStartNode, key));
        }
    }

    private ProcessorNode findNextNode(ProcessorNode node) {
        ProcessorNode nextNode = null;
        Map<String, ProcessorNode> nextNodes = node.getNextNodes();
        if (node.getProcessor() instanceof DynamicProcessor) {
            // 动态计算出下一个节点
            DynamicProcessor dynamicProcessor = (DynamicProcessor) node.getProcessor();
            String nextNodeId = dynamicProcessor.nextNodeId(this);
            if (!nextNodes.containsKey(nextNodeId)) {
                throw new IllegalArgumentException("DynamicProcess can not find next node with id:" + nextNodeId);
            }
            nextNode = nextNodes.get(nextNodeId);
        } else {
            // 不是动态节点，每个节点只有一个后继节点
            if (!nextNodes.isEmpty()) {
                nextNode = nextNodes.values().stream().findAny().get();
            }
        }
        return nextNode;
    }

    /**
     * 从某个节点开始回滚
     *
     * @param targetNodeId 节点ID
     */
    public void rollbackFrom(String key, String targetNodeId) {
        buildRollbackStack(processorDefinition.getFirst());
        executeRollback(key, targetNodeId);
    }

    private void buildRollbackStack(ProcessorNode node) {
        if (node == null) {
            return;
        }
        Processor processor = node.getProcessor();
        if (processor instanceof RollbackProcessor) {
            rollbackProcessors.push((RollbackProcessor) processor);
        }
        ProcessorNode nextNode = findNextNode(node);
        buildRollbackStack(nextNode);
    }

    private void executeRollback(String key, String targetNodeId) {
        // 回滚前面的所有可回滚节点，按照所有节点的顺序倒叙回滚
        RollbackProcessor rollbackProcessor;
        // 标记流程进入回滚流程
        updateMetadata(key, META_CURRENT_EXECUTE_STATE_KEY, EXECUTE_STATE_ROLLBACK);
        boolean skip = targetNodeId != null;
        while (!rollbackProcessors.empty()) {
            rollbackProcessor = rollbackProcessors.pop();
            if (skip) {
                // 比如要回滚的顺序节点id为：5,4,3，在回滚到4节点的时候宕机了，此时需要回滚的节点和顺序为： 4,3
                // 所以如果当前回滚节点 != 目标节点的时候，需要跳过节点，当遇到目标节点的时候，后续的都不需要跳过
                skip = !rollbackProcessor.getName().equals(targetNodeId);
            }
            if (skip) {
                continue;
            }
            try {
                // 记录当前正在回滚的节点
                updateMetadata(key, META_CURRENT_EXECUTE_NODE_KEY, rollbackProcessor.getName());
                rollbackProcessor.rollback(this);
            } catch (Exception e1) {
                // 如果回滚过程中也抛异常了，此时暂时没有什么好的办法处理。
                log.warn("流程节点在回滚过程中抛出异常，process={}, context={}", rollbackProcessor.getName(), params.values());
            }
        }
        clearMetadata(key);
    }


    private void updateMetadata(String redisKey, String key, String value) {
        if (processStateStore != null) {
            redisKey = redisKey == null ? buildKey() : redisKey;
            processStateStore.updateMetadata(redisKey, key, value);
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String paramKey = entry.getKey();
                String paramClassName = entry.getValue().getClass().getCanonicalName();
                String paramJSONValue = JSONHelper.toString(entry.getValue());
                String paramsKey = META_PROCESS_PARAM_KEY + "#" + paramKey + "#" + paramClassName;
                processStateStore.updateMetadata(redisKey, paramsKey, paramJSONValue);
            }
        }
    }

    /**
     * 记录元数据
     */
    private void recordProcessMetaData(String key) {
        Map<String, String> metaData = buildMetaData();
        key = key == null ? buildKey() : key;
        if (processStateStore != null) {
            processStateStore.recordProcessMetadata(key, metaData);
        }
    }

    /**
     * 清除元数据
     */
    private void clearMetadata(String key) {
        if (processStateStore != null) {
            key = key == null ? buildKey() : key;
            processStateStore.clearMetadata(key);
        }
    }

    private Map<String, String> buildMetaData() {
        Map<String, String> result = new HashMap<>();
        result.put(META_CURRENT_EXECUTE_STATE_KEY, EXECUTE_STATE_INVOKE);
        result.put(META_PROCESS_NAME_KEY, processorDefinition.getName());
        return result;
    }

    private String buildKey() {
        return globalUniqueId + ":" + processorDefinition.getName();
    }

    public Map<String, Object> params() {
        return new HashMap<>(params);
    }

}
