package moe.ahao.operate.log.service;

import moe.ahao.operate.log.model.OperateLogInstance;

/**
 * 操作日志存储service
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public interface OperateLogStoreService {

    /**
     * 存储操作日志
     *
     * @param instance
     * @throws Exception
     */
    void storeOperateLog(OperateLogInstance instance) throws Exception;

}
