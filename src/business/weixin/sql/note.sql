/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50619
Source Host           : localhost:3306
Source Database       : usermaindb

Target Server Type    : MYSQL
Target Server Version : 50619
File Encoding         : 65001

Date: 2014-12-26 15:24:12
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `note`
-- ----------------------------
DROP TABLE IF EXISTS `note`;
CREATE TABLE `note` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `openid` varchar(256) NOT NULL,
  `edittime` datetime NOT NULL,
  `planday` varchar(255) NOT NULL COMMENT '格式:2014-10-12',
  `context` varchar(256) NOT NULL,
  `type` int(11) NOT NULL COMMENT '0:计划  1：总结',
  `msgid` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `msgid` (`msgid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of note
-- ----------------------------
INSERT INTO `note` VALUES ('12', 'odZfxt9vOko2C100d4Y19_jTC9k4', '2014-12-26 15:22:24', '2014-12-26', '45', '1', '6097043811627085342');
