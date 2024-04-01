package com.ledgerlogic.controllers;

import com.ledgerlogic.services.EmailService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/email")
public class EmailController {

    private EmailService emailService;

    public EmailController(EmailService emailService){
        this.emailService = emailService;
    }
    @PostMapping("/send")
    public void sendEmail(){
        this.emailService.send("abderrahimbahia@gmail.com", "Activate user account");
    }
}
