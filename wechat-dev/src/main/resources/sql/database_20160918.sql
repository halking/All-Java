ALTER TABLE `role`
MODIFY COLUMN `creator_id`  int(11) NULL COMMENT '创建用户ID' AFTER `created_at`;

ALTER TABLE `role_function`
MODIFY COLUMN `creator_id`  int(11) NULL COMMENT '创建用户ID' AFTER `created_at`;

ALTER TABLE `wechat`
MODIFY COLUMN `user_id`  int(11) NULL COMMENT '用户ID' AFTER `remark`;

ALTER TABLE `material`
MODIFY COLUMN `material_type`  tinyint(3) NOT NULL COMMENT '素材类型(1:图文,2:图片,3:语音,4:视频,5:小视频,6:文字,7:API)' AFTER `id`;
