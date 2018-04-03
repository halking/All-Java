ALTER TABLE `member_profile`
ADD COLUMN `xing`  varchar(10) CHARACTER SET utf8mb4 NULL COMMENT '姓' AFTER `unbund_at`,
ADD COLUMN `ming`  varchar(20) CHARACTER SET utf8mb4 NULL COMMENT '名' AFTER `xing`,
ADD COLUMN `sourceId`  int NULL COMMENT '用户来源ID' AFTER `ming`;




-- ----------------------------
-- Table structure for book_on_appointment
-- ----------------------------
DROP TABLE IF EXISTS `book_on_appointment`;
CREATE TABLE `book_on_appointment` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `member_id` int(11) DEFAULT NULL COMMENT '用户ID',
  `book_date` datetime DEFAULT NULL COMMENT '预订日期',
  `store_id` int(11) DEFAULT NULL COMMENT '店铺ID',
  `book_time` int(11) DEFAULT NULL COMMENT '1: 上午    2：下午',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;



CREATE TABLE `zegna_member_baa_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '表主键ID',
  `member_id` int(11) DEFAULT NULL COMMENT '用户ID',
  `goods_id` int(11) DEFAULT NULL COMMENT '商品ID',
  `date` datetime DEFAULT NULL COMMENT '记录时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4;