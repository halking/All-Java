CREATE TABLE `menu_extra_attr` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `menu_id` bigint(20) NOT NULL DEFAULT '0',
  `app_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `page_path` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `app_url` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`,`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--会话中的url以及event_key字段长度增加到500
ALTER TABLE `conversation`
	MODIFY COLUMN `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '消息链接' AFTER `description`,
	MODIFY COLUMN `event_key` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '事件KEY值' AFTER `url`;


