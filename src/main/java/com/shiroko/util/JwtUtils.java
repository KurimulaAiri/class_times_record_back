package com.shiroko.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * Description: TODO
 *
 * @author Guguguy
 * @version 1.0
 * @since 2026/3/19 下午4:59
 */
@Component
public class JwtUtils {

    // 签名密钥（生产环境建议从配置文件读取，长度至少32位）
    private static final String SECRET_KEY = "shiroko_project_secret_key_at_least_32_chars_long";
    private static final long EXPIRATION_TIME = 86400000; // 有效期 24 小时

    private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    /**
     * 生成 Token
     * @param userId 数据库中的用户自增ID
     */
    public String createToken(Long userId) {
        return Jwts.builder()
                .setSubject("user_auth")
                .claim("userId", userId) // 将用户ID存入载荷(Payload)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
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

    /**
     * 解析 Token 并获取用户 ID
     */
    public Long getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("userId", Long.class);
        } catch (Exception e) {
            // Token 过期或非法
            return null;
        }
    }
}
