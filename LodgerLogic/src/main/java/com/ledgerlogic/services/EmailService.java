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
    @Async
    public void send(String to, String body) {
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setTo(to);
            helper.setFrom("support@ledgerlogic.com");
            helper.setSubject("Activate user account");
            helper.setText("Hello, "+
                    "\n\n" +
                    body +
                    "\n\n" +
                    "regards,", false);
            mailSender.send(mimeMessage);
        }catch (MessagingException e){
            throw new IllegalStateException("failed to send email");
        }
    }

}
