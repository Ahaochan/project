package moe.ahao.process.engine.core.refresh;

import java.util.Map;

public interface ProcessRefreshPolicy {
    /**
     * 是否继续执行流程
     *
     * @param name     流程名称
     * @param metadata 流程元数据
     * @return 是否继续执行流程
     */
    boolean continueExecuteProcess(String name, Map<String, String> metadata);
}
