alter table member_tag_type add parent_id int default 1;

INSERT INTO `d1m_wechat`.`function` (`id`, `parent_id`, `code`, `name_cn`, `name_en`, `created_at`, `creator_id`, `role_id`) 
VALUES ('63', '56', 'system-setting:tag-category-management', '标签管理', 'tag-category-management', '2016-12-26 17:01:04', '1', NULL);

INSERT INTO `d1m_wechat`.`role_function` (`id`, `role_id`, `function_id`, `created_at`, `creator_id`) 
VALUES ('34', '1', '63', '2016-12-26 17:03:35', '1');
