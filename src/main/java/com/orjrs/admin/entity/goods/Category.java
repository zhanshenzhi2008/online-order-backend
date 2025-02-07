package com.orjrs.admin.entity.goods;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@TableName("category")
@ApiModel(description = "商品分类")
public class Category {

    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty("分类ID")
    private Long id;

    @ApiModelProperty("父级ID")
    private Long parentId;

    @ApiModelProperty("分类名称")
    @NotBlank(message = "分类名称不能为空")
    private String name;

    @ApiModelProperty("分类图标")
    private String icon;

    @ApiModelProperty("排序")
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @ApiModelProperty("状态：0-禁用，1-启用")
    private Integer status;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    @ApiModelProperty("是否删除")
    private Boolean deleted;
} 