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
DROP TABLE IF EXISTS `product_category`;
CREATE TABLE `product_category` (
  `id`          BIGINT(20)    UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '货物类别ID',
  `name`        VARCHAR(50)   DEFAULT NULL COMMENT '货物类别名',
  `description` VARCHAR(100)  DEFAULT NULL COMMENT '货物类别描述',
  `gmt_create`  TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `gmt_modify`  TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT ='货物类别表';

-- ----------------------------
-- Records of goods_category
-- ----------------------------
INSERT INTO `product_category` VALUES ('1', '电脑配件', '电脑硬件相关', current_timestamp, current_timestamp);
INSERT INTO `product_category` VALUES ('2', '书籍', '纸制品书籍', current_timestamp, current_timestamp);
INSERT INTO `product_category` VALUES ('3', '办公用品', '办公用的东西', current_timestamp, current_timestamp);


SET FOREIGN_KEY_CHECKS = 1;
