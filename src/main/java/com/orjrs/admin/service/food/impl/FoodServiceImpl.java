package com.orjrs.admin.service.food.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.orjrs.admin.dto.food.FoodCreateDTO;
import com.orjrs.admin.dto.food.FoodUpdateDTO;
import com.orjrs.admin.entity.food.Food;
import com.orjrs.admin.entity.food.FoodAttribute;
import com.orjrs.admin.entity.food.FoodSpecification;
import com.orjrs.admin.mapper.food.FoodAttributeMapper;
import com.orjrs.admin.mapper.food.FoodMapper;
import com.orjrs.admin.mapper.food.FoodSpecificationMapper;
import com.orjrs.admin.service.food.CategoryService;
import com.orjrs.admin.service.food.FoodService;
import com.orjrs.admin.vo.food.FoodDetailVO;
import com.orjrs.admin.vo.food.FoodVO;
import com.orjrs.common.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品信息服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FoodServiceImpl extends ServiceImpl<FoodMapper, Food> implements FoodService {

    private final FoodSpecificationMapper specificationMapper;
    private final FoodAttributeMapper attributeMapper;
    private final CategoryService categoryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Long> create(FoodCreateDTO dto) {
        // TODO: 实现创建商品的逻辑
        return R.success(1L);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> update(Long id, FoodUpdateDTO dto) {
        // TODO: 实现更新商品的逻辑
        return R.success(true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> delete(Long id) {
        // TODO: 实现删除商品的逻辑
        return R.success(true);
    }

    @Override
    public R<FoodDetailVO> getById(Long id) {
        // TODO: 实现获取商品详情的逻辑
        return R.success(new FoodDetailVO());
    }

    @Override
    public R<Page<FoodVO>> page(Pageable pageable, String name, Long categoryId, Integer status) {
        // TODO: 实现分页查询商品列表的逻辑
        return R.success(Page.empty());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> enable(Long id) {
        // TODO: 实现上架商品的逻辑
        return R.success(true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> disable(Long id) {
        // TODO: 实现下架商品的逻辑
        return R.success(true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> deleteBatch(List<Long> ids) {
        // TODO: 实现批量删除商品的逻辑
        return R.success(true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> enableBatch(List<Long> ids) {
        // TODO: 实现批量上架商品的逻辑
        return R.success(true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> disableBatch(List<Long> ids) {
        // TODO: 实现批量下架商品的逻辑
        return R.success(true);
    }

    @Override
    public IPage<Food> page(int page, int size, Long categoryId, String name, Integer status) {
        LambdaQueryWrapper<Food> wrapper = new LambdaQueryWrapper<Food>()
                .eq(categoryId != null, Food::getCategoryId, categoryId)
                .like(StringUtils.isNotBlank(name), Food::getName, name)
                .eq(status != null, Food::getStatus, status)
                .orderByAsc(Food::getSort);

        return page(new Page<>(page, size), wrapper);
    }

    @Override
    public Food getDetail(Long id) {
        Food food = getById(id);
        if (food == null) {
            return null;
        }

        // 查询规格列表
        List<FoodSpecification> specifications = specificationMapper.selectByFoodId(id);
        food.setSpecifications(specifications);

        // 查询属性列表
        List<FoodAttribute> attributes = attributeMapper.selectByFoodId(id);
        food.setAttributes(attributes);

        return food;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveFood(Food food) {
        // 保存商品信息
        save(food);

        // 保存规格列表
        List<FoodSpecification> specifications = food.getSpecifications();
        if (specifications != null && !specifications.isEmpty()) {
            for (FoodSpecification specification : specifications) {
                specification.setFoodId(food.getId());
            }
            specificationMapper.insertBatch(specifications);
        }

        // 保存属性列表
        List<FoodAttribute> attributes = food.getAttributes();
        if (attributes != null && !attributes.isEmpty()) {
            for (FoodAttribute attribute : attributes) {
                attribute.setFoodId(food.getId());
            }
            attributeMapper.insertBatch(attributes);
        }

        // 更新分类商品数量
        categoryService.updateFoodCount(food.getCategoryId(), 1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFood(Food food) {
        Food oldFood = getById(food.getId());
        if (oldFood == null) {
            throw new RuntimeException("商品不存在");
        }

        // 更新商品信息
        updateById(food);

        // 删除旧的规格和属性
        specificationMapper.deleteByFoodId(food.getId());
        attributeMapper.deleteByFoodId(food.getId());

        // 保存新的规格列表
        List<FoodSpecification> specifications = food.getSpecifications();
        if (specifications != null && !specifications.isEmpty()) {
            for (FoodSpecification specification : specifications) {
                specification.setFoodId(food.getId());
            }
            specificationMapper.insertBatch(specifications);
        }

        // 保存新的属性列表
        List<FoodAttribute> attributes = food.getAttributes();
        if (attributes != null && !attributes.isEmpty()) {
            for (FoodAttribute attribute : attributes) {
                attribute.setFoodId(food.getId());
            }
            attributeMapper.insertBatch(attributes);
        }

        // 如果分类发生变化，更新分类商品数量
        if (!oldFood.getCategoryId().equals(food.getCategoryId())) {
            categoryService.updateFoodCount(oldFood.getCategoryId(), -1);
            categoryService.updateFoodCount(food.getCategoryId(), 1);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFood(Long id) {
        Food food = getById(id);
        if (food == null) {
            return;
        }

        // 删除商品信息
        removeById(id);

        // 删除规格和属性
        specificationMapper.deleteByFoodId(id);
        attributeMapper.deleteByFoodId(id);

        // 更新分类商品数量
        categoryService.updateFoodCount(food.getCategoryId(), -1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatusBatch(Long[] ids, Integer status) {
        baseMapper.updateStatusBatch(ids, status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSales(Long foodId, Integer count) {
        baseMapper.updateSales(foodId, count);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRating(Long foodId, BigDecimal rating) {
        baseMapper.updateRating(foodId, rating);
    }
} 