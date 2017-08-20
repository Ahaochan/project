/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50544
Source Host           : localhost:3306
Source Database       : invoice

Target Server Type    : MYSQL
Target Server Version : 50544
File Encoding         : 65001

Date: 2017-08-12 20:47:19
*/

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for invoice_unit
-- ----------------------------
DROP TABLE IF EXISTS `invoice_unit`;
CREATE TABLE `invoice_unit` (
  `id`         BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '购销单位ID',
  `name`       VARCHAR(100)        NOT NULL
  COMMENT '购销单位名',
  `tax_id`     VARCHAR(20)         NOT NULL
  COMMENT '纳税人识别号',
  `address`    VARCHAR(100)                 DEFAULT NULL
  COMMENT '地址',
  `tel`        VARCHAR(15)                  DEFAULT NULL
  COMMENT '电话',
  `account`    VARCHAR(30)                  DEFAULT NULL
  COMMENT '银行账户',
  `gmt_create` TIMESTAMP           NOT NULL DEFAULT '0000-00-00 00:00:00'
  COMMENT '创建时间',
  `gmt_modify` TIMESTAMP           NOT NULL DEFAULT '0000-00-00 00:00:00'
  COMMENT '修改时间',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT ='购销单位表';

-- ----------------------------
-- Records of invoice_unit
-- ----------------------------


INSERT INTO `invoice_unit`
VALUES ('1', '韶关学院', '12440200455904030C', '广东省韶关市浈江区大学路288号', '无', '无', current_timestamp, current_timestamp);
INSERT INTO `invoice_unit` VALUES
  ('2', '广州大学', '124401007348911139', '广州市广州市番禺区大学城外环西路230号', '39366177', '3602114819100000192', current_timestamp,
   current_timestamp);
INSERT INTO `invoice_unit` VALUES
  ('3', '武汉极意网络科技有限公司', '91420100055714019Q', '武汉市东湖开发区大学园路武汉大学科技园内兴业楼2单元2楼204室-020号', '无', '6228480402564890018', current_timestamp,
   current_timestamp);

SET FOREIGN_KEY_CHECKS = 1;