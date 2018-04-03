CREATE TABLE `business_result` (
  `id` int(11) NOT NULL COMMENT '主键ID',
  `poi_id` varchar(30) COLLATE utf8_unicode_ci NOT NULL COMMENT '微信的门店ID',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `uniq_id` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '商户内部ID',
  `status` tinyint(2) NOT NULL COMMENT '微信门店发送状态（0-失败，1-成功）',
  `msg` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '推送信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='门店发布审核结果';

ALTER TABLE `business_photo`
ADD COLUMN `wx_url`  varchar(255) NULL DEFAULT NULL COMMENT '微信上传路径' AFTER `photo_url`;
