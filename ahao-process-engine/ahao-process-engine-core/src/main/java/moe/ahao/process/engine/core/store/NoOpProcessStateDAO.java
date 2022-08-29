package moe.ahao.process.engine.core.store;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class NoOpProcessStateDAO implements ProcessStateDAO {

    @Override
    public void put(String globalUniqueId, String key, String value) {

    }

    @Override
    public void put(String globalUniqueId, Map<String, String> params) {

    }

    @Override
    public void remove(String globalUniqueId) {

    }

    @Override
    public Map<String, String> getMap(String globalUniqueId) {
        return Collections.emptyMap();
    }

    @Override
    public List<String> pollUnCompletedProcess(int timeout, TimeUnit unit) {
        return Collections.emptyList();
    }
}
