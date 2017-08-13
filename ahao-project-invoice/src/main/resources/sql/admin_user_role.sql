/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50544
Source Host           : localhost:3306
Source Database       : invoice

Target Server Type    : MYSQL
Target Server Version : 50544
File Encoding         : 65001

Date: 2017-08-12 20:44:52
*/

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin_user_role
-- ----------------------------
DROP TABLE IF EXISTS `admin_user_role`;
CREATE TABLE `admin_user_role` (
  `id`         BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '用户角色ID',
  `user_id`    BIGINT(20) UNSIGNED          DEFAULT NULL
  COMMENT '用户ID',
  `role_id`    BIGINT(20) UNSIGNED          DEFAULT NULL
  COMMENT '角色ID',
  `gmt_create` TIMESTAMP           NOT NULL DEFAULT '0000-00-00 00:00:00'
  COMMENT '创建时间',
  `gmt_modify` TIMESTAMP           NOT NULL DEFAULT '0000-00-00 00:00:00'
  COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_role_id` (`user_id`, `role_id`) USING BTREE,
  KEY `fk_ur_role_id` (`role_id`),
  CONSTRAINT `fk_ur_role_id` FOREIGN KEY (`role_id`) REFERENCES `admin_role` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_ur_user_id` FOREIGN KEY (`user_id`) REFERENCES `admin_user` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT ='用户角色表';

-- ----------------------------
-- Records of admin_user_role
-- ----------------------------
INSERT INTO `admin_user_role` VALUES ('1', '1', '1', '2017-07-22 23:05:26', '2017-07-22 23:05:26');
INSERT INTO `admin_user_role` VALUES ('2', '1', '2', '2017-07-22 23:05:26', '2017-07-22 23:05:26');
INSERT INTO `admin_user_role` VALUES ('3', '1', '3', '2017-07-22 23:05:26', '2017-07-22 23:05:26');
SET FOREIGN_KEY_CHECKS = 1;
