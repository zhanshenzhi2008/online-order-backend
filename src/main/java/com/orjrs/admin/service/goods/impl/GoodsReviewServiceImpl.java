package com.orjrs.admin.service.goods.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.orjrs.admin.entity.goods.GoodsReview;
import com.orjrs.admin.mapper.goods.GoodsReviewMapper;
import com.orjrs.admin.service.goods.GoodsReviewService;
import com.orjrs.common.exception.BusinessException;
import com.orjrs.common.service.RedisLockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoodsReviewServiceImpl extends ServiceImpl<GoodsReviewMapper, GoodsReview> implements GoodsReviewService {

    private final RedisLockService redisLockService;
    private static final String REVIEW_LOCK_PREFIX = "goods:review:lock:";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createReview(GoodsReview review) {
        String lockKey = REVIEW_LOCK_PREFIX + review.getOrderId();
        try {
            if (!redisLockService.tryLock(lockKey, 5, TimeUnit.SECONDS)) {
                throw new BusinessException("获取评价锁失败");
            }

            // 检查是否已评价
            if (hasReviewed(review.getOrderId(), review.getUserId())) {
                throw new BusinessException("该订单已评价");
            }

            // 保存评价
            save(review);
            return review.getId();
        } finally {
            redisLockService.unlock(lockKey);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void replyReview(Long reviewId, String replyContent) {
        String lockKey = REVIEW_LOCK_PREFIX + reviewId;
        try {
            if (!redisLockService.tryLock(lockKey, 5, TimeUnit.SECONDS)) {
                throw new BusinessException("获取评价锁失败");
            }

            // 获取评价
            GoodsReview review = getById(reviewId);
            if (review == null) {
                throw new BusinessException("评价不存在");
            }

            // 更新回复
            review.setReplyContent(replyContent);
            review.setReplyTime(LocalDateTime.now());
            updateById(review);
        } finally {
            redisLockService.unlock(lockKey);
        }
    }

    @Override
    public List<GoodsReview> getGoodsReviews(Long goodsId) {
        return list(new LambdaQueryWrapper<GoodsReview>()
                .eq(GoodsReview::getGoodsId, goodsId)
                .orderByDesc(GoodsReview::getCreateTime));
    }

    @Override
    public boolean hasReviewed(Long orderId, Long userId) {
        return count(new LambdaQueryWrapper<GoodsReview>()
                .eq(GoodsReview::getOrderId, orderId)
                .eq(GoodsReview::getUserId, userId)) > 0;
    }
} 