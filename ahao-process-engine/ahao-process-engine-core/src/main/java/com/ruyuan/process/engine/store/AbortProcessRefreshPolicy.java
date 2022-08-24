package com.ruyuan.process.engine.store;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Slf4j
public class AbortProcessRefreshPolicy implements ProcessRefreshPolicy {
    @Override
    public boolean continueExecuteProcess(String name, Map<String, String> metadata) {
        log.warn("因为流程引擎刷新了，所以丢弃了回放流程：{} -> {}", name, metadata);
        return false;
    }
}
