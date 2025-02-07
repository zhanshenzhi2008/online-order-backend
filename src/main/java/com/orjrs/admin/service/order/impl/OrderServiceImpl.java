package com.orjrs.admin.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.orjrs.admin.entity.order.Order;
import com.orjrs.admin.entity.order.OrderRefund;
import com.orjrs.admin.mapper.order.OrderMapper;
import com.orjrs.admin.service.order.OrderService;
import com.orjrs.admin.service.order.OrderStatisticsService;
import com.orjrs.admin.service.goods.GoodsService;
import com.orjrs.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    private final OrderStatisticsService orderStatisticsService;
    private final GoodsService goodsService;

    @Override
    public IPage<Order> page(int page, int size, String status, Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(status), Order::getStatus, status)
                .eq(userId != null, Order::getUserId, userId)
                .between(startTime != null && endTime != null, Order::getCreateTime, startTime, endTime)
                .orderByDesc(Order::getCreateTime);
        
        return page(new Page<>(page, size), wrapper);
    }

    @Override
    public Order getDetail(Long id) {
        Order order = getById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long id, String reason) {
        Order order = getById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (!"pending".equals(order.getStatus())) {
            throw new BusinessException("订单状态不允许取消");
        }

        order.setStatus("cancelled");
        order.setRemark(reason);
        updateById(order);

        // 更新统计数据
        orderStatisticsService.updateStatistics(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refundOrder(Long id, String reason) {
        Order order = getById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (!"completed".equals(order.getStatus())) {
            throw new BusinessException("订单状态不允许退款");
        }

        order.setStatus("refunded");
        order.setRemark(reason);
        updateById(order);

        // 创建退款记录
        OrderRefund refund = new OrderRefund();
        refund.setOrderId(order.getId());
        refund.setAmount(order.getActualAmount());
        refund.setReason(reason);
        refund.setStatus("pending");
        // TODO: 保存退款记录

        // 更新统计数据
        orderStatisticsService.updateStatistics(order);
    }

    @Override
    public void exportOrders(String status, Long userId, LocalDateTime startTime, LocalDateTime endTime, HttpServletResponse response) {
        try {
            // 查询数据
            LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(StringUtils.isNotBlank(status), Order::getStatus, status)
                    .eq(userId != null, Order::getUserId, userId)
                    .between(startTime != null && endTime != null, Order::getCreateTime, startTime, endTime)
                    .orderByDesc(Order::getCreateTime);
            List<Order> orderList = list(wrapper);

            // 创建工作簿
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("订单列表");

            // 创建表头
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("订单编号");
            headerRow.createCell(1).setCellValue("用户ID");
            headerRow.createCell(2).setCellValue("订单状态");
            headerRow.createCell(3).setCellValue("实付金额");
            headerRow.createCell(4).setCellValue("创建时间");

            // 填充数据
            for (int i = 0; i < orderList.size(); i++) {
                Order order = orderList.get(i);
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(order.getOrderNo());
                row.createCell(1).setCellValue(order.getUserId());
                row.createCell(2).setCellValue(order.getStatus());
                row.createCell(3).setCellValue(order.getActualAmount().toString());
                row.createCell(4).setCellValue(order.getCreateTime().toString());
            }

            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("订单列表", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 写入响应流
            workbook.write(response.getOutputStream());
            
        } catch (IOException e) {
            log.error("导出订单失败", e);
            throw new BusinessException("导出订单失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createOrderWithStockCheck(Order order, Long goodsId, Integer quantity) {
        // 1. 预占库存
        if (!goodsService.preoccupyStock(goodsId, quantity)) {
            throw new BusinessException("商品库存不足");
        }

        try {
            // 2. 创建订单
            save(order);
            
            // 3. 确认库存扣减
            goodsService.confirmStock(goodsId, quantity);
            
            return order.getId();
        } catch (Exception e) {
            // 4. 发生异常时释放库存
            goodsService.releaseStock(goodsId, quantity);
            throw e;
        }
    }
} 