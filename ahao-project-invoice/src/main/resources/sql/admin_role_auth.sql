/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50544
Source Host           : localhost:3306
Source Database       : invoice

Target Server Type    : MYSQL
Target Server Version : 50544
File Encoding         : 65001

Date: 2017-08-12 20:46:39
*/

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin_role_auth
-- ----------------------------
DROP TABLE IF EXISTS `admin_role_auth`;
CREATE TABLE `admin_role_auth` (
  `id`         BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '角色权限ID',
  `role_id`    BIGINT(20) UNSIGNED NOT NULL
  COMMENT '角色ID',
  `auth_id`    BIGINT(20) UNSIGNED NOT NULL
  COMMENT '权限ID',
  `gmt_create` TIMESTAMP           NOT NULL DEFAULT '0000-00-00 00:00:00'
  COMMENT '创建时间',
  `gmt_modify` TIMESTAMP           NOT NULL DEFAULT '0000-00-00 00:00:00'
  COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_role_auth_id` (`role_id`, `auth_id`),
  KEY `fk_ra_auth_id` (`auth_id`),
  CONSTRAINT `fk_ra_auth_id` FOREIGN KEY (`auth_id`) REFERENCES `admin_auth` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_ra_role_id` FOREIGN KEY (`role_id`) REFERENCES `admin_role` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT ='角色权限表';

-- ----------------------------
-- Records of admin_role_auth
-- ----------------------------
INSERT INTO `admin_role_auth` VALUES ('1', '1', '1', '2017-07-22 23:05:26', '2017-07-22 23:05:26');
INSERT INTO `admin_role_auth` VALUES ('2', '1', '2', '2017-07-22 23:05:26', '2017-07-22 23:05:26');
INSERT INTO `admin_role_auth` VALUES ('3', '1', '3', '2017-07-22 23:05:26', '2017-07-22 23:05:26');
INSERT INTO `admin_role_auth` VALUES ('4', '1', '4', '2017-07-22 23:05:26', '2017-07-22 23:05:26');
INSERT INTO `admin_role_auth` VALUES ('5', '1', '5', '2017-07-22 23:05:26', '2017-07-22 23:05:26');
INSERT INTO `admin_role_auth` VALUES ('6', '2', '1', '2017-07-22 23:05:26', '2017-07-22 23:05:26');
INSERT INTO `admin_role_auth` VALUES ('7', '3', '1', '2017-07-22 23:05:26', '2017-07-22 23:05:26');
SET FOREIGN_KEY_CHECKS = 1;
