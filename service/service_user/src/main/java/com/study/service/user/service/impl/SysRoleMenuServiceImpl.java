package com.study.service.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.common.model.entity.SysRole;
import com.study.common.model.entity.SysRoleMenu;
import com.study.service.user.mapper.SysRoleMapper;
import com.study.service.user.mapper.SysRoleMenuMapper;
import com.study.service.user.service.SysRoleMenuService;
import com.study.service.user.service.SysRoleService;
import org.springframework.stereotype.Service;

@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {

}
