package com.nhnacademy.message.service;

import com.nhnacademy.message.client.DoorayClient;
import com.nhnacademy.message.dto.DoorayMessageRequest;
import com.nhnacademy.message.util.JWTUtils;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class MassageServiceTest {

    private MassageServiceImp massageService;

    @Mock
    private JWTUtils jwtUtils;
    @Mock
    private DoorayClient doorayClient;
    @Mock
    private JavaMailSender mailSender;
    @Mock
    private TemplateEngine templateEngine;
    @Mock
    private RedisTemplate<String, String> redisTemplate;
    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        massageService = new MassageServiceImp(jwtUtils, doorayClient, mailSender, templateEngine, redisTemplate);
        ReflectionTestUtils.setField(massageService, "mailRedirectUri", "http://localhost:8080");
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
    }

    @Test
    void testSendRecoverMail() {
        String email = "test@example.com";
        String token = "token123";

        when(jwtUtils.createJwt(anyString(), anyString())).thenReturn(token);
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("HTML Content");

        doAnswer(invocation -> {
            MimeMessagePreparator preparatory = invocation.getArgument(0);
            MimeMessage mockMessage = mock(MimeMessage.class);
            preparatory.prepare(mockMessage);
            return null;
        }).when(mailSender).send(any(MimeMessagePreparator.class));

        String result = massageService.sendRecoverMail(email);

        verify(hashOperations).put("recovery-account", token, email);
        verify(redisTemplate).expire(token, 2L, TimeUnit.HOURS);
        verify(mailSender).send(any(MimeMessagePreparator.class));
        assertEquals("이메일로 복구 메일을 전송하였습니다.", result);
    }

    @Test
    void testSendChangePasswordMail() {
        String email = "test@example.com";
        String token = "token456";

        when(jwtUtils.createJwt(anyString(), anyString())).thenReturn(token);
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("HTML Content");

        doAnswer(invocation -> {
            MimeMessagePreparator preparatory = invocation.getArgument(0);
            MimeMessage mockMessage = mock(MimeMessage.class);
            preparatory.prepare(mockMessage);
            return null;
        }).when(mailSender).send(any(MimeMessagePreparator.class));

        String result = massageService.sendChangePasswordMail(email);

        verify(hashOperations).put("change-password", token, email);
        verify(redisTemplate).expire(token, 2L, TimeUnit.HOURS);
        verify(mailSender).send(any(MimeMessagePreparator.class));
        assertEquals("이메일로 변경 메일을 전송하였습니다.", result);
    }

    @Test
    void testSendRecoverMessageDooray() {
        String email = "test@example.com";
        String token = "token789";

        when(jwtUtils.createJwt(anyString(), anyString())).thenReturn(token);

        String result = massageService.sendRecoverMessageDooray(email);

        verify(hashOperations).put("recovery-account", token, email);
        verify(redisTemplate).expire(token, 2L, TimeUnit.HOURS);
        verify(doorayClient).sendMessage(any(DoorayMessageRequest.class));
        assertEquals("Dooray로 복구 Message을 전송하였습니다.", result);
    }
}