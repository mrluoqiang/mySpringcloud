package com.study.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 菜单表
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_menu")
@ApiModel("菜单表")
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    @TableId(type=IdType.UUID)
    @ApiModelProperty("菜单ID")
    private String menuId;

    /**
     * 菜单名称
     */
    @ApiModelProperty("菜单名称")
    private String menuName;

    /**
     * 菜单连接地址
     */
    @ApiModelProperty("菜单连接地址")
    private String url;

    /**
     * 父菜单
     */
    @ApiModelProperty("父菜单")
    private String parentId;

    /**
     * 排序码
     */
    @ApiModelProperty("排序码")
    private Integer seq;

    /**
     * 是否启用
     */
    @ApiModelProperty("是否启用")
    private Boolean valid;
    /**
     * 图标样式
     */
    @ApiModelProperty("图标样式")
    private String iconClassName;
}
