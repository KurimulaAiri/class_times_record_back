package com.shiroko.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

/**
 * Description: JWT 工具类 - 支持双 Token 机制（Access Token + Refresh Token）
 *
 * @author Guguguy
 * @version 2.0
 * @since 2026/3/19 下午4:59
 */
@Component
public class JwtUtils {

    private static final String SECRET_KEY = "shiroko_project_secret_key_at_least_32_chars_long";
    public static final String TOKEN_TYPE_ACCESS = "access";
    public static final String TOKEN_TYPE_REFRESH = "refresh";
    private static final long ACCESS_EXPIRATION = 1000 * 60 * 5;
    private static final long REFRESH_EXPIRATION = 1000 * 60 * 60 * 24 * 7;

    private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    public String createAccessToken(Long userId, Long roleId) {
        return Jwts.builder()
                .setSubject("user_auth")
                .claim("userId", userId)
                .claim("roleId", roleId)
                .claim("tokenType", TOKEN_TYPE_ACCESS)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(Long userId, Long roleId) {
        return Jwts.builder()
                .setSubject("user_auth")
                .claim("userId", userId)
                .claim("roleId", roleId)
                .claim("tokenType", TOKEN_TYPE_REFRESH)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateAccessToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return TOKEN_TYPE_ACCESS.equals(claims.get("tokenType"));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return TOKEN_TYPE_REFRESH.equals(claims.get("tokenType"));
        } catch (Exception e) {
            return false;
        }
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Map<String, Object> getUserInfoFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    public Map<String, Object> getUserInfoFromRefreshToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            if (!TOKEN_TYPE_REFRESH.equals(claims.get("tokenType"))) {
                return null;
            }
            return claims;
        } catch (Exception e) {
            return null;
        }
    }
}
