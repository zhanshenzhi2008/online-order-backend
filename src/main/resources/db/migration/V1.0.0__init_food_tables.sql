-- 商品分类表
CREATE TABLE `food_category` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    `name` varchar(50) NOT NULL COMMENT '分类名称',
    `parent_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '父级ID',
    `icon` varchar(100) DEFAULT NULL COMMENT '分类图标',
    `image` varchar(200) DEFAULT NULL COMMENT '分类图片',
    `description` varchar(500) DEFAULT NULL COMMENT '分类描述',
    `food_count` int(11) NOT NULL DEFAULT '0' COMMENT '商品数量',
    `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
    `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- 商品信息表
CREATE TABLE `food` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
    `name` varchar(100) NOT NULL COMMENT '商品名称',
    `category_id` bigint(20) NOT NULL COMMENT '分类ID',
    `price` decimal(10,2) NOT NULL COMMENT '商品价格',
    `image` varchar(200) DEFAULT NULL COMMENT '商品图片',
    `description` varchar(500) DEFAULT NULL COMMENT '商品描述',
    `detail` text COMMENT '商品详情',
    `tags` varchar(200) DEFAULT NULL COMMENT '商品标签，多个标签用逗号分隔',
    `sales` int(11) NOT NULL DEFAULT '0' COMMENT '销量',
    `rating` decimal(2,1) NOT NULL DEFAULT '5.0' COMMENT '评分',
    `comment_count` int(11) NOT NULL DEFAULT '0' COMMENT '评价数量',
    `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
    `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态：0-下架，1-上架',
    `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_sort` (`sort`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品信息表';

-- 商品规格表
CREATE TABLE `food_specification` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '规格ID',
    `food_id` bigint(20) NOT NULL COMMENT '商品ID',
    `name` varchar(50) NOT NULL COMMENT '规格名称',
    `price` decimal(10,2) NOT NULL COMMENT '规格价格',
    `image` varchar(200) DEFAULT NULL COMMENT '规格图片',
    `description` varchar(200) DEFAULT NULL COMMENT '规格描述',
    `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
    `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_food_id` (`food_id`),
    KEY `idx_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品规格表';

-- 商品属性表
CREATE TABLE `food_attribute` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '属性ID',
    `food_id` bigint(20) NOT NULL COMMENT '商品ID',
    `name` varchar(50) NOT NULL COMMENT '属性名称',
    `value` varchar(200) NOT NULL COMMENT '属性值',
    `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
    `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_food_id` (`food_id`),
    KEY `idx_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品属性表'; 