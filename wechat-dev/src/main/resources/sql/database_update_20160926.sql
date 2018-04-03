ALTER TABLE `member`
	MODIFY COLUMN `nickname` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '昵称' AFTER `open_id`;

ALTER TABLE `member` ADD UNIQUE KEY `wechat_open_id` (`open_id`,`wechat_id`) USING BTREE;