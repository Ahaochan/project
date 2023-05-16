-- ----------------------------
-- Table structure for biz_config
-- ----------------------------
DROP TABLE IF EXISTS `biz_config`;
CREATE TABLE `biz_config` (
    `id`                  bigint   NOT NULL AUTO_INCREMENT COMMENT '主键',
    `business_identifier` tinyint  NOT NULL COMMENT '业务线',
    `order_type`          tinyint  NOT NULL COMMENT '订单类型',
    `name`                varchar(64)       DEFAULT NULL COMMENT '业务名称',
    `create_time`         datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`         datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_biz_order_type` (`order_type`, `business_identifier`) USING BTREE,
    UNIQUE KEY `uk_name` (`name`) USING BTREE
) ENGINE = InnoDB COMMENT ='业务配置表';

-- ----------------------------
-- Records of biz_config
-- ----------------------------
INSERT INTO `biz_config` VALUES (1, 1, 1, '自营普通订单', now() , now());
INSERT INTO `biz_config` VALUES (2, 1, 2, '自营虚拟订单', now() , now());
INSERT INTO `biz_config` VALUES (3, 1, 3, '自营预售订单', now() , now());
INSERT INTO `biz_config` VALUES (4, 4, 1, '三方普通订单', now() , now());

-- ----------------------------
-- Table structure for process_biz_relation
-- ----------------------------
DROP TABLE IF EXISTS `process_biz_relation`;
CREATE TABLE `process_biz_relation`(
    `id`                  bigint      NOT NULL AUTO_INCREMENT COMMENT '主键',
    `process_config_name` varchar(64) NOT NULL COMMENT '流程配置名称',
    `biz_config_id`       bigint      NOT NULL COMMENT '业务配置id',
    `create_time`         datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`         datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_relation` (`biz_config_id`, `process_config_name`) USING BTREE
) ENGINE = InnoDB COMMENT ='流程业务关联表';

-- ----------------------------
-- Records of process_biz_relation
-- ----------------------------
INSERT INTO `process_biz_relation` VALUES (13, '自营子订单生单流程', 1, now(), now());
INSERT INTO `process_biz_relation` VALUES (14, '自营子订单生单流程', 2, now(), now());
INSERT INTO `process_biz_relation` VALUES (15, '自营子订单生单流程', 3, now(), now());
INSERT INTO `process_biz_relation` VALUES (16, '售后审核拒绝流程', 1, now(), now());
INSERT INTO `process_biz_relation` VALUES (17, '售后审核拒绝流程', 2, now(), now());
INSERT INTO `process_biz_relation` VALUES (18, '售后审核拒绝流程', 3, now(), now());
INSERT INTO `process_biz_relation` VALUES (22, '售后审核通过流程', 1, now(), now());
INSERT INTO `process_biz_relation` VALUES (23, '售后审核通过流程', 2, now(), now());
INSERT INTO `process_biz_relation` VALUES (24, '售后审核通过流程', 3, now(), now());
INSERT INTO `process_biz_relation` VALUES (26, '对接三方平台售后申请数据流程', 4, now(), now());
INSERT INTO `process_biz_relation` VALUES (27, '对接三方平台售后审核通过流程', 4, now(), now());
INSERT INTO `process_biz_relation` VALUES (28, '对接三方平台售后审核拒绝流程', 4, now(), now());
INSERT INTO `process_biz_relation` VALUES (34, '自营主订单生单流程', 1, now(), now());
INSERT INTO `process_biz_relation` VALUES (35, '自营主订单生单流程', 2, now(), now());
INSERT INTO `process_biz_relation` VALUES (36, '自营主订单生单流程', 3, now(), now());
INSERT INTO `process_biz_relation` VALUES (37, '三方订单生单流程', 1, now(), now());

