ALTER TABLE `business`
DROP COLUMN `sid`;

ALTER TABLE `business`
ADD COLUMN `is_push`  tinyint(2) NOT NULL DEFAULT 0 COMMENT '是否发布（0-未发布，1-已发布）' AFTER `bus`;