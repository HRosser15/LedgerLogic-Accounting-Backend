package com.ledgerlogic.email;

public interface EmailSender {
    void send(String to, String from, String subject, String body);
}
