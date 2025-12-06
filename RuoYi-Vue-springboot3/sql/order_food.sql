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

DROP TABLE IF EXISTS `store_table`;
CREATE TABLE `store_table` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `store_id` bigint NOT NULL COMMENT '门店ID',
  `table_code` varchar(64) NOT NULL COMMENT '桌码',
  `table_name` varchar(128) DEFAULT NULL COMMENT '桌名',
  `qrcode_content` varchar(255) DEFAULT NULL COMMENT '二维码内容',
  `enabled` tinyint NOT NULL DEFAULT 1 COMMENT '是否启用（0/1）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='桌台';

DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `store_id` bigint NOT NULL COMMENT '门店ID',
  `name` varchar(128) NOT NULL COMMENT '分类名称',
  `sort_order` int DEFAULT 0 COMMENT '排序',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜品分类';

DROP TABLE IF EXISTS `product_spu`;
CREATE TABLE `product_spu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `store_id` bigint NOT NULL COMMENT '门店ID',
  `category_id` bigint NOT NULL COMMENT '分类ID',
  `name` varchar(128) NOT NULL COMMENT '菜品名称',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `image_url` varchar(255) DEFAULT NULL COMMENT '主图',
  `on_sale` tinyint NOT NULL DEFAULT 1 COMMENT '是否上架（0/1）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜品SPU';

DROP TABLE IF EXISTS `product_sku`;
CREATE TABLE `product_sku` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `spu_id` bigint NOT NULL COMMENT 'SPU ID',
  `sku_name` varchar(128) NOT NULL COMMENT '规格名',
  `price` decimal(10,2) NOT NULL COMMENT '单价',
  `available` tinyint NOT NULL DEFAULT 1 COMMENT '是否可售（0/1）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜品SKU';

DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `store_id` bigint NOT NULL COMMENT '门店ID',
  `table_id` bigint DEFAULT NULL COMMENT '桌台ID',
  `user_id` varchar(128) DEFAULT NULL COMMENT '用户标识',
  `total_amount` decimal(10,2) NOT NULL COMMENT '订单总金额',
  `pay_amount` decimal(10,2) NOT NULL COMMENT '实付金额',
  `status` varchar(32) NOT NULL COMMENT '订单状态',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单';

DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `spu_id` bigint NOT NULL COMMENT 'SPU ID',
  `sku_id` bigint NOT NULL COMMENT 'SKU ID',
  `product_name` varchar(128) NOT NULL COMMENT '菜品名称',
  `sku_name` varchar(128) NOT NULL COMMENT '规格名称',
  `quantity` int NOT NULL COMMENT '数量',
  `unit_price` decimal(10,2) NOT NULL COMMENT '单价',
  `total_price` decimal(10,2) NOT NULL COMMENT '小计',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细';
