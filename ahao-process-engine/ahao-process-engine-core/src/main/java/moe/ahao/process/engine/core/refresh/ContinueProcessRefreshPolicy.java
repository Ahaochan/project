package moe.ahao.process.engine.core.refresh;

import java.util.Map;

public class ContinueProcessRefreshPolicy implements ProcessRefreshPolicy {
    @Override
    public boolean continueExecuteProcess(String name, Map<String, String> metadata) {
        return true;
    }
}
