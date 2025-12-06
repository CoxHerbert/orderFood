-- 点餐管理系统基础表

DROP TABLE IF EXISTS `store`;
CREATE TABLE `store` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(128) NOT NULL COMMENT '门店名称',
  `code` varchar(64) NOT NULL COMMENT '门店编码',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `enabled` tinyint NOT NULL DEFAULT 1 COMMENT '是否启用（0/1）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_store_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='门店';
