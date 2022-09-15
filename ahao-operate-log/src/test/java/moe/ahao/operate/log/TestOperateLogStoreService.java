package moe.ahao.operate.log;

import lombok.extern.slf4j.Slf4j;
import moe.ahao.operate.log.model.OperateLogInstance;
import moe.ahao.operate.log.service.OperateLogStoreService;
import moe.ahao.util.commons.io.JSONHelper;

@Slf4j
public class TestOperateLogStoreService implements OperateLogStoreService {
    private final ThreadLocal<OperateLogInstance> threadLocal = new ThreadLocal<>();

    @Override
    public void storeOperateLog(OperateLogInstance instance) throws Exception {
        log.info("operateLog = {}", JSONHelper.toString(instance));
        threadLocal.set(instance);
    }

    public OperateLogInstance loadOperateLog() {
        return threadLocal.get();
    }
}
