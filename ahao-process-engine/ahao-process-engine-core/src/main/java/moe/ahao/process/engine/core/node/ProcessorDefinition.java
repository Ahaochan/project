package moe.ahao.process.engine.core.node;

import lombok.Getter;
import moe.ahao.process.engine.core.utils.ProcessorUtils;

/**
 * 表示一个流程
 */
@Getter
public class ProcessorDefinition {
    private final String name;
    /**
     * 初始节点
     */
    private ProcessorNode first;

    public ProcessorDefinition(String name) {
        this.name = name;
    }
    public ProcessorDefinition(String name, ProcessorNode first) {
        this.name = name;
        this.setFirst(first);
    }

    public void setFirst(ProcessorNode first) {
        if(first == null) {
            throw new IllegalArgumentException("Processor chain first node missing.");
        }
        this.first = first;
        // 校验是否成环
        if (ProcessorUtils.hasRing(first)) {
            throw new IllegalArgumentException("Processor chain exists ring.");
        }
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