-- ----------------------------
-- Table structure for process_config
-- ----------------------------
DROP TABLE IF EXISTS `process_config`;
CREATE TABLE `process_config` (
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `type`        tinyint      NOT NULL COMMENT '类型，1-正向，2-逆向',
    `xml_name`    varchar(128) NOT NULL COMMENT '流程xml name',
    `name`        varchar(256) NOT NULL COMMENT '流程名称',
    `remark`      varchar(256)          DEFAULT NULL COMMENT '备注',
    `enable`      tinyint      NOT NULL DEFAULT '1' COMMENT '启用/禁用，0-禁用，1-启用',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_name` (`xml_name`) USING BTREE,
    UNIQUE KEY `uk_xml_name` (`xml_name`) USING BTREE
) ENGINE = InnoDB COMMENT ='流程配置表';

-- ----------------------------
-- Records of process_config
-- ----------------------------
INSERT INTO `process_config` VALUES (3, 1, 'subOrderCreateProcess', '自营子订单生单流程', NULL, 1, now(), now());
INSERT INTO `process_config` VALUES (4, 2, 'afterSaleRejectProcess', '售后审核拒绝流程', NULL, 1, now(), now());
INSERT INTO `process_config` VALUES (6, 2, 'afterSaleAuditProcess', '售后审核通过流程', NULL, 1, now(), now());
INSERT INTO `process_config` VALUES (7, 1, 'thirdOrderCreateProcess', '三方订单生单流程', NULL, 1, '2022-03-14 21:21:51', '2022-03-14 21:21:51');
INSERT INTO `process_config` VALUES (8, 2, 'receiveAfterSaleInformationProcess', '对接三方平台售后申请数据流程', NULL, 1, '2022-03-14 21:21:51', '2022-03-14 21:21:51');
INSERT INTO `process_config` VALUES (9, 2, 'receiveAfterSaleAuditResultPassProcess', '对接三方平台售后审核通过流程', NULL, 1, '2022-03-14 21:21:51', '2022-03-14 21:21:51');
INSERT INTO `process_config` VALUES (10, 2, 'receiveAfterSaleAuditResultRejectProcess', '对接三方平台售后审核拒绝流程', NULL, 1, '2022-03-14 21:21:51', '2022-03-14 21:21:51');
INSERT INTO `process_config` VALUES (11, 1, 'masterOrderCreateProcess', '自营主订单生单流程', NULL, 1, now(), now());

-- ----------------------------
-- Table structure for process_node
-- ----------------------------
DROP TABLE IF EXISTS `process_node`;
CREATE TABLE `process_node` (
    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `type`            tinyint      NOT NULL COMMENT '类型，1-正向，2-逆向',
    `name`            varchar(128) NOT NULL COMMENT '节点名称',
    `bean_name`       varchar(128) NOT NULL COMMENT '节点对应的beanName',
    `bean_clazz_name` varchar(128) NOT NULL COMMENT 'bean的权限定类名',
    `remark`          varchar(256)          DEFAULT NULL COMMENT '备注',
    `create_time`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_name` (`name`) USING BTREE,
    UNIQUE KEY `uk_bean_name` (`bean_name`) USING BTREE
) ENGINE = InnoDB COMMENT ='流程节点表';

