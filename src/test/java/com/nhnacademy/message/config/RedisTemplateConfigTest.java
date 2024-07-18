package com.nhnacademy.message.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import static org.junit.jupiter.api.Assertions.*;

class RedisTemplateConfigTest {

    private RedisTemplateConfig redisTemplateConfig;

    @BeforeEach
    void setUp() {
        redisTemplateConfig = new RedisTemplateConfig("localhost", 6379, "password", 0);
    }

    @Test
    void testRedisConnectionFactory() {
        RedisConnectionFactory connectionFactory = redisTemplateConfig.redisConnectionFactory();

        assertNotNull(connectionFactory);
        assertTrue(connectionFactory instanceof LettuceConnectionFactory);

        LettuceConnectionFactory lettuceConnectionFactory = (LettuceConnectionFactory) connectionFactory;
        RedisStandaloneConfiguration standaloneConfig = lettuceConnectionFactory.getStandaloneConfiguration();

        assertEquals("localhost", standaloneConfig.getHostName());
        assertEquals(6379, standaloneConfig.getPort());
        assertEquals(0, standaloneConfig.getDatabase());
    }

    @Test
    void testRedisTemplate() {
        RedisConnectionFactory connectionFactory = redisTemplateConfig.redisConnectionFactory();
        RedisTemplate<String, Object> redisTemplate = redisTemplateConfig.redisTemplate(connectionFactory);

        assertNotNull(redisTemplate);
        assertEquals(connectionFactory, redisTemplate.getConnectionFactory());

        assertTrue(redisTemplate.getKeySerializer() instanceof StringRedisSerializer);
        assertTrue(redisTemplate.getValueSerializer() instanceof GenericJackson2JsonRedisSerializer);
        assertTrue(redisTemplate.getHashKeySerializer() instanceof StringRedisSerializer);
        assertTrue(redisTemplate.getHashValueSerializer() instanceof GenericJackson2JsonRedisSerializer);
    }
}