package com.shiroko.interceptor;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shiroko.context.UserContext;
import com.shiroko.converter.UserConverter;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.entity.User;
import com.shiroko.service.UserService;
import com.shiroko.util.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * Description: TODO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/19 下午5:07
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(JwtInterceptor.class);

    private final JwtUtils jwtUtils;

    private final UserService userService;

    private final UserConverter userConverter;

    private final ResponseDTO<Void> dto;

    @Autowired
    private JwtInterceptor(JwtUtils jwtUtils, UserService userService, UserConverter userConverter) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
        this.userConverter = userConverter;
        this.dto = new ResponseDTO<>();
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler) {
        // 从 Header 中获取 token（对应你 request.js 里的 'token' 字段）
        String token = request.getHeader("token");

        // 校验 token 是否为空
        if (token == null) {
            // 校验失败返回 401 错误码
            response.setStatus(401);
            response.setContentType("application/json;charset=utf-8");
            try {
                dto.setCode(401L);
                dto.setMessage("未登录！");
                response.getWriter().write(JSON.toJSONString(dto));
            } catch (IOException e) {
                logger.error("响应写入失败", e);
            }
            return false;
        }

        // 校验 token 是否过期
        if (!jwtUtils.validateToken(token)) {
            // 校验失败返回 401 错误码
            response.setStatus(401);
            response.setContentType("application/json;charset=utf-8");
            try {
                dto.setCode(401L);
                dto.setMessage("登录过期，请重新登录！");
                response.getWriter().write(JSON.toJSONString(dto));
            } catch (IOException e) {
                logger.error("响应写入失败", e);
            }
            return false;
        }

        Map<String, Object> userInfo = jwtUtils.getUserInfoFromToken(token);
        Long userId = Long.valueOf(userInfo.get("userId").toString());
        Long roleId = Long.valueOf(userInfo.get("roleId").toString());

        // 存入当前线程，方便后续 Service 使用
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getId, userId));
        UserContext.setUser(userConverter.pojoToDTO(user, roleId));

        // 3. 滑动过期逻辑：检查是否快过期了
        Claims claims = jwtUtils.parseClaims(token);
        Date expiration = claims.getExpiration();
        long remainingTime = expiration.getTime() - System.currentTimeMillis();
        long refreshThreshold = 5 * 60 * 1000L; // 设置阈值：如果剩余时间小于 5 分钟

        if (remainingTime < refreshThreshold) {
            // 生成新 Token
            String newToken = jwtUtils.createToken(userId, roleId);
            // 将新 Token 放入响应头，约定字段名为 "new-token"
            response.setHeader("new-token", newToken);
            // 记得处理跨域暴露 Header 问题
            response.setHeader("Access-Control-Expose-Headers", "new-token");
        }

        return true;

    }

    @Override
    public void afterCompletion(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler,
            @NonNull Exception ex) {
        // 请求结束，务必清理 ThreadLocal 防止内存泄漏
        UserContext.remove();
    }
}
