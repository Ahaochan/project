/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50544
Source Host           : localhost:3306
Source Database       : invoice

Target Server Type    : MYSQL
Target Server Version : 50544
File Encoding         : 65001

Date: 2017-08-12 20:46:31
*/

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin_role
-- ----------------------------
DROP TABLE IF EXISTS `admin_role`;
CREATE TABLE `admin_role` (
  `id`          BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `name`        VARCHAR(50) NOT NULL COMMENT '角色名',
  `description` VARCHAR(100)  DEFAULT NULL COMMENT '角色描述',
  `enabled`     TINYINT(1)    DEFAULT NULL COMMENT '角色可用',
  `gmt_create`  TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `gmt_modify`  TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT ='角色表';

-- ----------------------------
-- Records of admin_role
-- ----------------------------
INSERT INTO `admin_role` VALUES ('1', 'role.root', '最高管理员', '1', '2017-08-12 20:01:30', '2017-08-12 12:01:04');
INSERT INTO `admin_role` VALUES ('2', 'role.operation', '运营管理员', '1', '2017-08-12 20:01:04', '2017-08-12 12:01:04');
INSERT INTO `admin_role` VALUES ('3', 'role.finance', '财务管理员', '1', '2017-08-12 20:01:04', '2017-08-12 12:01:04');
SET FOREIGN_KEY_CHECKS = 1;
