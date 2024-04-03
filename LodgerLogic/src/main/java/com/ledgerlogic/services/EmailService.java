package com.ledgerlogic.services;

import com.ledgerlogic.email.EmailSender;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements EmailSender {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void send(String to, String from, String subject, String body) {
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setTo(to);
            helper.setFrom(from);
            helper.setSubject(subject);
            helper.setText(
                    body, false);
            mailSender.send(mimeMessage);
        }catch (MessagingException e){
            throw new IllegalStateException("failed to send email");
        }
    }

}
