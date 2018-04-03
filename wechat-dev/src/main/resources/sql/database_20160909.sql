#ALTER TABLE `qrcode` ADD COLUMN `sopport_subscribe_reply` tinyint(2) DEFAULT NULL COMMENT '是否支持关注回复(1:支持,0:不支持)';

ALTER TABLE `business_category`
DROP COLUMN `status`,
DROP COLUMN `created_at`;

ALTER TABLE `business_category` DROP FOREIGN KEY `business_category_ibfk_1`;
ALTER TABLE `business_category` ADD CONSTRAINT `business_category_ibfk_1` FOREIGN KEY (`parent_id`) REFERENCES `business_category` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

#ALTER TABLE `member` ADD COLUMN `source` varchar(255) DEFAULT NULL COMMENT '来源';
#ALTER TABLE `member` ADD COLUMN `keyword` varchar(255) DEFAULT NULL COMMENT '关键词';