-- ----------------------------
-- Records of process_node
-- ----------------------------
INSERT INTO `process_node` VALUES (2, 1, '风控节点', 'createOrderRiskCheckNode', 'com.ruyuan.eshop.order.statemachine.action.order.create.node.CreateOrderRiskCheckNode', NULL, '2022-03-09 14:02:56', '2022-03-09 14:02:56');
INSERT INTO `process_node` VALUES (3, 1, '生单金额计算节点', 'createOrderCalculateAmountNode', 'com.ruyuan.eshop.order.statemachine.action.order.create.node.CreateOrderCalculateAmountNode', NULL, '2022-03-09 14:02:56', '2022-03-09 14:02:56');
INSERT INTO `process_node` VALUES (4, 1, '锁定优惠券节点', 'createOrderLockCouponNode', 'com.ruyuan.eshop.order.statemachine.action.order.create.node.CreateOrderLockCouponNode', NULL, '2022-03-09 14:09:16', '2022-03-09 14:09:16');
INSERT INTO `process_node` VALUES (5, 1, '锁定库存节点', 'createOrderDeductStockNode', 'com.ruyuan.eshop.order.statemachine.action.order.create.node.CreateOrderDeductStockNode', NULL, '2022-03-09 14:09:16', '2022-03-09 14:09:16');
INSERT INTO `process_node` VALUES (6, 1, '构建主订单信息节点', 'createOrderMasterBuilderNode', 'com.ruyuan.eshop.order.statemachine.action.order.create.node.CreateOrderMasterBuilderNode', NULL, '2022-03-09 14:09:16', '2022-03-09 14:09:16');
INSERT INTO `process_node` VALUES (7, 1, '保存订单信息到数据库节点', 'orderCreateDBNode', 'com.ruyuan.eshop.order.statemachine.action.order.create.node.OrderCreateDBNode', NULL, '2022-03-09 14:09:16', '2022-03-09 14:09:16');
INSERT INTO `process_node` VALUES (8, 1, '发送超时取消MQ信息节点', 'createOrderSendPayTimeoutDelayMessageNode', 'com.ruyuan.eshop.order.statemachine.action.order.create.node.CreateOrderSendPayTimeoutDelayMessageNode', NULL, '2022-03-09 14:09:16', '2022-03-09 14:09:16');
INSERT INTO `process_node` VALUES (9, 1, '构建子订单信息节点', 'orderCreateSubBuilderNode', 'com.ruyuan.eshop.order.statemachine.action.order.create.node.OrderCreateSubBuilderNode', NULL, '2022-03-09 14:09:16', '2022-03-09 14:09:16');
INSERT INTO `process_node` VALUES (10, 2, '售后审核入参检查节点', 'checkAfterSaleInfoNode', 'com.ruyuan.eshop.order.statemachine.action.aftersale.node.CheckAfterSaleInfoNode', NULL, '2022-03-09 14:13:54', '2022-03-09 14:13:54');
INSERT INTO `process_node` VALUES (11, 2, '组装售后审核数据节点', 'afterSaleBuildAuditDataNode', 'com.ruyuan.eshop.order.statemachine.action.aftersale.node.AfterSaleBuildAuditDataNode', NULL, '2022-03-09 14:13:54', '2022-03-09 14:13:54');
INSERT INTO `process_node` VALUES (12, 2, '更新售后信息节点', 'afterSaleUpdateInfoNode', 'com.ruyuan.eshop.order.statemachine.action.aftersale.node.AfterSaleUpdateInfoNode', NULL, '2022-03-09 14:13:54', '2022-03-09 14:13:54');
INSERT INTO `process_node` VALUES (13, 2, '组装释放库存节点', 'afterSaleBuildInventoryDataNode', 'com.ruyuan.eshop.order.statemachine.action.aftersale.node.AfterSaleBuildInventoryDataNode', NULL, '2022-03-09 14:15:57', '2022-03-09 14:15:57');
INSERT INTO `process_node` VALUES (14, 2, '组装实际退款参数节点', 'afterSaleBuildRefundDataNode', 'com.ruyuan.eshop.order.statemachine.action.aftersale.node.AfterSaleBuildRefundDataNode', NULL, '2022-03-09 14:15:57', '2022-03-09 14:15:57');
INSERT INTO `process_node` VALUES (15, 2, '发送释放库存MQ节点', 'afterSaleAuditPassSendMessageNode', 'com.ruyuan.eshop.order.statemachine.action.aftersale.node.AfterSaleAuditPassSendMessageNode', NULL, '2022-03-09 14:15:57', '2022-03-09 14:15:57');
INSERT INTO `process_node` VALUES (16, 1, '三方生单请求参数校验节点', 'checkThirdOrderInfoNode', 'com.ruyuan.eshop.order.statemachine.action.order.create.node.CreateOrderThirdCheckNode', NULL, '2022-03-14 21:28:35', '2022-03-14 21:28:35');
INSERT INTO `process_node` VALUES (17, 1, '构建第三方订单信息节点', 'createOrderThirdBuilderNode', 'com.ruyuan.eshop.order.statemachine.action.order.create.node.OrderCreateThirdBuilderNode', NULL, '2022-03-14 21:28:35', '2022-03-14 21:28:35');
INSERT INTO `process_node` VALUES (18, 2, '解析三方平台发来的报文数据并封装数据对象节点', 'loadReceiveAfterSaleJson', 'com.ruyuan.eshop.order.statemachine.action.aftersale.three.LoadReceiveAfterSaleJsonNode', NULL, '2022-03-14 21:28:35', '2022-03-14 21:28:35');
INSERT INTO `process_node` VALUES (19, 2, '检查三方数据合法性节点', 'checkDataLegitimacyNode', 'com.ruyuan.eshop.order.statemachine.action.aftersale.three.CheckDataLegitimacyNode', NULL, '2022-03-14 21:28:35', '2022-03-14 21:28:35');
INSERT INTO `process_node` VALUES (20, 2, '检查接口幂等性节点', 'checkAfterSaleInfoIdempotencyNode', 'com.ruyuan.eshop.order.statemachine.action.aftersale.three.CheckAfterSaleInfoIdempotencyNode', NULL, '2022-03-14 21:28:35', '2022-03-14 21:28:35');
INSERT INTO `process_node` VALUES (21, 2, '三方平台售后数据落库节点', 'saveThreePartyPlatformAfterSaleInfosNode', 'com.ruyuan.eshop.order.statemachine.action.aftersale.three.SaveThreePartyPlatformAfterSaleInfosNode', NULL, '2022-03-14 21:28:35', '2022-03-14 21:28:35');
INSERT INTO `process_node` VALUES (22, 2, '检查三方平台发来的售后审核结果入参节点', 'checkAuditResultRequestNode', 'com.ruyuan.eshop.order.statemachine.action.aftersale.three.CheckAuditResultRequestNode', NULL, '2022-03-14 21:29:15', '2022-03-14 21:29:15');
INSERT INTO `process_node` VALUES (23, 2, '三方平台售后审核结果数据更新节点', 'updateThreePartyPlatformAfterSaleAuditInfos', 'com.ruyuan.eshop.order.statemachine.action.aftersale.three.UpdateThreePartyPlatformAfterSaleAuditInfos', NULL, '2022-03-14 21:31:53', '2022-03-14 21:31:53');
INSERT INTO `process_node` VALUES (24, 1, '生单入参检查节点', 'createOrderCheckNode', 'com.ruyuan.eshop.order.statemachine.action.order.create.node.CreateOrderCheckNode', NULL, '2022-03-15 10:53:23', '2022-03-15 10:53:23');

