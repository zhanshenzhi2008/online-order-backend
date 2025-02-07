package com.orjrs.admin.service.goods;

import com.baomidou.mybatisplus.extension.service.IService;
import com.orjrs.admin.entity.goods.GoodsReview;

public interface GoodsReviewService extends IService<GoodsReview> {
    
    /**
     * 创建商品评价
     * @param review 评价信息
     * @return 评价ID
     */
    Long createReview(GoodsReview review);
    
    /**
     * 回复商品评价
     * @param reviewId 评价ID
     * @param replyContent 回复内容
     */
    void replyReview(Long reviewId, String replyContent);
    
    /**
     * 获取商品评价
     * @param goodsId 商品ID
     * @return 评价列表
     */
    java.util.List<GoodsReview> getGoodsReviews(Long goodsId);
    
    /**
     * 检查用户是否已评价订单
     * @param orderId 订单ID
     * @param userId 用户ID
     * @return 是否已评价
     */
    boolean hasReviewed(Long orderId, Long userId);
} 