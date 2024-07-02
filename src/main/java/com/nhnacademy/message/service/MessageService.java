package com.nhnacademy.message.service;

public interface MessageService {
    String sendRecoverMail(String email);
    String sendChangePasswordMail(String email);
}
