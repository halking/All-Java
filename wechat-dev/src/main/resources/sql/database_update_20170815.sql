ALTER TABLE `mass_conversation`
  ADD COLUMN `pi_ci`  int NULL COMMENT '群发分组的第几批次' AFTER `wechat_id`;

ALTER TABLE `mass_conversation_result`
  ADD COLUMN `total_batch`  int NULL COMMENT '群发总批次' AFTER `event`;

CREATE TABLE `mass_conversation_batch_result` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `msg_id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '群发消息ID',
  `pi_ci` int(11) NOT NULL COMMENT '群发分组的第几批次',
  `status` tinyint(2) NOT NULL COMMENT '结果',
  `msg_type` tinyint DEFAULT NULL COMMENT '群发消息类型',
  `msg_content` text DEFAULT NULL COMMENT '群发消息内容',
  `total_count` int(11) DEFAULT NULL COMMENT 'tag_id下粉丝数；或者openid_list中的粉丝数',
  `filter_count` int(11) DEFAULT NULL COMMENT '过滤后准备发送的粉丝数',
  `send_count` int(11) DEFAULT NULL COMMENT '发送成功的粉丝数',
  `error_count` int(11) DEFAULT NULL COMMENT '发送失败的粉丝数',
  `conversation_id` int(11) NOT NULL COMMENT '群发会话ID',
  `wechat_id` int(11) NOT NULL COMMENT '公众号ID',
  PRIMARY KEY (`id`),
  KEY `conversation_id` (`conversation_id`),
  KEY `wechat_id` (`wechat_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='分批群发消息结果';

CREATE TABLE `mass_conversation_batch_member` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `open_id` varchar(50) COLLATE utf8_unicode_ci COMMENT 'openId',
  `member_id` int(11) NOT NULL COMMENT '粉丝ID',
  `batch_id` int(11) NOT NULL COMMENT '批次ID',
  `wechat_id` int(11) NOT NULL COMMENT '公众号ID',
  PRIMARY KEY (`id`),
  KEY `member_id` (`member_id`),
  KEY `batch_id` (`batch_id`),
  KEY `wechat_id` (`wechat_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='分批群发用户表';