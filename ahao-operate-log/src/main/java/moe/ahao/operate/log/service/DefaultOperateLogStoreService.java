package moe.ahao.operate.log.service;

import lombok.extern.slf4j.Slf4j;
import moe.ahao.operate.log.model.OperateLogInstance;
import moe.ahao.util.commons.io.JSONHelper;

/**
 * 默认的操作日志存储实现
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Slf4j
public class DefaultOperateLogStoreService implements OperateLogStoreService {

    @Override
    public void storeOperateLog(OperateLogInstance instance) throws Exception {
        log.info("operateLog = {}", JSONHelper.toString(instance));
    }

}
