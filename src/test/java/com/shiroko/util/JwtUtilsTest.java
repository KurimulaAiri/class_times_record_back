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
    @DisplayName("创建AccessToken应返回非空字符串")
    void createAccessToken_shouldReturnNonNullString() {
        String token = jwtUtils.createAccessToken(1L, 2L);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("创建AccessToken应包含三段式JWT格式")
    void createAccessToken_shouldHaveThreeParts() {
        String token = jwtUtils.createAccessToken(1L, 2L);
        String[] parts = token.split("\\.");
        assertEquals(3, parts.length);
    }

    @Test
    @DisplayName("有效AccessToken校验应返回true")
    void validateAccessToken_withValidToken_shouldReturnTrue() {
        String token = jwtUtils.createAccessToken(1L, 2L);
        assertTrue(jwtUtils.validateAccessToken(token));
    }

    @Test
    @DisplayName("无效AccessToken校验应返回false")
    void validateAccessToken_withInvalidToken_shouldReturnFalse() {
        assertFalse(jwtUtils.validateAccessToken("invalid.token.here"));
    }

    @Test
    @DisplayName("空AccessToken校验应返回false")
    void validateAccessToken_withEmptyToken_shouldReturnFalse() {
        assertFalse(jwtUtils.validateAccessToken(""));
    }

    @Test
    @DisplayName("Null AccessToken校验应返回false")
    void validateAccessToken_withNullToken_shouldReturnFalse() {
        assertFalse(jwtUtils.validateAccessToken(null));
    }

    @Test
    @DisplayName("解析有效AccessToken应返回正确Claims")
    void parseClaims_withValidToken_shouldReturnCorrectClaims() {
        Long userId = 100L;
        Long roleId = 200L;
        String token = jwtUtils.createAccessToken(userId, roleId);

        Claims claims = jwtUtils.parseClaims(token);

        assertNotNull(claims);
        assertEquals("user_auth", claims.getSubject());
        assertEquals(userId.intValue(), claims.get("userId", Integer.class));
        assertEquals(roleId.intValue(), claims.get("roleId", Integer.class));
    }

    @Test
    @DisplayName("解析被篡改的Token应抛出异常")
    void parseClaims_withTamperedToken_shouldThrowException() {
        String token = jwtUtils.createAccessToken(1L, 2L);
        String tamperedToken = token.substring(0, token.length() - 5) + "xxxxx";

        assertThrows(Exception.class, () -> jwtUtils.parseClaims(tamperedToken));
    }

    @Test
    @DisplayName("getUserInfoFromToken有效Token应返回包含userId和roleId的Map")
    void getUserInfoFromToken_withValidToken_shouldReturnUserInfo() {
        Long userId = 50L;
        Long roleId = 99L;
        String token = jwtUtils.createAccessToken(userId, roleId);

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
    @DisplayName("不同用户生成的AccessToken应不同")
    void createAccessToken_withDifferentUsers_shouldProduceDifferentTokens() {
        String token1 = jwtUtils.createAccessToken(1L, 1L);
        String token2 = jwtUtils.createAccessToken(2L, 1L);
        assertNotEquals(token1, token2);
    }

    @Test
    @DisplayName("RefreshToken校验应通过")
    void validateRefreshToken_withValidToken_shouldReturnTrue() {
        String token = jwtUtils.createRefreshToken(1L, 2L);
        assertTrue(jwtUtils.validateRefreshToken(token));
    }

    @Test
    @DisplayName("AccessToken不应用作RefreshToken校验")
    void validateRefreshToken_withAccessToken_shouldReturnFalse() {
        String token = jwtUtils.createAccessToken(1L, 2L);
        assertFalse(jwtUtils.validateRefreshToken(token));
    }

    @Test
    @DisplayName("RefreshToken不应用作AccessToken校验")
    void validateAccessToken_withRefreshToken_shouldReturnFalse() {
        String token = jwtUtils.createRefreshToken(1L, 2L);
        assertFalse(jwtUtils.validateAccessToken(token));
    }

    @Test
    @DisplayName("getUserInfoFromRefreshToken有效RefreshToken应返回用户信息")
    void getUserInfoFromRefreshToken_withValidToken_shouldReturnUserInfo() {
        Long userId = 50L;
        Long roleId = 99L;
        String token = jwtUtils.createRefreshToken(userId, roleId);

        Map<String, Object> userInfo = jwtUtils.getUserInfoFromRefreshToken(token);

        assertNotNull(userInfo);
        assertEquals(userId.intValue(), ((Number) userInfo.get("userId")).intValue());
        assertEquals(roleId.intValue(), ((Number) userInfo.get("roleId")).intValue());
    }

    @Test
    @DisplayName("getUserInfoFromRefreshToken使用AccessToken应返回null")
    void getUserInfoFromRefreshToken_withAccessToken_shouldReturnNull() {
        String token = jwtUtils.createAccessToken(1L, 2L);
        assertNull(jwtUtils.getUserInfoFromRefreshToken(token));
    }
}
