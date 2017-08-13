/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50544
Source Host           : localhost:3306
Source Database       : invoice

Target Server Type    : MYSQL
Target Server Version : 50544
File Encoding         : 65001

Date: 2017-08-12 20:47:04
*/

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for goods_category
-- ----------------------------
DROP TABLE IF EXISTS `goods_category`;
CREATE TABLE `goods_category` (
  `id`          BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '货物类别ID',
  `name`        VARCHAR(50)                  DEFAULT NULL
  COMMENT '货物类别名',
  `description` VARCHAR(100)                 DEFAULT NULL
  COMMENT '货物类别描述',
  `gmt_create`  TIMESTAMP           NOT NULL DEFAULT '0000-00-00 00:00:00'
  COMMENT '创建时间',
  `gmt_modify`  TIMESTAMP           NOT NULL DEFAULT '0000-00-00 00:00:00'
  COMMENT '修改时间',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT ='货物类别表';

-- ----------------------------
-- Records of goods_category
-- ----------------------------
SET FOREIGN_KEY_CHECKS = 1;
