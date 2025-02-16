package com.orjrs.admin.integration;

import com.orjrs.BaseTest;
import com.orjrs.admin.entity.goods.Goods;
import com.orjrs.admin.entity.order.Order;
import com.orjrs.admin.entity.order.OrderRefund;
import com.orjrs.admin.entity.order.OrderStatistics;
import com.orjrs.admin.entity.goods.GoodsReview;
import com.orjrs.admin.service.goods.GoodsService;
import com.orjrs.admin.service.order.OrderRefundService;
import com.orjrs.admin.service.order.OrderService;
import com.orjrs.admin.service.order.OrderStatisticsService;
import com.orjrs.admin.service.user.UserPointsService;
import com.orjrs.admin.service.goods.GoodsReviewService;
import com.orjrs.common.exception.BusinessException;
import com.orjrs.common.service.RedisLockService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.ThreadLocalRandom;
import java.math.RoundingMode;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class ConcurrentOperationIntegrationTest extends BaseTest {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRefundService orderRefundService;

    @Autowired
    private OrderStatisticsService orderStatisticsService;

    @Autowired
    private RedisLockService redisLockService;

    @Autowired
    private UserPointsService userPointsService;

    @Autowired
    private GoodsReviewService goodsReviewService;

    @Test
    @Rollback(false)
    void testConcurrentStockOperation() throws InterruptedException {
        // 1. 准备测试商品
        Goods goods = new Goods();
        goods.setShopId(1L);
        goods.setCategoryId(1L);
        goods.setName("并发测试商品");
        goods.setOriginalPrice(new BigDecimal("100.00"));
        goods.setPrice(new BigDecimal("88.00"));
        goods.setStock(100);
        goods.setStatus(1);
        
        goodsService.saveGoods(goods);
        assertNotNull(goods.getId());

        // 2. 模拟100个用户同时购买
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        List<Order> successOrders = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    startLatch.await(); // 等待统一开始
                    
                    try {
                        // 创建订单
                        Order order = new Order();
                        order.setUserId(1L);
                        order.setShopId(1L);
                        order.setOrderNo("TEST" + System.currentTimeMillis());
                        order.setStatus("pending");
                        order.setTotalPrice(goods.getPrice());
                        order.setActualAmount(goods.getPrice());
                        order.setAddressId(1L);
                        
                        orderService.save(order);
                        
                        // 更新库存
                        goodsService.updateStock(goods.getId(), -1);
                        
                        successCount.incrementAndGet();
                        successOrders.add(order);
                        
                    } catch (BusinessException e) {
                        log.info("库存不足: {}", e.getMessage());
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endLatch.countDown();
                }
            });
        }

        // 3. 开始并发测试
        startLatch.countDown();
        
        // 4. 等待所有线程完成
        boolean allCompleted = endLatch.await(30, TimeUnit.SECONDS);
        assertTrue(allCompleted, "并发测试超时");
        
        executorService.shutdown();
        assertTrue(executorService.awaitTermination(5, TimeUnit.SECONDS));

        // 5. 验证结果
        Goods updatedGoods = goodsService.getDetail(goods.getId());
        assertEquals(100 - successCount.get(), updatedGoods.getStock());
        assertEquals(successCount.get(), successOrders.size());
        
        // 清理测试数据
        goodsService.deleteGoods(goods.getId());
        for (Order order : successOrders) {
            orderService.removeById(order.getId());
        }
    }

    @Test
    @Rollback(false)
    void testConcurrentCategoryOperation() throws InterruptedException {
        // 1. 模拟多个用户同时查询分类树
        int threadCount = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);
        AtomicInteger exceptionCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    startLatch.await();
                    try {
                        // 并发查询商品列表
                        goodsService.page(1, 10, null, null, null);
                    } catch (Exception e) {
                        exceptionCount.incrementAndGet();
                        log.error("查询异常: ", e);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endLatch.countDown();
                }
            });
        }

        // 2. 开始并发测试
        startLatch.countDown();
        
        // 3. 等待所有线程完成
        boolean allCompleted = endLatch.await(30, TimeUnit.SECONDS);
        assertTrue(allCompleted, "并发测试超时");
        
        executorService.shutdown();
        assertTrue(executorService.awaitTermination(5, TimeUnit.SECONDS));

        // 4. 验证结果
        assertEquals(0, exceptionCount.get(), "并发查询出现异常");
    }

    @Test
    @Rollback(false)
    void testConcurrentOrderStatusUpdate() throws InterruptedException {
        // 1. 创建测试订单
        Order order = new Order();
        order.setUserId(1L);
        order.setShopId(1L);
        order.setOrderNo("TEST" + System.currentTimeMillis());
        order.setStatus("pending");
        order.setTotalPrice(new BigDecimal("100.00"));
        order.setActualAmount(new BigDecimal("100.00"));
        order.setAddressId(1L);
        
        orderService.save(order);
        assertNotNull(order.getId());

        // 2. 模拟多个操作同时更新订单状态
        int threadCount = 20;
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);
        ConcurrentHashMap<String, AtomicInteger> statusCounts = new ConcurrentHashMap<>();
        List<Future<?>> futures = new ArrayList<>();

        // 3. 提交并发任务
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            Future<?> future = executorService.submit(() -> {
                try {
                    startLatch.await(); // 等待统一开始
                    
                    try {
                        String newStatus;
                        if (index % 4 == 0) {
                            newStatus = "completed";
                            order.setPaymentMethod("wechat");
                            order.setPaymentTime(LocalDateTime.now());
                        } else if (index % 4 == 1) {
                            newStatus = "cancelled";
                        } else if (index % 4 == 2) {
                            // 创建退款申请
                            OrderRefund refund = new OrderRefund();
                            refund.setOrderId(order.getId());
                            refund.setAmount(order.getActualAmount());
                            refund.setReason("并发测试退款");
                            orderRefundService.createRefund(refund);
                            newStatus = "refunded";
                        } else {
                            newStatus = "delivering";
                        }
                        
                        order.setStatus(newStatus);
                        orderService.updateById(order);
                        
                        statusCounts.computeIfAbsent(newStatus, k -> new AtomicInteger(0))
                                  .incrementAndGet();
                        
                    } catch (BusinessException e) {
                        log.info("状态更新失败: {}", e.getMessage());
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endLatch.countDown();
                }
            });
            futures.add(future);
        }

        // 4. 开始并发测试
        startLatch.countDown();
        
        // 5. 等待所有线程完成
        boolean allCompleted = endLatch.await(30, TimeUnit.SECONDS);
        assertTrue(allCompleted, "并发测试超时");
        
        executorService.shutdown();
        assertTrue(executorService.awaitTermination(5, TimeUnit.SECONDS));

        // 6. 验证结果
        Order finalOrder = orderService.getDetail(order.getId());
        assertNotNull(finalOrder);
        assertTrue(statusCounts.containsKey(finalOrder.getStatus()), 
                  "最终状态应该在并发更新的状态中");
        
        // 记录各状态的更新次数
        statusCounts.forEach((status, count) -> 
            log.info("状态 {} 更新次数: {}", status, count.get())
        );

        // 7. 清理测试数据
        orderService.removeById(order.getId());
    }

    @Test
    @Rollback(false)
    void testConcurrentRefundOperation() throws InterruptedException {
        // 1. 创建测试订单
        Order order = new Order();
        order.setUserId(1L);
        order.setShopId(1L);
        order.setOrderNo("TEST" + System.currentTimeMillis());
        order.setStatus("completed");
        order.setTotalPrice(new BigDecimal("100.00"));
        order.setActualAmount(new BigDecimal("100.00"));
        order.setAddressId(1L);
        order.setPaymentMethod("wechat");
        order.setPaymentTime(LocalDateTime.now());
        
        orderService.save(order);
        assertNotNull(order.getId());

        // 2. 模拟多个用户同时申请退款
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        List<OrderRefund> successRefunds = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    startLatch.await();
                    try {
                        // 创建退款申请
                        OrderRefund refund = new OrderRefund();
                        refund.setOrderId(order.getId());
                        refund.setAmount(order.getActualAmount());
                        refund.setReason("并发退款测试");
                        
                        orderRefundService.createRefund(refund);
                        
                        // 更新订单状态
                        order.setStatus("refunded");
                        orderService.updateById(order);
                        
                        successCount.incrementAndGet();
                        synchronized (successRefunds) {
                            successRefunds.add(refund);
                        }
                        
                    } catch (BusinessException e) {
                        log.info("退款申请失败: {}", e.getMessage());
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endLatch.countDown();
                }
            });
        }

        // 3. 开始并发测试
        startLatch.countDown();
        
        // 4. 等待所有线程完成
        boolean allCompleted = endLatch.await(30, TimeUnit.SECONDS);
        assertTrue(allCompleted, "并发测试超时");
        
        executorService.shutdown();
        assertTrue(executorService.awaitTermination(5, TimeUnit.SECONDS));

        // 5. 验证结果
        assertEquals(1, successCount.get(), "应该只有一个退款申请成功");
        Order finalOrder = orderService.getDetail(order.getId());
        assertEquals("refunded", finalOrder.getStatus());

        // 6. 清理测试数据
        orderService.removeById(order.getId());
        for (OrderRefund refund : successRefunds) {
            orderRefundService.deleteRefund(refund.getId());
        }
    }

    @Test
    @Rollback(false)
    void testConcurrentPriceUpdate() throws InterruptedException {
        // 1. 准备测试商品
        Goods goods = new Goods();
        goods.setShopId(1L);
        goods.setCategoryId(1L);
        goods.setName("价格并发测试商品");
        goods.setOriginalPrice(new BigDecimal("100.00"));
        goods.setPrice(new BigDecimal("88.00"));
        goods.setStock(100);
        goods.setStatus(1);
        
        goodsService.saveGoods(goods);
        assertNotNull(goods.getId());

        // 2. 模拟多个操作同时更新商品价格
        int threadCount = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);
        ConcurrentHashMap<BigDecimal, AtomicInteger> priceUpdateCounts = new ConcurrentHashMap<>();
        List<Future<?>> futures = new ArrayList<>();

        // 3. 提交并发任务
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            Future<?> future = executorService.submit(() -> {
                try {
                    startLatch.await(); // 等待统一开始
                    
                    try {
                        // 根据索引设置不同的价格
                        BigDecimal newPrice;
                        if (index % 3 == 0) {
                            newPrice = new BigDecimal("90.00");
                        } else if (index % 3 == 1) {
                            newPrice = new BigDecimal("85.00");
                        } else {
                            newPrice = new BigDecimal("95.00");
                        }
                        
                        // 更新商品价格
                        goodsService.updatePrice(goods.getId(), newPrice);
                        
                        // 记录更新次数
                        priceUpdateCounts.computeIfAbsent(newPrice, k -> new AtomicInteger(0))
                                       .incrementAndGet();
                        
                    } catch (BusinessException e) {
                        log.info("价格更新失败: {}", e.getMessage());
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endLatch.countDown();
                }
            });
            futures.add(future);
        }

        // 4. 开始并发测试
        startLatch.countDown();
        
        // 5. 等待所有线程完成
        boolean allCompleted = endLatch.await(30, TimeUnit.SECONDS);
        assertTrue(allCompleted, "并发测试超时");
        
        executorService.shutdown();
        assertTrue(executorService.awaitTermination(5, TimeUnit.SECONDS));

        // 6. 验证结果
        Goods updatedGoods = goodsService.getDetail(goods.getId());
        assertNotNull(updatedGoods);
        assertTrue(priceUpdateCounts.containsKey(updatedGoods.getPrice()), 
                  "最终价格应该在并发更新的价格中");
        
        // 记录各价格的更新次数
        priceUpdateCounts.forEach((price, count) -> 
            log.info("价格 {} 更新次数: {}", price, count.get())
        );

        // 7. 验证原价未被修改
        assertEquals(new BigDecimal("100.00"), updatedGoods.getOriginalPrice(), 
                    "原价不应该被修改");

        // 8. 清理测试数据
        goodsService.deleteGoods(goods.getId());
    }

    @Test
    @Rollback(false)
    void testConcurrentPriceAndStockUpdate() throws InterruptedException {
        // 1. 准备测试商品
        Goods goods = new Goods();
        goods.setShopId(1L);
        goods.setCategoryId(1L);
        goods.setName("价格库存并发测试商品");
        goods.setOriginalPrice(new BigDecimal("100.00"));
        goods.setPrice(new BigDecimal("88.00"));
        goods.setStock(100);
        goods.setStatus(1);
        
        goodsService.saveGoods(goods);
        assertNotNull(goods.getId());

        // 2. 模拟多个操作同时更新价格和库存
        int threadCount = 30;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);
        AtomicInteger priceUpdateCount = new AtomicInteger(0);
        AtomicInteger stockUpdateCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            executorService.submit(() -> {
                try {
                    startLatch.await();
                    try {
                        if (index % 2 == 0) {
                            // 更新价格
                            BigDecimal newPrice = new BigDecimal("95.00");
                            goodsService.updatePrice(goods.getId(), newPrice);
                            priceUpdateCount.incrementAndGet();
                        } else {
                            // 更新库存
                            goodsService.updateStock(goods.getId(), 90);
                            stockUpdateCount.incrementAndGet();
                        }
                    } catch (BusinessException e) {
                        log.info("更新失败: {}", e.getMessage());
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endLatch.countDown();
                }
            });
        }

        // 3. 开始并发测试
        startLatch.countDown();
        
        // 4. 等待所有线程完成
        boolean allCompleted = endLatch.await(30, TimeUnit.SECONDS);
        assertTrue(allCompleted, "并发测试超时");
        
        executorService.shutdown();
        assertTrue(executorService.awaitTermination(5, TimeUnit.SECONDS));

        // 5. 验证结果
        Goods updatedGoods = goodsService.getDetail(goods.getId());
        assertNotNull(updatedGoods);
        
        log.info("价格更新次数: {}", priceUpdateCount.get());
        log.info("库存更新次数: {}", stockUpdateCount.get());
        
        // 验证最终状态
        assertEquals(Integer.valueOf(90), updatedGoods.getStock(), 
                    "最终库存应该是最后一次更新的值");
        assertEquals(new BigDecimal("95.00"), updatedGoods.getPrice(), 
                    "最终价格应该是最后一次更新的值");

        // 6. 清理测试数据
        goodsService.deleteGoods(goods.getId());
    }

    @Test
    @Rollback(false)
    void testConcurrentOrderStatisticsUpdate() throws InterruptedException {
        // 1. 准备测试数据
        LocalDate today = LocalDate.now();
        OrderStatistics statistics = new OrderStatistics();
        statistics.setDate(today);
        statistics.setTotalOrders(0);
        statistics.setTotalAmount(BigDecimal.ZERO);
        statistics.setCompletedOrders(0);
        statistics.setCancelledOrders(0);
        statistics.setRefundedOrders(0);
        orderStatisticsService.save(statistics);

        // 2. 准备并发测试环境
        int threadCount = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);
        List<Future<?>> futures = new ArrayList<>();
        
        ConcurrentHashMap<String, AtomicInteger> statusCounts = new ConcurrentHashMap<>();
        AtomicReference<BigDecimal> totalAmount = new AtomicReference<>(BigDecimal.ZERO);

        // 3. 创建并发任务
        for (int i = 0; i < threadCount; i++) {
            Future<?> future = executorService.submit(() -> {
                try {
                    startLatch.await();
                    try {
                        // 创建订单
                        Order order = new Order();
                        order.setUserId(1L);
                        order.setShopId(1L);
                        order.setOrderNo("TEST" + System.nanoTime());
                        order.setTotalPrice(new BigDecimal("100.00"));
                        order.setPackingFee(new BigDecimal("2.00"));
                        order.setDeliveryFee(new BigDecimal("5.00"));
                        order.setActualAmount(new BigDecimal("107.00"));
                        order.setAddressId(1L);
                        
                        // 随机设置订单状态
                        String status = new String[]{"completed", "cancelled", "refunded"}[ThreadLocalRandom.current().nextInt(3)];
                        order.setStatus(status);
                        order.setCreateTime(LocalDateTime.now());
                        
                        // 保存订单并更新统计
                        orderService.save(order);
                        orderStatisticsService.updateStatistics(order);
                        
                        // 记录状态和金额
                        statusCounts.computeIfAbsent(status, k -> new AtomicInteger(0))
                                  .incrementAndGet();
                        totalAmount.accumulateAndGet(order.getActualAmount(), BigDecimal::add);
                        
                    } catch (Exception e) {
                        log.error("订单创建或统计更新失败", e);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endLatch.countDown();
                }
            });
            futures.add(future);
        }

        // 4. 开始并发测试
        startLatch.countDown();
        
        // 5. 等待所有线程完成
        boolean allCompleted = endLatch.await(30, TimeUnit.SECONDS);
        assertTrue(allCompleted, "并发测试超时");
        
        executorService.shutdown();
        assertTrue(executorService.awaitTermination(5, TimeUnit.SECONDS));

        // 6. 验证统计结果
        OrderStatistics finalStats = orderStatisticsService.getOne(
            new LambdaQueryWrapper<OrderStatistics>()
                .eq(OrderStatistics::getDate, today)
        );
        assertNotNull(finalStats);
        
        // 验证订单总数
        assertEquals(threadCount, finalStats.getTotalOrders());
        
        // 验证总金额
        assertEquals(totalAmount.get().setScale(2, RoundingMode.HALF_UP), 
                    finalStats.getTotalAmount().setScale(2, RoundingMode.HALF_UP));
        
        // 验证各状态订单数
        assertEquals(statusCounts.getOrDefault("completed", new AtomicInteger(0)).get(), 
                    finalStats.getCompletedOrders());
        assertEquals(statusCounts.getOrDefault("cancelled", new AtomicInteger(0)).get(), 
                    finalStats.getCancelledOrders());
        assertEquals(statusCounts.getOrDefault("refunded", new AtomicInteger(0)).get(), 
                    finalStats.getRefundedOrders());
        
        log.info("订单统计结果：总订单数={}, 总金额={}, 完成订单数={}, 取消订单数={}, 退款订单数={}",
                finalStats.getTotalOrders(),
                finalStats.getTotalAmount(),
                finalStats.getCompletedOrders(),
                finalStats.getCancelledOrders(),
                finalStats.getRefundedOrders());
    }

    @Test
    @Rollback(false)
    void testConcurrentDailyStatisticsGeneration() throws InterruptedException {
        // 1. 准备测试数据
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        LocalDate yesterdayDate = yesterday.toLocalDate();
        
        // 创建一些测试订单
        for (int i = 0; i < 10; i++) {
            Order order = new Order();
            order.setUserId(1L);
            order.setShopId(1L);
            order.setOrderNo("TEST" + System.currentTimeMillis() + i);
            order.setTotalPrice(new BigDecimal("100.00"));
            order.setActualAmount(new BigDecimal("100.00"));
            order.setAddressId(1L);
            order.setCreateTime(yesterday);
            order.setStatus(i % 2 == 0 ? "completed" : "cancelled");
            
            orderService.save(order);
        }

        // 2. 模拟多次并发生成统计数据
        int threadCount = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    startLatch.await();
                    try {
                        // 生成每日统计
                        orderStatisticsService.generateDailyStatistics();
                    } catch (Exception e) {
                        log.error("生成统计失败: ", e);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endLatch.countDown();
                }
            });
        }

        // 3. 开始并发测试
        startLatch.countDown();
        
        // 4. 等待所有线程完成
        boolean allCompleted = endLatch.await(30, TimeUnit.SECONDS);
        assertTrue(allCompleted, "并发测试超时");
        
        executorService.shutdown();
        assertTrue(executorService.awaitTermination(5, TimeUnit.SECONDS));

        // 5. 验证结果
        List<OrderStatistics> statistics = orderStatisticsService.getStatistics(
            LocalDateTime.of(yesterdayDate, LocalTime.MIN),
            LocalDateTime.of(yesterdayDate, LocalTime.MAX)
        );
        
        assertFalse(statistics.isEmpty(), "应该有统计数据");
        OrderStatistics yesterdayStats = statistics.get(0);
        
        // 验证统计数据的准确性
        assertEquals(10, yesterdayStats.getTotalOrders(), "总订单数应该正确");
        assertEquals(5, yesterdayStats.getCompletedOrders(), "完成订单数应该正确");
        assertEquals(5, yesterdayStats.getCancelledOrders(), "取消订单数应该正确");
        
        BigDecimal expectedTotalAmount = new BigDecimal("1000.00"); // 10 * 100.00
        assertEquals(0, expectedTotalAmount.compareTo(yesterdayStats.getTotalAmount()), "总金额应该正确");

        // 6. 清理测试数据
        orderService.remove(null); // 清除所有测试订单
    }

    @Test
    @Rollback(false)
    void testConcurrentStockPreoccupy() throws InterruptedException {
        // 1. 准备测试数据
        int initialStock = 50;  // 初始库存
        int threadCount = 100;  // 并发线程数
        
        // 获取测试商品并设置初始库存
        Goods goods = goodsService.getById(1L);
        goods.setStock(initialStock);
        goodsService.updateById(goods);
        
        // 2. 准备并发工具
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);
        List<Order> successOrders = Collections.synchronizedList(new ArrayList<>());
        
        // 3. 提交并发任务
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    startLatch.await(); // 等待统一开始
                    
                    try {
                        // 创建订单对象
                        Order order = new Order();
                        order.setUserId(1L);
                        order.setShopId(1L);
                        order.setOrderNo("TEST" + System.currentTimeMillis() + Thread.currentThread().getId());
                        order.setStatus("pending");
                        order.setTotalPrice(goods.getPrice());
                        order.setPackingFee(goods.getPackingFee());
                        order.setDeliveryFee(new BigDecimal("5.00"));
                        order.setActualAmount(goods.getPrice().add(goods.getPackingFee()).add(new BigDecimal("5.00")));
                        order.setAddressId(1L);
                        
                        // 尝试创建订单（带库存检查）
                        Long orderId = orderService.createOrderWithStockCheck(order, goods.getId(), 1);
                        if (orderId != null) {
                            successCount.incrementAndGet();
                            successOrders.add(order);
                        }
                    } catch (BusinessException e) {
                        log.info("创建订单失败: {}", e.getMessage());
                        failCount.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endLatch.countDown();
                }
            });
        }
        
        // 4. 开始并发测试
        long startTime = System.currentTimeMillis();
        startLatch.countDown();
        
        // 5. 等待所有线程完成
        boolean allFinished = endLatch.await(30, TimeUnit.SECONDS);
        long endTime = System.currentTimeMillis();
        
        // 6. 关闭线程池
        executorService.shutdown();
        
        // 7. 验证结果
        Goods finalGoods = goodsService.getById(goods.getId());
        
        // 打印测试结果
        log.info("并发库存预占测试完成:");
        log.info("总耗时: {}ms", endTime - startTime);
        log.info("初始库存: {}", initialStock);
        log.info("剩余库存: {}", finalGoods.getStock());
        log.info("成功次数: {}", successCount.get());
        log.info("失败次数: {}", failCount.get());
        log.info("总尝试次数: {}", threadCount);
        
        // 8. 断言验证
        assertTrue(allFinished, "并发测试超时");
        assertEquals(initialStock - successCount.get(), finalGoods.getStock(), "库存扣减数量应等于成功订单数");
        assertEquals(successCount.get(), successOrders.size(), "成功订单数量应等于库存扣减数量");
        assertTrue(finalGoods.getStock() >= 0, "库存不应为负数");
        assertEquals(threadCount, successCount.get() + failCount.get(), "成功和失败次数之和应等于总尝试次数");
        
        // 9. 验证订单状态
        for (Order order : successOrders) {
            Order savedOrder = orderService.getById(order.getId());
            assertNotNull(savedOrder, "订单应该存在");
            assertEquals("pending", savedOrder.getStatus(), "订单状态应该为pending");
        }
    }

    @Test
    void testConcurrentDistributedLock() throws InterruptedException {
        String lockKey = "test:concurrent:lock:" + System.currentTimeMillis();
        int threadCount = 50;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);
        AtomicReference<String> lockHolder = new AtomicReference<>();

        // 创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        // 启动多个线程同时尝试获取锁
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executorService.submit(() -> {
                try {
                    startLatch.await(); // 等待统一开始
                    String threadName = "Thread-" + threadId;
                    
                    // 尝试获取锁
                    boolean acquired = redisLockService.tryLock(lockKey, 5, TimeUnit.SECONDS);
                    if (acquired) {
                        successCount.incrementAndGet();
                        lockHolder.set(threadName);
                        log.info("{} 成功获取锁", threadName);
                        
                        // 模拟业务处理
                        Thread.sleep(100);
                        
                        // 释放锁
                        boolean released = redisLockService.unlock(lockKey);
                        log.info("{} 释放锁 {}", threadName, released ? "成功" : "失败");
                    } else {
                        failCount.incrementAndGet();
                        log.info("{} 获取锁失败", threadName);
                    }
                } catch (Exception e) {
                    log.error("线程执行异常", e);
                } finally {
                    endLatch.countDown();
                }
            });
        }

        // 开始并发测试
        startLatch.countDown();
        
        // 等待所有线程完成
        endLatch.await(10, TimeUnit.SECONDS);
        executorService.shutdown();
        
        // 验证结果
        log.info("并发锁测试完成 - 成功次数: {}, 失败次数: {}, 最后持有锁的线程: {}", 
                successCount.get(), failCount.get(), lockHolder.get());
                
        // 断言结果
        assertEquals(threadCount, successCount.get() + failCount.get(), "总尝试次数应等于线程数");
        assertTrue(successCount.get() > 0, "至少有一个线程应该成功获取锁");
        assertFalse(redisLockService.isLocked(lockKey), "测试结束后锁应该被释放");
    }

    @Test
    @Rollback(false)
    void testConcurrentCacheUpdate() throws InterruptedException {
        // 1. 准备测试数据
        Goods goods = goodsService.getDetail(1L);
        int threadCount = 50;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);
        Map<String, AtomicInteger> updateTypes = new ConcurrentHashMap<>();

        // 2. 创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Future<?>> futures = new ArrayList<>();

        // 3. 提交并发任务
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            Future<?> future = executorService.submit(() -> {
                try {
                    startLatch.await(); // 等待统一开始
                    
                    try {
                        // 根据索引执行不同的更新操作
                        if (index % 3 == 0) {
                            // 更新商品名称
                            goods.setName("测试商品-" + index);
                            goodsService.updateGoods(goods);
                            updateTypes.computeIfAbsent("name", k -> new AtomicInteger(0))
                                     .incrementAndGet();
                        } else if (index % 3 == 1) {
                            // 更新商品价格
                            goods.setPrice(new BigDecimal("88.00").add(new BigDecimal(index)));
                            goodsService.updateGoods(goods);
                            updateTypes.computeIfAbsent("price", k -> new AtomicInteger(0))
                                     .incrementAndGet();
                        } else {
                            // 更新商品描述
                            goods.setDescription("测试描述-" + index);
                            goodsService.updateGoods(goods);
                            updateTypes.computeIfAbsent("description", k -> new AtomicInteger(0))
                                     .incrementAndGet();
                        }
                        
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        failCount.incrementAndGet();
                        log.error("更新失败: ", e);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endLatch.countDown();
                }
            });
            futures.add(future);
        }

        // 4. 开始并发测试
        startLatch.countDown();
        
        // 5. 等待所有线程完成
        boolean allCompleted = endLatch.await(30, TimeUnit.SECONDS);
        assertTrue(allCompleted, "并发测试超时");
        
        executorService.shutdown();
        assertTrue(executorService.awaitTermination(5, TimeUnit.SECONDS));

        // 6. 验证结果
        assertEquals(threadCount, successCount.get() + failCount.get(), 
                "成功和失败的总数应该等于线程总数");
        
        // 验证缓存是否被正确更新
        Goods updatedGoods = goodsService.getDetail(goods.getId());
        assertNotNull(updatedGoods);
        
        // 输出更新统计
        log.info("缓存并发更新测试结果：总请求数={}, 成功数={}, 失败数={}", 
                threadCount, 
                successCount.get(),
                failCount.get());
        
        updateTypes.forEach((type, count) -> 
            log.info("更新类型: {}, 更新次数: {}", type, count.get()));
    }

    @Test
    @Rollback(false)
    void testConcurrentOrderPayment() throws InterruptedException {
        // 1. 创建测试订单
        Order order = new Order();
        order.setUserId(1L);
        order.setShopId(1L);
        order.setOrderNo("TEST" + System.currentTimeMillis());
        order.setStatus("pending");
        order.setTotalPrice(new BigDecimal("100.00"));
        order.setActualAmount(new BigDecimal("100.00"));
        order.setAddressId(1L);
        
        orderService.save(order);
        assertNotNull(order.getId());

        // 2. 准备并发测试环境
        int threadCount = 20;
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);
        Map<String, AtomicInteger> paymentMethods = new ConcurrentHashMap<>();

        // 3. 创建并发任务
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            executorService.submit(() -> {
                try {
                    startLatch.await();
                    try {
                        // 随机选择支付方式
                        String paymentMethod = index % 2 == 0 ? "wechat" : "alipay";
                        
                        // 使用分布式锁确保支付操作的原子性
                        String lockKey = "order:payment:" + order.getId();
                        if (redisLockService.tryLock(lockKey, 5, TimeUnit.SECONDS)) {
                            try {
                                // 检查订单状态
                                Order currentOrder = orderService.getDetail(order.getId());
                                if ("pending".equals(currentOrder.getStatus())) {
                                    // 更新订单状态为已支付
                                    currentOrder.setStatus("paid");
                                    currentOrder.setPaymentMethod(paymentMethod);
                                    currentOrder.setPaymentTime(LocalDateTime.now());
                                    orderService.updateById(currentOrder);
                                    
                                    successCount.incrementAndGet();
                                    paymentMethods.computeIfAbsent(paymentMethod, k -> new AtomicInteger(0))
                                                .incrementAndGet();
                                } else {
                                    failCount.incrementAndGet();
                                    log.info("订单状态不正确: {}", currentOrder.getStatus());
                                }
                            } finally {
                                redisLockService.unlock(lockKey);
                            }
                        } else {
                            failCount.incrementAndGet();
                            log.info("获取支付锁失败");
                        }
                    } catch (BusinessException e) {
                        failCount.incrementAndGet();
                        log.info("支付失败: {}", e.getMessage());
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endLatch.countDown();
                }
            });
        }

        // 4. 开始并发测试
        startLatch.countDown();
        
        // 5. 等待所有线程完成
        boolean allCompleted = endLatch.await(30, TimeUnit.SECONDS);
        assertTrue(allCompleted, "并发测试超时");
        
        executorService.shutdown();
        assertTrue(executorService.awaitTermination(5, TimeUnit.SECONDS));

        // 6. 验证结果
        Order finalOrder = orderService.getDetail(order.getId());
        assertEquals("paid", finalOrder.getStatus(), "订单最终状态应该是已支付");
        assertNotNull(finalOrder.getPaymentMethod(), "支付方式不应为空");
        assertNotNull(finalOrder.getPaymentTime(), "支付时间不应为空");
        
        // 验证只有一次支付成功
        assertEquals(1, successCount.get(), "应该只有一次支付成功");
        assertEquals(threadCount - 1, failCount.get(), "其他尝试应该都失败");
        
        // 输出支付统计
        log.info("订单支付并发测试结果：总尝试次数={}, 成功次数={}, 失败次数={}", 
                threadCount, successCount.get(), failCount.get());
        
        paymentMethods.forEach((method, count) -> 
            log.info("支付方式: {}, 成功次数: {}", method, count.get()));

        // 7. 清理测试数据
        orderService.removeById(order.getId());
    }

    @Test
    @Rollback(false)
    void testConcurrentUserPointsUpdate() throws InterruptedException {
        // 1. 准备测试数据
        Long userId = 1L;
        int initialPoints = 1000;
        userPointsService.increasePoints(userId, initialPoints, "初始积分");

        // 2. 准备并发测试环境
        int threadCount = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);
        AtomicInteger increaseSuccessCount = new AtomicInteger(0);
        AtomicInteger decreaseSuccessCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        // 3. 创建并发任务
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            executorService.submit(() -> {
                try {
                    startLatch.await();
                    try {
                        if (index % 2 == 0) {
                            // 增加积分
                            userPointsService.increasePoints(userId, 100, "并发测试增加积分");
                            increaseSuccessCount.incrementAndGet();
                        } else {
                            // 扣减积分
                            userPointsService.decreasePoints(userId, 50, "并发测试扣减积分");
                            decreaseSuccessCount.incrementAndGet();
                        }
                    } catch (BusinessException e) {
                        failCount.incrementAndGet();
                        log.info("积分操作失败: {}", e.getMessage());
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endLatch.countDown();
                }
            });
        }

        // 4. 开始并发测试
        startLatch.countDown();
        
        // 5. 等待所有线程完成
        boolean allCompleted = endLatch.await(30, TimeUnit.SECONDS);
        assertTrue(allCompleted, "并发测试超时");
        
        executorService.shutdown();
        assertTrue(executorService.awaitTermination(5, TimeUnit.SECONDS));

        // 6. 验证结果
        Integer finalPoints = userPointsService.getCurrentPoints(userId);
        
        // 计算期望的最终积分
        int expectedPoints = initialPoints + 
                           (increaseSuccessCount.get() * 100) - 
                           (decreaseSuccessCount.get() * 50);
        
        assertEquals(expectedPoints, finalPoints, "最终积分应该符合预期");
        
        // 输出统计信息
        log.info("用户积分并发测试结果：初始积分={}, 最终积分={}", 
                initialPoints, finalPoints);
        log.info("增加积分成功次数={}, 扣减积分成功次数={}, 失败次数={}", 
                increaseSuccessCount.get(), 
                decreaseSuccessCount.get(), 
                failCount.get());
        
        // 验证总操作次数
        assertEquals(threadCount, 
                    increaseSuccessCount.get() + decreaseSuccessCount.get() + failCount.get(),
                    "总操作次数应该等于线程数");
    }

    @Test
    @Rollback(false)
    void testConcurrentGoodsReview() throws InterruptedException {
        // 1. 准备测试数据
        int threadCount = 20;  // 并发线程数
        Long orderId = 1L;     // 测试订单ID
        Long goodsId = 1L;     // 测试商品ID
        Long userId = 1L;      // 测试用户ID
        
        // 2. 准备并发工具
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);
        List<GoodsReview> successReviews = Collections.synchronizedList(new ArrayList<>());
        
        // 3. 提交并发任务
        for (int i = 0; i < threadCount; i++) {
            final int rating = (i % 5) + 1;  // 1-5星评价
            executorService.submit(() -> {
                try {
                    startLatch.await(); // 等待统一开始
                    
                    try {
                        // 创建评价对象
                        GoodsReview review = new GoodsReview();
                        review.setGoodsId(goodsId);
                        review.setUserId(userId);
                        review.setOrderId(orderId);
                        review.setRating(rating);
                        review.setContent("并发测试评价-" + rating + "星");
                        review.setIsAnonymous(false);
                        
                        // 尝试创建评价
                        Long reviewId = goodsReviewService.createReview(review);
                        if (reviewId != null) {
                            successCount.incrementAndGet();
                            successReviews.add(review);
                        }
                    } catch (BusinessException e) {
                        log.info("创建评价失败: {}", e.getMessage());
                        failCount.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endLatch.countDown();
                }
            });
        }
        
        // 4. 开始并发测试
        long startTime = System.currentTimeMillis();
        startLatch.countDown();
        
        // 5. 等待所有线程完成
        boolean allFinished = endLatch.await(30, TimeUnit.SECONDS);
        long endTime = System.currentTimeMillis();
        
        // 6. 关闭线程池
        executorService.shutdown();
        
        // 7. 验证结果
        List<GoodsReview> finalReviews = goodsReviewService.getGoodsReviews(goodsId);
        
        // 打印测试结果
        log.info("并发商品评价测试完成:");
        log.info("总耗时: {}ms", endTime - startTime);
        log.info("总尝试次数: {}", threadCount);
        log.info("成功次数: {}", successCount.get());
        log.info("失败次数: {}", failCount.get());
        log.info("最终评价数: {}", finalReviews.size());
        
        // 8. 断言验证
        assertTrue(allFinished, "并发测试超时");
        assertEquals(1, successCount.get(), "应该只有一个评价成功");
        assertEquals(threadCount - 1, failCount.get(), "其他评价应该都失败");
        assertEquals(1, finalReviews.size(), "最终应该只有一条评价记录");
        assertEquals(orderId, finalReviews.get(0).getOrderId(), "评价应该关联到正确的订单");
        
        // 9. 验证评价内容
        GoodsReview savedReview = finalReviews.get(0);
        assertNotNull(savedReview.getId(), "评价ID不应为空");
        assertTrue(savedReview.getRating() >= 1 && savedReview.getRating() <= 5, "评分应该在1-5之间");
        assertTrue(savedReview.getContent().startsWith("并发测试评价-"), "评价内容应该符合预期格式");
        assertFalse(savedReview.getIsAnonymous(), "评价不应该是匿名的");
        assertNull(savedReview.getReplyContent(), "新评价还没有回复内容");
    }

    @Test
    @Rollback(false)
    void testConcurrentGoodsReviewReply() throws InterruptedException {
        // 1. 准备测试数据 - 创建一个评价
        GoodsReview review = new GoodsReview();
        review.setGoodsId(1L);
        review.setUserId(1L);
        review.setOrderId(1L);
        review.setRating(5);
        review.setContent("测试评价");
        review.setIsAnonymous(false);
        goodsReviewService.createReview(review);
        assertNotNull(review.getId());

        // 2. 准备并发测试环境
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        // 3. 提交并发任务
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            executorService.submit(() -> {
                try {
                    startLatch.await();
                    try {
                        // 尝试回复评价
                        goodsReviewService.replyReview(review.getId(), 
                            "感谢您的评价，我们会继续努力！-" + index);
                        successCount.incrementAndGet();
                    } catch (BusinessException e) {
                        failureCount.incrementAndGet();
                        log.info("评价回复失败: {}", e.getMessage());
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endLatch.countDown();
                }
            });
        }

        // 4. 开始并发测试
        long startTime = System.currentTimeMillis();
        startLatch.countDown();
        
        // 5. 等待所有任务完成
        boolean allFinished = endLatch.await(30, TimeUnit.SECONDS);
        long endTime = System.currentTimeMillis();
        
        // 6. 关闭线程池
        executorService.shutdown();
        
        // 7. 验证结果
        assertTrue(allFinished, "并发测试超时");
        assertEquals(threadCount, successCount.get() + failureCount.get(), "总尝试次数不匹配");
        assertEquals(1, successCount.get(), "应该只有一个回复成功");
        assertEquals(threadCount - 1, failureCount.get(), "失败次数不匹配");
        
        // 8. 验证评价状态
        GoodsReview updatedReview = goodsReviewService.getById(review.getId());
        assertNotNull(updatedReview.getReplyContent(), "评价应该已被回复");
        assertNotNull(updatedReview.getReplyTime(), "回复时间应该已设置");
        
        // 9. 输出统计信息
        log.info("并发回复测试完成:");
        log.info("总耗时: {}ms", endTime - startTime);
        log.info("总尝试次数: {}", threadCount);
        log.info("成功次数: {}", successCount.get());
        log.info("失败次数: {}", failureCount.get());
    }

    @Test
    @Rollback(false)
    void testConcurrentMultiOrderReviews() throws InterruptedException {
        // 1. 准备测试数据 - 多个订单
        Long[] orderIds = {1L, 2L, 3L, 4L, 5L};  // 使用测试数据中的5个订单
        Long goodsId = 1L;
        Long userId = 1L;
        int threadsPerOrder = 4;  // 每个订单4个并发线程
        int totalThreads = orderIds.length * threadsPerOrder;
        
        // 2. 准备并发工具
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(totalThreads);
        Map<Long, AtomicInteger> successCountPerOrder = new ConcurrentHashMap<>();
        Map<Long, AtomicInteger> failCountPerOrder = new ConcurrentHashMap<>();
        List<GoodsReview> successReviews = Collections.synchronizedList(new ArrayList<>());
        
        // 3. 提交并发任务
        for (Long orderId : orderIds) {
            for (int i = 0; i < threadsPerOrder; i++) {
                final int rating = (i % 5) + 1;
                executorService.submit(() -> {
                    try {
                        startLatch.await();
                        
                        try {
                            // 创建评价对象
                            GoodsReview review = new GoodsReview();
                            review.setGoodsId(goodsId);
                            review.setUserId(userId);
                            review.setOrderId(orderId);
                            review.setRating(rating);
                            review.setContent("订单" + orderId + "的并发测试评价-" + rating + "星");
                            review.setIsAnonymous(false);
                            
                            // 尝试创建评价
                            Long reviewId = goodsReviewService.createReview(review);
                            if (reviewId != null) {
                                successCountPerOrder.computeIfAbsent(orderId, k -> new AtomicInteger(0))
                                                  .incrementAndGet();
                                successReviews.add(review);
                            }
                        } catch (BusinessException e) {
                            failCountPerOrder.computeIfAbsent(orderId, k -> new AtomicInteger(0))
                                           .incrementAndGet();
                            log.info("创建评价失败 - 订单{}: {}", orderId, e.getMessage());
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        endLatch.countDown();
                    }
                });
            }
        }
        
        // 4. 开始并发测试
        long startTime = System.currentTimeMillis();
        startLatch.countDown();
        
        // 5. 等待所有线程完成
        boolean allFinished = endLatch.await(30, TimeUnit.SECONDS);
        long endTime = System.currentTimeMillis();
        
        // 6. 关闭线程池
        executorService.shutdown();
        
        // 7. 验证结果
        List<GoodsReview> finalReviews = goodsReviewService.getGoodsReviews(goodsId);
        
        // 打印测试结果
        log.info("多订单并发评价测试完成:");
        log.info("总耗时: {}ms", endTime - startTime);
        log.info("总订单数: {}", orderIds.length);
        log.info("每个订单的并发线程数: {}", threadsPerOrder);
        log.info("总尝试次数: {}", totalThreads);
        
        // 按订单ID输出统计信息
        for (Long orderId : orderIds) {
            int successCount = successCountPerOrder.getOrDefault(orderId, new AtomicInteger(0)).get();
            int failCount = failCountPerOrder.getOrDefault(orderId, new AtomicInteger(0)).get();
            log.info("订单{} - 成功次数: {}, 失败次数: {}", orderId, successCount, failCount);
        }
        
        // 8. 断言验证
        assertTrue(allFinished, "并发测试超时");
        assertEquals(orderIds.length, finalReviews.size(), "每个订单应该有一个成功的评价");
        
        // 验证每个订单的评价情况
        for (Long orderId : orderIds) {
            // 验证每个订单只有一个成功的评价
            assertEquals(1, successCountPerOrder.getOrDefault(orderId, new AtomicInteger(0)).get(),
                    "订单" + orderId + "应该只有一个评价成功");
            assertEquals(threadsPerOrder - 1, failCountPerOrder.getOrDefault(orderId, new AtomicInteger(0)).get(),
                    "订单" + orderId + "的其他评价应该都失败");
            
            // 验证评价内容
            GoodsReview review = finalReviews.stream()
                    .filter(r -> r.getOrderId().equals(orderId))
                    .findFirst()
                    .orElse(null);
            assertNotNull(review, "订单" + orderId + "的评价不应为空");
            assertTrue(review.getContent().startsWith("订单" + orderId), 
                    "评价内容应该包含正确的订单ID");
            assertTrue(review.getRating() >= 1 && review.getRating() <= 5,
                    "评分应该在1-5之间");
        }
    }
} 