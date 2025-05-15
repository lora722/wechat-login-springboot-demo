package com.example.wechatlogin.service;

import com.example.wechatlogin.dto.WechatAccessTokenDTO;

public interface WechatService {
    
    /**
     * 获取登录二维码URL
     * @param uuid 唯一标识
     * @return 二维码URL
     */
    String getQrUrl(String uuid);
    
    /**
     * 处理微信回调，获取用户openid
     * @param code 授权码
     * @return 微信账号信息
     */
    WechatAccessTokenDTO processCallback(String code);
    
    /**
     * 检查登录状态
     * @param uuid 唯一标识
     * @return 登录状态信息
     */
    String checkLoginStatus(String uuid);
    
    /**
     * 更新登录状态
     * @param uuid 唯一标识
     * @param token JWT令牌
     */
    void updateLoginStatus(String uuid, String token);
} 