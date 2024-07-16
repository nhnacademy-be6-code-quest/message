package com.nhnacademy.message.service;

import com.nhnacademy.message.util.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MassageServiceImp implements MessageService {
    @Value("${mail.redirect.uri}")
    private String mailRedirectUri;

    private final JWTUtils jwtUtils;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public String sendRecoverMail(String email) {
        String token = jwtUtils.createJwt("recover-account", email);
        Context context = new Context();
        context.setVariable("verification_url", mailRedirectUri + "/recover-account" +
                "?email=" + email +
                "&token=" + token);

        MimeMessagePreparator preparatory = mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            String content = templateEngine.process("accountRecovery", context);

            helper.setTo(email);
            helper.setSubject("book-store 계정복구 mail 입니다.");
            helper.setText(content, true);
        };
        mailSender.send(preparatory);
        addHistory(email, token, "recovery-account");
        return "이메일로 복구 메일을 전송하였습니다.";
    }

    @Override
    public String sendChangePasswordMail(String email) {
        String token = jwtUtils.createJwt("change-password", email);
        Context context = new Context();
        context.setVariable("changeLink", mailRedirectUri + "/change-password" +
                "?email=" + email +
                "&token=" + token);

        MimeMessagePreparator preparatory = mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            String content = templateEngine.process("changePassword", context);

            helper.setTo(email);
            helper.setSubject("book-store 비밀번호 변경 mail 입니다.");
            helper.setText(content, true);
        };
        mailSender.send(preparatory);
        addHistory(email, token, "change-password");
        return "이메일로 변경 메일을 전송하였습니다.";
    }

    private void addHistory(String email, String token, String type) {
        redisTemplate.opsForHash().put(type, token, email);
        redisTemplate.expire(token, 2, TimeUnit.HOURS);
    }
}
