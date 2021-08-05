package com.study.service.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.common.model.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserInfoMapper extends BaseMapper<SysUser> {

    public List<Map> getUserPermissionByUserId(@Param("userId")String userId);
}
