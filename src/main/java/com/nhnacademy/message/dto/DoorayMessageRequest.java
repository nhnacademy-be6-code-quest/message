package com.nhnacademy.message.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DoorayMessageRequest {
    private String botName;
    private List<Attachment> attachments;

    @Getter
    @Builder
    public static class Attachment {
        private String title;
        private String titleLink;
        private String text;
    }
}
