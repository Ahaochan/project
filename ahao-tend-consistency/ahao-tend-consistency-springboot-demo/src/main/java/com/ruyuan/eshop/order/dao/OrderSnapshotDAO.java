package com.ruyuan.eshop.order.dao;

import cn.hutool.json.JSONUtil;
import com.ruyuan.consistency.annotation.ConsistencyTask;
import com.ruyuan.eshop.order.domain.entity.OrderSnapshotDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 订单快照表 DAO
 *
 * @author zhonghuashishan
 */
@Slf4j
@Component
public class OrderSnapshotDAO {

    /**
     * 批量插入操作
     *
     * @param orderSnapshotDOList 要插入快照的集合
     */
    @ConsistencyTask(alertActionBeanName = "tendConsistencyAlerter")
    public void batchSave(List<OrderSnapshotDO> orderSnapshotDOList) {
        log.info("进入OrderSnapshotDAO#batchSave orderSnapshotDOList={}", JSONUtil.toJsonPrettyStr(orderSnapshotDOList));
    }



}
