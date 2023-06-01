/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50051
Source Host           : localhost:3306
Source Database       : project_db

Target Server Type    : MYSQL
Target Server Version : 50051
File Encoding         : 65001

Date: 2018-07-23 19:36:42
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `admin`
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `username` varchar(20) NOT NULL default '',
  `password` varchar(32) default NULL,
  PRIMARY KEY  (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('a', 'a');

-- ----------------------------
-- Table structure for `t_dataauth`
-- ----------------------------
DROP TABLE IF EXISTS `t_dataauth`;
CREATE TABLE `t_dataauth` (
  `authId` int(11) NOT NULL auto_increment COMMENT '授权id',
  `dataObj` int(11) NOT NULL COMMENT '项目资料',
  `userObj` varchar(30) NOT NULL COMMENT '用户',
  `requestTime` varchar(20) default NULL COMMENT '申请时间',
  `shenHeState` varchar(20) NOT NULL COMMENT '申请状态',
  `shenHeReply` varchar(800) NOT NULL COMMENT '审核回复',
  PRIMARY KEY  (`authId`),
  KEY `dataObj` (`dataObj`),
  KEY `userObj` (`userObj`),
  CONSTRAINT `t_dataauth_ibfk_1` FOREIGN KEY (`dataObj`) REFERENCES `t_projectdata` (`dataId`),
  CONSTRAINT `t_dataauth_ibfk_2` FOREIGN KEY (`userObj`) REFERENCES `t_userinfo` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_dataauth
-- ----------------------------
INSERT INTO `t_dataauth` VALUES ('2', '2', 'STU001', '2018-04-25 13:04:20', '审核通过', '可以看哈');

-- ----------------------------
-- Table structure for `t_proauth`
-- ----------------------------
DROP TABLE IF EXISTS `t_proauth`;
CREATE TABLE `t_proauth` (
  `authId` int(11) NOT NULL auto_increment COMMENT '记录id',
  `projectObj` int(11) NOT NULL COMMENT '项目',
  `userObj` varchar(30) NOT NULL COMMENT '用户',
  `remark` varchar(800) default NULL COMMENT '备注',
  `addTime` varchar(20) default NULL COMMENT '加入时间',
  PRIMARY KEY  (`authId`),
  KEY `projectObj` (`projectObj`),
  KEY `userObj` (`userObj`),
  CONSTRAINT `t_proauth_ibfk_1` FOREIGN KEY (`projectObj`) REFERENCES `t_project` (`projectId`),
  CONSTRAINT `t_proauth_ibfk_2` FOREIGN KEY (`userObj`) REFERENCES `t_userinfo` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_proauth
-- ----------------------------
INSERT INTO `t_proauth` VALUES ('1', '1', 'STU001', '项目人员备注1', '2018-04-14 18:29:29');
INSERT INTO `t_proauth` VALUES ('2', '2', 'STU002', 'test', '2018-04-24 19:17:04');

-- ----------------------------
-- Table structure for `t_proaward`
-- ----------------------------
DROP TABLE IF EXISTS `t_proaward`;
CREATE TABLE `t_proaward` (
  `awardId` int(11) NOT NULL auto_increment COMMENT '获奖id',
  `projectObj` int(11) NOT NULL COMMENT '获奖项目',
  `awardName` varchar(40) NOT NULL COMMENT '获奖名称',
  `level` varchar(20) NOT NULL COMMENT '级别',
  `userObj` varchar(30) NOT NULL COMMENT '获奖用户',
  `awardTime` varchar(20) default NULL COMMENT '获奖时间',
  `createTime` varchar(20) default NULL COMMENT '创建时间',
  `createUser` varchar(20) NOT NULL COMMENT '创建人',
  `accessory` varchar(60) NOT NULL COMMENT '附件信息',
  PRIMARY KEY  (`awardId`),
  KEY `projectObj` (`projectObj`),
  KEY `userObj` (`userObj`),
  CONSTRAINT `t_proaward_ibfk_1` FOREIGN KEY (`projectObj`) REFERENCES `t_project` (`projectId`),
  CONSTRAINT `t_proaward_ibfk_2` FOREIGN KEY (`userObj`) REFERENCES `t_userinfo` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_proaward
-- ----------------------------
INSERT INTO `t_proaward` VALUES ('1', '1', '获奖名称1', '一等奖', 'STU001', '2018-04-10 18:29:47', '2018-04-14 18:29:52', '获奖创建人', 'upload/50fceeb4-ad82-411e-9ae5-77311e364265.jpg');

-- ----------------------------
-- Table structure for `t_project`
-- ----------------------------
DROP TABLE IF EXISTS `t_project`;
CREATE TABLE `t_project` (
  `projectId` int(11) NOT NULL auto_increment COMMENT '项目id',
  `projectTyoeObj` int(11) NOT NULL COMMENT '项目类别',
  `serialnumber` varchar(30) NOT NULL COMMENT '项目编号',
  `name` varchar(100) NOT NULL COMMENT '项目名称',
  `content` varchar(8000) NOT NULL COMMENT '项目内容',
  `beginTime` varchar(20) default NULL COMMENT '开始时间',
  `endTime` varchar(20) default NULL COMMENT '结束时间',
  `money` float NOT NULL COMMENT '合同金额',
  `createuser` varchar(20) NOT NULL COMMENT '项目创建人',
  PRIMARY KEY  (`projectId`),
  KEY `projectTyoeObj` (`projectTyoeObj`),
  CONSTRAINT `t_project_ibfk_1` FOREIGN KEY (`projectTyoeObj`) REFERENCES `t_projecttype` (`typeId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_project
-- ----------------------------
INSERT INTO `t_project` VALUES ('1', '1', 'BH001', '项目名称1', '<p>项目内容1</p>', '2018-04-03 18:28:56', '2018-04-14 18:29:00', '5000', 'TH001');
INSERT INTO `t_project` VALUES ('2', '2', 'BG002', '项目名称2', '<p>项目内容2</p>', '2018-04-17 19:15:10', '2018-08-16 19:15:14', '4800', 'TH001');

-- ----------------------------
-- Table structure for `t_projectdata`
-- ----------------------------
DROP TABLE IF EXISTS `t_projectdata`;
CREATE TABLE `t_projectdata` (
  `dataId` int(11) NOT NULL auto_increment COMMENT '资料id',
  `projectObj` int(11) NOT NULL COMMENT '项目',
  `dataname` varchar(40) NOT NULL COMMENT '资料名称',
  `datacontent` varchar(8000) NOT NULL COMMENT '资料介绍',
  `dataurl` varchar(60) NOT NULL COMMENT '文件路径',
  `sortNumber` int(11) NOT NULL COMMENT '排序号',
  `remark` varchar(800) default NULL COMMENT '备注',
  `createuser` varchar(20) NOT NULL COMMENT '创建人',
  PRIMARY KEY  (`dataId`),
  KEY `projectObj` (`projectObj`),
  CONSTRAINT `t_projectdata_ibfk_1` FOREIGN KEY (`projectObj`) REFERENCES `t_project` (`projectId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_projectdata
-- ----------------------------
INSERT INTO `t_projectdata` VALUES ('1', '1', '资料1', '<p>资料介绍1</p>', 'upload/c17857a8-1cf8-4813-848c-9b3cef81ffc0.doc', '1', '资料备注1', '李明');
INSERT INTO `t_projectdata` VALUES ('2', '2', '资料2', '<p>资料介绍2</p>', 'upload/d77f5d01-9fbb-4767-98c3-418be76a190e.doc', '1', 'test2', '王帅');

-- ----------------------------
-- Table structure for `t_projecttype`
-- ----------------------------
DROP TABLE IF EXISTS `t_projecttype`;
CREATE TABLE `t_projecttype` (
  `typeId` int(11) NOT NULL auto_increment COMMENT '项目类别id',
  `typeName` varchar(20) NOT NULL COMMENT '项目类别名称',
  `remark` varchar(800) default NULL COMMENT '备注',
  PRIMARY KEY  (`typeId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_projecttype
-- ----------------------------
INSERT INTO `t_projecttype` VALUES ('1', '项目类型1', '项目类型1');
INSERT INTO `t_projecttype` VALUES ('2', '项目类型2', '类型2，类型2');

-- ----------------------------
-- Table structure for `t_teacher`
-- ----------------------------
DROP TABLE IF EXISTS `t_teacher`;
CREATE TABLE `t_teacher` (
  `teacherNo` varchar(30) NOT NULL COMMENT 'teacherNo',
  `password` varchar(30) NOT NULL COMMENT '登录密码',
  `name` varchar(20) NOT NULL COMMENT '姓名',
  `gender` varchar(4) NOT NULL COMMENT '性别',
  `birthDate` varchar(20) default NULL COMMENT '出生日期',
  `teacherrPhoto` varchar(60) NOT NULL COMMENT '教师照片',
  `telephone` varchar(20) NOT NULL COMMENT '联系电话',
  `address` varchar(80) default NULL COMMENT '家庭地址',
  `teacherDesc` varchar(8000) NOT NULL COMMENT '教师简介',
  PRIMARY KEY  (`teacherNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_teacher
-- ----------------------------
INSERT INTO `t_teacher` VALUES ('TH001', '123', '王明刚', '男', '2018-04-02', 'upload/e8aa91eb-e21d-4df9-a6fb-bb6c44c4e3e2.jpg', '13808083085', '四川攀枝花', '<p>好老师</p>');
INSERT INTO `t_teacher` VALUES ('TH002', '123', '李明涛', '男', '2018-04-10', 'upload/f241d5e1-be2c-40c2-a584-78cf4721a3d5.jpg', '13098949343', '福建福州市阳光路10号', '<p>认真负责的老师</p>');

-- ----------------------------
-- Table structure for `t_userinfo`
-- ----------------------------
DROP TABLE IF EXISTS `t_userinfo`;
CREATE TABLE `t_userinfo` (
  `user_name` varchar(30) NOT NULL COMMENT 'user_name',
  `password` varchar(30) NOT NULL COMMENT '登录密码',
  `name` varchar(20) NOT NULL COMMENT '姓名',
  `gender` varchar(4) NOT NULL COMMENT '性别',
  `birthDate` varchar(20) default NULL COMMENT '出生日期',
  `userPhoto` varchar(60) NOT NULL COMMENT '用户照片',
  `telephone` varchar(20) NOT NULL COMMENT '联系电话',
  `email` varchar(50) NOT NULL COMMENT '邮箱',
  `address` varchar(80) default NULL COMMENT '家庭地址',
  `remark` varchar(800) default NULL COMMENT '备注',
  `regTime` varchar(20) default NULL COMMENT '注册时间',
  PRIMARY KEY  (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_userinfo
-- ----------------------------
INSERT INTO `t_userinfo` VALUES ('STU001', '123', '用户1', '男', '2018-04-12', 'upload/b62d9cbe-4f50-4901-8d0d-db5530548ec5.jpg', '13589834234', 'test1@163.com', '四川达州大足县祥光镇', '测试', '2018-04-14 18:28:27');
INSERT INTO `t_userinfo` VALUES ('STU002', '123', '用户2', '男', '2018-04-10', 'upload/edf2afc0-fc79-486c-a0aa-dba8c52c0c51.jpg', '13589834234', 'tesst@163.com', '四川南充滨江路', '测试', '2018-04-24 19:10:11');
