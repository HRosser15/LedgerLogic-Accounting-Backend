package com.ledgerlogic.controllers;

import com.ledgerlogic.dtos.EmailRequest;
import com.ledgerlogic.services.EmailService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/sendEmail")
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/text")
    public void sendTextEmail(@RequestBody EmailRequest emailRequest) {
        this.emailService.send(
                emailRequest.getTo(),
                emailRequest.getFrom(),
                emailRequest.getSubject(),
                emailRequest.getBody()
        );
    }

    @PostMapping("/attachment")
    public void sendEmailWithAttachment(
            @RequestParam("to") String to,
            @RequestParam("from") String from,
            @RequestParam("subject") String subject,
            @RequestParam("body") String body,
            @RequestParam(value = "attachment", required = false) MultipartFile attachment) {
        this.emailService.sendWithAttachment(to, from, subject, body, attachment);
    }
}
