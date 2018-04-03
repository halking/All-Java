ALTER TABLE config modify column `cfg_value` varchar(500);

START TRANSACTION;

-- CAMPAIGN_STATUS: FALSE 有货 | TRUE 缺货
INSERT INTO `config` (`cfg_group`, `cfg_group_label`, `cfg_key_label`, `cfg_key`, `cfg_value`, `wechat_id`) VALUES
 ('ZEGNA_CAMPAIGN_RED', 'zegna_campaign_red', 'ROUTINE, 注册页tip', 'REGISTER_ROUTINE', '注册绑定', '1'),
 ('ZEGNA_CAMPAIGN_RED', 'zegna_campaign_red', '红包足，注册页tip', 'REGISTER_CNY_NORMAL', '领取新年红包袋', '1'),
 ('ZEGNA_CAMPAIGN_RED', 'zegna_campaign_red', '红包缺货，注册页tip', 'REGISTER_CNY_LACK', '欢迎完善信息<br>优先尊享品牌活动', '1'),
 ('ZEGNA_CAMPAIGN_RED', 'zegna_campaign_red', 'ROUTINE,编辑页tip', 'PROFILE_BIND_ROUTINE', '诚邀阁下完善信息<br><br>优先尊享品牌活动', '1'),
 ('ZEGNA_CAMPAIGN_RED', 'zegna_campaign_red', '红包足,编辑页tip', 'PROFILE_BIND_NORMAL', '诚邀阁下完善信息后<br><br>即日起至2月15日<br>光临杰尼亚精品店<br>领取新年红包袋', '1'),
 ('ZEGNA_CAMPAIGN_RED', 'zegna_campaign_red', '红包缺货,编辑页tip', 'PROFILE_BIND_LACK', '诚邀阁下完善信息<br><br>优先尊享品牌活动', '1'),
 ('ZEGNA_CAMPAIGN_RED', 'zegna_campaign_red', 'ROUTINE,信息页tip', 'PROFILE_INFO_ROUTINE', '阁下的信息', '1'),
 ('ZEGNA_CAMPAIGN_RED', 'zegna_campaign_red', '红包足,信息页tip', 'PROFILE_INFO_NORMAL', '感谢参与，阁下的信息已确认。<br>或者点击“返回编辑”继续修改。', '1'),
 ('ZEGNA_CAMPAIGN_RED', 'zegna_campaign_red', '红包缺货,信息页tip', 'PROFILE_INFO_LACK', '阁下的信息', '1'),
 ('ZEGNA_CAMPAIGN_RED', 'zegna_campaign_red', 'CONFIRM按钮', 'CONFIRM_BUTTON', '确认', '1'),
 ('ZEGNA_CAMPAIGN_RED', 'zegna_campaign_red', 'CLOSE按钮', 'CLOSE_BUTTON', '确认', '1'),
 ('ZEGNA_CAMPAIGN_RED', 'zegna_campaign_red', 'ROUTINE,信息页按钮', 'PROFILE_RETURN_ROUTINE_BTN', '返回编辑', '1'),
 ('ZEGNA_CAMPAIGN_RED', 'zegna_campaign_red', '红包足,信息页按钮', 'PROFILE_RETURN_NORMAL_BTN', '返回编辑', '1'),
 ('ZEGNA_CAMPAIGN_RED', 'zegna_campaign_red', '红包缺货,信息页按钮', 'PROFILE_RETURN_LACK_BTN', '返回编辑', '1'),
 ('ZEGNA_CAMPAIGN_RED', 'zegna_campaign_red', '红包足,信息页按钮', 'PROFILE_NEXT_LACK_BTN', '查看红包袋', '1'),
 ('ZEGNA_CAMPAIGN_RED', 'zegna_campaign_red', '红包页按钮', 'REDEEMED_BUTTON', '点击兑礼', '1'),
 ('ZEGNA_CAMPAIGN_RED', 'zegna_campaign_red', '有红包user信息不全tip', 'PROFILE_NO_USER_TIP', '感谢参与，阁下的信息已确认。<br>或者点击“返回编辑”继续修改。', '1'),
 ('ZEGNA_CAMPAIGN_RED', 'zegna_campaign_red', '有红包user信息不全close', 'PROFILE_NO_USER_CLOSE_BTN', '关闭', '1'),
 ('ZEGNA_CAMPAIGN_RED', 'zegna_campaign_red', '有红包user信息不全edit', 'PROFILE_NO_USER_EDIT_BTN', '返回编辑', '1'),
 ('ZEGNA_CAMPAIGN_RED', 'zegna_campaign_red', '兑换页按钮', 'REDEEMED_CONFIRM_BUTTON', '确认兑礼', '1'),
 ('ZEGNA_CAMPAIGN_RED', 'zegna_campaign_red', 'red是否有货', 'CAMPAIGN_STATUS', 'FALSE', '1'),
 ('ZEGNA_CAMPAIGN_RED', 'zegna_campaign_red', 'red活动过期时间', 'CAMPAIGN_DATETIME', '2018-02-15', '1'),
 ('ZEGNA_CAMPAIGN_RED', 'zegna_campaign_red', 'page文案展示', 'REDEEMED_NORMAL', '感谢阁下参与。<br>即日起至2月15日光临杰尼亚精品店<br>领取新年红包袋<br><br>新年红包袋限量兑礼，先到先得，赠完为止<br><br>关闭退出后，可点击杰尼亚官方微信菜单中的<br>“个人首页”查看您的新年红包袋信息。', '1'),
 ('ZEGNA_CAMPAIGN_RED', 'zegna_campaign_red', '店铺编码', 'BOUTIQUE_CODE', 'AJ6,903,901,A9U,AA6,A1Q,ABJ,515,A55,396,A4P,442,952,975,833,A7V,957,83A,443,879,851,820,81A,428,A1D,826,951,821,ABG,699,947,AA7,A4R,701,514,P1V,516,82A,884,508,434,A1K,976,836,482,440,A7D,P1T,A4Z,955,A1C,A8D,A62,905,890,AD3,904,A9Y,893,992,980,872,989,AGO,ABF,739,AEP,791,852', '1'),
 ('ZEGNA_CAMPAIGN_NOTIFICATION', 'zegna_campaign_notification','领取红包消息通知','RED_NOTIFICATION_MESSAGE','恭喜您！新年红包袋领取成功。您可点击杰尼亚官方微信右下角菜单“尊享服务”中的“个人首页”或“领取红包袋”查看您的新年红包袋信息，也可<a href="http://zegna.wechat.d.d1m.cn/backend/oauth/u/cny?redirectUrl=bonus">直接查看</a>。欢迎您于2月15日之前光临杰尼亚店铺领取。限量兑礼，先到先得，赠完为止。', '1');

