package com.orjrs.miniapp.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel("退款申请参数")
public class RefundApplyDTO {

    @NotNull(message = "退款类型不能为空")
    @ApiModelProperty("退款类型：1-仅退款，2-退货退款")
    private Integer type;

    @NotNull(message = "退款原因不能为空")
    @ApiModelProperty("退款原因：1-商品质量问题，2-商品与描述不符，3-商品损坏，4-其他")
    private Integer reason;

    @ApiModelProperty("退款说明")
    private String description;

    @NotNull(message = "退款金额不能为空")
    @ApiModelProperty("退款金额")
    private BigDecimal amount;

    @ApiModelProperty("凭证图片")
    private List<String> images;

    @ApiModelProperty("联系人")
    private String contact;

    @ApiModelProperty("联系电话")
    private String phone;
} 