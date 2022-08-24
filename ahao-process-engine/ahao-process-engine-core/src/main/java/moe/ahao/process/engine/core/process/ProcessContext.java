package moe.ahao.process.engine.core.process;

import lombok.extern.slf4j.Slf4j;
import moe.ahao.process.engine.core.enums.InvokeMethod;
import moe.ahao.process.engine.core.node.ProcessorDefinition;
import moe.ahao.process.engine.core.node.ProcessorNode;
import moe.ahao.process.engine.core.store.ProcessStateStore;
import moe.ahao.process.engine.core.utils.ProcessorUtils;
import moe.ahao.util.commons.io.JSONHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * 流程上下文
 */
@Slf4j
public class ProcessContext {
    public static final String META_CURRENT_EXECUTE_STATE_KEY = "current-execute-state";
    public static final String META_CURRENT_EXECUTE_NODE_KEY = "current-execute-node";
    public static final String META_PROCESS_NAME_KEY = "process-name";
    public static final String META_PROCESS_PARAM_KEY = "process-param";
    public static final String EXECUTE_STATE_INVOKE = "invoke";
    public static final String EXECUTE_STATE_ROLLBACK = "rollback";

    // 本次流程的全局参数，可以在任意节点获取到
    private final Map<String, Object> params = new HashMap<>();

    private final ProcessorDefinition processorDefinition;

    // 回滚栈, 在发生异常的时候pop出来执行回滚
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
        if (key.contains("#")) {
            // 如果带有#，会一致性机制数据异常
            throw new RuntimeException("参数名字不合法");
        }
        // 本次流程的全局参数，可以在任意节点获取到
        params.put(key, value);
    }

    public <T> T get(String key) {
        // 本次流程的全局参数，可以在任意节点获取到
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
        log.info("流程name:{}, globalUniqueId:{}, 开始执行start", processorDefinition.getName(), globalUniqueId);
        recordProcessMetaData(key);
        process(processorDefinition.getFirst(), true, startNode, key);
        log.info("流程name:{}, globalUniqueId:{}, 执行完毕finished", processorDefinition.getName(), globalUniqueId);
    }

    /**
     * 执行流程
     *
     * @param node         当前节点
     * @param isMainThread 是否主线程在执行
     * @param startNode    起始节点
     * @param key          流程的全局唯一标志
     */
    private void process(ProcessorNode node, boolean isMainThread, String startNode, String key) {
        // 1. 获取当前节点的动作Processor
        Processor processor = node.getProcessor();
        if (node.getName().equals(startNode)) {
            // 如果当前节点是指定的开始节点，则从该节点开始执行
            startNode = null;
        }
        // 2. 执行当前节点的动作
        try {
            if (processor instanceof RollbackProcessor) {
                // 如果是可回滚流程动作，就push到回滚栈，在发生异常的时候pop出来执行回滚
                rollbackProcessors.push((RollbackProcessor) processor);
            }
            if (startNode == null) {
                updateMetadata(key, META_CURRENT_EXECUTE_NODE_KEY, node.getName());
                // 2. 执行当前节点的动作
                processor.process(this);
                log.info("流程name:{}, globalUniqueId:{}, node:{}, processor:{}, context:{}, 执行成功success", processorDefinition.getName(), globalUniqueId, node.getName(), processor.getName(), params);
            }
        } catch (Exception e) {
            log.error("流程name:{}, globalUniqueId:{}, node:{}, processor:{}, context:{}, 执行失败failure", processorDefinition.getName(), globalUniqueId, node.getName(), processor.getName(), params, e);
            // 出现异常先回滚
            this.executeRollback(key, null);
            // 然后执行流程动作的caughtException方法
            processor.caughtException(this, e);
            // 流程出现问题的时候，需要将异常往外抛，让业务方处理.
            throw e;
        }
        // 3. 获取下一个节点
        ProcessorNode nextNode = this.findNextNode(node);
        if (nextNode == null) {
            if (isMainThread) {
                // 如果是主线程的流程到达了最后一个节点，则标记整个流程已经执行完毕，把元数据清除
                this.clearMetadata(key);
            }
            return;
        }

        // 4. 根据当前节点配置的invoke-method，决定下一个节点是同步还是异步调用
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
            // 1. 如果是动态节点, 就计算出下一个节点的Id
            DynamicProcessor dynamicProcessor = (DynamicProcessor) node.getProcessor();
            String nextNodeId = dynamicProcessor.nextNodeId(this);
            if (!nextNodes.containsKey(nextNodeId)) {
                throw new IllegalArgumentException("DynamicProcess can not find next node with id:" + nextNodeId);
            }
            nextNode = nextNodes.get(nextNodeId);
        } else {
            // 2. 如果不是动态节点，每个节点只有一个后继节点
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
        // 从回滚栈中取出之前push进去的流程动作，进行流程回滚
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
                // 执行回滚动作
                rollbackProcessor.rollback(this);
                log.info("流程name:{}, globalUniqueId:{}, processor:{}, context:{}, 回滚成功success", processorDefinition.getName(), globalUniqueId, rollbackProcessor.getName(), params);
            } catch (Exception e) {
                // TODO 如果回滚过程中也抛异常了，此时暂时没有什么好的办法处理。
                log.error("流程name:{}, globalUniqueId:{}, processor:{}, context:{}, 回滚失败failure", processorDefinition.getName(), globalUniqueId, rollbackProcessor.getName(), params, e);
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
