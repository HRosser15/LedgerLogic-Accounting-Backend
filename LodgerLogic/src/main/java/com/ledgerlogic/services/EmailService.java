package com.ledgerlogic.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("$(spring.mail.username)")
    private String fromEmail;

    public void sendApprovalRequestEmail(String adminEmail, String newUserEmail) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(fromEmail);
        message.setTo("abderrahim.bahiaa@gmail.com");
        message.setSubject("New user account approval required");
        message.setText("Dear Admin, A new user with email " + newUserEmail + " has registered. Please approve their account");

        mailSender.send(message);
    }

    public void sendApprovalResponseEmail(String newUserEmail, String adminEmail, String responseSubject, String responseBody){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(fromEmail);
        message.setTo("abderrahim.bahiaa@gmail.com");
        message.setSubject(responseSubject);
        message.setText(responseBody);

        mailSender.send(message);
    }

    public void endOfSuspensionNotification(String adminEmail, String notification){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(fromEmail);
        message.setTo("abderrahim.bahiaa@gmail.com");
        message.setSubject("End Of Suspension Period!");
        message.setText(notification);

        mailSender.send(message);
    }


}
