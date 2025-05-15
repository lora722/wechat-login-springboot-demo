package com.example.wechatlogin.controller;

import com.example.wechatlogin.dto.ApiResult;
import com.example.wechatlogin.dto.WechatAccessTokenDTO;
import com.example.wechatlogin.service.WechatService;
import com.example.wechatlogin.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/wechat")
public class WechatLoginController {

    @Autowired
    private WechatService wechatService;

    /**
     * 获取微信登录二维码
     * @param uuid 客户端生成的唯一标识
     * @return 二维码URL
     */
    @GetMapping("/qr")
    public ApiResult<Map<String, String>> getQrUrl(@RequestParam String uuid) {
        log.info("生成微信登录二维码，uuid: {}", uuid);
        String qrUrl = wechatService.getQrUrl(uuid);
        Map<String, String> result = new HashMap<>();
        result.put("qrUrl", qrUrl);
        return ApiResult.success(result);
    }

    /**
     * 微信扫码回调接口
     * @param code 授权码
     * @param state 唯一标识（UUID）
     * @return 结果页面
     */
    @GetMapping("/callback")
    public String wechatCallback(@RequestParam String code, @RequestParam String state) {
        log.info("接收到微信回调，code: {}, state: {}", code, state);
        
        // 使用code获取微信用户信息
        WechatAccessTokenDTO wechatInfo = wechatService.processCallback(code);
        
        if (wechatInfo != null && wechatInfo.getErrcode() == null) {
            // 生成JWT令牌
            String jwt = JwtUtil.generateToken(wechatInfo.getOpenid());
            
            // 更新Redis中的登录状态
            wechatService.updateLoginStatus(state, jwt);
            
            // 返回关闭窗口的JavaScript
            return "<html><body><h3>登录成功，请关闭窗口</h3><script>setTimeout(function(){window.close();}, 1000);</script></body></html>";
        } else {
            return "<html><body><h3>登录失败，请重试</h3></body></html>";
        }
    }

    /**
     * 检查登录状态
     * @param uuid 唯一标识
     * @return 登录状态
     */
    @GetMapping("/status")
    public ApiResult<Map<String, Object>> checkStatus(@RequestParam String uuid) {
        String status = wechatService.checkLoginStatus(uuid);
        Map<String, Object> result = new HashMap<>();
        
        if (status == null) {
            result.put("status", "expired");
            return ApiResult.success(result);
        } else if (status.equals("PENDING")) {
            result.put("status", "pending");
            return ApiResult.success(result);
        } else if (status.startsWith("DONE:")) {
            String jwt = status.substring(5);
            result.put("status", "done");
            result.put("jwt", jwt);
            // 登录成功后从Redis中删除该记录
            wechatService.updateLoginStatus(uuid, null);
            return ApiResult.success(result);
        }
        
        return ApiResult.error("未知状态");
    }
} 