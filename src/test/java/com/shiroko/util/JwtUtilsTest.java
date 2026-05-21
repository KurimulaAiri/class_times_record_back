package com.shiroko.util;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
    }

    @Test
    @DisplayName("创建Token应返回非空字符串")
    void createToken_shouldReturnNonNullString() {
        String token = jwtUtils.createToken(1L, 2L);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("创建Token应包含三段式JWT格式")
    void createToken_shouldHaveThreeParts() {
        String token = jwtUtils.createToken(1L, 2L);
        String[] parts = token.split("\\.");
        assertEquals(3, parts.length);
    }

    @Test
    @DisplayName("有效Token校验应返回true")
    void validateToken_withValidToken_shouldReturnTrue() {
        String token = jwtUtils.createToken(1L, 2L);
        assertTrue(jwtUtils.validateToken(token));
    }

    @Test
    @DisplayName("无效Token校验应返回false")
    void validateToken_withInvalidToken_shouldReturnFalse() {
        assertFalse(jwtUtils.validateToken("invalid.token.here"));
    }

    @Test
    @DisplayName("空Token校验应返回false")
    void validateToken_withEmptyToken_shouldReturnFalse() {
        assertFalse(jwtUtils.validateToken(""));
    }

    @Test
    @DisplayName("Null Token校验应返回false")
    void validateToken_withNullToken_shouldReturnFalse() {
        assertFalse(jwtUtils.validateToken(null));
    }

    @Test
    @DisplayName("解析有效Token应返回正确Claims")
    void parseClaims_withValidToken_shouldReturnCorrectClaims() {
        Long userId = 100L;
        Long roleId = 200L;
        String token = jwtUtils.createToken(userId, roleId);

        Claims claims = jwtUtils.parseClaims(token);

        assertNotNull(claims);
        assertEquals("user_auth", claims.getSubject());
        assertEquals(userId.intValue(), claims.get("userId", Integer.class));
        assertEquals(roleId.intValue(), claims.get("roleId", Integer.class));
    }

    @Test
    @DisplayName("解析被篡改的Token应抛出异常")
    void parseClaims_withTamperedToken_shouldThrowException() {
        String token = jwtUtils.createToken(1L, 2L);
        String tamperedToken = token.substring(0, token.length() - 5) + "xxxxx";

        assertThrows(Exception.class, () -> jwtUtils.parseClaims(tamperedToken));
    }

    @Test
    @DisplayName("getUserInfoFromToken有效Token应返回包含userId和roleId的Map")
    void getUserInfoFromToken_withValidToken_shouldReturnUserInfo() {
        Long userId = 50L;
        Long roleId = 99L;
        String token = jwtUtils.createToken(userId, roleId);

        Map<String, Object> userInfo = jwtUtils.getUserInfoFromToken(token);

        assertNotNull(userInfo);
        assertEquals(userId.intValue(), ((Number) userInfo.get("userId")).intValue());
        assertEquals(roleId.intValue(), ((Number) userInfo.get("roleId")).intValue());
    }

    @Test
    @DisplayName("getUserInfoFromToken无效Token应返回null")
    void getUserInfoFromToken_withInvalidToken_shouldReturnNull() {
        Map<String, Object> userInfo = jwtUtils.getUserInfoFromToken("invalid.token");
        assertNull(userInfo);
    }

    @Test
    @DisplayName("getUserInfoFromToken空Token应返回null")
    void getUserInfoFromToken_withEmptyToken_shouldReturnNull() {
        Map<String, Object> userInfo = jwtUtils.getUserInfoFromToken("");
        assertNull(userInfo);
    }

    @Test
    @DisplayName("不同用户生成的Token应不同")
    void createToken_withDifferentUsers_shouldProduceDifferentTokens() {
        String token1 = jwtUtils.createToken(1L, 1L);
        String token2 = jwtUtils.createToken(2L, 1L);
        assertNotEquals(token1, token2);
    }
}
