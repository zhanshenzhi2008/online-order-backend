package com.orjrs.miniapp.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel("订单评价参数")
public class OrderCommentDTO {

    @NotEmpty(message = "评价内容不能为空")
    @ApiModelProperty("评价内容")
    private String content;

    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最小为1")
    @Max(value = 5, message = "评分最大为5")
    @ApiModelProperty("评分(1-5)")
    private Integer rating;

    @ApiModelProperty("评价图片")
    private List<String> images;

    @ApiModelProperty("是否匿名")
    private Boolean anonymous;

    @NotEmpty(message = "商品评价不能为空")
    @ApiModelProperty("商品评价")
    private List<FoodCommentDTO> foodComments;

    @Data
    @ApiModel("商品评价参数")
    public static class FoodCommentDTO {
        @NotNull(message = "商品ID不能为空")
        @ApiModelProperty("商品ID")
        private Long foodId;

        @NotEmpty(message = "评价内容不能为空")
        @ApiModelProperty("评价内容")
        private String content;

        @NotNull(message = "评分不能为空")
        @Min(value = 1, message = "评分最小为1")
        @Max(value = 5, message = "评分最大为5")
        @ApiModelProperty("评分(1-5)")
        private Integer rating;

        @ApiModelProperty("评价图片")
        private List<String> images;
    }
} 