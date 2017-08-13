/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50544
Source Host           : localhost:3306
Source Database       : invoice

Target Server Type    : MYSQL
Target Server Version : 50544
File Encoding         : 65001

Date: 2017-08-12 20:47:09
*/

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for invoice
-- ----------------------------
DROP TABLE IF EXISTS `invoice`;
CREATE TABLE `invoice` (
  `id`                BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '发票ID',
  `invoice_code`      VARCHAR(20)         NOT NULL
  COMMENT '发票代码',
  `invoice_number`    VARCHAR(20)         NOT NULL
  COMMENT '发票号码',
  `date`              TIMESTAMP           NOT NULL DEFAULT '0000-00-00 00:00:00'
  COMMENT '开票日期',
  `verify_code`       VARCHAR(50)         NOT NULL
  COMMENT '校验码',
  `purchases_unit_id` BIGINT(20) UNSIGNED NOT NULL
  COMMENT '发票购贷单位id',
  `sales_unit_id`     BIGINT(20) UNSIGNED NOT NULL
  COMMENT '销贷单位id',
  `password_area`     VARCHAR(100)                 DEFAULT NULL
  COMMENT '密码区',
  `mema`              VARCHAR(255)                 DEFAULT NULL
  COMMENT '备注',
  `payee_id`          BIGINT(20) UNSIGNED          DEFAULT NULL
  COMMENT '收款人id',
  `review_id`         BIGINT(20) UNSIGNED          DEFAULT NULL
  COMMENT '复核人id',
  `drawer_id`         BIGINT(20) UNSIGNED          DEFAULT NULL
  COMMENT '开票人id',
  `gmt_create`        TIMESTAMP           NOT NULL DEFAULT '0000-00-00 00:00:00'
  COMMENT '创建时间',
  `gmt_modify`        TIMESTAMP           NOT NULL DEFAULT '0000-00-00 00:00:00'
  COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `un_invoice_code_number` (`invoice_code`, `invoice_number`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT ='发票表';

-- ----------------------------
-- Records of invoice
-- ----------------------------
SET FOREIGN_KEY_CHECKS = 1;
