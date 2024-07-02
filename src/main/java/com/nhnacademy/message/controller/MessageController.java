package com.nhnacademy.message.controller;

import com.nhnacademy.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @GetMapping("/send/recover-account")
    public ResponseEntity<String> sendRecoverAccount(@RequestParam("email") String email) {
        log.info("send recovery email: {}", email);
        return ResponseEntity.ok(messageService.sendRecoverMail(email));
    }

    @GetMapping("/send/change-password")
    public ResponseEntity<String> sendChangePassword(@RequestParam("email") String email) {
        log.info("send change password email: {}", email);
        return ResponseEntity.ok(messageService.sendChangePasswordMail(email));
    }
}
