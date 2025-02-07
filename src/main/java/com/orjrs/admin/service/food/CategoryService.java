package com.orjrs.admin.service.food;

import com.baomidou.mybatisplus.extension.service.IService;
import com.orjrs.admin.entity.food.FoodCategory;
import com.orjrs.admin.dto.food.CategoryCreateDTO;
import com.orjrs.admin.dto.food.CategoryUpdateDTO;
import com.orjrs.common.R;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 商品分类服务接口
 */
public interface CategoryService extends IService<FoodCategory> {

    /**
     * 创建分类
     *
     * @param dto 分类创建参数
     * @return 分类ID
     */
    R<Long> create(CategoryCreateDTO dto);

    /**
     * 更新分类
     *
     * @param id  分类ID
     * @param dto 分类更新参数
     * @return 是否成功
     */
    R<Boolean> update(Long id, CategoryUpdateDTO dto);

    /**
     * 删除分类
     *
     * @param id 分类ID
     * @return 是否成功
     */
    R<Boolean> delete(Long id);

    /**
     * 获取分类详情
     *
     * @param id 分类ID
     * @return 分类详情
     */
    R<CategoryVO> getById(Long id);

    /**
     * 分页查询分类列表
     *
     * @param pageable 分页参数
     * @param name     分类名称
     * @param status   状态
     * @return 分类列表
     */
    R<Page<CategoryVO>> page(Pageable pageable, String name, Integer status);

    /**
     * 获取分类树形结构
     *
     * @return 分类树形结构
     */
    List<FoodCategory> tree();

    /**
     * 启用分类
     *
     * @param id 分类ID
     * @return 是否成功
     */
    R<Boolean> enable(Long id);

    /**
     * 禁用分类
     *
     * @param id 分类ID
     * @return 是否成功
     */
    R<Boolean> disable(Long id);

    /**
     * 批量删除分类
     *
     * @param ids 分类ID列表
     * @return 是否成功
     */
    R<Boolean> deleteBatch(List<Long> ids);

    /**
     * 批量启用分类
     *
     * @param ids 分类ID列表
     * @return 是否成功
     */
    R<Boolean> enableBatch(List<Long> ids);

    /**
     * 批量禁用分类
     *
     * @param ids 分类ID列表
     * @return 是否成功
     */
    R<Boolean> disableBatch(List<Long> ids);

    /**
     * 更新商品数量
     *
     * @param categoryId 分类ID
     * @param count 增加的数量，可以为负数
     */
    void updateFoodCount(Long categoryId, Integer count);

    /**
     * 批量更新状态
     *
     * @param ids ID列表
     * @param status 状态：0-禁用，1-启用
     */
    void updateStatusBatch(Long[] ids, Integer status);

    /**
     * 删除分类
     * 如果分类下有商品，则不允许删除
     *
     * @param id 分类ID
     */
    void deleteCategory(Long id);
} 