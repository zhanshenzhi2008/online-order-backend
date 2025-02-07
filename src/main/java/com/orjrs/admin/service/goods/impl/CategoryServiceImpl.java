package com.orjrs.admin.service.goods.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.orjrs.admin.entity.goods.Category;
import com.orjrs.admin.mapper.goods.CategoryMapper;
import com.orjrs.admin.service.goods.CategoryService;
import com.orjrs.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public IPage<Category> page(int page, int size, String name, Integer status) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(name), Category::getName, name)
                .eq(status != null, Category::getStatus, status)
                .orderByAsc(Category::getSort);
        
        return page(new Page<>(page, size), wrapper);
    }

    @Override
    public List<Category> tree() {
        // 查询所有分类
        List<Category> allCategories = list(new LambdaQueryWrapper<Category>()
                .orderByAsc(Category::getSort));
        
        // 构建树形结构
        Map<Long, List<Category>> parentMap = allCategories.stream()
                .collect(Collectors.groupingBy(Category::getParentId));
        
        // 获取顶级分类
        List<Category> rootCategories = parentMap.getOrDefault(0L, new ArrayList<>());
        
        // 递归设置子分类
        rootCategories.forEach(root -> setChildren(root, parentMap));
        
        return rootCategories;
    }

    private void setChildren(Category parent, Map<Long, List<Category>> parentMap) {
        List<Category> children = parentMap.get(parent.getId());
        if (children != null) {
            children.forEach(child -> setChildren(child, parentMap));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createCategory(Category category) {
        // 检查分类名称是否重复
        checkNameUnique(category);
        save(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCategory(Category category) {
        // 检查分类是否存在
        Category existingCategory = getById(category.getId());
        if (existingCategory == null) {
            throw new BusinessException("分类不存在");
        }
        
        // 检查分类名称是否重复
        checkNameUnique(category);
        
        // 不能将分类的父级设置为自己或自己的子分类
        checkParentValid(category);
        
        updateById(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCategory(Long id) {
        // 检查是否存在子分类
        Long count = count(new LambdaQueryWrapper<Category>()
                .eq(Category::getParentId, id));
        if (count > 0) {
            throw new BusinessException("存在子分类，不能删除");
        }
        
        // 检查是否有商品使用该分类
        // TODO: 检查商品分类关联
        
        removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, Integer status) {
        Category category = new Category();
        category.setId(id);
        category.setStatus(status);
        updateById(category);
    }

    private void checkNameUnique(Category category) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getName, category.getName())
                .eq(Category::getParentId, category.getParentId())
                .ne(category.getId() != null, Category::getId, category.getId());
        if (count(wrapper) > 0) {
            throw new BusinessException("分类名称已存在");
        }
    }

    private void checkParentValid(Category category) {
        if (category.getParentId() == null || category.getParentId() == 0) {
            return;
        }
        
        if (category.getParentId().equals(category.getId())) {
            throw new BusinessException("父级分类不能是自己");
        }
        
        // 获取所有子分类ID
        List<Long> childIds = new ArrayList<>();
        getChildIds(category.getId(), childIds);
        
        if (childIds.contains(category.getParentId())) {
            throw new BusinessException("父级分类不能是自己的子分类");
        }
    }

    private void getChildIds(Long parentId, List<Long> childIds) {
        List<Category> children = list(new LambdaQueryWrapper<Category>()
                .eq(Category::getParentId, parentId));
        
        children.forEach(child -> {
            childIds.add(child.getId());
            getChildIds(child.getId(), childIds);
        });
    }
} 