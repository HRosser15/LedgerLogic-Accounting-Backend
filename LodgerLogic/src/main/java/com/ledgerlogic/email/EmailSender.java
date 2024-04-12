package com.ledgerlogic.email;

import org.springframework.web.multipart.MultipartFile;

public interface EmailSender {
    void send(String to, String from, String subject, String body);
    void sendWithAttachment(String to, String from, String subject, String body, MultipartFile attachment);
}