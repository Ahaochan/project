/*
Navicat MySQL Data Transfer

Source Server : mysql
Source Server Version : 50544
Source Host : localhost:3306
Source Database : invoice

Target Server Type : MYSQL
Target Server Version : 50544
File Encoding : 65001

Date: 2017-08-12 20:46:55
*/

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `product_goods`;
CREATE TABLE `product_goods` (
  `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '货物ID',
  `name` VARCHAR(50) DEFAULT NULL COMMENT '货物名',
  `category_id` BIGINT(20) UNSIGNED DEFAULT NULL COMMENT '货物类别id',
  `specification` VARCHAR(10) DEFAULT NULL COMMENT '规格型号',
  `unit` VARCHAR(5) DEFAULT NULL COMMENT '单位',
  `unite_price` DECIMAL(10, 2) UNSIGNED DEFAULT NULL COMMENT '单价',
  `tax_rate` VARCHAR(10) DEFAULT NULL COMMENT '税率',
  `gmt_create` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `gmt_modify` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT ='货物表';

-- ----------------------------
-- Records of goods
-- ----------------------------
INSERT INTO `product_goods` VALUES ('1', '雷柏无线光学鼠标', '1', '3600', '个', '99.00', '17%', current_timestamp, current_timestamp);
INSERT INTO `product_goods` VALUES ('2', '雷柏三模无线激光鼠标', '1', 'MT750', '个', '299.00', '17%', current_timestamp, current_timestamp);
INSERT INTO `product_goods` VALUES ('3', '雷柏光学无线鼠标', '1', '3500PRO', '个', '99.00', '17%', current_timestamp, current_timestamp);
INSERT INTO `product_goods` VALUES ('4', '雷柏无线办公机械键盘', '1', 'KX', '个', '299.00', '17%', current_timestamp, current_timestamp);

INSERT INTO `product_goods` VALUES ('5', '三国演义', '2', '', '本', '100.00', '17%', current_timestamp, current_timestamp);
INSERT INTO `product_goods` VALUES ('6', '西游记', '2', '', '本', '110.00', '17%', current_timestamp, current_timestamp);
INSERT INTO `product_goods` VALUES ('7', '隋唐演义', '2', '', '本', '120.00', '17%', current_timestamp, current_timestamp);
INSERT INTO `product_goods` VALUES ('8', '水浒传', '2', '', '本', '130.00', '17%', current_timestamp, current_timestamp);
INSERT INTO `product_goods` VALUES ('9', '红楼梦', '2', '', '本', '140.00', '17%', current_timestamp, current_timestamp);
INSERT INTO `product_goods` VALUES ('10', '格林童话', '2', '', '本', '150.00', '17%', current_timestamp, current_timestamp);

INSERT INTO `product_goods` VALUES ('11', '订书钉', '3', '得力0012', '盒', '0.96', '17%', current_timestamp, current_timestamp);
INSERT INTO `product_goods` VALUES ('12', '液体胶水', '3', '得力7303', '支', '4.03', '17%', current_timestamp, current_timestamp);
INSERT INTO `product_goods` VALUES ('13', '笔筒', '3', '史泰博B101', '个', '8.80', '17%', current_timestamp, current_timestamp);
INSERT INTO `product_goods` VALUES ('14', '塑包面纸', '3', '200抽双层', '3包/提', '19.80', '17%', current_timestamp, current_timestamp);

SET FOREIGN_KEY_CHECKS = 1;
