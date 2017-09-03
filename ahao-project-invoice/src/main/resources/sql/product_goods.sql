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
  `id`  BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '货物ID',
  `name` VARCHAR(50) DEFAULT NULL COMMENT '货物名',
  `category_id` BIGINT(20) UNSIGNED DEFAULT NULL COMMENT '货物类别id',
  `specification` VARCHAR(10) DEFAULT NULL COMMENT '规格型号',
  `unit` VARCHAR(5) DEFAULT NULL COMMENT '单位',
  `unite_price` DECIMAL(10, 2) UNSIGNED DEFAULT NULL COMMENT '单价',
  `tax_rate` DECIMAL(3, 2) DEFAULT 0 COMMENT '税率',
  `gmt_create` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `gmt_modify` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT ='货物表';

-- ----------------------------
-- Records of goods
-- ----------------------------
INSERT INTO `product_goods` VALUES ('1', '鸡肉罐头', '2', null, '个', '8.00' , 0.17, current_timestamp, current_timestamp);
INSERT INTO `product_goods` VALUES ('2', '鱼肉罐头', '2', null, '个', '9.00' , 0.17, current_timestamp, current_timestamp);
INSERT INTO `product_goods` VALUES ('3', '猪肉罐头', '2', null, '个', '10.00', 0.17, current_timestamp, current_timestamp);

INSERT INTO `product_goods` VALUES ('4', '鸡肉', '1', null, '斤', '0.80', 0.17, current_timestamp, current_timestamp);
INSERT INTO `product_goods` VALUES ('5', '鱼肉', '1', null, '斤', '1.30', 0.17, current_timestamp, current_timestamp);
INSERT INTO `product_goods` VALUES ('6', '猪肉', '1', null, '斤', '2.60', 0.17, current_timestamp, current_timestamp);

INSERT INTO `product_goods` VALUES ('7' , '面粉'  , '3', null, '斤', '11.00', 0.17, current_timestamp, current_timestamp);
INSERT INTO `product_goods` VALUES ('8' , '玻璃'  , '3', null, '块', '10.00', 0.17, current_timestamp, current_timestamp);
INSERT INTO `product_goods` VALUES ('9' , '铁皮'  , '3', null, '斤', '50.00', 0.17, current_timestamp, current_timestamp);
INSERT INTO `product_goods` VALUES ('10', '包装纸', '3', null, '盒', '29.80', 0.17, current_timestamp, current_timestamp);
INSERT INTO `product_goods` VALUES ('11', '纯净水', '3', null, '升', '4.30' , 0.17, current_timestamp, current_timestamp);


SET FOREIGN_KEY_CHECKS = 1;
