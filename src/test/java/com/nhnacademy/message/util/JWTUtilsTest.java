package com.nhnacademy.message.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.jwt.secret=01234567890123456789012345678901",
        "spring.jwt.access.expiredMs=604800000" // in milliseconds
})
class JWTUtilsTest {

    @Value("${spring.jwt.secret}")
    private String secret;

    @Value("${spring.jwt.access.expiredMs}")
    private Long accessExpiredMs;

    private JWTUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JWTUtils(secret, accessExpiredMs);
    }

    @Test
    void testCreateJwt() {
        String email = "123e4567-e89b-12d3-a456-426614174000";

        String token = jwtUtils.createJwt("test", email);

        assertThat(token).isNotNull();
    }

    @Test
    void testGetCategory() {
        String uuid = "123e4567-e89b-12d3-a456-426614174000";
        String category = "test";

        String token = jwtUtils.createJwt(category, uuid);
        String extractedCategory = jwtUtils.getCategory(token);

        assertThat(extractedCategory).isEqualTo(category);
    }

    @Test
    void testGetEmail() {
        String uuid = "123e4567-e89b-12d3-a456-426614174000";
        String category = "test";

        String token = jwtUtils.createJwt(category, uuid);
        String extractedUUID = jwtUtils.getEmail(token);

        assertThat(extractedUUID).isEqualTo(uuid);
    }

    @Test
    void testIsExpired() {
        String uuid = "123e4567-e89b-12d3-a456-426614174000";
        String category = "test";

        String token = jwtUtils.createJwt(category, uuid);

        assertThat(jwtUtils.isExpired(token)).isFalse();
    }
}