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
@ApiModel("优惠券更新参数")
public class CouponUpdateDTO {

    @NotBlank(message = "优惠券名称不能为空")
    @ApiModelProperty("优惠券名称")
    private String name;

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