-- ----------------------------
-- Table structure for process_node_linked
-- ----------------------------
DROP TABLE IF EXISTS `process_node_linked`;
CREATE TABLE `process_node_linked` (
    `id`                  bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `process_config_name` varchar(256) NOT NULL COMMENT '所属流程配置名称',
    `process_node_name`   varchar(256) NOT NULL COMMENT '流程节点名称',
    `invoke_method`       varchar(8)   NOT NULL DEFAULT 'SYNC' COMMENT '调用方式：SYNC/ASYNC',
    `create_time`         datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`         datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT ='流程节点构建表';

-- ----------------------------
-- Records of process_node_linked
-- ----------------------------
INSERT INTO `process_node_linked` VALUES (33, '自营子订单生单流程', '构建子订单信息节点', 'SYNC', now(), now());
INSERT INTO `process_node_linked` VALUES (34, '自营子订单生单流程', '保存订单信息到数据库节点', 'SYNC', now(), now());
INSERT INTO `process_node_linked` VALUES (35, '售后审核拒绝流程', '售后审核入参检查节点', 'SYNC', now(), now());
INSERT INTO `process_node_linked` VALUES (36, '售后审核拒绝流程', '组装售后审核数据节点', 'SYNC', now(), now());
INSERT INTO `process_node_linked` VALUES (37, '售后审核拒绝流程', '更新售后信息节点', 'SYNC', now(), now());
INSERT INTO `process_node_linked` VALUES (44, '售后审核通过流程', '售后审核入参检查节点', 'SYNC', now(), now());
INSERT INTO `process_node_linked` VALUES (45, '售后审核通过流程', '组装售后审核数据节点', 'SYNC', now(), now());
INSERT INTO `process_node_linked` VALUES (46, '售后审核通过流程', '组装释放库存节点', 'SYNC', now(), now());
INSERT INTO `process_node_linked` VALUES (47, '售后审核通过流程', '组装实际退款参数节点', 'SYNC', now(), now());
INSERT INTO `process_node_linked` VALUES (48, '售后审核通过流程', '更新售后信息节点', 'SYNC', now(), now());
INSERT INTO `process_node_linked` VALUES (49, '售后审核通过流程', '发送释放库存MQ节点', 'SYNC', now(), now());
INSERT INTO `process_node_linked` VALUES (53, '对接三方平台售后申请数据流程', '解析三方平台发来的报文数据并封装数据对象节点', 'SYNC', now(), now());
INSERT INTO `process_node_linked` VALUES (54, '对接三方平台售后申请数据流程', '检查三方数据合法性节点', 'SYNC', now(), now());
INSERT INTO `process_node_linked` VALUES (55, '对接三方平台售后申请数据流程', '检查接口幂等性节点', 'SYNC', now(), now());
INSERT INTO `process_node_linked` VALUES (56, '对接三方平台售后申请数据流程', '三方平台售后数据落库节点', 'SYNC', now(), now());
INSERT INTO `process_node_linked` VALUES (57, '对接三方平台售后审核通过流程', '检查三方平台发来的售后审核结果入参节点', 'SYNC', now(), now());
INSERT INTO `process_node_linked` VALUES (58, '对接三方平台售后审核通过流程', '检查接口幂等性节点', 'SYNC', now(), now());
INSERT INTO `process_node_linked` VALUES (59, '对接三方平台售后审核通过流程', '三方平台售后审核结果数据更新节点', 'SYNC', now(), now());
INSERT INTO `process_node_linked` VALUES (60, '对接三方平台售后审核拒绝流程', '检查三方平台发来的售后审核结果入参节点', 'SYNC', now(), now());
INSERT INTO `process_node_linked` VALUES (61, '对接三方平台售后审核拒绝流程', '检查接口幂等性节点', 'SYNC', now(), now());
INSERT INTO `process_node_linked` VALUES (62, '对接三方平台售后审核拒绝流程', '三方平台售后审核结果数据更新节点', 'SYNC', now(), now());
INSERT INTO `process_node_linked` VALUES (77, '自营主订单生单流程', '生单入参检查节点', 'SYNC', now(), now());
INSERT INTO `process_node_linked` VALUES (78, '自营主订单生单流程', '风控节点', 'SYNC', now(), now());
INSERT INTO `process_node_linked` VALUES (79, '自营主订单生单流程', '生单金额计算节点', 'SYNC', now(), now());
INSERT INTO `process_node_linked` VALUES (80, '自营主订单生单流程', '锁定优惠券节点', 'SYNC', now(), now());
INSERT INTO `process_node_linked` VALUES (81, '自营主订单生单流程', '锁定库存节点', 'SYNC', now(), now());
INSERT INTO `process_node_linked` VALUES (82, '自营主订单生单流程', '构建主订单信息节点', 'SYNC', now(), now());
INSERT INTO `process_node_linked` VALUES (83, '自营主订单生单流程', '保存订单信息到数据库节点', 'SYNC', now(), now());
INSERT INTO `process_node_linked` VALUES (84, '自营主订单生单流程', '发送超时取消MQ信息节点', 'SYNC', now(), now());
INSERT INTO `process_node_linked` VALUES (85, '三方订单生单流程', '三方生单请求参数校验节点', 'SYNC', now(), now());
INSERT INTO `process_node_linked` VALUES (86, '三方订单生单流程', '构建第三方订单信息节点', 'SYNC', now(), now());
INSERT INTO `process_node_linked` VALUES (87, '三方订单生单流程', '保存订单信息到数据库节点', 'SYNC', now(), now());
