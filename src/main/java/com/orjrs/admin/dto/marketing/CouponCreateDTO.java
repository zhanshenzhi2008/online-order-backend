package com.orjrs.admin.dto.marketing;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel("优惠券创建参数")
public class CouponCreateDTO {

    @NotBlank(message = "优惠券名称不能为空")
    @ApiModelProperty("优惠券名称")
    private String name;

    @NotNull(message = "优惠券类型不能为空")
    @ApiModelProperty("优惠券类型：1-满减券，2-折扣券，3-无门槛券")
    private Integer type;

    @NotNull(message = "优惠券面值不能为空")
    @ApiModelProperty("优惠券面值")
    private BigDecimal value;

    @ApiModelProperty("使用门槛金额")
    private BigDecimal minAmount;

    @ApiModelProperty("每人限领数量")
    private Integer limitCount;

    @ApiModelProperty("发行数量")
    private Integer totalCount;

    @NotNull(message = "生效时间不能为空")
    @ApiModelProperty("生效时间")
    private LocalDateTime startTime;

    @NotNull(message = "失效时间不能为空")
    @ApiModelProperty("失效时间")
    private LocalDateTime endTime;

    @ApiModelProperty("使用说明")
    private String description;

    @ApiModelProperty("可用商品：0-全部商品，1-指定商品")
    private Integer goodsType;

    @ApiModelProperty("指定商品ID列表")
    private List<Long> foodIds;

    @ApiModelProperty("可用商品分类：0-全部分类，1-指定分类")
    private Integer categoryType;

    @ApiModelProperty("指定分类ID列表")
    private List<Long> categoryIds;

    @ApiModelProperty("状态：0-未开始，1-进行中，2-已结束，3-已关闭")
    private Integer status;
} 