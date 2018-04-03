ALTER TABLE `menu_rule`
ADD COLUMN `tag_id`  int(11) NULL DEFAULT NULL COMMENT '微信标签id' AFTER `language`;