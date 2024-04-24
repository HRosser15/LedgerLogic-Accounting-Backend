package com.ledgerlogic.services;

import com.ledgerlogic.models.EventLog;
import com.ledgerlogic.repositories.AccountRepository;
import com.ledgerlogic.repositories.EventLogRepository;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class EventLogService {
    private final AccountRepository accountRepository;
    private final EventLogRepository eventLogRepository;

    public EventLogService(AccountRepository accountRepository, EventLogRepository eventLogRepository) {
        this.accountRepository = accountRepository;
        this.eventLogRepository = eventLogRepository;
    }

    public List<EventLog> getEventLog(){
        return this.eventLogRepository.findAll();
    }

    public EventLog saveEventLog(EventLog userEventLog) {
        if (userEventLog.getCurrentState() != null) {
            if (userEventLog.getCurrentState().startsWith("Account(")) {
                String currentStateJson = convertAccountToJson(userEventLog.getCurrentState());
                userEventLog.setCurrentState(currentStateJson);
            } else if (userEventLog.getCurrentState().startsWith("Journal{")) {
                String currentStateJson = convertJournalToJson(userEventLog.getCurrentState());
                userEventLog.setCurrentState(currentStateJson);
            }
        }
        return this.eventLogRepository.save(userEventLog);
    }

    private String convertAccountToJson(String accountString) {
        String accountJson = accountString.substring("Account(".length(), accountString.length() - 1);
        accountJson = accountJson.replace('\'', '\"');
        accountJson = accountJson.replaceAll("(\\w+)=", "\"$1\":");
        accountJson = accountJson.replaceAll("\"(\\w+)\":", "$1:");
        accountJson = accountJson.replaceAll("\",\\s*\"", "\", \"");
        accountJson = accountJson.replace("\"accountId\":\"null,", "\"accountId\":null,");
        return "{" + accountJson + "}";
    }

    private String convertJournalToJson(String journalString) {
        String journalJson = journalString.substring("Journal{".length(), journalString.length() - 1);
        journalJson = journalJson.replace('\'', '\"');
        journalJson = journalJson.replaceAll("(\\w+)=", "\"$1\":");
        return "{" + journalJson + "}";
    }
}