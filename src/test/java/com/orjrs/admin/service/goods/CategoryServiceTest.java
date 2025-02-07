package com.orjrs.admin.service.goods;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.orjrs.BaseTest;
import com.orjrs.admin.entity.goods.Category;
import com.orjrs.common.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryServiceTest extends BaseTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    void testPage() {
        IPage<Category> page = categoryService.page(1, 10, null, null);
        assertNotNull(page);
        assertTrue(page.getTotal() > 0);
        assertEquals(9, page.getRecords().size());
    }

    @Test
    void testPageWithName() {
        IPage<Category> page = categoryService.page(1, 10, "主食", null);
        assertNotNull(page);
        assertTrue(page.getTotal() > 0);
        page.getRecords().forEach(category -> 
            assertTrue(category.getName().contains("主食"))
        );
    }

    @Test
    void testPageWithStatus() {
        IPage<Category> page = categoryService.page(1, 10, null, 1);
        assertNotNull(page);
        assertTrue(page.getTotal() > 0);
        page.getRecords().forEach(category -> 
            assertEquals(1, category.getStatus())
        );
    }

    @Test
    void testTree() {
        List<Category> tree = categoryService.tree();
        assertNotNull(tree);
        assertFalse(tree.isEmpty());
        
        // 验证顶级分类
        assertEquals(3, tree.size());
        tree.forEach(category -> 
            assertEquals(0L, category.getParentId())
        );
    }

    @Test
    void testCreateCategory() {
        Category category = new Category();
        category.setParentId(1L);
        category.setName("测试分类");
        category.setIcon("test");
        category.setSort(1);
        category.setStatus(1);

        categoryService.createCategory(category);
        assertNotNull(category.getId());

        Category saved = categoryService.getById(category.getId());
        assertNotNull(saved);
        assertEquals("测试分类", saved.getName());
    }

    @Test
    void testCreateCategoryWithDuplicateName() {
        Category category = new Category();
        category.setParentId(1L);
        category.setName("主食"); // 已存在的名称
        category.setIcon("test");
        category.setSort(1);
        category.setStatus(1);

        assertThrows(BusinessException.class, () -> 
            categoryService.createCategory(category)
        );
    }

    @Test
    void testUpdateCategory() {
        Category category = categoryService.getById(4L);
        assertNotNull(category);

        category.setName("更新后的分类");
        category.setIcon("updated");
        categoryService.updateCategory(category);

        Category updated = categoryService.getById(4L);
        assertEquals("更新后的分类", updated.getName());
        assertEquals("updated", updated.getIcon());
    }

    @Test
    void testUpdateCategoryWithInvalidParent() {
        Category category = categoryService.getById(1L);
        assertNotNull(category);

        category.setParentId(4L); // 设置子分类为父分类
        assertThrows(BusinessException.class, () -> 
            categoryService.updateCategory(category)
        );
    }

    @Test
    void testDeleteCategory() {
        // 删除没有子分类的分类
        categoryService.deleteCategory(4L);
        assertNull(categoryService.getById(4L));
    }

    @Test
    void testDeleteCategoryWithChildren() {
        // 尝试删除有子分类的分类
        assertThrows(BusinessException.class, () -> 
            categoryService.deleteCategory(1L)
        );
    }

    @Test
    void testUpdateStatus() {
        categoryService.updateStatus(4L, 0);
        Category category = categoryService.getById(4L);
        assertEquals(0, category.getStatus());
    }
} 