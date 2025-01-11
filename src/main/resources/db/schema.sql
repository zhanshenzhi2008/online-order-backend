-- 创建数据库
CREATE DATABASE IF NOT EXISTS online_order DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE online_order;

-- 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id` varchar(32) NOT NULL COMMENT '用户ID',
    `open_id` varchar(64) DEFAULT NULL COMMENT '微信openId',
    `nickname` varchar(64) DEFAULT NULL COMMENT '昵称',
    `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
    `phone` varchar(11) DEFAULT NULL COMMENT '手机号',
    `gender` varchar(10) DEFAULT NULL COMMENT '性别',
    `status` varchar(10) NOT NULL DEFAULT 'normal' COMMENT '状态',
    `roles` json DEFAULT NULL COMMENT '角色列表',
    `permissions` json DEFAULT NULL COMMENT '权限列表',
    `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 地址表
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address` (
    `id` varchar(32) NOT NULL COMMENT '地址ID',
    `user_id` varchar(32) NOT NULL COMMENT '用户ID',
    `name` varchar(64) NOT NULL COMMENT '收货人姓名',
    `phone` varchar(11) NOT NULL COMMENT '收货人手机号',
    `province` varchar(64) NOT NULL COMMENT '省份',
    `city` varchar(64) NOT NULL COMMENT '城市',
    `district` varchar(64) NOT NULL COMMENT '区县',
    `detail` varchar(255) NOT NULL COMMENT '详细地址',
    `is_default` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否默认地址',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='地址表';

-- 分类表
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
    `id` varchar(32) NOT NULL COMMENT '分类ID',
    `shop_id` varchar(32) NOT NULL COMMENT '店铺ID',
    `name` varchar(64) NOT NULL COMMENT '分类名称',
    `icon` varchar(255) DEFAULT NULL COMMENT '分类图标',
    `sort` int NOT NULL DEFAULT '0' COMMENT '排序',
    `status` varchar(10) NOT NULL DEFAULT 'normal' COMMENT '状态',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_shop_id` (`shop_id`),
    KEY `idx_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分类表';

-- 食品表
DROP TABLE IF EXISTS `food`;
CREATE TABLE `food` (
    `id` varchar(32) NOT NULL COMMENT '食品ID',
    `shop_id` varchar(32) NOT NULL COMMENT '店铺ID',
    `category_id` varchar(32) NOT NULL COMMENT '分类ID',
    `name` varchar(64) NOT NULL COMMENT '食品名称',
    `image` varchar(255) DEFAULT NULL COMMENT '食品图片',
    `description` varchar(255) DEFAULT NULL COMMENT '食品描述',
    `original_price` decimal(10,2) NOT NULL COMMENT '原价',
    `price` decimal(10,2) NOT NULL COMMENT '现价',
    `packing_fee` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '包装费',
    `sales` int NOT NULL DEFAULT '0' COMMENT '销量',
    `stock` int NOT NULL DEFAULT '0' COMMENT '库存',
    `specs` json DEFAULT NULL COMMENT '规格选项',
    `additions` json DEFAULT NULL COMMENT '加料选项',
    `status` varchar(10) NOT NULL DEFAULT 'normal' COMMENT '状态',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_shop_id` (`shop_id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_sales` (`sales`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='食品表';

-- 店铺表
DROP TABLE IF EXISTS `shop`;
CREATE TABLE `shop` (
    `id` varchar(32) NOT NULL COMMENT '店铺ID',
    `name` varchar(64) NOT NULL COMMENT '店铺名称',
    `image` varchar(255) DEFAULT NULL COMMENT '店铺图片',
    `address` varchar(255) NOT NULL COMMENT '店铺地址',
    `phone` varchar(11) NOT NULL COMMENT '店铺电话',
    `business_hours` varchar(64) NOT NULL COMMENT '营业时间',
    `delivery_fee` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '配送费',
    `min_order_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '起送价',
    `monthly_sales` int NOT NULL DEFAULT '0' COMMENT '月销量',
    `rating` decimal(2,1) NOT NULL DEFAULT '5.0' COMMENT '评分',
    `status` varchar(10) NOT NULL DEFAULT 'normal' COMMENT '状态',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_monthly_sales` (`monthly_sales`),
    KEY `idx_rating` (`rating`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='店铺表';

-- 订单表
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
    `id` varchar(32) NOT NULL COMMENT '订单ID',
    `user_id` varchar(32) NOT NULL COMMENT '用户ID',
    `shop_id` varchar(32) NOT NULL COMMENT '店铺ID',
    `order_no` varchar(32) NOT NULL COMMENT '订单编号',
    `status` varchar(20) NOT NULL DEFAULT 'pending' COMMENT '订单状态',
    `total_price` decimal(10,2) NOT NULL COMMENT '商品总价',
    `packing_fee` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '包装费',
    `delivery_fee` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '配送费',
    `actual_amount` decimal(10,2) NOT NULL COMMENT '实付金额',
    `payment_method` varchar(20) DEFAULT NULL COMMENT '支付方式',
    `payment_time` datetime DEFAULT NULL COMMENT '支付时间',
    `address_id` varchar(32) NOT NULL COMMENT '配送地址ID',
    `delivery_time` datetime DEFAULT NULL COMMENT '配送时间',
    `remark` varchar(255) DEFAULT NULL COMMENT '备注',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_shop_id` (`shop_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

-- 订单项表
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item` (
    `id` varchar(32) NOT NULL COMMENT '订单项ID',
    `order_id` varchar(32) NOT NULL COMMENT '订单ID',
    `food_id` varchar(32) NOT NULL COMMENT '食品ID',
    `food_name` varchar(64) NOT NULL COMMENT '食品名称',
    `food_image` varchar(255) DEFAULT NULL COMMENT '食品图片',
    `price` decimal(10,2) NOT NULL COMMENT '食品价格',
    `quantity` int NOT NULL COMMENT '购买数量',
    `subtotal` decimal(10,2) NOT NULL COMMENT '小计金额',
    `specs` json DEFAULT NULL COMMENT '规格信息',
    `additions` json DEFAULT NULL COMMENT '加料信息',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_food_id` (`food_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单项表';

-- 购物车表
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart` (
    `id` varchar(32) NOT NULL COMMENT '购物车ID',
    `user_id` varchar(32) NOT NULL COMMENT '用户ID',
    `shop_id` varchar(32) NOT NULL COMMENT '店铺ID',
    `food_id` varchar(32) NOT NULL COMMENT '食品ID',
    `food_name` varchar(64) NOT NULL COMMENT '食品名称',
    `food_image` varchar(255) DEFAULT NULL COMMENT '食品图片',
    `price` decimal(10,2) NOT NULL COMMENT '食品价格',
    `quantity` int NOT NULL COMMENT '购买数量',
    `specs` json DEFAULT NULL COMMENT '规格信息',
    `additions` json DEFAULT NULL COMMENT '加料信息',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_shop_id` (`shop_id`),
    KEY `idx_food_id` (`food_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='购物车表'; 