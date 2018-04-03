#ALTER TABLE `wechat` CHANGE COLUMN `is_default` `priority`  int(11) NULL DEFAULT NULL COMMENT '优先级' AFTER `company_id`;

#ALTER TABLE `user_wechat` ADD COLUMN `is_use`  tinyint(2) NOT NULL DEFAULT 0 COMMENT '是否在使用（0-未使用，1-正使用）' AFTER `created_at`;

ALTER TABLE `coupon_setting` ADD COLUMN `pordInfos` varchar(255) COMMENT '赠品信息';