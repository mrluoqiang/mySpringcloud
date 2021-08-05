package com.study.service.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.common.model.entity.SysMenu;
import com.study.common.model.entity.SysRole;
import com.study.service.user.mapper.SysMenuMapper;
import com.study.service.user.mapper.SysRoleMapper;
import com.study.service.user.service.SysMenuService;
import com.study.service.user.service.SysRoleService;
import org.springframework.stereotype.Service;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

}
