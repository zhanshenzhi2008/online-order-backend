package com.orjrs.admin.service.food.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.orjrs.admin.entity.food.FoodCategory;
import com.orjrs.admin.mapper.food.FoodCategoryMapper;
import com.orjrs.admin.mapper.food.FoodMapper;
import com.orjrs.admin.service.food.CategoryService;
import com.orjrs.admin.vo.food.CategoryTreeVO;
import com.orjrs.admin.vo.food.CategoryVO;
import com.orjrs.common.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品分类服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<FoodCategoryMapper, FoodCategory> implements CategoryService {

    private final FoodMapper foodMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Long> create(CategoryCreateDTO dto) {
        // TODO: 实现创建分类的逻辑
        return R.success(1L);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> update(Long id, CategoryUpdateDTO dto) {
        // TODO: 实现更新分类的逻辑
        return R.success(true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> delete(Long id) {
        // TODO: 实现删除分类的逻辑
        return R.success(true);
    }

    @Override
    public R<CategoryVO> getById(Long id) {
        // TODO: 实现获取分类详情的逻辑
        return R.success(new CategoryVO());
    }

    @Override
    public R<Page<CategoryVO>> page(Pageable pageable, String name, Integer status) {
        // TODO: 实现分页查询分类列表的逻辑
        return R.success(Page.empty());
    }

    @Override
    public R<List<CategoryTreeVO>> tree() {
        // 查询所有分类
        List<FoodCategory> categories = list(new LambdaQueryWrapper<FoodCategory>()
                .orderByAsc(FoodCategory::getSort));

        // 构建树形结构
        Map<Long, List<FoodCategory>> parentMap = categories.stream()
                .collect(Collectors.groupingBy(FoodCategory::getParentId));

        // 递归设置子分类
        List<FoodCategory> tree = buildTree(0L, parentMap);

        // 转换为VO
        List<CategoryTreeVO> treeVOs = new ArrayList<>();
        for (FoodCategory category : tree) {
            CategoryTreeVO treeVO = new CategoryTreeVO();
            treeVO.setId(category.getId());
            treeVO.setName(category.getName());
            treeVO.setParentId(category.getParentId());
            treeVO.setChildren(new ArrayList<>());
            treeVOs.add(treeVO);
        }

        return R.success(treeVOs);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> enable(Long id) {
        // TODO: 实现启用分类的逻辑
        return R.success(true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> disable(Long id) {
        // TODO: 实现禁用分类的逻辑
        return R.success(true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> deleteBatch(List<Long> ids) {
        // TODO: 实现批量删除分类的逻辑
        return R.success(true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> enableBatch(List<Long> ids) {
        // TODO: 实现批量启用分类的逻辑
        return R.success(true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> disableBatch(List<Long> ids) {
        // TODO: 实现批量禁用分类的逻辑
        return R.success(true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFoodCount(Long categoryId, Integer count) {
        baseMapper.updateFoodCount(categoryId, count);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatusBatch(Long[] ids, Integer status) {
        baseMapper.updateStatusBatch(ids, status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCategory(Long id) {
        // 检查是否有子分类
        long subCount = count(new LambdaQueryWrapper<FoodCategory>()
                .eq(FoodCategory::getParentId, id));
        if (subCount > 0) {
            throw new RuntimeException("该分类下有子分类，不能删除");
        }

        // 检查分类下是否有商品
        int foodCount = foodMapper.countByCategory(id);
        if (foodCount > 0) {
            throw new RuntimeException("该分类下有商品，不能删除");
        }

        // 删除分类
        removeById(id);
    }

    /**
     * 递归构建树形结构
     *
     * @param parentId 父级ID
     * @param parentMap 父级ID与子分类的映射
     * @return 树形结构
     */
    private List<FoodCategory> buildTree(Long parentId, Map<Long, List<FoodCategory>> parentMap) {
        List<FoodCategory> children = parentMap.get(parentId);
        if (children == null) {
            return new ArrayList<>();
        }

        for (FoodCategory child : children) {
            List<FoodCategory> subChildren = buildTree(child.getId(), parentMap);
            child.setChildren(subChildren);
        }

        return children;
    }
} 