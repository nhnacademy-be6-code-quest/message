package com.nhnacademy.message.dto;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DoorayMessageRequestTest {

    @Test
    void testDoorayMessageRequestBuilder() {
        String botName = "TestBot";
        List<DoorayMessageRequest.Attachment> attachments = Arrays.asList(
                DoorayMessageRequest.Attachment.builder()
                        .title("Test Title")
                        .titleLink("http://test.com")
                        .text("Test Text")
                        .build()
        );

        DoorayMessageRequest request = DoorayMessageRequest.builder()
                .botName(botName)
                .attachments(attachments)
                .build();

        assertNotNull(request);
        assertEquals(botName, request.getBotName());
        assertEquals(attachments, request.getAttachments());
    }

    @Test
    void testAttachmentBuilder() {
        String title = "Test Title";
        String titleLink = "http://test.com";
        String text = "Test Text";

        DoorayMessageRequest.Attachment attachment = DoorayMessageRequest.Attachment.builder()
                .title(title)
                .titleLink(titleLink)
                .text(text)
                .build();

        assertNotNull(attachment);
        assertEquals(title, attachment.getTitle());
        assertEquals(titleLink, attachment.getTitleLink());
        assertEquals(text, attachment.getText());
    }

    @Test
    void testDoorayMessageRequestWithMultipleAttachments() {
        String botName = "TestBot";
        List<DoorayMessageRequest.Attachment> attachments = Arrays.asList(
                DoorayMessageRequest.Attachment.builder()
                        .title("Title 1")
                        .titleLink("http://test1.com")
                        .text("Text 1")
                        .build(),
                DoorayMessageRequest.Attachment.builder()
                        .title("Title 2")
                        .titleLink("http://test2.com")
                        .text("Text 2")
                        .build()
        );

        DoorayMessageRequest request = DoorayMessageRequest.builder()
                .botName(botName)
                .attachments(attachments)
                .build();

        assertNotNull(request);
        assertEquals(botName, request.getBotName());
        assertEquals(2, request.getAttachments().size());
        assertEquals("Title 1", request.getAttachments().get(0).getTitle());
        assertEquals("Title 2", request.getAttachments().get(1).getTitle());
    }

    @Test
    void testDoorayMessageRequestWithNullValues() {
        DoorayMessageRequest request = DoorayMessageRequest.builder().build();

        assertNotNull(request);
        assertNull(request.getBotName());
        assertNull(request.getAttachments());
    }
}