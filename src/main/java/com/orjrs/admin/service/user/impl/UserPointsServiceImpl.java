package com.orjrs.admin.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.orjrs.admin.entity.user.UserPoints;
import com.orjrs.admin.mapper.user.UserPointsMapper;
import com.orjrs.admin.service.user.UserPointsService;
import com.orjrs.common.exception.BusinessException;
import com.orjrs.common.service.RedisLockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserPointsServiceImpl extends ServiceImpl<UserPointsMapper, UserPoints> implements UserPointsService {

    private final RedisLockService redisLockService;
    private static final String POINTS_LOCK_PREFIX = "user:points:lock:";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer increasePoints(Long userId, Integer points, String description) {
        String lockKey = POINTS_LOCK_PREFIX + userId;
        try {
            if (!redisLockService.tryLock(lockKey, 5, TimeUnit.SECONDS)) {
                throw new BusinessException("获取积分锁失败");
            }

            // 获取用户当前积分记录
            UserPoints userPoints = getLatestUserPoints(userId);
            if (userPoints == null) {
                userPoints = new UserPoints();
                userPoints.setUserId(userId);
                userPoints.setPoints(0);
            }

            // 增加积分
            Integer currentPoints = userPoints.getPoints() + points;
            userPoints.setPoints(currentPoints);
            userPoints.setType("increase");
            userPoints.setDescription(description);

            // 保存或更新
            saveOrUpdate(userPoints);

            return currentPoints;
        } finally {
            redisLockService.unlock(lockKey);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer decreasePoints(Long userId, Integer points, String description) {
        String lockKey = POINTS_LOCK_PREFIX + userId;
        try {
            if (!redisLockService.tryLock(lockKey, 5, TimeUnit.SECONDS)) {
                throw new BusinessException("获取积分锁失败");
            }

            // 获取用户当前积分记录
            UserPoints userPoints = getLatestUserPoints(userId);
            if (userPoints == null || userPoints.getPoints() < points) {
                throw new BusinessException("积分不足");
            }

            // 扣减积分
            Integer currentPoints = userPoints.getPoints() - points;
            userPoints.setPoints(currentPoints);
            userPoints.setType("decrease");
            userPoints.setDescription(description);

            // 保存或更新
            saveOrUpdate(userPoints);

            return currentPoints;
        } finally {
            redisLockService.unlock(lockKey);
        }
    }

    @Override
    public Integer getCurrentPoints(Long userId) {
        UserPoints userPoints = getLatestUserPoints(userId);
        return userPoints != null ? userPoints.getPoints() : 0;
    }

    private UserPoints getLatestUserPoints(Long userId) {
        return getOne(new LambdaQueryWrapper<UserPoints>()
                .eq(UserPoints::getUserId, userId)
                .orderByDesc(UserPoints::getCreateTime)
                .last("LIMIT 1"));
    }
} 