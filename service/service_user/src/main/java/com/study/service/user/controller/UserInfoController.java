package com.study.service.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.study.common.constant.Result;
import com.study.common.model.dto.UserInfoDto;
import com.study.common.model.dto.UserPasswordDto;
import com.study.common.model.entity.SysRole;
import com.study.common.model.entity.SysRoleMenu;
import com.study.common.model.entity.SysUser;
import com.study.common.model.entity.SysUserRole;
import com.study.common.model.vo.UserInfoVo;
import com.study.common.utils.*;
import com.study.service.user.service.SysRoleMenuService;
import com.study.service.user.service.SysRoleService;
import com.study.service.user.service.SysUserRoleService;
import com.study.service.user.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@Api(tags = {"系统用户相关方法"})
public class UserInfoController {
    //创建 log 对象
    private static final Logger log = LoggerFactory.getLogger(UserInfoController.class);
    private static final String USER_TOKEN = "user_";
    private static final String USERINFO_TOKEN = "userInfo_";
    private static final String AUTHORIZE_TOKEN = "token";
    @Value("${RSA.privateKey}")
    private String privateKey;
    @Value("${RSA.publicKey}")
    private String publicKey;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private SysUserRoleService userRoleService;
    @Autowired
    private SysRoleService roleService;
    @Autowired
    private SysRoleMenuService roleMenuService;

    @RequestMapping(value = "/findSysUserPage",method = RequestMethod.GET)
    @ApiOperation(value = "查询系统用户分页信息")
    public IPage<UserInfoVo> findSysUserPage(int page, int rows){
        IPage<SysUser> sysUserIPage = userInfoService.page(new Page<>(page, rows), new QueryWrapper<>());
        IPage<UserInfoVo> userInfoVoPage = new Page<>();
        BeanUtils.copyProperties(sysUserIPage,userInfoVoPage);
        List<SysUser> list = sysUserIPage.getRecords();
        List<UserInfoVo> userInfoList=  list.stream().map(user -> {
            UserInfoVo userInfoVo = new UserInfoVo();
            QueryWrapper<SysUserRole>wrapper = new QueryWrapper<>();
            wrapper.eq("user_id",user.getUserId());
            List<SysUserRole>  sysUserRoleList= userRoleService.list(wrapper);
            //查询用户的角色
            List<SysRole> roleList = sysUserRoleList.stream().map(role -> {
                QueryWrapper<SysRole> roleWrapper = new QueryWrapper<>();
                roleWrapper.eq("role_id", role.getRoleId());
                return roleService.getOne(roleWrapper);
            }).collect(Collectors.toList());
            BeanUtils.copyProperties(user,userInfoVo);
            userInfoVo.setRoleList(roleList);
            //清空密码
            userInfoVo.setPassword(null);
            return userInfoVo;
        }).collect(Collectors.toList());
        userInfoVoPage.setRecords(userInfoList);
        return userInfoVoPage;
    }

