package moe.ahao.process.engine.core.node;

import moe.ahao.process.engine.core.ProcessContext;

/**
 * 可以动态选择下一节点的流程动作
 */
public abstract class DynamicProcessorNode extends AbstractProcessorNode {
    /**
     * 获取下一个节点的id
     *
     * @param context 上下文
     * @return 下一个节点的id
     */
    public abstract String nextNodeId(ProcessContext context);
}
