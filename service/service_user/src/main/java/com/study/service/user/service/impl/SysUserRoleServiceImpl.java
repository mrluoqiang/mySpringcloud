package com.study.service.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.common.model.entity.SysRole;
import com.study.common.model.entity.SysUserRole;
import com.study.service.user.mapper.SysRoleMapper;
import com.study.service.user.mapper.SysUserRoleMapper;
import com.study.service.user.service.SysRoleService;
import com.study.service.user.service.SysUserRoleService;
import org.springframework.stereotype.Service;

@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

}
