ALTER TABLE `conversation` ADD COLUMN `voice_url` varchar(255) DEFAULT NULL COMMENT '语音url';
ALTER TABLE `conversation` ADD COLUMN `video_url` varchar(255) DEFAULT NULL COMMENT '视频url';
ALTER TABLE `conversation` ADD COLUMN `short_video_url` varchar(255) DEFAULT NULL COMMENT '小视频url';