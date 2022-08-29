package moe.ahao.process.engine.core.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MemoryProcessStateDAO implements ProcessStateDAO {
    private final Map<String, Map<String, String>> processStatusMap;
    private final Map<String, Long> lastUpdateTimeMap;

    public MemoryProcessStateDAO() {
        this.processStatusMap = new HashMap<>();
        this.lastUpdateTimeMap = new HashMap<>();
    }

    @Override
    public void put(String globalUniqueId, Map<String, String> params) {
        processStatusMap.put(globalUniqueId, params);
        lastUpdateTimeMap.put(globalUniqueId, System.currentTimeMillis());
    }

    @Override
    public void remove(String globalUniqueId) {
        processStatusMap.remove(globalUniqueId);
        lastUpdateTimeMap.remove(globalUniqueId);
    }

    @Override
    public Map<String, String> getMap(String globalUniqueId) {
        return processStatusMap.get(globalUniqueId);
    }

    @Override
    public List<String> pollUnCompletedProcess(int timeout, TimeUnit unit) {
        long timeoutMillis = unit.toMillis(timeout);

        List<String> processNames = new ArrayList<>();
        for (Map.Entry<String, Long> entry : lastUpdateTimeMap.entrySet()) {
            String processName = entry.getKey();
            Long lastUpdateTime = entry.getValue();

            // 过滤超出时间限制的流程
            if (System.currentTimeMillis() - lastUpdateTime > timeoutMillis) {
                continue;
            }
            processNames.add(processName);
        }
        return processNames;
    }
}
