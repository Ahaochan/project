/*
Navicat MySQL Data Transfer

Source Server : mysql
Source Server Version : 50544
Source Host : localhost:3306
Source Database : invoice

Target Server Type  : MYSQL
Target Server Version : 50544
File Encoding : 65001

Date: 2017-08-12 20:47:09
*/

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for invoice
-- ----------------------------
DROP TABLE IF EXISTS `invoice`;
CREATE TABLE `invoice` (
  `id`              BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '发票ID',
  `invoice_code`    VARCHAR(20) NOT NULL COMMENT '发票代码',
  `invoice_number`  VARCHAR(20) NOT NULL COMMENT '发票号码',
  `date`            TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00'  COMMENT '开票日期',
  `is_sell`   TINYINT(1) DEFAULT NULL COMMENT '是否为销项发票',
  `unit_id`   BIGINT(20) UNSIGNED NOT NULL  COMMENT '购销单位id',
  `review_id` BIGINT(20) UNSIGNED DEFAULT NULL  COMMENT '复核人id',
  `drawer_id` BIGINT(20) UNSIGNED DEFAULT NULL  COMMENT '开票人id',
  `gmt_create`  TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00'  COMMENT '创建时间',
  `gmt_modify`  TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00'  COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `un_invoice_code_number` (`invoice_code`, `invoice_number`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARSET = utf8  COMMENT ='发票表';

-- ----------------------------
-- Records of invoice
-- ----------------------------
SET FOREIGN_KEY_CHECKS = 1;

# 进项数据
INSERT INTO `invoice` VALUES ('1', '4400163320', '00000001', '2016-05-01 00:00:00','0', '1', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('2', '4400163320', '00000002', '2016-10-01 00:00:00','0', '1', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('3', '4400163320', '00000003', '2016-05-01 00:00:00','0', '1', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('4', '4400163320', '00000004', '2016-12-01 00:00:00','0', '1', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('5', '4400163320', '00000005', '2016-10-01 00:00:00','0', '1', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('6', '4400163320', '00000006', '2016-03-01 00:00:00','0', '1', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('7', '4400163320', '00000007', '2016-06-01 00:00:00','0', '1', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('8', '4400163320', '00000008', '2016-03-01 00:00:00','0', '1', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('9', '4400163320', '00000009', '2016-08-01 00:00:00','0', '1', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('10', '4400163320', '00000010', '2016-01-01 00:00:00','0', '1', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('11', '4400163320', '00000011', '2016-10-01 00:00:00','0', '1', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('12', '4400163320', '00000012', '2016-04-01 00:00:00','0', '1', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('13', '4400163320', '00000013', '2016-05-01 00:00:00','0', '1', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('14', '4400163320', '00000014', '2016-05-01 00:00:00','0', '1', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('15', '4400163320', '00000015', '2016-09-01 00:00:00','0', '1', '', '', current_timestamp, current_timestamp);

INSERT INTO `invoice` VALUES ('16', '4400163320', '00000016', '2016-07-01 00:00:00','0', '2', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('17', '4400163320', '00000017', '2016-02-01 00:00:00','0', '2', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('18', '4400163320', '00000018', '2016-11-01 00:00:00','0', '2', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('19', '4400163320', '00000019', '2016-05-01 00:00:00','0', '2', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('20', '4400163320', '00000020', '2016-02-01 00:00:00','0', '2', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('21', '4400163320', '00000021', '2016-10-01 00:00:00','0', '2', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('22', '4400163320', '00000022', '2016-06-01 00:00:00','0', '2', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('23', '4400163320', '00000023', '2016-07-01 00:00:00','0', '2', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('24', '4400163320', '00000024', '2016-08-01 00:00:00','0', '2', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('25', '4400163320', '00000025', '2016-10-01 00:00:00','0', '2', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('26', '4400163320', '00000026', '2016-10-01 00:00:00','0', '2', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('27', '4400163320', '00000027', '2016-02-01 00:00:00','0', '2', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('28', '4400163320', '00000028', '2016-09-01 00:00:00','0', '2', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('29', '4400163320', '00000029', '2016-05-01 00:00:00','0', '2', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('30', '4400163320', '00000030', '2016-08-01 00:00:00','0', '2', '', '', current_timestamp, current_timestamp);

INSERT INTO `invoice` VALUES ('31', '5500163320', '00000031', '2016-08-01 00:00:00','0', '3', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('32', '5500163320', '00000032', '2016-03-01 00:00:00','0', '3', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('33', '5500163320', '00000033', '2016-02-01 00:00:00','0', '3', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('34', '5500163320', '00000034', '2016-06-01 00:00:00','0', '3', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('35', '5500163320', '00000035', '2016-10-01 00:00:00','0', '3', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('36', '5500163320', '00000036', '2016-07-01 00:00:00','0', '3', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('37', '5500163320', '00000037', '2016-01-01 00:00:00','0', '3', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('38', '5500163320', '00000038', '2016-03-01 00:00:00','0', '3', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('39', '5500163320', '00000039', '2016-03-01 00:00:00','0', '3', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('40', '5500163320', '00000040', '2016-02-01 00:00:00','0', '3', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('41', '5500163320', '00000041', '2016-05-01 00:00:00','0', '3', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('42', '5500163320', '00000042', '2016-12-01 00:00:00','0', '3', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('43', '5500163320', '00000043', '2016-10-01 00:00:00','0', '3', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('44', '5500163320', '00000044', '2016-09-01 00:00:00','0', '3', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('45', '5500163320', '00000045', '2016-09-01 00:00:00','0', '3', '', '', current_timestamp, current_timestamp);

INSERT INTO `invoice` VALUES ('46', '5500163320', '00000046', '2016-02-01 00:00:00','1', '1', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('47', '5500163320', '00000047', '2016-07-01 00:00:00','1', '1', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('48', '5500163320', '00000048', '2016-03-01 00:00:00','1', '1', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('49', '5500163320', '00000049', '2016-01-01 00:00:00','1', '1', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('50', '5500163320', '00000050', '2016-10-01 00:00:00','1', '1', '', '', current_timestamp, current_timestamp);

INSERT INTO `invoice` VALUES ('51', '5500163320', '00000051', '2016-11-01 00:00:00','1', '2', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('52', '5500163320', '00000052', '2016-07-01 00:00:00','1', '2', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('53', '5500163320', '00000053', '2016-12-01 00:00:00','1', '2', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('54', '5500163320', '00000054', '2016-08-01 00:00:00','1', '2', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('55', '5500163320', '00000055', '2016-07-01 00:00:00','1', '2', '', '', current_timestamp, current_timestamp);

INSERT INTO `invoice` VALUES ('51', '5500163320', '00000051', '2016-11-01 00:00:00','1', '3', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('52', '5500163320', '00000052', '2016-01-01 00:00:00','1', '3', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('53', '5500163320', '00000053', '2016-11-01 00:00:00','1', '3', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('54', '5500163320', '00000054', '2016-06-01 00:00:00','1', '3', '', '', current_timestamp, current_timestamp);
INSERT INTO `invoice` VALUES ('55', '5500163320', '00000055', '2016-10-01 00:00:00','1', '3', '', '', current_timestamp, current_timestamp);