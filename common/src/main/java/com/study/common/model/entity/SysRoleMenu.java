package com.study.common.model.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色权限表
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(description = "角色权限表")
@TableName("sys_role_menu")
public class SysRoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.UUID)
    private String roleMenuId;
    
    /**
     * 角色id
     */
    private String roleId;

    /**
     * 权限id
     */
    private String menuId;


    public SysRoleMenu() {
   	}
       
   	public SysRoleMenu(String roleId, String menuId) {
   		this.roleId = roleId;
   		this.menuId = menuId;
   	}

}
