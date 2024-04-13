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

    @PostMapping()
    public void sendEmail(@RequestBody EmailRequest emailRequest) {
        this.emailService.send(
                emailRequest.getTo(),
                emailRequest.getFrom(),
                emailRequest.getSubject(),
                emailRequest.getBody(),
                emailRequest.getAttachment()
        );
    }
}