    @RequestMapping(value = "/insertSysUser",method = RequestMethod.POST)
    @ApiOperation(value = "新增系统用户")
    @Transactional
    public Result insertSysUser(@RequestBody UserInfoDto userInfoDto){
        if(ObjectUtils.isEmpty(userInfoDto)){
            return Result.fail("参数异常");
        }
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userInfoDto,sysUser);
        sysUser.setUserId(RandomUtils.getUUID());
        String salt = userInfoDto.getSalt();
        if(StringUtils.isEmpty(salt)){
            sysUser.setPassword(MD5Utils.encodePassword(userInfoDto.getPassword()));
        }else{
            sysUser.setPassword(MD5Utils.encodePasswordSalt(userInfoDto.getPassword(),userInfoDto.getSalt()));
        }
        userInfoService.save(sysUser);
        String roleIds = userInfoDto.getRoleIds();
        String[] split = roleIds.split(",");
        for (String roleId:split) {
            QueryWrapper<SysUserRole> wrapper =new QueryWrapper<>();
            wrapper.eq("user_id",sysUser.getUserId());
            wrapper.eq("role_id",roleId);
            SysUserRole one = userRoleService.getOne(wrapper);
            if(ObjectUtils.isEmpty(one)){
                SysUserRole userRole=new SysUserRole();
                userRole.setUserId(sysUser.getUserId());
                userRole.setRoleId(roleId);
                userRoleService.save(userRole);
            }
        }
        return Result.ok("新增系统用户成功");
    }

    @RequestMapping(value = "/updateSysUser",method = RequestMethod.PUT)
    @ApiOperation(value = "修改系统用户信息")
    @Transactional
    //@CacheEvict(key ="'userInfo_'+#userInfoDto.getUserId()",cacheNames="userInfoCash",allEntries=true)
    public Result updateSysUser(@RequestBody UserInfoDto userInfoDto){
        if(ObjectUtils.isEmpty(userInfoDto)){
            return Result.fail("参数异常");
        }
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userInfoDto,sysUser);
        userInfoService.updateById(sysUser);
        String roleIds = userInfoDto.getRoleIds();
        String[] split = roleIds.split(",");
        QueryWrapper<SysUserRole> wrapper =new QueryWrapper<>();
        wrapper.eq("user_id",sysUser.getUserId());
        List<SysUserRole> list = userRoleService.list(wrapper);
        List<String> roleIdList = list.stream().map(sysUserRole -> sysUserRole.getUserRoleId()).collect(Collectors.toList());
        userRoleService.removeByIds(roleIdList);
        for (String roleId:split) {
                SysUserRole userRole=new SysUserRole();
                userRole.setUserId(sysUser.getUserId());
                userRole.setRoleId(roleId);
                userRoleService.save(userRole);
        }
        UserInfoVo userInfoVo = userInfoService.getUserPermissionByUserId(userInfoDto.getUserId());
        redisTemplate.opsForValue().set(USERINFO_TOKEN+userInfoDto.getUserId(),JSONObject.toJSONString(userInfoVo),2,TimeUnit.HOURS);
        return Result.ok(userInfoVo);
    }

    @RequestMapping(value = "/insertSysRole",method = RequestMethod.POST)
    @ApiOperation(value = "新增系统角色")
    public Result insertSysRole(@RequestBody SysRole role){
        if(ObjectUtils.isEmpty(role)){
            return Result.fail("参数异常");
        }
        boolean save = roleService.save(role);
        return save?Result.ok("新增系统角色成功"):Result.fail("新增系统角色失败");
    }

    @RequestMapping(value = "/roleBindSysPermission",method = RequestMethod.POST)
    @ApiOperation(value = "角色关联权限")
    public Result roleBindSysPermission(@RequestBody SysRoleMenu roleMenu){
        if(ObjectUtils.isEmpty(roleMenu)|| StringUtils.isEmpty(roleMenu.getMenuId())){
            return Result.fail("参数异常");
        }
        String[] split = roleMenu.getMenuId().split(",");
        for (String menuId:split) {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            QueryWrapper<SysRoleMenu> wrapper =new QueryWrapper<>();
            wrapper.eq("role_id",roleMenu.getRoleId());
            wrapper.eq("menu_id",menuId);
            SysRoleMenu one = roleMenuService.getOne(wrapper);
            if(ObjectUtils.isEmpty(one)){
                sysRoleMenu.setRoleId(roleMenu.getRoleId());
                sysRoleMenu.setMenuId(menuId);
                roleMenuService.save(sysRoleMenu);
            }
        }
        return  Result.ok("角色关联权限成功");
    }

    @ApiOperation(value = "生成token测试")
    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public Result test(){
        String token = JwtUtils.createToken("b3ed8f57cd8343e88e00ba6d6f2026e6", "管理员");
        return  Result.ok(token);
    }

    @ApiOperation(value = "测试公钥加密密码")
    @RequestMapping(value = "/testPublicKeyPassword",method = RequestMethod.GET)
    public Result testPublicKeyPassword(String password){
        String encrypt = RSAEncryUtils.encrypt(password, publicKey.trim());
        return  Result.ok(encrypt);
    }

    @ApiOperation(value = "根据用户id获取用户的权限")
    @RequestMapping(value = "api/getUserPermissionByUserId/{userId}",method = RequestMethod.GET)
    //@Cacheable(key ="'userInfo_'+#userId",cacheNames="userInfoCash")
    public UserInfoVo getUserPermissionByUserId(@PathVariable("userId")String userId){
        UserInfoVo userInfoVo = userInfoService.getUserPermissionByUserId(userId);
        return  userInfoVo;
    }

    //登录界面获取图形验证码
    @RequestMapping(value = "/verifyCode.jpg",method = RequestMethod.GET)
    @ApiOperation(value = "图片验证码")
    public void verifyCode(HttpServletRequest  request,HttpServletResponse   response) {
        /*禁止缓存*/
        response.setDateHeader("Expires",0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        /*获取验证码*/
        String code = VerifyCodeUtils.generateVerifyCode(4);
        /*验证码已key，value的形式缓存到redis 存放时间一分钟*/
        log.info("验证码============>" + code);
        String uuid = RandomUtils.getUUID();
        try {
            redisTemplate.opsForValue().set(uuid,code,1, TimeUnit.MINUTES);
            Cookie cookie = new Cookie("captcha",uuid);
            /*key写入cookie，验证时获取*/
            response.addCookie(cookie);
            ServletOutputStream outputStream = response.getOutputStream();
            //ImageIO.write(bufferedImage,"jpg",outputStream);
            VerifyCodeUtils.outputImage(110,40,outputStream,code);
            outputStream.flush();
            outputStream.close();
        }catch (IOException e){
            log.error("生成图片验证异常============>" );
            log.error(e.getMessage());
        }
    }

    //登录接口(密码前端用RSA非对称加密处理下)
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @Transactional
    public  Result  sysLogin(@RequestBody  UserInfoDto user){
        if(ObjectUtils.isEmpty(user)||StringUtils.isEmpty(user.getLoginAccount())
                ||StringUtils.isEmpty(user.getPassword())
                ||StringUtils.isEmpty(user.getVerifyCode())
                ||StringUtils.isEmpty(user.getVerifyCodeUuid())){
            return Result.fail("参数异常");
        }
        String verifyCode = redisTemplate.opsForValue().get(user.getVerifyCodeUuid());
        if(!user.getVerifyCode().equals(verifyCode)){
            return Result.fail("验证码错误");
        }
        //校验用户账号
        QueryWrapper<SysUser> userWrapper = new QueryWrapper<>();
        userWrapper.eq("login_account",user.getLoginAccount());
        SysUser one = userInfoService.getOne(userWrapper);
        if(ObjectUtils.isEmpty(one)){
            return Result.fail("账号不存在");
        }
        try {
            if(!StringUtils.isEmpty(one.getLastLoginDate())){
                if("0".equals(one.getStatus())||(DateUtils.conpareTwoDate(one.getLastLoginDate(),new Date(),"yyyy-MM-dd")&&one.getLoginErrorTimes()>=3)){
                    return Result.fail("登录错误次数过多请稍后再试");
                }
            }
        } catch (ParseException e) {
            return Result.fail("日期转换错误");
        }
        //校验密码
       String password= RSAEncryUtils.decrypt(user.getPassword(),privateKey.trim());//得到了明文密码
        if(!StringUtils.isEmpty(one.getSalt())){
            password =MD5Utils.encodePasswordSalt(password,one.getSalt());
        }else{
            password = MD5Utils.encodePassword(password);
        }
        if(!one.getPassword().equals(password)){
            one.setLastLoginDate(new Date());
            one.setLoginErrorTimes(one.getLoginErrorTimes()+1);
            userInfoService.updateById(one);
            return Result.fail("密码不正确");
        }
        //登录成功
        String token = redisTemplate.opsForValue().get(USER_TOKEN + one.getUserId());
        if(StringUtils.isEmpty(token)){
            token = JwtUtils.createToken(one.getUserId(),one.getLoginAccount());
        }
        redisTemplate.opsForValue().set(USER_TOKEN + one.getUserId(),token,2,TimeUnit.HOURS);
        //清除用户登录错误次数
        one.setLastLoginDate(new Date());
        one.setLoginErrorTimes(0);
        userInfoService.updateById(one);
        //获取用户信息数据储存到redis中
        UserInfoVo userInfo = userInfoService.getUserPermissionByUserId(one.getUserId());
        redisTemplate.opsForValue().set(USERINFO_TOKEN + one.getUserId(), JSONObject.toJSONString(userInfo), 2, TimeUnit.HOURS);
        return  Result.ok("登录成功");
    }

    @ApiOperation(value = "用户注销登录")
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public Result logout(HttpServletRequest request){
        String token = request.getHeader(AUTHORIZE_TOKEN);
        String userId = JwtUtils.getUserId(token);
         redisTemplate.delete(USER_TOKEN + userId);
         redisTemplate.delete(USERINFO_TOKEN + userId);
        return  Result.ok("退出成功");
    }
    //用户的旧密码和新密码都是 rsa 加密后的
    @ApiOperation(value = "修改用户密码")
    @RequestMapping(value = "/updatePassword",method = RequestMethod.POST)
    public Result updatePassword(@RequestBody UserPasswordDto user){
        if(ObjectUtils.isEmpty(user)){
            return Result.fail("参数异常");
        }
        //校验用户账号
        QueryWrapper<SysUser> userWrapper = new QueryWrapper<>();
        userWrapper.eq("login_account",user.getLoginAccount());
        SysUser one = userInfoService.getOne(userWrapper);
        if(ObjectUtils.isEmpty(one)){
            return Result.fail("账号不存在");
        }
        //校验密码
        String password= RSAEncryUtils.decrypt(user.getOldPassword(),privateKey.trim());//得到了明文密码
        if(!StringUtils.isEmpty(one.getSalt())){
            password =MD5Utils.encodePasswordSalt(password,one.getSalt());
        }else{
            password = MD5Utils.encodePassword(password);
        }
        if(!one.getPassword().equals(password)){
            return Result.fail("密码输入错误");
        }
        //得到了明文密码
        String newPassword= RSAEncryUtils.decrypt(user.getNewPassword(),privateKey.trim());
        if(!StringUtils.isEmpty(one.getSalt())){
            one.setPassword(MD5Utils.encodePasswordSalt(newPassword,one.getSalt()));
        }else{
            one.setPassword(MD5Utils.encodePassword(newPassword));
        }
        one.setUpdatePwdTime(new Date());
        //删除 redis 的token  强制要求用户 重新登录
        redisTemplate.delete(USER_TOKEN + user.getUserId());
        return  Result.ok("修改密码成功");
    }
}
