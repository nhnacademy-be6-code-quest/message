package com.nhnacademy.message.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class KeyManagerConfigTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private KeyManagerConfig keyManagerConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(keyManagerConfig, "apiKey", "test-api-key");
        ReflectionTestUtils.setField(keyManagerConfig, "accessKeyId", "test-access-key-id");
        ReflectionTestUtils.setField(keyManagerConfig, "accessKeySecret", "test-access-key-secret");
        ReflectionTestUtils.setField(keyManagerConfig, "redisDbKey", "redis-db-key");
        ReflectionTestUtils.setField(keyManagerConfig, "redisHostKey", "redis-host-key");
        ReflectionTestUtils.setField(keyManagerConfig, "redisPasswordKey", "redis-password-key");
        ReflectionTestUtils.setField(keyManagerConfig, "redisPortKey", "redis-port-key");
        ReflectionTestUtils.setField(keyManagerConfig, "jwtKey", "jwt-key");
        ReflectionTestUtils.setField(keyManagerConfig, "mailPasswordKey", "mail-password-key");
    }

    private void mockRestTemplateResponse(String secretValue) {
        String jsonResponse = "{\"body\":{\"secret\":\"" + secretValue + "\"}}";
        ResponseEntity<String> responseEntity = ResponseEntity.ok(jsonResponse);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseEntity);
    }

    @Test
    void testRedisDb() {
        mockRestTemplateResponse("0");
        assertEquals(0, keyManagerConfig.redisDb());
    }

    @Test
    void testRedisPassword() {
        mockRestTemplateResponse("test-password");
        assertEquals("test-password", keyManagerConfig.redisPassword());
    }

    @Test
    void testRedisHost() {
        mockRestTemplateResponse("localhost");
        assertEquals("localhost", keyManagerConfig.redisHost());
    }

    @Test
    void testRedisPort() {
        mockRestTemplateResponse("6379");
        assertEquals(6379, keyManagerConfig.redisPort());
    }

    @Test
    void testJwtKey() {
        mockRestTemplateResponse("test-jwt-secret");
        SecretKey secretKey = keyManagerConfig.jwtKey();
        assertNotNull(secretKey);
        assertEquals("HmacSHA256", secretKey.getAlgorithm());
    }

    @Test
    void testEmailPassword() {
        mockRestTemplateResponse("test-email-password");
        assertEquals("test-email-password", keyManagerConfig.emailPassword());
    }

    @Test
    void testObjectMapper() {
        ObjectMapper objectMapper = keyManagerConfig.objectMapper();
        assertNotNull(objectMapper);
        assertFalse(objectMapper.isEnabled(com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS));
    }

    @Test
    void testGetKeyWithInvalidJson() {
        mockRestTemplateResponse("invalid-json");
        assertNull(ReflectionTestUtils.invokeMethod(keyManagerConfig, "getKey", "invalid-json"));
    }
}