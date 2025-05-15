package com.example.wechatlogin.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JwtUtil {
    private static final String SECRET = "wechat_login_secret_key";
    private static final long EXPIRE_TIME = 24 * 60 * 60 * 1000; // 24小时有效期

    /**
     * 生成JWT令牌
     * @param openid 微信用户唯一标识
     * @return JWT令牌
     */
    public static String generateToken(String openid) {
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + EXPIRE_TIME);
        
        return JWT.create()
                .withClaim("openid", openid)
                .withIssuedAt(now)
                .withExpiresAt(expireTime)
                .sign(Algorithm.HMAC256(SECRET));
    }

    /**
     * 验证JWT令牌
     * @param token JWT令牌
     * @return 验证结果
     */
    public static boolean verify(String token) {
        try {
            JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 从令牌中获取微信用户openid
     * @param token JWT令牌
     * @return 微信用户openid
     */
    public static String getOpenid(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("openid").asString();
        } catch (Exception e) {
            return null;
        }
    }
} 