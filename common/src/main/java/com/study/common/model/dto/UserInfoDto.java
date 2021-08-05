package com.study.common.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户表
 * </p>
 */
@Data
public class UserInfoDto  {
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

//    /**
//     * 删除状态（0，正常，1已删除）
//     */
//    @Excel(name = "删除状态", width = 15,dicCode="del_flag")
//    @TableLogic
//    private Integer delFlag;

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

    //角色id
    private String roleIds;

    //验证码
    private String verifyCode;

    //登录人验证码的uuid
    private String verifyCodeUuid;

    //上次登录时间
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date lastLoginDate;
}
