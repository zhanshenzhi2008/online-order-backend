package com.orjrs.miniapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orjrs.miniapp.entity.Order;
import com.orjrs.miniapp.service.OrderService;
import com.orjrs.utils.CacheUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl {
    private final OrderService orderService;
    private final CacheUtils cacheUtils;

    /**
     * 每5分钟清理一次过期订单
     * 超过30分钟未支付的订单自动取消
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void cleanExpiredOrders() {
        log.info("开始清理过期订单");
        LocalDateTime expireTime = LocalDateTime.now().minusMinutes(30);

        // 查询过期未支付订单
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getStatus, "pending")
                .le(Order::getCreateTime, expireTime);
        List<Order> orders = orderService.list(wrapper);

        // 取消订单
        for (Order order : orders) {
            order.setStatus("cancelled");
            order.setStatusText("超时未支付，自动取消");
            orderService.updateById(order);
            log.info("订单{}已取消", order.getOrderNo());
        }
    }

    /**
     * 每天凌晨1点更新商品销量统计
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void updateFoodSales() {
        log.info("开始更新商品销量统计");
        orderService.updateFoodSales();
    }

    /**
     * 每小时清理一次过期缓存
     */
    @Scheduled(cron = "0 0 */1 * * ?")
    public void cleanExpiredCache() {
        log.info("开始清理过期缓存");
        // 清理验证码缓存
        List<String> codeKeys = cacheUtils.keys("sms:code:*").stream().toList();
        cacheUtils.delete(codeKeys);

        // 清理发送记录缓存
        List<String> sendKeys = cacheUtils.keys("sms:last_send:*").stream().toList();
        cacheUtils.delete(sendKeys);
    }
} 