DROP TABLE IF EXISTS report_config;
CREATE TABLE report_config
(
    id INT(11) PRIMARY KEY NOT NULL COMMENT '主键ID' AUTO_INCREMENT,
    report_key VARCHAR(50) NOT NULL COMMENT '报表索引键',
    filename VARCHAR(100) COMMENT '导出的文件名',
    sheetname VARCHAR(50) COMMENT '工作表名',
    report_sql VARCHAR(2000) NOT NULL COMMENT '报表SQL',
    wechat_id INT(11) NOT NULL COMMENT '公众号ID'
 ) COMMENT 'report_key/filename可以冗余, sheetname和report_sql需要一一对应';
CREATE INDEX report_key_index ON report_config (report_key);
