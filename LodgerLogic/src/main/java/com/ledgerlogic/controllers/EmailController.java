package com.ledgerlogic.controllers;

import com.ledgerlogic.dtos.EmailRequest;
import com.ledgerlogic.services.EmailService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/sendEmail")
public class EmailController {

    EmailService emailService;

    public EmailController(EmailService emailService){
        this.emailService = emailService;
    }

    @PostMapping()
    public void sendEmail(@RequestBody EmailRequest emailRequest){
        this.emailService.send(emailRequest.getTo(),
                emailRequest.getFrom(),
                emailRequest.getSubject(),
                emailRequest.getBody());
    }
}
