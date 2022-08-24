package com.ruyuan.process.engine.node;

import com.ruyuan.process.engine.utils.ProcessorUtils;
import lombok.Getter;

/**
 * 表示一个流程
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Getter
public class ProcessorDefinition {

    private String name;
    /**
     * 初始节点
     */
    private ProcessorNode first;

    public ProcessorDefinition() {
    }

    public ProcessorDefinition(ProcessorNode first) {
        setFirst(first);
    }

    public void setFirst(ProcessorNode first) {
        this.first = first;
        if (ProcessorUtils.hasRing(first)) {
            throw new IllegalArgumentException("Processor chain exists ring.");
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toStr() {
        StringBuilder sb = new StringBuilder();
        buildStr(sb, first);
        return sb.toString();
    }

    private void buildStr(StringBuilder sb, ProcessorNode node) {
        for (ProcessorNode processorNode : node.getNextNodes().values()) {
            sb.append(node.getName()).append(" -> ").append(processorNode.getName()).append("\n");
            buildStr(sb, processorNode);
        }
    }

}
