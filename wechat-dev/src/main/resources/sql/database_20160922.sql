ALTER TABLE `member_subscribe`
MODIFY COLUMN `subscribe`  tinyint(4) NOT NULL COMMENT '是否关注(0:已关注,1:未关注)' AFTER `member_id`;