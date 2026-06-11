package com.shiroko.interceptor;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shiroko.context.UserContext;
import com.shiroko.converter.UserConverter;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.entity.User;
import com.shiroko.service.UserService;
import com.shiroko.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Map;

/**
 * Description: JWT 拦截器 - 校验 Access Token
 *
 * @author Guguguy
 * @version 2.0
 * @since 2026/3/19 下午5:07
 */
@Component
@NullMarked
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
            HttpServletResponse response,
            Object handler) {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        if (token == null) {
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

        if (!jwtUtils.validateAccessToken(token)) {
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

        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getId, userId));
        UserContext.setUser(userConverter.pojoToDTO(user, roleId));

        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex) {
        UserContext.remove();
    }
}
