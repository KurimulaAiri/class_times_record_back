package com.shiroko.interceptor;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.shiroko.support.RepeatedlyRequestWrapper;
import com.shiroko.util.SM3Util;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description: 签名拦截器
 * 1. 从 Header 中获取签名参数
 * 2. 校验时间戳是否过期
 * 3. 收集所有参与签名的参数
 * 4. 校验签名是否正确
 * @author Guguguy
 * @version 1.0
 * @since 2026/5/7 下午5:34
 */
@Component
@RequiredArgsConstructor
@Slf4j // 自动注入名为 log 的静态属性
@NullMarked
public class SignInterceptor implements HandlerInterceptor {

    // 必须与前端定义的盐值完全一致
    private static final String APP_SECRET = "SHIROKO_SM3_SALT_2026";
    private static final long TIME_OUT = 60 * 1000; // 60秒过期
    // Spring 自带的 Jackson 实例

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if (request.getDispatcherType() == DispatcherType.ERROR) {
            return true;
        }

        // 1. 获取 Header 中的签名参数
        String clientSign = request.getHeader("x-sign");
        String timestampStr = request.getHeader("x-timestamp");
        String nonce = request.getHeader("x-nonce");

        if (StrUtil.hasBlank(clientSign, timestampStr, nonce)) {
            throw new RuntimeException("签名参数缺失");
        }

        // 2. 时间戳防重放校验
        long timestamp = Long.parseLong(timestampStr);
        if (System.currentTimeMillis() - timestamp > TIME_OUT) {
            throw new RuntimeException("请求已超时");
        }

        // 3. 收集所有参与签名的参数
        Map<String, Object> allParams = new HashMap<>();

// A. 放入 URL 里的 Query 参数 (改为更稳妥的迭代)
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap != null && !parameterMap.isEmpty()) {
            parameterMap.forEach((k, v) -> {
                if (v != null && v.length > 0) allParams.put(k, v[0]);
            });
        }

// B. 放入 Body 里的 JSON 参数
        if (request instanceof RepeatedlyRequestWrapper wrapper) {
            String body = wrapper.getBodyString();
            // 调试必备：确认拦截器到底拿到了什么
            log.info("【SignInterceptor】Body内容: {}", body);


            if (StrUtil.isNotBlank(body)) {
                try {
                    // 建议：如果 body 是数组格式 [1,2,3]，parseObject 会报错
                    // 如果是对象格式 {"id":1}，则没问题
                    Map<String, Object> bodyMap = JSON.parseObject(body);
                    if (bodyMap != null) {
                        allParams.putAll(bodyMap);
                    }
                } catch (Exception e) {
                    log.error("【SignInterceptor】JSON解析失败: {}", e.getMessage(), e);
                }
            }
        } else {
            // 【关键排查点】
            log.error("【SignInterceptor】错误：Request 不是 RepeatedlyRequestWrapper！当前类型: {}", request.getClass().getName());
        }

        // C. 放入系统级参数
        allParams.put("timestamp", timestampStr);
        allParams.put("nonce", nonce);

        // 4. 字典序排序并拼接字符串
        String stringA = allParams.entrySet().stream()
                // 1. 先过滤掉 Entry 为空，或者 Value 真正为 null 的情况
                .filter(Objects::nonNull)
                .filter(e -> e.getValue() != null && StrUtil.isNotBlank(e.getValue().toString()))
                .sorted(Map.Entry.comparingByKey())
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));

        log.info("stringA: {}", stringA);

        // 5. 调用你的 SM3Util 进行验签
        // 逻辑：stringA + APP_SECRET (对应前端拼接逻辑)
        String rawData = stringA + APP_SECRET;
        if (!SM3Util.verify(rawData, clientSign)) {
            throw new RuntimeException("签名验证失败，数据可能被篡改");
        }

        return true;
    }
}