package com.study.feign.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.common.model.entity.SysUser;
import com.study.common.model.vo.UserInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 用戶系统相关提供的feign接口
 */
@FeignClient("service-user")
public interface UserFeignService{

    //提供一个 根据用户id查询出 该用户拥有的权限方法
    @GetMapping("/user/api/getUserPermissionByUserId/{userId}")
     public UserInfoVo getUserPermissionByUserId(@PathVariable("userId")String userId);
}
