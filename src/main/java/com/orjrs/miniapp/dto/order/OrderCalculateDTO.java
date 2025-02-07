package com.orjrs.miniapp.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel("订单金额计算参数")
public class OrderCalculateDTO {

    @NotEmpty(message = "商品不能为空")
    @ApiModelProperty("商品列表")
    private List<OrderItemDTO> items;

    @ApiModelProperty("优惠券ID")
    private Long couponId;

    @ApiModelProperty("收货地址ID")
    private Long addressId;

    @Data
    @ApiModel("订单商品参数")
    public static class OrderItemDTO {
        @NotNull(message = "商品ID不能为空")
        @ApiModelProperty("商品ID")
        private Long foodId;

        @NotNull(message = "商品数量不能为空")
        @ApiModelProperty("商品数量")
        private Integer quantity;

        @ApiModelProperty("规格ID")
        private Long specificationId;
    }
} 