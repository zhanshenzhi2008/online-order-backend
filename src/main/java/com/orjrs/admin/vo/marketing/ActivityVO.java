package com.orjrs.admin.vo.marketing;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel("营销活动信息")
public class ActivityVO {

    @ApiModelProperty("活动ID")
    private Long id;

    @ApiModelProperty("活动名称")
    private String name;

    @ApiModelProperty("活动类型：1-满减活动，2-折扣活动，3-秒杀活动，4-拼团活动")
    private Integer type;

    @ApiModelProperty("活动图片")
    private String image;

    @ApiModelProperty("活动描述")
    private String description;

    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("活动规则")
    private List<RuleVO> rules;

    @ApiModelProperty("活动商品")
    private List<ActivityFoodVO> foods;

    @ApiModelProperty("参与人数")
    private Integer joinCount;

    @ApiModelProperty("订单数量")
    private Integer orderCount;

    @ApiModelProperty("订单金额")
    private BigDecimal orderAmount;

    @ApiModelProperty("状态：0-未开始，1-进行中，2-已结束，3-已关闭")
    private Integer status;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @Data
    @ApiModel("活动规则信息")
    public static class RuleVO {
        @ApiModelProperty("规则ID")
        private Long id;

        @ApiModelProperty("满足金额")
        private BigDecimal meetAmount;

        @ApiModelProperty("优惠金额")
        private BigDecimal discountAmount;

        @ApiModelProperty("优惠折扣")
        private BigDecimal discountRate;
    }

    @Data
    @ApiModel("活动商品信息")
    public static class ActivityFoodVO {
        @ApiModelProperty("活动商品ID")
        private Long id;

        @ApiModelProperty("商品ID")
        private Long foodId;

        @ApiModelProperty("商品名称")
        private String foodName;

        @ApiModelProperty("商品图片")
        private String foodImage;

        @ApiModelProperty("商品原价")
        private BigDecimal originalPrice;

        @ApiModelProperty("活动价格")
        private BigDecimal activityPrice;

        @ApiModelProperty("活动库存")
        private Integer activityStock;

        @ApiModelProperty("已售数量")
        private Integer soldCount;

        @ApiModelProperty("每人限购数量")
        private Integer limitCount;

        @ApiModelProperty("排序")
        private Integer sort;
    }
} 