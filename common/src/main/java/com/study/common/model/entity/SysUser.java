package com.study.common.model.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(description = "用户表")
@TableName("sys_user")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.UUID)
    private String userId;

    /**
     * 登录账号
     */
    @ApiModelProperty(value = "所属上级")
    @TableField("login_account")
    private String loginAccount;

    /**
     * 真实姓名
     */
    @ApiModelProperty(value = "真实姓名")
    private String realName;

    /**
     * 密码
     */
    private String password;

    /**
     * md5密码盐
     */
    private String salt;

    /**
     * 性别（1：男 2：女）
     */

    private String sex;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 电话
     */
    private String phone;

    /**
     * 状态(1：正常  2：冻结 ）
     */
    private String status;

//    /**
//     * 删除状态（0，正常，1已删除）
//     */
//    @Excel(name = "删除状态", width = 15,dicCode="del_flag")
//    @TableLogic
//    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(value = "create_by",fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新人
     */
    @TableField(value = "update_by",fill = FieldFill.UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time",fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 修改密码时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatePwdTime;
//    /**
//     * 同步工作流引擎1同步0不同步
//     */
//    private String activitiSync;
    //用户登录错误次数，超过一定次数则锁定账户
    private Integer loginErrorTimes;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date lastLoginDate;
}
