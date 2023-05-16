package moe.ahao.process.engine.core;

import lombok.extern.slf4j.Slf4j;
import moe.ahao.process.engine.core.definition.BizConfig;
import moe.ahao.process.engine.core.definition.ProcessorDefinition;
import moe.ahao.process.engine.core.definition.ProcessorNodeDefinition;
import moe.ahao.process.engine.core.enums.InvokeMethod;
import moe.ahao.process.engine.core.node.DynamicProcessorNode;
import moe.ahao.process.engine.core.node.ProcessorNode;
import moe.ahao.process.engine.core.node.RollbackProcessorNode;
import moe.ahao.process.engine.core.store.ProcessStateStore;
import moe.ahao.process.engine.core.utils.ProcessorUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * 流程上下文
 */
@Slf4j
public class ProcessContext {
    /**
     * 流程的唯一id
     */
    private final String globalUniqueId;
    /**
     * 流程的全局参数, 可以在任意节点获取到
     */
    private final Map<String, Object> params;
    /**
     * 流程定义, 节点是怎么扭转的
     */
    private final ProcessorDefinition processorDefinition;
    /**
     * 回滚栈, 在发生异常的时候pop出来执行回滚
     */
    private final Stack<RollbackProcessorNode> rollbackProcessors;
    /**
     * 流程状态持久化组件
     */
    private final ProcessStateStore processStateStore;

    public ProcessContext(String globalUniqueId, ProcessorDefinition processorDefinition, ProcessStateStore processStateStore) {
        this.globalUniqueId = globalUniqueId;
        this.params = new HashMap<>();
        this.processorDefinition = processorDefinition;
        this.rollbackProcessors = new Stack<>();
        this.processStateStore = processStateStore;
    }

    public void set(String key, Object value) {
        if (key.contains("#")) {
            // 如果带有#，会一致性机制数据异常
            throw new RuntimeException("参数名字不合法");
        }
        // 本次流程的全局参数, 可以在任意节点获取到
        params.put(key, value);
    }

    public <T> T get(String key) {
        // 本次流程的全局参数, 可以在任意节点获取到
        return (T) params.get(key);
    }

    /**
     * 启动
     */
    public void start() {
        start(null);
    }

    /**
     * 是否匹配当前的业务
     * @param businessIdentifier 业务线
     * @param orderType 订单类型
     */
    public boolean matchBiz(Integer businessIdentifier, Integer orderType) {
        return processorDefinition.matchBiz(businessIdentifier, orderType);
    }

    public Set<BizConfig> getMatchedBiz() {
        return processorDefinition.getBizConfigs();
    }

    /**
     * 指定从流程的某个节点开始执行
     *
     * @param startNode 开始执行节点ID
     */
    public void start(String startNode) {
        log.info("流程name:{}, globalUniqueId:{}, 开始执行start", processorDefinition.getName(), globalUniqueId);
        // 1. 初始化流程元数据信息到存储
        String processorName = processorDefinition.getName();
        processStateStore.initProcessMetadata(globalUniqueId, processorName);
        // 2. 开始流程节点的执行
        this.process(processorDefinition.getFirst(), true, startNode);
        log.info("流程name:{}, globalUniqueId:{}, 执行完毕finished", processorDefinition.getName(), globalUniqueId);
    }

