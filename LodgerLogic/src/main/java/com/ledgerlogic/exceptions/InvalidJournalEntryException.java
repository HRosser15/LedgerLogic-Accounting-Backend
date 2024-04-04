package com.ledgerlogic.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Credits and Debits amount need to be balanced!")
public class InvalidJournalEntryException extends Exception{

    public InvalidJournalEntryException(String message){
        super(message);
    }
}
