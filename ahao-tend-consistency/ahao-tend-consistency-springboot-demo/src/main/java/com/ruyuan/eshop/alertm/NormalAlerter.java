package com.ruyuan.eshop.alertm;

import moe.ahao.tend.consistency.core.spi.alerter.ConsistencyFrameworkAlerter;
import moe.ahao.tend.consistency.core.infrastructure.repository.impl.mybatis.data.ConsistencyTaskInstance;
import org.springframework.stereotype.Component;

/**
 * 自定义告警通知类  需要实现ConsistencyFrameworkAlerter类
 *
 * @author zhonghuashishan
 **/
@Component // 需要加@Component 框架使用了spring容器来获取相关的实现类
public class NormalAlerter implements ConsistencyFrameworkAlerter {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(NormalAlerter.class);

    /**
     * 发送告警通知 拿到告警实例通知给对应的人 要通知的人 在该方法中实现即可
     *
     * @param consistencyTaskInstance 发送告警通知
     */
    @Override
    public void sendAlertNotice(ConsistencyTaskInstance consistencyTaskInstance) {
        LOG.info("执行告警通知逻辑... 方法签名为：{}", consistencyTaskInstance.getMethodSignName());
    }

}
