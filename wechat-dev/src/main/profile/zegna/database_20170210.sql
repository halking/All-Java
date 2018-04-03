CREATE TABLE zegna_member_profile
(
    id INT(20) PRIMARY KEY NOT NULL COMMENT '主键ID' AUTO_INCREMENT,
    firstname VARCHAR(50) COMMENT '会员名',
    lastname VARCHAR(50) COMMENT '会员姓',
    mobile VARCHAR(50) COMMENT '手机号',
    email VARCHAR(255) COMMENT '邮箱',

    member_id INT(11) NOT NULL COMMENT '会员ID',
    wechat_id INT(20) NOT NULL COMMENT '所属公众号ID',
    status TINYINT(2) COMMENT '绑定状态(0:已解绑,1:已绑定)',
    bind_at DATETIME COMMENT '绑定时间',
    unbind_at DATETIME COMMENT '解绑时间'
);
CREATE INDEX zmp_member_id ON zegna_member_profile (member_id);
CREATE INDEX zmp_wechat_id ON zegna_member_profile (wechat_id);

