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

INSERT INTO `invoice__goods` VALUES ('1', '1', '7', '13', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('2', '2', '5', '19', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('3', '3', '9', '18', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('4', '4', '7', '19', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('5', '5', '5', '20', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('6', '6', '7', '19', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('7', '7', '6', '17', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('8', '8', '11', '14', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('9', '9', '11', '20', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('10', '10', '4', '20', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('11', '11', '9', '14', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('12', '12', '7', '13', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('13', '13', '5', '14', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('14', '14', '5', '10', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('15', '15', '5', '12', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('16', '16', '6', '19', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('17', '17', '9', '14', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('18', '18', '8', '15', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('19', '19', '8', '10', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('20', '20', '11', '18', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('21', '21', '11', '11', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('22', '22', '9', '19', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('23', '23', '8', '20', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('24', '24', '8', '10', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('25', '25', '9', '12', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('26', '26', '7', '15', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('27', '27', '9', '13', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('28', '28', '11', '13', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('29', '29', '8', '10', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('30', '30', '8', '16', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('31', '31', '7', '20', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('32', '32', '11', '19', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('33', '33', '7', '19', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('34', '34', '11', '12', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('35', '35', '10', '19', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('36', '36', '10', '18', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('37', '37', '5', '13', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('38', '38', '7', '20', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('39', '39', '6', '16', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('40', '40', '11', '11', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('41', '41', '9', '13', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('42', '42', '11', '17', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('43', '43', '7', '12', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('44', '44', '4', '15', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('45', '45', '7', '15', current_timestamp, current_timestamp);

INSERT INTO `invoice__goods` VALUES ('46', '46', '2', '12', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('47', '47', '1', '11', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('48', '48', '3', '9', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('49', '49', '3', '9', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('50', '50', '2', '11', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('51', '51', '3', '12', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('52', '52', '1', '6', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('53', '53', '3', '6', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('54', '54', '3', '10', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('55', '55', '2', '5', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('56', '56', '3', '9', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('57', '57', '3', '9', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('58', '58', '2', '11', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('59', '59', '3', '12', current_timestamp, current_timestamp);
INSERT INTO `invoice__goods` VALUES ('60', '60', '1', '6', current_timestamp, current_timestamp);