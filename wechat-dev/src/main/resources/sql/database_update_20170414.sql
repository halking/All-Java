ALTER TABLE `business`
MODIFY COLUMN `telephone`  varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '电话' AFTER `address`;

ALTER TABLE `business`
MODIFY COLUMN `business_name`  varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '门店名称' AFTER `business_code`,
MODIFY COLUMN `branch_name`  varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '分店名称' AFTER `business_name`;