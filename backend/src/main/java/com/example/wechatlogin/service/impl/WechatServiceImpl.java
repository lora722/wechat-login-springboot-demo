package com.example.wechatlogin.service.impl;

import com.example.wechatlogin.dto.WechatAccessTokenDTO;
import com.example.wechatlogin.service.WechatService;
import com.example.wechatlogin.util.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Slf4j
@Service
public class WechatServiceImpl implements WechatService {

    @Value("${wechat.appid}")
    private String appId;

    @Value("${wechat.secret}")
    private String appSecret;

    @Value("${wechat.redirectUri}")
    private String redirectUri;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String REDIS_LOGIN_KEY_PREFIX = "wechat:login:";
    private static final String PENDING_STATUS = "PENDING";
    private static final String DONE_STATUS_PREFIX = "DONE:";

    @Override
    public String getQrUrl(String uuid) {
        String url = String.format(
                "https://open.weixin.qq.com/connect/qrconnect?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_login&state=%s#wechat_redirect",
                appId, URLEncoder.encode(redirectUri, StandardCharsets.UTF_8), uuid
        );
        // 设置登录状态为待处理，有效期5分钟
        redisTemplate.opsForValue().set(REDIS_LOGIN_KEY_PREFIX + uuid, PENDING_STATUS, Duration.ofMinutes(5));
        log.info("生成二维码URL: {}", url);
        return url;
    }

    @Override
    public WechatAccessTokenDTO processCallback(String code) {
        String url = String.format(
                "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
                appId, appSecret, code
        );
        WechatAccessTokenDTO result = HttpClientUtil.doGet(url, WechatAccessTokenDTO.class);
        if (result != null && result.getErrcode() == null) {
            log.info("微信授权成功，openid: {}", result.getOpenid());
        } else {
            log.error("微信授权失败: {}", result != null ? result.getErrmsg() : "未知错误");
        }
        return result;
    }

    @Override
    public String checkLoginStatus(String uuid) {
        return redisTemplate.opsForValue().get(REDIS_LOGIN_KEY_PREFIX + uuid);
    }

    @Override
    public void updateLoginStatus(String uuid, String token) {
        redisTemplate.opsForValue().set(REDIS_LOGIN_KEY_PREFIX + uuid, DONE_STATUS_PREFIX + token, Duration.ofMinutes(5));
        log.info("更新登录状态，uuid: {}", uuid);
    }
} 