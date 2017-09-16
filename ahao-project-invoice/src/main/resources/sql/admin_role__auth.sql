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
-- Table structure for admin_role__auth
-- ----------------------------
DROP TABLE IF EXISTS `admin_role__auth`;
CREATE TABLE `admin_role__auth` (
  `id`         BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '角色权限ID',
  `role_id`    BIGINT(20) UNSIGNED NOT NULL COMMENT '角色ID',
  `auth_id`    BIGINT(20) UNSIGNED NOT NULL COMMENT '权限ID',
  `gmt_create` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `gmt_modify` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_role__auth_id` (`role_id`, `auth_id`),
  KEY `fk_ra_auth_id` (`auth_id`),
  CONSTRAINT `fk_ra_auth_id` FOREIGN KEY (`auth_id`) REFERENCES `admin_auth` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_ra_role_id` FOREIGN KEY (`role_id`) REFERENCES `admin_role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT ='角色权限表';

-- ----------------------------
-- Records of admin_role__auth
-- ----------------------------
INSERT INTO `admin_role__auth` VALUES ('1', '1', '1', current_timestamp, current_timestamp);
INSERT INTO `admin_role__auth` VALUES ('2', '1', '2', current_timestamp, current_timestamp);
INSERT INTO `admin_role__auth` VALUES ('3', '1', '3', current_timestamp, current_timestamp);
INSERT INTO `admin_role__auth` VALUES ('4', '1', '4', current_timestamp, current_timestamp);
INSERT INTO `admin_role__auth` VALUES ('5', '1', '5', current_timestamp, current_timestamp);
INSERT INTO `admin_role__auth` VALUES ('6', '1', '6', current_timestamp, current_timestamp);
INSERT INTO `admin_role__auth` VALUES ('7', '1', '7', current_timestamp, current_timestamp);
INSERT INTO `admin_role__auth` VALUES ('8', '1', '8', current_timestamp, current_timestamp);
INSERT INTO `admin_role__auth` VALUES ('9', '1', '9', current_timestamp, current_timestamp);
INSERT INTO `admin_role__auth` VALUES ('10', '1', '10', current_timestamp, current_timestamp);
INSERT INTO `admin_role__auth` VALUES ('11', '1', '11', current_timestamp, current_timestamp);
INSERT INTO `admin_role__auth` VALUES ('12', '1', '12', current_timestamp, current_timestamp);
INSERT INTO `admin_role__auth` VALUES ('13', '1', '13', current_timestamp, current_timestamp);
INSERT INTO `admin_role__auth` VALUES ('14', '1', '14', current_timestamp, current_timestamp);
INSERT INTO `admin_role__auth` VALUES ('15', '1', '15', current_timestamp, current_timestamp);
INSERT INTO `admin_role__auth` VALUES ('16', '1', '16', current_timestamp, current_timestamp);
INSERT INTO `admin_role__auth` VALUES ('17', '1', '17', current_timestamp, current_timestamp);
INSERT INTO `admin_role__auth` VALUES ('18', '1', '18', current_timestamp, current_timestamp);
INSERT INTO `admin_role__auth` VALUES ('19', '1', '19', current_timestamp, current_timestamp);
INSERT INTO `admin_role__auth` VALUES ('20', '1', '20', current_timestamp, current_timestamp);
INSERT INTO `admin_role__auth` VALUES ('21', '1', '21', current_timestamp, current_timestamp);
INSERT INTO `admin_role__auth` VALUES ('22', '1', '22', current_timestamp, current_timestamp);
INSERT INTO `admin_role__auth` VALUES ('23', '1', '23', current_timestamp, current_timestamp);
INSERT INTO `admin_role__auth` VALUES ('24', '1', '24', current_timestamp, current_timestamp);
INSERT INTO `admin_role__auth` VALUES ('25', '1', '25', current_timestamp, current_timestamp);
INSERT INTO `admin_role__auth` VALUES ('26', '1', '26', current_timestamp, current_timestamp);
INSERT INTO `admin_role__auth` VALUES ('27', '1', '27', current_timestamp, current_timestamp);
INSERT INTO `admin_role__auth` VALUES ('28', '1', '28', current_timestamp, current_timestamp);
INSERT INTO `admin_role__auth` VALUES ('29', '1', '29', current_timestamp, current_timestamp);
INSERT INTO `admin_role__auth` VALUES ('30', '1', '30', current_timestamp, current_timestamp);
INSERT INTO `admin_role__auth` VALUES ('31', '1', '31', current_timestamp, current_timestamp);
INSERT INTO `admin_role__auth` VALUES ('32', '1', '32', current_timestamp, current_timestamp);
INSERT INTO `admin_role__auth` VALUES ('33', '1', '32', current_timestamp, current_timestamp);


INSERT INTO `admin_role__auth` VALUES ('34', '2', '1', current_timestamp, current_timestamp);
INSERT INTO `admin_role__auth` VALUES ('35', '3', '1', current_timestamp, current_timestamp);
SET FOREIGN_KEY_CHECKS = 1;
