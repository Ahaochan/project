package moe.ahao.process.engine.core.utils;

import moe.ahao.process.engine.core.node.ProcessorNode;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ProcessorUtils {
    private static ExecutorService DEFAULT_POOL = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
        60, TimeUnit.MICROSECONDS, new SynchronousQueue<>());

    /**
     * 校验 ProcessorNode 是否有环
     * @param node 头节点
     * @return true则有环, false则无环
     */
    public static boolean hasRing(ProcessorNode node) {
        return hasRing(node, new HashSet<>());
    }

    private static boolean hasRing(ProcessorNode node, Set<String> idSet) {
        // 1. 如果没有后继节点, 就不可能成环了
        Map<String, ProcessorNode> nextNodes = node.getNextNodes();
        if (nextNodes == null || nextNodes.isEmpty()) {
            return false;
        }

        // 2. 将当前节点加入集合, 用于成环判断
        idSet.add(node.getName());

        boolean ret = false;
        for (Map.Entry<String, ProcessorNode> entry : nextNodes.entrySet()) {
            ProcessorNode value = entry.getValue();
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

    public static void executeAsync(Runnable runnable) {
        DEFAULT_POOL.execute(runnable);
    }

}
