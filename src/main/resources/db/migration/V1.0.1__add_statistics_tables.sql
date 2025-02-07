-- 订单统计表
CREATE TABLE `order_statistics` (
    `id` varchar(32) NOT NULL COMMENT '统计ID',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单统计表';

-- 订单退款表
CREATE TABLE `order_refund` (
    `id` varchar(32) NOT NULL COMMENT '退款ID',
    `order_id` varchar(32) NOT NULL COMMENT '订单ID',
    `amount` decimal(10,2) NOT NULL COMMENT '退款金额',
    `reason` varchar(255) NOT NULL COMMENT '退款原因',
    `status` varchar(20) NOT NULL COMMENT '退款状态',
    `refund_time` datetime DEFAULT NULL COMMENT '退款时间',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单退款表'; 