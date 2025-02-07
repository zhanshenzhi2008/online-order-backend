package com.orjrs.admin.dto.marketing;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(description = "营销活动更新DTO")
public class ActivityUpdateDTO {

    @NotBlank(message = "活动名称不能为空")
    @ApiModelProperty(value = "活动名称", required = true)
    private String name;

    @NotNull(message = "活动类型不能为空")
    @ApiModelProperty(value = "活动类型：1-满减，2-折扣", required = true)
    private Integer type;

    @ApiModelProperty(value = "活动图片")
    private String image;

    @ApiModelProperty(value = "活动描述")
    private String description;

    @NotNull(message = "活动开始时间不能为空")
    @ApiModelProperty(value = "活动开始时间", required = true)
    private LocalDateTime startTime;

    @NotNull(message = "活动结束时间不能为空")
    @ApiModelProperty(value = "活动结束时间", required = true)
    private LocalDateTime endTime;

    @NotEmpty(message = "活动规则不能为空")
    @Valid
    @ApiModelProperty(value = "活动规则", required = true)
    private List<RuleDTO> rules;

    @NotEmpty(message = "活动商品不能为空")
    @Valid
    @ApiModelProperty(value = "活动商品", required = true)
    private List<ActivityFoodDTO> foods;

    @Data
    @ApiModel(description = "活动规则DTO")
    public static class RuleDTO {
        @NotNull(message = "规则类型不能为空")
        @ApiModelProperty(value = "规则类型：1-满减，2-折扣", required = true)
        private Integer type;

        @NotNull(message = "规则条件不能为空")
        @ApiModelProperty(value = "规则条件：满减-金额，折扣-无", required = true)
        private Integer condition;

        @NotNull(message = "规则值不能为空")
        @ApiModelProperty(value = "规则值：满减-减免金额，折扣-折扣率", required = true)
        private Integer value;
    }

    @Data
    @ApiModel(description = "活动商品DTO")
    public static class ActivityFoodDTO {
        @NotNull(message = "商品ID不能为空")
        @ApiModelProperty(value = "商品ID", required = true)
        private Long foodId;

        @ApiModelProperty(value = "商品原价")
        private Integer originalPrice;

        @ApiModelProperty(value = "商品活动价")
        private Integer activityPrice;
    }
} 