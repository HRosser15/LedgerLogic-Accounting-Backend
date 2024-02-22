package com.ledgerlogic.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendApprovalRequestEmail(String adminEmail, String newUserEmail) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(adminEmail);
        message.setSubject("New user account approval required");
        message.setText("Dear Admin, A new user with email " + newUserEmail + " has registered. Please approve their account");

        javaMailSender.send(message);
    }

    public void sendApprovalResponseEmail(String newUserEmail, String adminEmail, String responseSubject, String responseBody){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(newUserEmail);
        message.setSubject(responseSubject);
        message.setText(responseBody);

        javaMailSender.send(message);
    }
}
