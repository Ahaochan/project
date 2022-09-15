package moe.ahao.operate.log.service;

import com.alibaba.fastjson.JSONObject;
import moe.ahao.operate.log.model.OperateLogInstance;
import lombok.extern.slf4j.Slf4j;

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
        log.info("operateLog = {}", JSONObject.toJSONString(instance));
    }

}
