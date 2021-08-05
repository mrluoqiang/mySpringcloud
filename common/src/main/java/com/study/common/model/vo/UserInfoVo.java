package com.study.common.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.study.common.model.entity.SysMenu;
import com.study.common.model.entity.SysRole;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户表
 * </p>
 */
@Data
public class UserInfoVo implements Serializable {
    /**
     * id
     */

    private String userId;

    /**
     * 登录账号
     */
    @ApiModelProperty(value = "登录账号")
    private String loginAccount;

    /**
     * 真实姓名
     */
    @ApiModelProperty(value = "真实姓名")
    private String realName;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * md5密码盐
     */
    @ApiModelProperty(value = "md5密码盐")
    private String salt;

    /**
     * 性别（1：男 2：女）
     */
    @ApiModelProperty(value = "性别（1：男 2：女）")
    private String sex;

    /**
     * 电子邮件
     */
    @ApiModelProperty(value = "电子邮件")
    private String email;

    /**
     * 电话
     */
    @ApiModelProperty(value = "电话")
    private String phone;

    /**
     * 状态(1：正常  2：冻结 ）
     */
    @ApiModelProperty(value = "状态(1：正常  2：冻结 ）")
    private String status;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 修改密码时间
     */
    @ApiModelProperty(value = "修改密码时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatePwdTime;

    //用户登录错误次数，超过一定次数则锁定账户
    @ApiModelProperty(value = "用户登录错误次数")
    private Integer loginErrorTimes;

    //用户拥有的角色
    private List<SysRole> RoleList;

    //用户拥有的权限
    private List<SysMenu> permissionList;
}