    /**
     * 执行流程
     *
     * @param node         当前节点
     * @param isMainThread 是否主线程在执行
     * @param startNode    起始节点
     */
    private void process(ProcessorNodeDefinition node, boolean isMainThread, String startNode) {
        // 1. 获取当前节点的动作Processor
        ProcessorNode processor = node.getProcessorNode();
        // 如果当前节点不是要开始的节点, 就标记为跳过, 但是仍然要去压栈和其他操作
        boolean skip = startNode != null && !node.getName().equals(startNode);
        try {
            // 2. 如果是可回滚流程动作，就push到回滚栈，在发生异常的时候pop出来执行回滚
            if (processor instanceof RollbackProcessorNode) {
                rollbackProcessors.push((RollbackProcessorNode) processor);
            }
            // 3. 如果当前节点不是要开始的节点, 就跳过不执行
            if (!skip) {
                // 3.1. 每个流程节点执行的时候, 都在Redis存储下状态
                processStateStore.updateExecutionNodeMetadata(globalUniqueId, node.getName(), params);
                // 3.2. 执行当前节点的动作
                processor.process(this);
                log.info("流程name:{}, globalUniqueId:{}, node:{}, processor:{}, context:{}, 执行成功success", processorDefinition.getName(), globalUniqueId, node.getName(), processor.getName(), params);
            }
        } catch (Exception e) {
            log.error("流程name:{}, globalUniqueId:{}, node:{}, processor:{}, context:{}, 执行失败failure", processorDefinition.getName(), globalUniqueId, node.getName(), processor.getName(), params, e);
            // 出现异常先回滚
            this.rollback(null);
            // 然后执行流程动作的caughtException方法
            processor.caughtException(this, e);
            // 流程出现问题的时候，需要将异常往外抛，让业务方处理.
            throw e;
        }
        // 3. 获取下一个节点
        ProcessorNodeDefinition nextNode = this.findNextNode(node);
        if (nextNode == null) {
            if (isMainThread) {
                // 如果是主线程的流程到达了最后一个节点，则标记整个流程已经执行完毕，把元数据清除
                processStateStore.clearMetadata(globalUniqueId);
            }
            return;
        }

        // 4. 根据当前节点配置的invoke-method，决定下一个节点是同步还是异步调用
        InvokeMethod invokeMethod = node.getInvokeMethod();
        if (invokeMethod.equals(InvokeMethod.SYNC)) {
            this.process(nextNode, isMainThread, startNode);
        } else {
            ProcessorUtils.executeAsync(() -> this.process(nextNode, false, startNode));
        }
    }

    /**
     * 从某个节点开始回滚
     *
     * @param targetNodeId 节点ID
     */
    public void rollbackFrom(String targetNodeId) {
        // 1. 构建回滚栈
        this.buildRollbackStack(processorDefinition.getFirst());
        // 2. 执行回滚
        this.rollback(targetNodeId);
    }

    private void rollback(String targetNodeId) {
        // 1. 标记流程进入回滚流程
        processStateStore.updateRollbackMetadata(globalUniqueId, params);
        // 2. 从回滚栈中取出之前push进去的流程动作，进行流程回滚
        while (!rollbackProcessors.empty()) {
            RollbackProcessorNode rollbackProcessNode = rollbackProcessors.pop();
            // 比如要回滚的顺序节点id为：5,4,3，在回滚到4节点的时候宕机了，此时需要回滚的节点和顺序为： 4,3
            // 所以如果当前回滚节点 != 目标节点的时候，需要跳过节点，当遇到目标节点的时候，后续的都不需要跳过
            boolean skip = targetNodeId != null &&!rollbackProcessNode.getName().equals(targetNodeId);
            if (skip) {
                continue;
            }
            try {
                // 3. 记录当前正在回滚的节点
                processStateStore.updateExecutionNodeMetadata(globalUniqueId, rollbackProcessNode.getName(), params);
                // 4. 执行回滚动作
                rollbackProcessNode.rollback(this);
                log.info("流程name:{}, globalUniqueId:{}, processor:{}, context:{}, 回滚成功success", processorDefinition.getName(), globalUniqueId, rollbackProcessNode.getName(), params);
            } catch (Exception e) {
                // TODO 如果回滚过程中也抛异常了，此时暂时没有什么好的办法处理。
                log.error("流程name:{}, globalUniqueId:{}, processor:{}, context:{}, 回滚失败failure", processorDefinition.getName(), globalUniqueId, rollbackProcessNode.getName(), params, e);
            }
        }
        processStateStore.clearMetadata(globalUniqueId);
    }

    private ProcessorNodeDefinition findNextNode(ProcessorNodeDefinition node) {
        ProcessorNodeDefinition nextNode = null;
        Map<String, ProcessorNodeDefinition> nextNodes = node.getNextNodes();
        if (node.getProcessorNode() instanceof DynamicProcessorNode) {
            // 1. 如果是动态节点, 就计算出下一个节点的Id
            DynamicProcessorNode dynamicProcessor = (DynamicProcessorNode) node.getProcessorNode();
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

    private void buildRollbackStack(ProcessorNodeDefinition node) {
        if (node == null) {
            return;
        }
        ProcessorNode processor = node.getProcessorNode();
        if (processor instanceof RollbackProcessorNode) {
            rollbackProcessors.push((RollbackProcessorNode) processor);
        }
        ProcessorNodeDefinition nextNode = this.findNextNode(node);
        buildRollbackStack(nextNode);
    }

    public Map<String, Object> params() {
        return new HashMap<>(params);
    }
}
