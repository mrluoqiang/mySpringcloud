package com.study.common.utils;

import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class JwtUtils {
    //过期时间
    //private static long  tokenExpiration = 24*60*60*1000;
    private static long  tokenExpiration = 2*60*60*1000;
    //签名密钥
    private static String tokenSignKey = "123456";

    //生成token
    public static String createToken (String userId,String userName){
        String token = Jwts.builder().setSubject("loginToken")
                .claim("userId", userId)
                .claim("userName", userName)
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .compressWith(CompressionCodecs.GZIP)
                .compact();
        return token;
    }
    //根据token字符串得到用户id
    public static String getUserId(String token) {
        if(StringUtils.isEmpty(token)) {
            return null;
        }
        Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(tokenSignKey)
                .parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        String userId = (String)claims.get("userId");
        return userId;
    }
    //根据token字符串得到用户名称
    public static String getUserName(String token) {
        if(StringUtils.isEmpty(token)){
            return "";
        }
        Jws<Claims> claimsJws
                = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        return (String)claims.get("userName");
    }

    //根据token字符串得到 Claims
    public static Claims getClaim(String token) {
        if(StringUtils.isEmpty(token)){
            return null;
        }
        Jws<Claims> claimsJws
                = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        return claims;
    }

    //获取当前用户id
    public static String getUserIdByRequest(HttpServletRequest request) {
        //从header获取token
        String token = request.getHeader("token");
        //jwt从token获取userid
        String userId = JwtUtils.getUserId(token);
        return userId;
    }
    //获取当前用户名称
    public static String getUserNameByRequest(HttpServletRequest request) {
        //从header获取token
        String token = request.getHeader("token");
        //jwt从token获取userid
        String userName = JwtUtils.getUserName(token);
        return userName;
    }

    public static void main(String[] args) {
          /*String token = JwtUtils.createToken("ABC", "法外狂徒张三");
        System.out.println(token);
        System.out.println(JwtUtils.getUserId(token));
        System.out.println(JwtUtils.getUserName(token));*/
        String userName = getUserName("eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJSyslPz8wLyc9OzVPSUSotTi3yTAGKOjo5Q7l-ibmpQIHne6c_3bMLKJZaUaBkZWhmZG5uZGhoaVELAKL3oXNIAAAA.IIFDav_fqFa-Rm42MQe3yCYHyp10CKvAgRqeFnALgH14CVdPqdgrXT7vJoxLQyXa-7urgMNv4bH5KcPk46fF5w");
        System.out.println(userName);
    }
}
