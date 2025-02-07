package com.orjrs.admin.service.marketing;

import com.orjrs.admin.dto.marketing.ActivityCreateDTO;
import com.orjrs.admin.dto.marketing.ActivityUpdateDTO;
import com.orjrs.common.R;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ActivityService {

    /**
     * 创建活动
     *
     * @param dto 活动创建参数
     * @return 活动ID
     */
    R<Long> create(ActivityCreateDTO dto);

    /**
     * 更新活动
     *
     * @param id  活动ID
     * @param dto 活动更新参数
     * @return 是否成功
     */
    R<Boolean> update(Long id, ActivityUpdateDTO dto);

    /**
     * 删除活动
     *
     * @param id 活动ID
     * @return 是否成功
     */
    R<Boolean> delete(Long id);

    /**
     * 获取活动详情
     *
     * @param id 活动ID
     * @return 活动详情
     */
    R<ActivityVO> getById(Long id);

    /**
     * 分页查询活动列表
     *
     * @param pageable 分页参数
     * @param name     活动名称
     * @param type     活动类型
     * @param status   状态
     * @return 活动列表
     */
    R<Page<ActivityVO>> page(Pageable pageable, String name, Integer type, Integer status);

    /**
     * 开始活动
     *
     * @param id 活动ID
     * @return 是否成功
     */
    R<Boolean> start(Long id);

    /**
     * 结束活动
     *
     * @param id 活动ID
     * @return 是否成功
     */
    R<Boolean> end(Long id);

    /**
     * 关闭活动
     *
     * @param id 活动ID
     * @return 是否成功
     */
    R<Boolean> close(Long id);

    /**
     * 批量删除活动
     *
     * @param ids 活动ID列表
     * @return 是否成功
     */
    R<Boolean> deleteBatch(List<Long> ids);

    /**
     * 批量开始活动
     *
     * @param ids 活动ID列表
     * @return 是否成功
     */
    R<Boolean> startBatch(List<Long> ids);

    /**
     * 批量结束活动
     *
     * @param ids 活动ID列表
     * @return 是否成功
     */
    R<Boolean> endBatch(List<Long> ids);

    /**
     * 批量关闭活动
     *
     * @param ids 活动ID列表
     * @return 是否成功
     */
    R<Boolean> closeBatch(List<Long> ids);
} 