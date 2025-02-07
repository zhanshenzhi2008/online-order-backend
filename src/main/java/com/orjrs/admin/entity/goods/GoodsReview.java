package com.orjrs.admin.entity.goods;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("goods_review")
public class GoodsReview {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long goodsId;

    private Long userId;

    private Long orderId;

    private Integer rating;

    private String content;

    private String images;

    private Boolean isAnonymous;

    private String replyContent;

    private LocalDateTime replyTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Boolean deleted;
} 