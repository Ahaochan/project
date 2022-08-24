package moe.ahao.process.engine.core.process;

import lombok.NoArgsConstructor;

/**
 * 可以动态选择下一节点的流程动作
 */
@NoArgsConstructor
public abstract class DynamicProcessor extends AbstractProcessor {
    public DynamicProcessor(String name) {
        super(name);
    }

    /**
     * 获取下一个节点的id
     *
     * @param context 上下文
     * @return 下一个节点的id
     */
    protected abstract String nextNodeId(ProcessContext context);
}
