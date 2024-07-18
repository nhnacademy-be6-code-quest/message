package com.nhnacademy.message.util;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JWTUtilsTest {

    private JWTUtils jwtUtils;
    private final String secretKeyString = "thisIsAVeryLongSecretKeyForTestingPurposes";
    private final SecretKey secretKey = new SecretKeySpec(secretKeyString.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

    @BeforeEach
    void setUp() {
        jwtUtils = new JWTUtils(secretKey);
        ReflectionTestUtils.setField(jwtUtils, "expiredMs", 3600000L); // 1 hour
    }

    @Test
    void testCreateAndParseJwt() {
        String category = "user";
        String email = "test@example.com";

        String token = jwtUtils.createJwt(category, email);

        assertNotNull(token);
        assertEquals(category, jwtUtils.getCategory(token));
        assertEquals(email, jwtUtils.getEmail(token));
    }

    @Test
    void testIsExpired() {
        String token = jwtUtils.createJwt("user", "test@example.com");
        assertFalse(jwtUtils.isExpired(token));

        // Create an expired token
        String expiredToken = Jwts.builder()
                .claim("category", "user")
                .claim("email", "test@example.com")
                .issuedAt(new Date(System.currentTimeMillis() - 2 * 3600000)) // 2 hours ago
                .expiration(new Date(System.currentTimeMillis() - 3600000)) // 1 hour ago
                .signWith(secretKey)
                .compact();

        assertTrue(jwtUtils.isExpired(expiredToken));
    }

    @Test
    void testInvalidToken() {
        String invalidToken = "invalidToken";
        assertTrue(jwtUtils.isExpired(invalidToken));
    }
}