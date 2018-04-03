DROP TABLE IF EXISTS d1m_wechat.customer_info;
CREATE TABLE d1m_wechat.customer_info
(
    id INT(11) PRIMARY KEY NOT NULL COMMENT '主键ID' AUTO_INCREMENT,
    appid VARCHAR(40) COMMENT '微信appid',
    appkey VARCHAR(64) COMMENT 'D1M内部的appkey',
    wechat_id INTEGER COMMENT '微信id',
    created_at TIMESTAMP DEFAULT current_timestamp NOT NULL COMMENT '客户的创建时间',
    status TINYINT(2) COMMENT '客户状态(0:无效,1:有效)'
);

DROP TABLE IF EXISTS d1m_wechat.customer_token;
CREATE TABLE d1m_wechat.customer_token
(
    id INT(11) PRIMARY KEY NOT NULL COMMENT '主键ID' AUTO_INCREMENT,
    customer_info_id INTEGER NOT NULL COMMENT '客户资料ID',
    appid VARCHAR(40) COMMENT '微信appid',
    appkey VARCHAR(64) COMMENT 'D1M内部的appkey',
    token VARCHAR(200) COMMENT 'D1M内部的访问token',
    wechat_id INTEGER COMMENT '微信id',
    created_at TIMESTAMP DEFAULT current_timestamp NOT NULL COMMENT 'token的创建时间',
    expired_at DATETIME NOT NULL COMMENT 'app_token的过期时间',
    status TINYINT(2) COMMENT '令牌状态(0:无效,1:有效,2过期)',
    CONSTRAINT `customer_info_ibfk_id` FOREIGN KEY (`customer_info_id`) REFERENCES d1m_wechat.`customer_info` (`id`),
    CONSTRAINT `wechat._ibfk_id` FOREIGN KEY (`wechat_id`) REFERENCES d1m_wechat.`wechat` (`id`)
);