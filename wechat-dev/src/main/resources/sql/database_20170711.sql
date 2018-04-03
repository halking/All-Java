create table spi_config
(
    id int auto_increment primary key,
    wechat_id int NOT NULL ,
    event varchar(20) NOT NULL DEFAULT '',
    action varchar(100) NOT NULL DEFAULT '',
    token varchar(100) DEFAULT '',
    remote varchar(200) DEFAULT '',
    creator_id int NOT NULL,
    created_at datetime DEFAULT current_date,
    status tinyint DEFAULT 1
);
