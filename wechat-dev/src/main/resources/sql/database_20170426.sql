-- ----------------------------
-- Table structure for activity_session
-- ----------------------------
DROP TABLE IF EXISTS `activity_session`;
CREATE TABLE `activity_session` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `start` time DEFAULT NULL COMMENT '开始时间',
  `end` time DEFAULT NULL COMMENT '结束时间',
  `name` varchar(255) DEFAULT NULL COMMENT '场次名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of activity_session
-- ----------------------------
INSERT INTO `activity_session` VALUES ('1', '11:00:00', '14:00:00', null);
INSERT INTO `activity_session` VALUES ('2', '15:00:00', '18:00:00', null);
INSERT INTO `activity_session` VALUES ('3', '19:00:00', '21:00:00', null);





-- ----------------------------
-- Table structure for coupon_activity
-- ----------------------------
DROP TABLE IF EXISTS `coupon_activity`;
CREATE TABLE `coupon_activity` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '表主键',
  `day` datetime DEFAULT NULL COMMENT '活动日期',
  `activity_session_id` int(11) DEFAULT NULL COMMENT '活动场次',
  `number` int(11) DEFAULT NULL COMMENT '票数',
  `left_number` int(11) DEFAULT NULL COMMENT '剩余票数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of coupon_activity
-- ----------------------------
INSERT INTO `coupon_activity` VALUES ('17', '2017-05-11 00:00:00', '1', '70', '0');
INSERT INTO `coupon_activity` VALUES ('18', '2017-05-11 00:00:00', '2', '70', '0');
INSERT INTO `coupon_activity` VALUES ('19', '2017-05-11 00:00:00', '3', '70', '1');
INSERT INTO `coupon_activity` VALUES ('20', '2017-05-12 00:00:00', '1', '70', '0');
INSERT INTO `coupon_activity` VALUES ('21', '2017-05-12 00:00:00', '2', '70', '2');
INSERT INTO `coupon_activity` VALUES ('22', '2017-05-12 00:00:00', '3', '70', '0');
INSERT INTO `coupon_activity` VALUES ('23', '2017-05-13 00:00:00', '1', '70', '66');
INSERT INTO `coupon_activity` VALUES ('24', '2017-05-13 00:00:00', '2', '70', '70');
INSERT INTO `coupon_activity` VALUES ('25', '2017-05-13 00:00:00', '3', '70', '70');
INSERT INTO `coupon_activity` VALUES ('26', '2017-05-14 00:00:00', '1', '70', '70');
INSERT INTO `coupon_activity` VALUES ('27', '2017-05-14 00:00:00', '2', '70', '70');
INSERT INTO `coupon_activity` VALUES ('28', '2017-05-14 00:00:00', '3', '70', '68');
INSERT INTO `coupon_activity` VALUES ('29', '2017-05-15 00:00:00', '1', '70', '66');
INSERT INTO `coupon_activity` VALUES ('30', '2017-05-15 00:00:00', '2', '70', '70');
INSERT INTO `coupon_activity` VALUES ('31', '2017-05-15 00:00:00', '3', '70', '68');





-- ----------------------------
-- Table structure for coupon_activity_member
-- ----------------------------
DROP TABLE IF EXISTS `coupon_activity_member`;
CREATE TABLE `coupon_activity_member` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `member_id` int(11) DEFAULT NULL COMMENT '用户ID',
  `day` datetime DEFAULT NULL COMMENT '活动日期',
  `activity_session_id` int(11) DEFAULT NULL COMMENT '活动场次',
  `number` int(11) DEFAULT NULL COMMENT '领取数量',
  `business_id` int(11) DEFAULT NULL COMMENT '门店ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8mb4;