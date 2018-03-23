/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50544
Source Host           : localhost:3306
Source Database       : invoice

Target Server Type    : MYSQL
Target Server Version : 50544
File Encoding         : 65001

Date: 2017-08-12 20:46:48
*/

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin_user
-- ----------------------------
DROP TABLE IF EXISTS `admin_user`;
CREATE TABLE `admin_user` (
  `id`                  BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username`            VARCHAR(20) DEFAULT NULL COMMENT '用户名',
  `password`            VARCHAR(20) DEFAULT NULL COMMENT '密码',
  `last_login_time`     TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00' COMMENT '最后一次登录时间',
  `last_login_ip`       VARCHAR(20) DEFAULT NULL COMMENT '最后一次登录IP',
  `email`               VARCHAR(30) DEFAULT NULL COMMENT '电子邮箱',
  `enabled`             TINYINT(1)  DEFAULT '1' COMMENT '账户可用',
  `account_expired`     TINYINT(1)  DEFAULT '0' COMMENT '账户未过期',
  `credentials_expired` TINYINT(1)  DEFAULT '0' COMMENT '用户证书未过期',
  `account_locked`      TINYINT(1)  DEFAULT '0' COMMENT '账户未上锁',
  `gmt_create`          TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `gmt_modify`          TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00' COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_username` (`username`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT ='用户表';

-- ----------------------------
-- Records of admin_user
-- ----------------------------
INSERT INTO `admin_user` VALUES ('1', 'admin1', 'admin1', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('2', 'admin2', 'admin2', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('3', 'admin3', 'admin3', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('4', 'admin4', 'admin4', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('5', 'admin5', 'admin5', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('6', 'admin6', 'admin6', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('7', 'admin7', 'admin7', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('8', 'admin8', 'admin8', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('9', 'admin9', 'admin9', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('10', 'admin10', 'admin10', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('11', 'admin11', 'admin11', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('12', 'admin12', 'admin12', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('13', 'admin13', 'admin13', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('14', 'admin14', 'admin14', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('15', 'admin15', 'admin15', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('16', 'admin16', 'admin16', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('17', 'admin17', 'admin17', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('18', 'admin18', 'admin18', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('19', 'admin19', 'admin19', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('20', 'admin20', 'admin20', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('21', 'admin21', 'admin21', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('22', 'admin22', 'admin22', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('23', 'admin23', 'admin23', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('24', 'admin24', 'admin24', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('25', 'admin25', 'admin25', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('26', 'admin26', 'admin26', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('27', 'admin27', 'admin27', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('28', 'admin28', 'admin28', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('29', 'admin29', 'admin29', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('30', 'admin30', 'admin30', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('31', 'admin31', 'admin31', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('32', 'admin32', 'admin32', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('33', 'admin33', 'admin33', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('34', 'admin34', 'admin34', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('35', 'admin35', 'admin35', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('36', 'admin36', 'admin36', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('37', 'admin37', 'admin37', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('38', 'admin38', 'admin38', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('39', 'admin39', 'admin39', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('40', 'admin40', 'admin40', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('41', 'admin41', 'admin41', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('42', 'admin42', 'admin42', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('43', 'admin43', 'admin43', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('44', 'admin44', 'admin44', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('45', 'admin45', 'admin45', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('46', 'admin46', 'admin46', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('47', 'admin47', 'admin47', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('48', 'admin48', 'admin48', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
INSERT INTO `admin_user` VALUES ('49', 'admin49', 'admin49', NULL, NULL, '123@qq.com', '1', '0', '0', '0', '2017-07-22 23:05:26', '2017-08-12 12:02:10');
SET FOREIGN_KEY_CHECKS = 1;
