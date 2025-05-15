package com.example.wechatlogin.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class HttpClientUtil {

    private static final RestTemplate restTemplate = new RestTemplate();

    /**
     * 发送GET请求
     * @param url 请求URL
     * @param responseType 响应类型
     * @param <T> 响应类型泛型
     * @return 响应结果
     */
    public static <T> T doGet(String url, Class<T> responseType) {
        try {
            ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
            return response.getBody();
        } catch (Exception e) {
            log.error("HTTP GET请求失败, URL: {}, 错误: {}", url, e.getMessage());
            return null;
        }
    }
} 