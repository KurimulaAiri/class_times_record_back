package com.shiroko.interceptor;

import com.alibaba.fastjson2.JSON;
import com.shiroko.context.UserContext;
import com.shiroko.repository.dto.ResponseDTO;
import com.shiroko.repository.dto.UserDTO;
import com.shiroko.repository.entity.SysUser;
import com.shiroko.service.SysUserService;
import com.shiroko.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Map;

/**
 * Admin JWT 拦截器 - 校验 Access Token 并设置 UserContext
 * 专用于管理后台，使用 SysUserService 查询用户
 */
@Component
@RequiredArgsConstructor
public class AdminJwtInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AdminJwtInterceptor.class);

    private final JwtUtils jwtUtils;

    private final SysUserService sysUserService;

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
            writeUnauthorized(response, "未登录！");
            return false;
        }

        if (!jwtUtils.validateAccessToken(token)) {
            writeUnauthorized(response, "登录过期，请重新登录！");
            return false;
        }

        Map<String, Object> userInfo = jwtUtils.getUserInfoFromToken(token);
        Long userId = Long.valueOf(userInfo.get("userId").toString());
        Long roleId = Long.valueOf(userInfo.get("roleId").toString());

        SysUser sysUser = sysUserService.getById(userId);
        if (sysUser == null) {
            writeUnauthorized(response, "用户不存在！");
            return false;
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setId(sysUser.getId());
        userDTO.setRoleId(roleId);
        userDTO.setUsername(sysUser.getUsername());
        userDTO.setCreateTime(sysUser.getCreateTime());
        userDTO.setUpdateTime(sysUser.getUpdateTime());
        UserContext.setUser(userDTO);

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

    private void writeUnauthorized(HttpServletResponse response, String message) {
        response.setStatus(401);
        response.setContentType("application/json;charset=utf-8");
        try {
            ResponseDTO<Void> dto = new ResponseDTO<>();
            dto.setCode(401L);
            dto.setMessage(message);
            response.getWriter().write(JSON.toJSONString(dto));
        } catch (IOException e) {
            logger.error("响应写入失败", e);
        }
    }
}
