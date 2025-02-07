package com.orjrs.admin.service.goods;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.orjrs.admin.entity.goods.Category;

import java.util.List;

public interface CategoryService extends IService<Category> {
    
    /**
     * 分页查询分类列表
     */
    IPage<Category> page(int page, int size, String name, Integer status);
    
    /**
     * 获取分类树形结构
     */
    List<Category> tree();
    
    /**
     * 创建分类
     */
    void createCategory(Category category);
    
    /**
     * 更新分类
     */
    void updateCategory(Category category);
    
    /**
     * 删除分类
     */
    void deleteCategory(Long id);
    
    /**
     * 更新分类状态
     */
    void updateStatus(Long id, Integer status);
} 