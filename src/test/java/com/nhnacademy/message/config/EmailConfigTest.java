package com.nhnacademy.message.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class EmailConfigTest {

    private EmailConfig emailConfig;

    @BeforeEach
    void setUp() {
        emailConfig = new EmailConfig("testPassword");
        ReflectionTestUtils.setField(emailConfig, "emailHost", "smtp.gmail.com");
        ReflectionTestUtils.setField(emailConfig, "emailPort", 587);
        ReflectionTestUtils.setField(emailConfig, "emailUsername", "test@gmail.com");
        ReflectionTestUtils.setField(emailConfig, "emailSmtpAuth", true);
        ReflectionTestUtils.setField(emailConfig, "emailSmtpStartTlsEnable", true);
    }

    @Test
    void testJavaMailSender() {
        JavaMailSender mailSender = emailConfig.javaMailSender();

        assertNotNull(mailSender);
        assertTrue(mailSender instanceof JavaMailSenderImpl);

        JavaMailSenderImpl javaMailSender = (JavaMailSenderImpl) mailSender;

        assertEquals("smtp.gmail.com", javaMailSender.getHost());
        assertEquals(587, javaMailSender.getPort());
        assertEquals("test@gmail.com", javaMailSender.getUsername());
        assertEquals("testPassword", javaMailSender.getPassword());

        Properties props = javaMailSender.getJavaMailProperties();
        assertTrue((Boolean) props.get("mail.smtp.auth"));
        assertTrue((Boolean) props.get("mail.smtp.starttls.enable"));
    }
}