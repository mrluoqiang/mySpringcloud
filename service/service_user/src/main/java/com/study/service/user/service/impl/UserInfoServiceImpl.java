package com.study.service.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.common.model.entity.SysMenu;
import com.study.common.model.entity.SysRole;
import com.study.common.model.entity.SysUser;
import com.study.common.model.vo.UserInfoVo;
import com.study.common.utils.JsonObjectUtils;
import com.study.service.user.mapper.UserInfoMapper;
import com.study.service.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, SysUser> implements UserInfoService {
    @Autowired
    private  UserInfoMapper userInfoMapper;

    @Override
    public UserInfoVo getUserPermissionByUserId(String userId) {
        List<Map> resultList = userInfoMapper.getUserPermissionByUserId(userId);
        UserInfoVo userInfoVo = null;
       if(!ObjectUtils.isEmpty(resultList)&&resultList.size()>0){
           //resultList.stream().map()
            userInfoVo= JSON.parseObject(JSON.toJSONString(resultList.get(0)),UserInfoVo.class);
                   //针对  list<Map> 数据中对role_id分组
          /* Map<String, List<Map>> listMap = resultList.stream().collect(Collectors.groupingBy(map -> map.get("role_id").toString()));
           System.out.println("分组后数据-----");
           System.out.println(listMap.toString());*/
           //对role_id去重
           List<Map> roleMapList = resultList.stream()
                   .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(map -> map.get("role_id").toString()))), ArrayList::new));
           //得到去重后数据 转成 list<SysRole>
           List<SysRole> roleList = roleMapList.stream().map(map -> {
               SysRole sysRole = JSON.parseObject(JSON.toJSONString(map), SysRole.class);
               return sysRole;
           }).collect(Collectors.toList());
           userInfoVo.setRoleList(roleList);
           //把 List<Map>转成 list<SysMenu>
           List<SysMenu> permissionList = resultList.stream().map(map -> {
               SysMenu menu = JSON.parseObject(JSON.toJSONString(map), SysMenu.class);
               return menu;
           }).collect(Collectors.toList());
           //list<SysMenu> 再去重
           permissionList =permissionList.stream() .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(SysMenu::getMenuId))), ArrayList::new));
           userInfoVo.setPermissionList(permissionList);
       }
        return userInfoVo;
    }
}
