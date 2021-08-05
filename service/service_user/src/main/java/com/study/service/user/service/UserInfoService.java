package com.study.service.user.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.study.common.model.entity.SysUser;
import com.study.common.model.vo.UserInfoVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 用戶接口
 */
public interface UserInfoService  extends IService<SysUser> {

    public UserInfoVo getUserPermissionByUserId(String userId);

}
