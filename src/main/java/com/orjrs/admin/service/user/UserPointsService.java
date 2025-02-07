package com.orjrs.admin.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.orjrs.admin.entity.user.UserPoints;

public interface UserPointsService extends IService<UserPoints> {
    
    /**
     * 增加用户积分
     * @param userId 用户ID
     * @param points 积分数量
     * @param description 描述
     * @return 更新后的积分
     */
    Integer increasePoints(Long userId, Integer points, String description);
    
    /**
     * 扣减用户积分
     * @param userId 用户ID
     * @param points 积分数量
     * @param description 描述
     * @return 更新后的积分
     */
    Integer decreasePoints(Long userId, Integer points, String description);
    
    /**
     * 获取用户当前积分
     * @param userId 用户ID
     * @return 当前积分
     */
    Integer getCurrentPoints(Long userId);
} 