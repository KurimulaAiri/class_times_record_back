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
    @DisplayName("鍒涘缓AccessToken搴旇繑鍥為潪绌哄瓧绗︿覆")
    void createAccessToken_shouldReturnNonNullString() {
        String token = jwtUtils.createAccessToken(1L, 2L);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("鍒涘缓AccessToken搴斿寘鍚笁娈靛紡JWT鏍煎紡")
    void createAccessToken_shouldHaveThreeParts() {
        String token = jwtUtils.createAccessToken(1L, 2L);
        String[] parts = token.split("\\.");
        assertEquals(3, parts.length);
    }

    @Test
    @DisplayName("鏈夋晥AccessToken鏍￠獙搴旇繑鍥瀟rue")
    void validateAccessToken_withValidToken_shouldReturnTrue() {
        String token = jwtUtils.createAccessToken(1L, 2L);
        assertTrue(jwtUtils.validateAccessToken(token));
    }

    @Test
    @DisplayName("鏃犳晥AccessToken鏍￠獙搴旇繑鍥瀎alse")
    void validateAccessToken_withInvalidToken_shouldReturnFalse() {
        assertFalse(jwtUtils.validateAccessToken("invalid.token.here"));
    }

    @Test
    @DisplayName("绌篈ccessToken鏍￠獙搴旇繑鍥瀎alse")
    void validateAccessToken_withEmptyToken_shouldReturnFalse() {
        assertFalse(jwtUtils.validateAccessToken(""));
    }

    @Test
    @DisplayName("Null AccessToken鏍￠獙搴旇繑鍥瀎alse")
    void validateAccessToken_withNullToken_shouldReturnFalse() {
        assertFalse(jwtUtils.validateAccessToken(null));
    }

    @Test
    @DisplayName("瑙ｆ瀽鏈夋晥AccessToken搴旇繑鍥炴纭瓹laims")
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
    @DisplayName("瑙ｆ瀽琚鏀圭殑Token搴旀姏鍑哄紓甯?)
    void parseClaims_withTamperedToken_shouldThrowException() {
        String token = jwtUtils.createAccessToken(1L, 2L);
        String tamperedToken = token.substring(0, token.length() - 5) + "xxxxx";

        assertThrows(Exception.class, () -> jwtUtils.parseClaims(tamperedToken));
    }

    @Test
    @DisplayName("getUserInfoFromToken鏈夋晥Token搴旇繑鍥炲寘鍚玼serId鍜宺oleId鐨凪ap")
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
    @DisplayName("getUserInfoFromToken鏃犳晥Token搴旇繑鍥瀗ull")
    void getUserInfoFromToken_withInvalidToken_shouldReturnNull() {
        Map<String, Object> userInfo = jwtUtils.getUserInfoFromToken("invalid.token");
        assertNull(userInfo);
    }

    @Test
    @DisplayName("getUserInfoFromToken绌篢oken搴旇繑鍥瀗ull")
    void getUserInfoFromToken_withEmptyToken_shouldReturnNull() {
        Map<String, Object> userInfo = jwtUtils.getUserInfoFromToken("");
        assertNull(userInfo);
    }

    @Test
    @DisplayName("涓嶅悓鐢ㄦ埛鐢熸垚鐨凙ccessToken搴斾笉鍚?)
    void createAccessToken_withDifferentUsers_shouldProduceDifferentTokens() {
        String token1 = jwtUtils.createAccessToken(1L, 1L);
        String token2 = jwtUtils.createAccessToken(2L, 1L);
        assertNotEquals(token1, token2);
    }

    @Test
    @DisplayName("RefreshToken鏍￠獙搴旈€氳繃")
    void validateRefreshToken_withValidToken_shouldReturnTrue() {
        String token = jwtUtils.createRefreshToken(1L, 2L);
        assertTrue(jwtUtils.validateRefreshToken(token));
    }

    @Test
    @DisplayName("AccessToken涓嶅簲鐢ㄤ綔RefreshToken鏍￠獙")
    void validateRefreshToken_withAccessToken_shouldReturnFalse() {
        String token = jwtUtils.createAccessToken(1L, 2L);
        assertFalse(jwtUtils.validateRefreshToken(token));
    }

    @Test
    @DisplayName("RefreshToken涓嶅簲鐢ㄤ綔AccessToken鏍￠獙")
    void validateAccessToken_withRefreshToken_shouldReturnFalse() {
        String token = jwtUtils.createRefreshToken(1L, 2L);
        assertFalse(jwtUtils.validateAccessToken(token));
    }

    @Test
    @DisplayName("getUserInfoFromRefreshToken鏈夋晥RefreshToken搴旇繑鍥炵敤鎴蜂俊鎭?)
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
    @DisplayName("getUserInfoFromRefreshToken浣跨敤AccessToken搴旇繑鍥瀗ull")
    void getUserInfoFromRefreshToken_withAccessToken_shouldReturnNull() {
        String token = jwtUtils.createAccessToken(1L, 2L);
        assertNull(jwtUtils.getUserInfoFromRefreshToken(token));
    }
}
