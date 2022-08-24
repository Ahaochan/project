package com.ruyuan.process.engine.process;

/**
 * 可以动态选择下一节点的节点
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public abstract class DynamicProcessor extends AbstractProcessor {

    /**
     * 获取下一个节点的id
     *
     * @param context 上下文
     * @return 下一个节点的id
     */
    protected abstract String nextNodeId(ProcessContext context);

}
