package moe.ahao.process.engine.core.definition;

import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 定义一个流程
 */
@Getter
public class ProcessorDefinition {
    /**
     * 流程名称
     */
    private final String name;
    /**
     * 初始节点
     */
    private ProcessorNodeDefinition first;

    private ProcessorDefinition(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("Processor name missing.");
        }
        this.name = name;
    }

    private void setFirst(ProcessorNodeDefinition first) {
        if (first == null) {
            throw new IllegalArgumentException("Processor chain first node missing.");
        }
        this.first = first;
    }

    public String toStr() {
        StringBuilder sb = new StringBuilder();
        buildStr(sb, first);
        return sb.toString();
    }

    private void buildStr(StringBuilder sb, ProcessorNodeDefinition node) {
        for (ProcessorNodeDefinition processorNode : node.getNextNodes().values()) {
            sb.append(node.getName()).append(" -> ").append(processorNode.getName()).append("\n");
            this.buildStr(sb, processorNode);
        }
    }

    @Data
    public static class Builder {
        private String name;
        private ProcessorNodeDefinition first;

        public Builder(String name) {
            this(name, null);
        }

        public Builder(String name, ProcessorNodeDefinition first) {
            this.name = name;
            this.first = first;
        }

        public Builder setFirst(ProcessorNodeDefinition first) {
            this.first = first;
            return this;
        }

        public ProcessorDefinition build() {
            ProcessorDefinition definition = new ProcessorDefinition(name);
            definition.setFirst(first);
            // 校验是否成环
            if (this.hasRing(first)) {
                throw new IllegalArgumentException("Processor chain exists ring.");
            }
            return definition;
        }

        /**
         * 校验 ProcessorNode 是否有环
         *
         * @param node 头节点
         * @return true则有环, false则无环
         */
        private boolean hasRing(ProcessorNodeDefinition node) {
            return hasRing(node, new HashSet<>());
        }

        private boolean hasRing(ProcessorNodeDefinition node, Set<String> idSet) {
            // 1. 如果没有后继节点, 就不可能成环了
            Map<String, ProcessorNodeDefinition> nextNodes = node.getNextNodes();
            if (nextNodes == null || nextNodes.isEmpty()) {
                return false;
            }

            // 2. 将当前节点加入集合, 用于成环判断
            idSet.add(node.getName());

            boolean ret = false;
            for (Map.Entry<String, ProcessorNodeDefinition> entry : nextNodes.entrySet()) {
                ProcessorNodeDefinition value = entry.getValue();
                // 3. 如果集合存在当前节点name, 说明成环, 直接return
                if (idSet.contains(value.getName())) {
                    return true;
                }

                // 4. 将当前节点加入集合中, 然后判断后继节点是否有环
                idSet.add(value.getName());
                ret = ret || hasRing(value, new HashSet<>(idSet));
            }
            return ret;
        }
    }
}