COMMIT;


START TRANSACTION;

ALTER TABLE member_profile
 ADD interests varchar(100) DEFAULT NULL  COMMENT '兴趣',
 ADD styles varchar(100) DEFAULT NULL  COMMENT '款型',
 ADD is_vip BIT(1) DEFAULT NULL  COMMENT '是否是VIP',
 ADD occupation varchar(50) DEFAULT NULL  COMMENT '职业',
 ADD alias varchar(50) DEFAULT NULL  COMMENT '别名',
 ADD rise varchar(50) DEFAULT NULL  COMMENT '抬头',
 ADD country varchar(50) DEFAULT NULL  COMMENT '国家',
 ADD sex tinyint(2) DEFAULT NULL  COMMENT '性别',
 ADD source varchar(20) DEFAULT NULL  COMMENT '来源',
 ADD keyword varchar(100) DEFAULT NULL  COMMENT '关键字';

COMMIT;



CREATE TABLE `zegna_campaign` (
 `id` int(20)  NOT NULL  AUTO_INCREMENT  COMMENT '自增id',
 `status` varchar(20)  DEFAULT NULL  COMMENT '活动状态',
 `create_date` datetime DEFAULT NULL  COMMENT '创建时间',
 `update_date` datetime DEFAULT NULL  COMMENT '核销时间',
 `member_profile_id` int(20)  DEFAULT '0'  COMMENT '会员个人信息ID',
 `name` varchar(50)  DEFAULT NULL  COMMENT '活动名称',
 `remark` varchar(50)  DEFAULT NULL  COMMENT '核销码',
 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='活动表';


CREATE TABLE `campaign_member_tag` (
 `id` int(20)  NOT NULL  AUTO_INCREMENT  COMMENT '自增id',
 `name` varchar(20)  DEFAULT NULL  COMMENT 'tag名称',
 `wx_tag_id` datetime DEFAULT NULL  COMMENT '微信tagId',
 `create_date` datetime DEFAULT NULL  COMMENT '创建时间',
 `member_id` int(20)  DEFAULT '0'  COMMENT '会员ID',
 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='会员标签表';


INSERT INTO `d1m_wechat`.`oauth_url` (`short_url`, `scope`, `process_class`, `params`, `wechat_id`, `status`, `created_at`) VALUES
 ('cny', 'snsapi_base', 'com.d1m.wechat.customization.zegna.oauth.impl.ZegnaBindingOauthImpl', '{"baseUrl":"http://zegna.wechat.d.d1m.cn/site/index-minisite.html#/"}', '1', '1', '2018-01-19 22:52:52');



