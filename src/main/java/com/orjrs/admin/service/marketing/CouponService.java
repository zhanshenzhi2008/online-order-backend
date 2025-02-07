package com.orjrs.admin.service.marketing;

import com.orjrs.admin.dto.marketing.CouponCreateDTO;
import com.orjrs.admin.dto.marketing.CouponUpdateDTO;
import com.orjrs.common.R;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CouponService {

    /**
     * 创建优惠券
     *
     * @param dto 优惠券创建参数
     * @return 优惠券ID
     */
    R<Long> create(CouponCreateDTO dto);

    /**
     * 更新优惠券
     *
     * @param id  优惠券ID
     * @param dto 优惠券更新参数
     * @return 是否成功
     */
    R<Boolean> update(Long id, CouponUpdateDTO dto);

    /**
     * 删除优惠券
     *
     * @param id 优惠券ID
     * @return 是否成功
     */
    R<Boolean> delete(Long id);

    /**
     * 获取优惠券详情
     *
     * @param id 优惠券ID
     * @return 优惠券详情
     */
    R<CouponVO> getById(Long id);

    /**
     * 分页查询优惠券列表
     *
     * @param pageable 分页参数
     * @param name     优惠券名称
     * @param type     优惠券类型
     * @param status   状态
     * @return 优惠券列表
     */
    R<Page<CouponVO>> page(Pageable pageable, String name, Integer type, Integer status);

    /**
     * 发放优惠券
     *
     * @param id 优惠券ID
     * @param memberIds 会员ID列表
     * @return 是否成功
     */
    R<Boolean> grant(Long id, List<Long> memberIds);

    /**
     * 作废优惠券
     *
     * @param id 优惠券ID
     * @return 是否成功
     */
    R<Boolean> invalid(Long id);

    /**
     * 批量删除优惠券
     *
     * @param ids 优惠券ID列表
     * @return 是否成功
     */
    R<Boolean> deleteBatch(List<Long> ids);

    /**
     * 批量作废优惠券
     *
     * @param ids 优惠券ID列表
     * @return 是否成功
     */
    R<Boolean> invalidBatch(List<Long> ids);
} 