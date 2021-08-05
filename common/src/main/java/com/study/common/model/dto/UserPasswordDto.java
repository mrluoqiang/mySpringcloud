package com.study.common.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * <p>
 * 用户修改密码
 * </p>
 *
 */
@Data
public class UserPasswordDto {
    /**
     * id
     */
    @NotNull
    private String userId;

    /**
     * 登录账号
     */
    @ApiModelProperty(value = "登录账号")
    @NotNull
    private String loginAccount;


    /**
     * 旧密码
     */
    @ApiModelProperty(value = "旧密码")
    @NotNull
    private String oldPassword;

    /**
     * 新密码
     */
    @ApiModelProperty(value = "新密码")
    @NotNull
    private String newPassword;
}
