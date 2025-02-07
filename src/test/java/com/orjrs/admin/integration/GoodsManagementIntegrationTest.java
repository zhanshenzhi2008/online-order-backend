package com.orjrs.admin.integration;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.orjrs.BaseTest;
import com.orjrs.admin.entity.goods.Category;
import com.orjrs.admin.entity.goods.Goods;
import com.orjrs.admin.service.goods.CategoryService;
import com.orjrs.admin.service.goods.GoodsService;
import com.orjrs.common.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GoodsManagementIntegrationTest extends BaseTest {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private CategoryService categoryService;

    @Test
    @Rollback(false)
    void testCompleteGoodsManagementFlow() {
        // 1. 创建商品分类
        Category category = new Category();
        category.setParentId(0L);
        category.setName("测试分类");
        category.setIcon("test-icon");
        category.setSort(1);
        category.setStatus(1);
        
        categoryService.createCategory(category);
        assertNotNull(category.getId());

        // 2. 验证分类树结构
        List<Category> tree = categoryService.tree();
        assertFalse(tree.isEmpty());
        assertTrue(tree.stream().anyMatch(c -> c.getName().equals("测试分类")));

        // 3. 创建商品
        Goods goods = new Goods();
        goods.setShopId(1L);
        goods.setCategoryId(category.getId());
        goods.setName("测试商品");
        goods.setDescription("测试商品描述");
        goods.setOriginalPrice(new BigDecimal("100.00"));
        goods.setPrice(new BigDecimal("88.00"));
        goods.setPackingFee(new BigDecimal("2.00"));
        goods.setStock(100);
        goods.setStatus(1);

        goodsService.saveGoods(goods);
        assertNotNull(goods.getId());

        // 4. 验证商品查询
        Goods savedGoods = goodsService.getDetail(goods.getId());
        assertEquals("测试商品", savedGoods.getName());
        assertEquals(new BigDecimal("88.00"), savedGoods.getPrice());

        // 5. 更新商品信息
        savedGoods.setPrice(new BigDecimal("90.00"));
        savedGoods.setStock(200);
        goodsService.updateGoods(savedGoods);

        // 6. 验证更新结果
        Goods updatedGoods = goodsService.getDetail(goods.getId());
        assertEquals(new BigDecimal("90.00"), updatedGoods.getPrice());
        assertEquals(200, updatedGoods.getStock());

        // 7. 测试商品分页查询
        IPage<Goods> page = goodsService.page(1, 10, category.getId(), "测试商品", 1);
        assertNotNull(page);
        assertTrue(page.getTotal() > 0);
        assertTrue(page.getRecords().stream().anyMatch(g -> g.getId().equals(goods.getId())));

        // 8. 测试商品上下架
        goodsService.updateStatusBatch(new Long[]{goods.getId()}, 0);
        Goods offlineGoods = goodsService.getDetail(goods.getId());
        assertEquals(0, offlineGoods.getStatus());

        // 9. 测试删除商品
        goodsService.deleteGoods(goods.getId());
        assertThrows(BusinessException.class, () -> goodsService.getDetail(goods.getId()));

        // 10. 测试删除分类
        categoryService.deleteCategory(category.getId());
        assertThrows(BusinessException.class, () -> categoryService.getById(category.getId()));
    }

    @Test
    void testGoodsValidation() {
        // 1. 测试创建商品时的必填字段验证
        Goods goods = new Goods();
        assertThrows(BusinessException.class, () -> goodsService.saveGoods(goods));

        // 2. 测试价格验证
        goods.setShopId(1L);
        goods.setCategoryId(1L);
        goods.setName("测试商品");
        goods.setOriginalPrice(new BigDecimal("-1.00"));
        goods.setPrice(new BigDecimal("0.00"));
        assertThrows(BusinessException.class, () -> goodsService.saveGoods(goods));

        // 3. 测试库存验证
        goods.setOriginalPrice(new BigDecimal("100.00"));
        goods.setPrice(new BigDecimal("88.00"));
        goods.setStock(-1);
        assertThrows(BusinessException.class, () -> goodsService.saveGoods(goods));
    }

    @Test
    void testCategoryValidation() {
        // 1. 测试创建分类时的必填字段验证
        Category category = new Category();
        assertThrows(BusinessException.class, () -> categoryService.createCategory(category));

        // 2. 测试重复名称验证
        category.setName("主食");
        category.setParentId(0L);
        category.setSort(1);
        assertThrows(BusinessException.class, () -> categoryService.createCategory(category));

        // 3. 测试父级分类验证
        category.setName("测试分类");
        category.setParentId(999L);
        assertThrows(BusinessException.class, () -> categoryService.createCategory(category));
    }
} 