package moe.ahao.process.engine.core.store;

import java.util.Map;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
public class ContinueProcessRefreshPolicy implements ProcessRefreshPolicy {
    @Override
    public boolean continueExecuteProcess(String name, Map<String, String> metadata) {
        return true;
    }
}
