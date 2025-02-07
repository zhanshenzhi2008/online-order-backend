-- 插入商品分类测试数据
INSERT INTO category (id, parent_id, name, icon, sort, status) VALUES
(1, 0, '主食', 'rice', 1, 1),
(2, 0, '小吃', 'snack', 2, 1),
(3, 0, '饮品', 'drink', 3, 1),
(4, 1, '米饭', 'rice-bowl', 1, 1),
(5, 1, '面食', 'noodle', 2, 1),
(6, 2, '炸物', 'fried', 1, 1),
(7, 2, '烤物', 'grilled', 2, 1),
(8, 3, '果汁', 'juice', 1, 1),
(9, 3, '奶茶', 'milk-tea', 2, 1);

-- 插入商品测试数据
INSERT INTO goods (id, shop_id, category_id, name, image, description, original_price, price, packing_fee, stock, status) VALUES
(1, 1, 4, '白米饭', 'rice.jpg', '香喷喷的白米饭', 3.00, 2.00, 0.00, 100, 1),
(2, 1, 5, '阳春面', 'noodle.jpg', '美味阳春面', 15.00, 12.00, 1.00, 50, 1),
(3, 1, 6, '薯条', 'fries.jpg', '黄金脆薯条', 12.00, 10.00, 1.00, 80, 1),
(4, 1, 8, '橙汁', 'orange-juice.jpg', '鲜榨橙汁', 10.00, 8.00, 0.00, 30, 1),
(5, 1, 9, '珍珠奶茶', 'bubble-tea.jpg', '香浓珍珠奶茶', 15.00, 12.00, 0.00, 40, 1);

-- 插入订单测试数据
INSERT INTO `order` (id, user_id, shop_id, order_no, status, total_price, packing_fee, delivery_fee, actual_amount, payment_method, address_id) VALUES
(1, 1, 1, 'O202403200001', 'completed', 24.00, 1.00, 5.00, 30.00, 'wechat', 1),
(2, 2, 1, 'O202403200002', 'pending', 32.00, 2.00, 5.00, 39.00, 'alipay', 2),
(3, 1, 1, 'O202403200003', 'cancelled', 18.00, 1.00, 5.00, 24.00, 'wechat', 1),
(4, 3, 1, 'O202403200004', 'completed', 45.00, 2.00, 5.00, 52.00, 'alipay', 3),
(5, 2, 1, 'O202403200005', 'refunded', 36.00, 2.00, 5.00, 43.00, 'wechat', 2);

-- 插入订单统计测试数据
INSERT INTO order_statistics (id, date, total_orders, total_amount, completed_orders, cancelled_orders, refunded_orders) VALUES
(1, '2024-03-20', 5, 188.00, 2, 1, 1),
(2, '2024-03-19', 8, 320.00, 6, 1, 1),
(3, '2024-03-18', 10, 450.00, 8, 1, 1);

-- 插入订单退款测试数据
INSERT INTO order_refund (id, order_id, amount, reason, status, refund_time) VALUES
(1, 5, 43.00, '商品质量问题', 'completed', '2024-03-20 15:30:00');

-- 插入商品评价测试数据
INSERT INTO goods_review (id, goods_id, user_id, order_id, rating, content, is_anonymous) VALUES
(1, 1, 1, 1, 5, '米饭很香，分量足', 0),
(2, 2, 2, 2, 4, '面条劲道，就是有点咸', 0),
(3, 3, 1, 3, 3, '薯条还行，不够脆', 1),
(4, 4, 3, 4, 5, '果汁新鲜，味道很好', 0),
(5, 5, 2, 5, 4, '奶茶不错，珍珠很Q弹', 1); 