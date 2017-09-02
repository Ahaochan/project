/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50544
Source Host           : localhost:3306
Source Database       : invoice

Target Server Type    : MYSQL
Target Server Version : 50544
File Encoding         : 65001

Date: 2017-08-12 20:47:15
*/

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for invoice__goods
-- ----------------------------
DROP TABLE IF EXISTS `invoice__goods`;
CREATE TABLE `invoice__goods` (
  `id`         BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '发票货物ID',
  `invoice_id` BIGINT(20) UNSIGNED NOT NULL COMMENT '发票ID',
  `goods_id`   BIGINT(20) UNSIGNED NOT NULL COMMENT '货物ID',
  `number`     BIGINT(20) DEFAULT NULL COMMENT '数量',
  `gmt_create` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `gmt_modify` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `fk_ig_invoice_id` (`invoice_id`),
  KEY `fk_ig_goods_id` (`goods_id`),
  CONSTRAINT `fk_ig_goods_id`   FOREIGN KEY (`goods_id`) REFERENCES `product_goods` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_ig_invoice_id` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT ='发票货物表';

-- ----------------------------
-- Records of invoice__goods
-- ----------------------------
SET FOREIGN_KEY_CHECKS = 1;
