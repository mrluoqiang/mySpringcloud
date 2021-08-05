package com.study.gatway.filter;

import com.alibaba.fastjson.JSONObject;
import com.study.common.constant.Result;
import com.study.common.constant.ResultCodeEnum;
import com.study.common.model.entity.SysMenu;
import com.study.common.model.vo.UserInfoVo;
import com.study.common.utils.JwtUtils;
import com.study.feign.user.UserFeignService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 全局Filter，统一处理校验token登录
 * </p>
 *
 * @author qy
 * @since 2019-11-21
 */
@Component
public class TokenGlobalFilter implements GlobalFilter, Ordered {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private UserFeignService userFeignService;

    private static final String AUTHORIZE_TOKEN = "token";

    private static final String USER_TOKEN = "user_";

    private static final String USERINFO_TOKEN = "userInfo_";

    private AntPathMatcher antPathMatcher = new AntPathMatcher();
    //创建 log 对象
    private static final Logger log = LoggerFactory.getLogger(TokenGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        log.info("请求的路径==="+path);
        //内部服务接口，不允许外部访问(可以后面自己调整哪些接口)
//        if(antPathMatcher.match("/**/inner/**", path)) {
//            ServerHttpResponse response = exchange.getResponse();
//            return out(response, ResultCodeEnum.PERMISSION);
//        }
        //如果是登录请求则放行
        if (request.getURI().getPath().contains("/user/login")||request.getURI().getPath().contains("/user/verifyCode.jpg")) {
            return chain.filter(exchange);
        }
        String token = request.getHeaders().getFirst(AUTHORIZE_TOKEN);
            //如果 token 已经失效
            try{
                Claims claim = JwtUtils.getClaim(token);
            }catch (ExpiredJwtException e){
                ServerHttpResponse response = exchange.getResponse();
                return out(response, ResultCodeEnum.TOKEN_EXPIRE);
            }
        //token 未失效但却被用户主动注销--redis设置一个token黑名单,注销过后的黑名单--现在有白名单好像就不需要黑名单了
        /*List<String> forbidTokenList = redisTemplate.opsForList().range(FORBID_TOKEN, 0, -1);
            if(!ObjectUtils.isEmpty(forbidTokenList)&&forbidTokenList.contains(token)){
                ServerHttpResponse response = exchange.getResponse();
                return out(response, ResultCodeEnum.TOKEN_FORBID);
            }*/
        String userId = this.getUserId(request);
        //判断 请求头里的token  是否是 用户对用的最新token(而不是用户注销前的旧token)
        String userToken = redisTemplate.opsForValue().get(USER_TOKEN + userId);
        if(StringUtils.isEmpty(userToken)||!token.equals(userToken)){
            ServerHttpResponse response = exchange.getResponse();
            return out(response, ResultCodeEnum.TOKEN_FORBID);
        }
        //api接口，异步请求，必须认证token
        if(antPathMatcher.match("/user/**", path)) {
            if(StringUtils.isEmpty(userId)) {
                ServerHttpResponse response = exchange.getResponse();
                return out(response, ResultCodeEnum.LOGIN_AUTH);
            }
            UserInfoVo userinfo=null;
            //先判断redis 中存不存在该userid的信息
            String userInfoJson = redisTemplate.opsForValue().get(USERINFO_TOKEN + userId);
            if(!StringUtils.isEmpty(userInfoJson)){
                userinfo=JSONObject.parseObject(userInfoJson,UserInfoVo.class);
            }else{
                // 没有则去查寻数据库(openfeign去调用服务)
                userinfo= userFeignService.getUserPermissionByUserId(userId);
                if(!ObjectUtils.isEmpty(userinfo)){
                    userinfo.setPassword(null);//密码不能显示出去
                    //用户信息储存到redis,有效2小时
                    redisTemplate.opsForValue().set(USERINFO_TOKEN + userId,JSONObject.toJSONString(userinfo), 2, TimeUnit.HOURS);
                }
            }
            //权限校验判断 有访问这个接口的权限
            //做 权限校验，没有权限则 返回异常
            if(ObjectUtils.isEmpty(userinfo)||ObjectUtils.isEmpty(userinfo.getPermissionList())||userinfo.getPermissionList().size()==0){
                ServerHttpResponse response = exchange.getResponse();
                return out(response, ResultCodeEnum.PERMISSION);
            }
            List<SysMenu> permissionList = userinfo.getPermissionList();
            for (int i = 0; i <permissionList.size() ; i++) {
                String url = permissionList.get(i).getUrl();
                if(!StringUtils.isEmpty(url)&&request.getURI().getPath().contains(url)){
                    return chain.filter(exchange);
                }
            }
            ServerHttpResponse response = exchange.getResponse();
            return out(response, ResultCodeEnum.PERMISSION);
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
    /**
     * api接口鉴权失败返回数据
     * @param response
     * @return
     */
    private Mono<Void> out(ServerHttpResponse response, ResultCodeEnum resultCodeEnum) {
        Result result = Result.build(null, resultCodeEnum);
        byte[] bits = JSONObject.toJSONString(result).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

    /**
     * 获取当前登录用户id
     * @param request
     * @returnloginUser
     */
    private String getUserId(ServerHttpRequest request) {
        String token = "";
        List<String> tokenList = request.getHeaders().get(AUTHORIZE_TOKEN);
        if(null  != tokenList) {
            token = tokenList.get(0);
        }
        if(!StringUtils.isEmpty(token)) {
            return JwtUtils.getUserId(token);
        }
        return null;
    }

    public static void main(String[] args) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        System.out.println(antPathMatcher.match("/user/**", "/user/findSysUserPage"));
    }
}
