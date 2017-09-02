/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50544
Source Host           : localhost:3306
Source Database       : invoice

Target Server Type    : MYSQL
Target Server Version : 50544
File Encoding         : 65001

Date: 2017-08-12 20:46:19
*/

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin_auth
-- ----------------------------
DROP TABLE IF EXISTS `admin_auth`;
CREATE TABLE `admin_auth` (
  `id`          BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '权限ID',
  `name`        VARCHAR(50)   DEFAULT NULL COMMENT '权限名',
  `description` VARCHAR(100)  DEFAULT NULL COMMENT '权限描述',
  `enabled`     TINYINT(1)    DEFAULT NULL COMMENT '权限可用',
  `gmt_create`  TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `gmt_modify`  TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT ='权限表';

-- ----------------------------
-- Records of admin_auth
-- ----------------------------
INSERT INTO `admin_auth` VALUES ('1', 'auth.index.view', '访问首页', '1', '2017-07-22 23:05:26', '2017-07-22 23:05:26');
INSERT INTO `admin_auth` VALUES ('2', 'auth.user.add', '添加用户', '1', '2017-07-22 23:05:26', '2017-07-22 23:05:26');
INSERT INTO `admin_auth` VALUES ('3', 'auth.user.disable', '禁用用户', '0', '2017-07-22 23:05:26', '2017-07-22 23:05:26');
INSERT INTO `admin_auth` VALUES ('4', 'auth.user.delete', '删除用户', '1', '2017-07-22 23:05:26', '2017-07-22 23:05:26');
INSERT INTO `admin_auth` VALUES ('5', 'auth.user.view.self', '查看自己的用户信息', '1', '2017-07-22 23:05:26', '2017-07-22 23:05:26');
INSERT INTO `admin_auth` VALUES ('6', 'auth.user.view.all', '查看所有的用户信息', '1', '2017-07-22 23:05:26', '2017-07-22 23:05:26');
INSERT INTO `admin_auth` VALUES ('7', 'auth.user.edit', '修改自己的信息', '1', '2017-07-22 23:05:26', '2017-07-22 23:05:26');
INSERT INTO `admin_auth` VALUES ('8', 'auth.user.edit.role', '修改用户的角色', '1', '2017-07-22 23:05:26', '2017-07-22 23:05:26');
INSERT INTO `admin_auth` VALUES ('9', 'auth.role.add', '添加角色', '1', '2017-07-22 23:05:26', '2017-07-22 23:05:26');
INSERT INTO `admin_auth` VALUES ('10', 'auth.role.disable', '禁用角色', '1', '2017-07-22 23:05:26', '2017-07-22 23:05:26');
INSERT INTO `admin_auth` VALUES ('11', 'auth.role.delete', '删除角色', '1', '2017-07-22 23:05:26', '2017-07-22 23:05:26');
INSERT INTO `admin_auth` VALUES ('12', 'auth.role.view.all', '查看所有的角色信息', '1', '2017-07-22 23:05:26', '2017-07-22 23:05:26');
INSERT INTO `admin_auth` VALUES ('13', 'auth.role.edit', '修改角色信息', '1', '2017-07-22 23:05:26', '2017-07-22 23:05:26');
INSERT INTO `admin_auth` VALUES ('14', 'auth.auth.add', '添加权限', '1', '2017-07-22 23:05:26', '2017-07-22 23:05:26');
INSERT INTO `admin_auth` VALUES ('15', 'auth.auth.disable', '禁用权限', '1', '2017-07-22 23:05:26', '2017-07-22 23:05:26');
INSERT INTO `admin_auth` VALUES ('16', 'auth.auth.delete', '删除权限', '1', '2017-07-22 23:05:26', '2017-07-22 23:05:26');
INSERT INTO `admin_auth` VALUES ('17', 'auth.auth.view.all', '查看所有权限信息', '1', '2017-07-22 23:05:26', '2017-07-22 23:05:26');
INSERT INTO `admin_auth` VALUES ('18', 'auth.auth.edit', '修改权限信息', '1', '2017-07-22 23:05:26', '2017-07-22 23:05:26');
INSERT INTO `admin_auth` VALUES ('19', 'auth.invoice.add', '添加发票', '1', '2017-07-22 23:05:26', '2017-07-22 23:05:26');
INSERT INTO `admin_auth` VALUES ('20', 'auth.invoice.disable', '禁用发票', '1', '2017-07-22 23:05:26', '2017-07-22 23:05:26');
INSERT INTO `admin_auth` VALUES ('21', 'auth.invoice.delete', '删除发票', '1', '2017-07-22 23:05:26', '2017-07-22 23:05:26');
INSERT INTO `admin_auth` VALUES ('22', 'auth.invoice.view.all', '查看所有发票信息', '1', '2017-07-22 23:05:26', '2017-07-22 23:05:26');
INSERT INTO `admin_auth` VALUES ('23', 'auth.invoice.edit', '编辑发票', '1', '2017-07-22 23:05:26', '2017-07-22 23:05:26');
INSERT INTO `admin_auth` VALUES ('24', 'auth.invoice.review', '审查发票', '1', '2017-07-22 23:05:26', '2017-07-22 23:05:26');
SET FOREIGN_KEY_CHECKS = 1;
