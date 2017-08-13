/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50544
Source Host           : localhost:3306
Source Database       : invoice

Target Server Type    : MYSQL
Target Server Version : 50544
File Encoding         : 65001

Date: 2017-08-12 20:46:55
*/

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods` (
  `id`            BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '货物ID',
  `name`          VARCHAR(50)                  DEFAULT NULL
  COMMENT '货物名',
  `category_id`   BIGINT(20) UNSIGNED          DEFAULT NULL
  COMMENT '货物类别id',
  `specification` VARCHAR(10)                  DEFAULT NULL
  COMMENT '规格型号',
  `unit`          VARCHAR(5)                   DEFAULT NULL
  COMMENT '单位',
  `unite_price`   DECIMAL(10, 2) UNSIGNED      DEFAULT NULL
  COMMENT '单价',
  `tax_rate`      VARCHAR(10)                  DEFAULT NULL
  COMMENT '税率',
  `gmt_create`    TIMESTAMP           NOT NULL DEFAULT '0000-00-00 00:00:00'
  COMMENT '创建时间',
  `gmt_modify`    TIMESTAMP           NOT NULL DEFAULT '0000-00-00 00:00:00'
  COMMENT '修改时间',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT ='货物表';

-- ----------------------------
-- Records of goods
-- ----------------------------
SET FOREIGN_KEY_CHECKS = 1;
