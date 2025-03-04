package com.wjp.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
    // TOKEN的有效期1小时（S）
    private static final int TOKEN_TIME_OUT = 1 * 3600;

    // 加密KEY
    private static final String TOKEN_SECRET = "wjp123456";

    public static void main(String[] args) {
        Map map = new HashMap();
        map.put("id",1);
        map.put("mobile","13800138000");
        //2、使用JWT的工具类生成token
        //System.out.println(getToken(map));

        String token = getToken(map);
        //校验是否有效
        System.out.println(verifyToken(token));
        System.out.println(getClaims(token));

    }


    // 生成Token
    public static String getToken(Map params){
        long currentTime = System.currentTimeMillis();
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, TOKEN_SECRET) //加密方式
                // 设置这个token有效期
                //.setExpiration(new Date(currentTime + TOKEN_TIME_OUT * 1000)) //过期时间戳
                .addClaims(params)
                .compact();
    }

    // 生成Token，项目中使用
    public static String createToken(Integer userId, String mobile){
        Map<String,Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("mobile", mobile);
        long currentTime = System.currentTimeMillis();
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, TOKEN_SECRET) //加密方式
                // 设置这个token有效期
                //.setExpiration(new Date(currentTime + TOKEN_TIME_OUT * 1000)) //过期时间戳
                .addClaims(map)
                .compact();
    }


    /**
     * 获取Token中的claims信息
     */
    public static Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(TOKEN_SECRET)
                .parseClaimsJws(token).getBody();
    }


    /**
     * 是否有效 true-有效，false-失效
     */
    public static boolean verifyToken(String token) {

        if(StringUtils.isEmpty(token)) {
            return false;
        }

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(TOKEN_SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e) {
            return false;
        }

        return true;
    }
}
