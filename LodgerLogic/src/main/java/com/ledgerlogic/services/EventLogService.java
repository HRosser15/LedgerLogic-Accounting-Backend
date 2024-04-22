package com.ledgerlogic.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ledgerlogic.models.Account;
import com.ledgerlogic.models.EventLog;
import com.ledgerlogic.repositories.EventLogRepository;
import org.springframework.stereotype.Service;
import com.ledgerlogic.dtos.AccountBalance;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class EventLogService {

    private EventLogRepository eventLogRepository;

    public EventLogService(EventLogRepository eventLogRepository){
        this.eventLogRepository = eventLogRepository;
    }

    public List<EventLog> getEventLog(){
        return this.eventLogRepository.findAll();
    }

    public EventLog saveEventLog(EventLog userEventLog) {
        if (userEventLog.getCurrentState() != null) {
            if (userEventLog.getCurrentState().startsWith("Account(")) {
                // Convert the Account object to JSON string
                String currentStateJson = convertAccountToJson(userEventLog.getCurrentState());
                userEventLog.setCurrentState(currentStateJson);
            } else if (userEventLog.getCurrentState().startsWith("Journal{")) {
                // Convert the Journal object to JSON string
                String currentStateJson = convertJournalToJson(userEventLog.getCurrentState());
                userEventLog.setCurrentState(currentStateJson);
            }
        }
        return this.eventLogRepository.save(userEventLog);
    }

    private String convertAccountToJson(String accountString) {
        // Remove the surrounding "Account(" and ")" from the string
        String accountJson = accountString.substring("Account(".length(), accountString.length() - 1);
        // Replace single quotes with double quotes
        accountJson = accountJson.replace('\'', '\"');
        // Enclose keys in double quotes
        accountJson = accountJson.replaceAll("(\\w+)=", "\"$1\":");
        // Remove extra double quotes and commas from string values
        accountJson = accountJson.replaceAll("\"(\\w+)\":", "$1:");
        accountJson = accountJson.replaceAll("\",\\s*\"", "\", \"");
        // Fix the "null," issue for accountId
        accountJson = accountJson.replace("\"accountId\":\"null,", "\"accountId\":null,");
        return "{" + accountJson + "}";
    }

    private String convertJournalToJson(String journalString) {
        // Remove the surrounding "Journal{" and "}" from the string
        String journalJson = journalString.substring("Journal{".length(), journalString.length() - 1);
        // Replace single quotes with double quotes
        journalJson = journalJson.replace('\'', '\"');
        // Enclose keys in double quotes
        journalJson = journalJson.replaceAll("(\\w+)=", "\"$1\":");
        return "{" + journalJson + "}";
    }

    public List<AccountBalance> getAggregatedAccountBalancesByDateRange(LocalDate startDate, LocalDate endDate) {
        List<EventLog> eventLogs = eventLogRepository.findByModificationTimeBetween(startDate.atStartOfDay(), endDate.atStartOfDay().plusDays(1));
        System.out.println("Event Logs: " + eventLogs);

        Map<Long, AccountBalance> accountBalanceMap = new HashMap<>();

        for (EventLog eventLog : eventLogs) {
            System.out.println("Current State: " + eventLog.getCurrentState());

            AccountBalance accountBalance = parseAccountBalance(eventLog.getCurrentState());
            System.out.println("Parsed Account Balance: " + accountBalance);

            if (accountBalance != null) {
                Long accountId = accountBalance.getAccountId();
                accountBalanceMap.put(accountId, accountBalance);
            }
        }

        System.out.println("Account Balance Map: " + accountBalanceMap);

        return new ArrayList<>(accountBalanceMap.values());
    }

    private AccountBalance parseAccountBalance(String currentState) {
        if (currentState == null) {
            return null;
        }

        if (currentState.startsWith("{accountId:null,")) {
            // Custom format: {accountId:null, accountNumber:1030, ...}
            Map<String, String> accountMap = new HashMap<>();
            String[] pairs = currentState.substring(1, currentState.length() - 1).split(", ");
            for (String pair : pairs) {
                String[] keyValue = pair.split(":");
                accountMap.put(keyValue[0].trim(), keyValue[1].trim());
            }

            AccountBalance accountBalance = new AccountBalance();
            accountBalance.setAccountName(accountMap.get("accountName"));
            String balanceString = accountMap.get("balance");
            if (balanceString != null) {
                accountBalance.setBalance(new BigDecimal(balanceString));
            }
            accountBalance.setAccountNumber(accountMap.get("accountNumber"));
            accountBalance.setCategory(accountMap.get("category"));
            accountBalance.setSubCategory(accountMap.get("subCategory"));

            return accountBalance;
        } else if (currentState.startsWith("{accountId:")) {
            // Custom format: {accountId:1, accountNumber:1010, ...}
            Map<String, String> accountMap = new HashMap<>();
            String[] pairs = currentState.substring(1, currentState.length() - 1).split(", ");
            for (String pair : pairs) {
                String[] keyValue = pair.split(":");
                accountMap.put(keyValue[0].trim(), keyValue[1].trim());
            }

            AccountBalance accountBalance = new AccountBalance();
            String accountIdString = accountMap.get("accountId");
            if (!"null".equals(accountIdString)) {
                accountBalance.setAccountId(Long.parseLong(accountIdString));
            }
            accountBalance.setAccountName(accountMap.get("accountName"));
            String balanceString = accountMap.get("balance");
            if (balanceString != null) {
                accountBalance.setBalance(new BigDecimal(balanceString));
            }
            accountBalance.setAccountNumber(accountMap.get("accountNumber"));
            accountBalance.setCategory(accountMap.get("category"));
            accountBalance.setSubCategory(accountMap.get("subCategory"));

            return accountBalance;
        }

        return null;
    }
}