-- 商品分类表
CREATE TABLE IF NOT EXISTS `category` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    `parent_id` bigint DEFAULT '0' COMMENT '父级ID',
    `name` varchar(50) NOT NULL COMMENT '分类名称',
    `icon` varchar(100) DEFAULT NULL COMMENT '分类图标',
    `sort` int DEFAULT '0' COMMENT '排序',
    `status` tinyint DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`)
);

-- 商品表
CREATE TABLE IF NOT EXISTS `goods` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品ID',
    `shop_id` bigint NOT NULL COMMENT '店铺ID',
    `category_id` bigint NOT NULL COMMENT '分类ID',
    `name` varchar(100) NOT NULL COMMENT '商品名称',
    `image` varchar(255) DEFAULT NULL COMMENT '商品图片',
    `description` text COMMENT '商品描述',
    `original_price` decimal(10,2) NOT NULL COMMENT '原价',
    `price` decimal(10,2) NOT NULL COMMENT '现价',
    `packing_fee` decimal(10,2) DEFAULT '0.00' COMMENT '包装费',
    `sales` int DEFAULT '0' COMMENT '销量',
    `stock` int NOT NULL DEFAULT '0' COMMENT '库存',
    `specs` text COMMENT '规格选项',
    `additions` text COMMENT '加料选项',
    `status` tinyint DEFAULT '0' COMMENT '状态：0-下架，1-上架',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_shop_id` (`shop_id`)
);

-- 订单表
CREATE TABLE IF NOT EXISTS `order` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `shop_id` bigint NOT NULL COMMENT '店铺ID',
    `order_no` varchar(32) NOT NULL COMMENT '订单编号',
    `status` varchar(20) NOT NULL COMMENT '订单状态',
    `total_price` decimal(10,2) NOT NULL COMMENT '商品总价',
    `packing_fee` decimal(10,2) DEFAULT '0.00' COMMENT '包装费',
    `delivery_fee` decimal(10,2) DEFAULT '0.00' COMMENT '配送费',
    `actual_amount` decimal(10,2) NOT NULL COMMENT '实付金额',
    `payment_method` varchar(20) DEFAULT NULL COMMENT '支付方式',
    `payment_time` datetime DEFAULT NULL COMMENT '支付时间',
    `address_id` bigint NOT NULL COMMENT '配送地址ID',
    `delivery_time` datetime DEFAULT NULL COMMENT '配送时间',
    `remark` varchar(255) DEFAULT NULL COMMENT '备注',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_shop_id` (`shop_id`)
);

-- 订单统计表
CREATE TABLE IF NOT EXISTS `order_statistics` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '统计ID',
    `date` date NOT NULL COMMENT '统计日期',
    `total_orders` int NOT NULL DEFAULT '0' COMMENT '订单总数',
    `total_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '订单总金额',
    `completed_orders` int NOT NULL DEFAULT '0' COMMENT '已完成订单数',
    `cancelled_orders` int NOT NULL DEFAULT '0' COMMENT '已取消订单数',
    `refunded_orders` int NOT NULL DEFAULT '0' COMMENT '已退款订单数',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_date` (`date`)
);

-- 订单退款表
CREATE TABLE IF NOT EXISTS `order_refund` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '退款ID',
    `order_id` bigint NOT NULL COMMENT '订单ID',
    `amount` decimal(10,2) NOT NULL COMMENT '退款金额',
    `reason` varchar(255) NOT NULL COMMENT '退款原因',
    `status` varchar(20) NOT NULL COMMENT '退款状态',
    `refund_time` datetime DEFAULT NULL COMMENT '退款时间',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`)
);

-- 商品评价表
CREATE TABLE IF NOT EXISTS `goods_review` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '评价ID',
    `goods_id` bigint NOT NULL COMMENT '商品ID',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `order_id` bigint NOT NULL COMMENT '订单ID',
    `rating` int NOT NULL COMMENT '评分：1-5星',
    `content` text COMMENT '评价内容',
    `images` text COMMENT '图片地址，多个用逗号分隔',
    `is_anonymous` tinyint DEFAULT '0' COMMENT '是否匿名：0-否，1-是',
    `reply_content` text COMMENT '商家回复内容',
    `reply_time` datetime DEFAULT NULL COMMENT '商家回复时间',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_goods_id` (`goods_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_order_id` (`order_id`)
); 