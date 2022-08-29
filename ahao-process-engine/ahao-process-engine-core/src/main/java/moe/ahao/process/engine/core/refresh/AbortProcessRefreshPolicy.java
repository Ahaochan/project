package moe.ahao.process.engine.core.refresh;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class AbortProcessRefreshPolicy implements ProcessRefreshPolicy {
    @Override
    public boolean continueExecuteProcess(String name, Map<String, String> metadata) {
        log.warn("因为流程引擎刷新了，所以丢弃了回放流程：{} -> {}", name, metadata);
        return false;
    }
}
