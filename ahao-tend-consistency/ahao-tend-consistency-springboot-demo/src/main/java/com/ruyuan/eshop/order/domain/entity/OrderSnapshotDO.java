package com.ruyuan.eshop.order.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订单快照表
 * </p>
 *
 * @author zhonghuashishan
 */
@Data
public class OrderSnapshotDO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 更新时间
     */
    private Date gmtModified;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 快照类型
     */
    private Integer snapshotType;

    /**
     * 订单快照内容
     */
    private String snapshotJson;

}
