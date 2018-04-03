ALTER TABLE `original_conversation`
ADD COLUMN `type`  varchar(50) NULL AFTER `execute_millis`;

DROP TABLE IF EXISTS `member_conversation_session`;
CREATE TABLE `member_conversation_session` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `member_open_id` varchar(50) NOT NULL COMMENT '会员openId',
  `account_id` varchar(50) DEFAULT NULL COMMENT 'communication account id',
  `type` varchar(20) DEFAULT NULL COMMENT 'session 类型',
  `start_at` datetime DEFAULT NULL COMMENT 'session 开始时间',
  `end_at` datetime DEFAULT NULL COMMENT 'session 结束时间',
  `status` int(11) DEFAULT NULL COMMENT 'session status',
  `is_closed` bit(1) DEFAULT NULL,
  `wechat_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `member_open_id` (`member_open_id`),
  KEY `wechat_id` (`wechat_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

INSERT INTO `config` (`cfg_group` , `cfg_group_label` ,`cfg_key_label`, `cfg_key`, `cfg_value` ,`wechat_id` ) VALUES ('ARVATO', 'ARVATO接入', null, 'TOKEN', 'ArvatoWxcc', '1');
INSERT INTO `config` (`cfg_group` , `cfg_group_label` ,`cfg_key_label`, `cfg_key`, `cfg_value` ,`wechat_id` ) VALUES ('ARVATO', 'ARVATO接入', null, 'SECRECT', 'dzdwQVcwT3N4RSpqUHgqTA==', '1');
INSERT INTO `config` (`cfg_group` , `cfg_group_label` ,`cfg_key_label`, `cfg_key`, `cfg_value` ,`wechat_id` ) VALUES ('ARVATO', 'ARVATO接入', null, 'MESSAGE_URL', 'https://livechat.zegna.cn:448/livechat/interface/', '1');
INSERT INTO `config`  (`cfg_group` , `cfg_group_label` ,`cfg_key_label`, `cfg_key`, `cfg_value` ,`wechat_id` ) VALUES ('ARVATO', 'ARVATO接入', null, 'MESSAGE_PERIOD_MINS', '10', '1');
INSERT INTO `config` (`cfg_group` , `cfg_group_label` ,`cfg_key_label`, `cfg_key`, `cfg_value` ,`wechat_id` ) VALUES ('ARVATO', 'ARVATO接入', null, 'TRIGGER_MENU_NAME', '联系客服', '1');
INSERT INTO `config` (`cfg_group` , `cfg_group_label` ,`cfg_key_label`, `cfg_key`, `cfg_value` ,`wechat_id` ) VALUES ('ARVATO', 'ARVATO接入', null, 'OLD_MESSAGE_URL', 'https://livechat.zegna.cn:448/livechat/oldMsg/', '1');