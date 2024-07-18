package com.nhnacademy.message.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTUtils {
    private final SecretKey secretKey;
    @Value("${spring.jwt.access.expiredMs}")
    private Long expiredMs;

    public String getCategory(String token) {
        return getClaimsFromToken(token).get("category", String.class);
    }

    public String getEmail(String token) {
        return getClaimsFromToken(token).get("email", String.class);
    }

    public boolean isExpired(String token) {
        try {
            return getClaimsFromToken(token).getExpiration().before(new Date());
        } catch (Exception e) {
            log.error(e.getMessage());
            return true;
        }
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

    public String createJwt(String category, String email) {
        return Jwts.builder()
                .claim("category", category)
                .claim("email", email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}
