package com.orjrs.admin.service.goods.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.orjrs.admin.entity.goods.Goods;
import com.orjrs.admin.mapper.goods.GoodsMapper;
import com.orjrs.admin.service.goods.GoodsService;
import com.orjrs.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    private final RedisLockService redisLockService;
    private static final String STOCK_LOCK_PREFIX = "goods:stock:lock:";
    private static final long STOCK_LOCK_TIMEOUT = 5;

    @Override
    public IPage<Goods> page(int page, int size, Long categoryId, String name, Integer status) {
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(categoryId != null, Goods::getCategoryId, categoryId)
                .like(StringUtils.isNotBlank(name), Goods::getName, name)
                .eq(status != null, Goods::getStatus, status)
                .orderByDesc(Goods::getUpdateTime);
        
        return page(new Page<>(page, size), wrapper);
    }

    @Override
    public Goods getDetail(Long id) {
        Goods goods = getById(id);
        if (goods == null) {
            throw new BusinessException("商品不存在");
        }
        return goods;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveGoods(Goods goods) {
        // 检查商品名称是否重复
        checkNameUnique(goods);
        save(goods);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateGoods(Goods goods) {
        // 检查商品是否存在
        if (!updateById(goods)) {
            throw new BusinessException("商品不存在");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteGoods(Long id) {
        // 检查商品是否存在
        if (!removeById(id)) {
            throw new BusinessException("商品不存在");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatusBatch(Long[] ids, Integer status) {
        List<Goods> goodsList = new ArrayList<>();
        Arrays.stream(ids).forEach(id -> {
            Goods goods = new Goods();
            goods.setId(id);
            goods.setStatus(status);
            goodsList.add(goods);
        });
        updateBatchById(goodsList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStock(Long id, Integer stock) {
        Goods goods = new Goods();
        goods.setId(id);
        goods.setStock(stock);
        if (!updateById(goods)) {
            throw new BusinessException("商品不存在");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePrice(Long id, BigDecimal price) {
        Goods goods = new Goods();
        goods.setId(id);
        goods.setPrice(price);
        if (!updateById(goods)) {
            throw new BusinessException("商品不存在");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importGoods(MultipartFile file) {
        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            
            List<Goods> goodsList = new ArrayList<>();
            // 从第二行开始读取数据（第一行是表头）
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                Goods goods = new Goods();
                goods.setName(getCellValue(row.getCell(0)));
                goods.setCategoryId(Long.valueOf(getCellValue(row.getCell(1))));
                goods.setPrice(new BigDecimal(getCellValue(row.getCell(2))));
                goods.setStock(Integer.valueOf(getCellValue(row.getCell(3))));
                goods.setStatus(Integer.valueOf(getCellValue(row.getCell(4))));
                
                goodsList.add(goods);
            }
            
            saveBatch(goodsList);
            
        } catch (IOException e) {
            log.error("导入商品失败", e);
            throw new BusinessException("导入商品失败");
        }
    }

    @Override
    public void exportGoods(Long categoryId, String name, Integer status, HttpServletResponse response) {
        try {
            // 查询数据
            LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(categoryId != null, Goods::getCategoryId, categoryId)
                    .like(StringUtils.isNotBlank(name), Goods::getName, name)
                    .eq(status != null, Goods::getStatus, status);
            List<Goods> goodsList = list(wrapper);

            // 创建工作簿
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("商品列表");

            // 创建表头
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("商品名称");
            headerRow.createCell(1).setCellValue("分类ID");
            headerRow.createCell(2).setCellValue("价格");
            headerRow.createCell(3).setCellValue("库存");
            headerRow.createCell(4).setCellValue("状态");

            // 填充数据
            for (int i = 0; i < goodsList.size(); i++) {
                Goods goods = goodsList.get(i);
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(goods.getName());
                row.createCell(1).setCellValue(goods.getCategoryId());
                row.createCell(2).setCellValue(goods.getPrice().toString());
                row.createCell(3).setCellValue(goods.getStock());
                row.createCell(4).setCellValue(goods.getStatus());
            }

            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("商品列表", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 写入响应流
            workbook.write(response.getOutputStream());
            
        } catch (IOException e) {
            log.error("导出商品失败", e);
            throw new BusinessException("导出商品失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean preoccupyStock(Long id, Integer quantity) {
        String lockKey = STOCK_LOCK_PREFIX + id;
        try {
            // 获取分布式锁
            if (!redisLockService.tryLock(lockKey, STOCK_LOCK_TIMEOUT, TimeUnit.SECONDS)) {
                log.warn("获取商品库存锁失败, goodsId: {}", id);
                return false;
            }

            // 查询商品库存
            Goods goods = getById(id);
            if (goods == null) {
                throw new BusinessException("商品不存在");
            }

            // 检查库存是否充足
            if (goods.getStock() < quantity) {
                log.warn("商品库存不足, goodsId: {}, current: {}, required: {}", 
                    id, goods.getStock(), quantity);
                return false;
            }

            // 预扣库存
            goods.setStock(goods.getStock() - quantity);
            updateById(goods);
            
            return true;
        } finally {
            redisLockService.unlock(lockKey);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void releaseStock(Long id, Integer quantity) {
        String lockKey = STOCK_LOCK_PREFIX + id;
        try {
            if (!redisLockService.tryLock(lockKey, STOCK_LOCK_TIMEOUT, TimeUnit.SECONDS)) {
                log.error("获取商品库存锁失败, goodsId: {}", id);
                throw new BusinessException("系统繁忙，请稍后重试");
            }

            Goods goods = getById(id);
            if (goods == null) {
                throw new BusinessException("商品不存在");
            }

            // 恢复库存
            goods.setStock(goods.getStock() + quantity);
            updateById(goods);
            
            log.info("释放商品库存成功, goodsId: {}, quantity: {}", id, quantity);
        } finally {
            redisLockService.unlock(lockKey);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmStock(Long id, Integer quantity) {
        // 库存已经在预占时扣减，这里只需要记录日志
        log.info("确认商品库存扣减, goodsId: {}, quantity: {}", id, quantity);
    }

    private void checkNameUnique(Goods goods) {
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Goods::getName, goods.getName())
                .ne(goods.getId() != null, Goods::getId, goods.getId());
        if (count(wrapper) > 0) {
            throw new BusinessException("商品名称已存在");
        }
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }
} 