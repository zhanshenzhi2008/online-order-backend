package com.orjrs.admin.entity.user;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user_points")
public class UserPoints {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long userId;

    private Integer points;

    private String description;

    private String type;  // increase, decrease

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Boolean deleted;
} 