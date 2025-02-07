package com.orjrs.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Redis分布式锁服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RedisLockService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String LOCK_PREFIX = "lock:";

    /**
     * 尝试获取分布式锁
     *
     * @param key 锁的key
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return 是否获取成功
     */
    public boolean tryLock(String key, long timeout, TimeUnit unit) {
        String lockKey = LOCK_PREFIX + key;
        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, Thread.currentThread().getId(), timeout, unit);
        return Boolean.TRUE.equals(success);
    }

    /**
     * 释放分布式锁
     *
     * @param key 锁的key
     * @return 是否释放成功
     */
    public boolean unlock(String key) {
        String lockKey = LOCK_PREFIX + key;
        try {
            Object value = redisTemplate.opsForValue().get(lockKey);
            if (value != null && value.equals(Thread.currentThread().getId())) {
                return Boolean.TRUE.equals(redisTemplate.delete(lockKey));
            }
            return false;
        } catch (Exception e) {
            log.error("释放锁失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 检查锁是否存在
     *
     * @param key 锁的key
     * @return 锁是否存在
     */
    public boolean isLocked(String key) {
        String lockKey = LOCK_PREFIX + key;
        return Boolean.TRUE.equals(redisTemplate.hasKey(lockKey));
    }
} 