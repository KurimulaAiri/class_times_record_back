package com.shiroko.filter;

import com.shiroko.context.UserContext;
import com.shiroko.repository.dto.UserDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Description: 网关用户过滤器
 * 读取网关转发的 X-User-Id / X-User-Role 请求头，设置到 UserContext
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/6/10
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
@Slf4j
public class GatewayUserFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String userIdHeader = request.getHeader("X-User-Id");
        String roleHeader = request.getHeader("X-User-Role");

        if (userIdHeader != null) {
            Long userId = Long.valueOf(userIdHeader);
            Long roleId = roleHeader != null ? Long.valueOf(roleHeader) : null;
            log.debug("userId: {}, roleId: {}", userId, roleId);
            UserDTO userDTO = new UserDTO();
            userDTO.setId(userId);
            userDTO.setRoleId(roleId);
            UserContext.setUser(userDTO);
        }

        filterChain.doFilter(request, response);
    }
}
