package com.orjrs.admin.service.goods;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.orjrs.BaseTest;
import com.orjrs.admin.entity.goods.Goods;
import com.orjrs.common.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class GoodsServiceTest extends BaseTest {

    @Autowired
    private GoodsService goodsService;

    @Test
    void testPage() {
        IPage<Goods> page = goodsService.page(1, 10, null, null, null);
        assertNotNull(page);
        assertTrue(page.getTotal() > 0);
        assertEquals(5, page.getRecords().size());
    }

    @Test
    void testGetDetail() {
        Goods goods = goodsService.getDetail(1L);
        assertNotNull(goods);
        assertEquals("白米饭", goods.getName());
        assertEquals(new BigDecimal("2.00"), goods.getPrice());
    }

    @Test
    void testGetDetailNotFound() {
        assertThrows(BusinessException.class, () -> goodsService.getDetail(999L));
    }

    @Test
    void testSaveGoods() {
        Goods goods = new Goods();
        goods.setShopId(1L);
        goods.setCategoryId(4L);
        goods.setName("测试商品");
        goods.setOriginalPrice(new BigDecimal("10.00"));
        goods.setPrice(new BigDecimal("8.00"));
        goods.setStock(100);

        goodsService.saveGoods(goods);
        assertNotNull(goods.getId());

        Goods saved = goodsService.getById(goods.getId());
        assertNotNull(saved);
        assertEquals("测试商品", saved.getName());
    }

    @Test
    void testUpdateGoods() {
        Goods goods = goodsService.getById(1L);
        assertNotNull(goods);

        goods.setName("更新后的商品");
        goods.setPrice(new BigDecimal("3.00"));
        goodsService.updateGoods(goods);

        Goods updated = goodsService.getById(1L);
        assertEquals("更新后的商品", updated.getName());
        assertEquals(new BigDecimal("3.00"), updated.getPrice());
    }

    @Test
    void testDeleteGoods() {
        goodsService.deleteGoods(1L);
        assertThrows(BusinessException.class, () -> goodsService.getDetail(1L));
    }

    @Test
    void testUpdateStatusBatch() {
        Long[] ids = {1L, 2L};
        goodsService.updateStatusBatch(ids, 0);

        Goods goods1 = goodsService.getById(1L);
        Goods goods2 = goodsService.getById(2L);
        assertEquals(0, goods1.getStatus());
        assertEquals(0, goods2.getStatus());
    }

    @Test
    void testUpdateStock() {
        goodsService.updateStock(1L, 50);
        Goods goods = goodsService.getById(1L);
        assertEquals(50, goods.getStock());
    }

    @Test
    void testUpdatePrice() {
        goodsService.updatePrice(1L, new BigDecimal("5.00"));
        Goods goods = goodsService.getById(1L);
        assertEquals(new BigDecimal("5.00"), goods.getPrice());
    }
} 