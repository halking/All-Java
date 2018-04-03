ALTER TABLE `member_reply`
	MODIFY COLUMN `reply_id` int(11) NULL COMMENT '自动回复ID' AFTER `member_id`;