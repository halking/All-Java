ALTER TABLE d1m_wechat.original_conversation ADD execute_start DATETIME NULL COMMENT '会话处理开始时间';

ALTER TABLE d1m_wechat.original_conversation ADD execute_end DATETIME NULL COMMENT '会话处理结束时间';

ALTER TABLE d1m_wechat.original_conversation ADD execute_millis INT NULL COMMENT '会话处理时间(毫秒)';
