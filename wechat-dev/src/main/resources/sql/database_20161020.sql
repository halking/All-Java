CREATE TABLE `reply_words` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `reply_id` int(11) NOT NULL COMMENT '自动回复ID',
  `reply_word` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT ' 关键词',
  `wechat_id` int(11) NOT NULL COMMENT '公众号ID',
  PRIMARY KEY (`id`),
  KEY `reply_id` (`reply_id`),
  KEY `wechat_id` (`wechat_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='关键词表';

