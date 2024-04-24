package com.ledgerlogic.services;

import com.ledgerlogic.models.Account;
import com.ledgerlogic.models.EventLog;
import com.ledgerlogic.repositories.AccountRepository;
import com.ledgerlogic.repositories.EventLogRepository;
import org.springframework.stereotype.Service;
import com.ledgerlogic.dtos.AccountBalance;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class EventLogService {
    private final AccountRepository accountRepository;
    private final EventLogRepository eventLogRepository;

    public EventLogService(AccountRepository accountRepository, EventLogRepository eventLogRepository) {
        this.accountRepository = accountRepository;
        this.eventLogRepository = eventLogRepository;
    }

//    public EventLogService(EventLogRepository eventLogRepository){
//        this.eventLogRepository = eventLogRepository;
//    }

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

//    public List<Map<String, Object>> getAggregatedAccountBalancesByDateRange(LocalDate startDate, LocalDate endDate) {
//        List<EventLog> eventLogs = eventLogRepository.findByModificationTimeBetween(startDate.atStartOfDay(), endDate.atStartOfDay().plusDays(1));
//
//        Map<Long, EventLog> latestEventLogsByAccountId = new HashMap<>();
//
//        // Find the latest event log entry for each account within the date range
//        for (EventLog eventLog : eventLogs) {
//            AccountBalance accountBalance = parseAccountBalance(eventLog.getCurrentState());
//            if (accountBalance != null && accountBalance.getAccountId() != null) {
//                Long accountId = accountBalance.getAccountId();
//                EventLog existingEventLog = latestEventLogsByAccountId.get(accountId);
//                if (existingEventLog == null || eventLog.getModificationTime().isAfter(existingEventLog.getModificationTime())) {
//                    latestEventLogsByAccountId.put(accountId, eventLog);
//                }
//            }
//        }
//
//        // Get all accounts from the database
//        List<Account> allAccounts = accountRepository.findAll();
//
//        List<Map<String, Object>> accountDetails = new ArrayList<>();
//
//        // Process the latest event log entries and create account details maps
//        for (Account account : allAccounts) {
//            EventLog eventLog = latestEventLogsByAccountId.get(account.getAccountId());
//            AccountBalance accountBalance;
//            if (eventLog != null) {
//                accountBalance = parseAccountBalance(eventLog.getCurrentState());
//            } else {
//                // If no event log entry exists for the account, use the account details directly
//                accountBalance = new AccountBalance();
//                accountBalance.setAccountId(account.getAccountId());
//                accountBalance.setAccountName(account.getAccountName());
//                accountBalance.setAccountNumber(String.valueOf(account.getAccountNumber()));
//                accountBalance.setCategory(account.getCategory());
//                accountBalance.setSubCategory(account.getSubCategory());
//                accountBalance.setDebit(account.getDebit());
//                accountBalance.setCredit(account.getCredit());
//            }
//
//            if (accountBalance != null) {
//                Map<String, Object> accountDetailsMap = new HashMap<>();
//                accountDetailsMap.put("accountId", accountBalance.getAccountId());
//                accountDetailsMap.put("accountName", accountBalance.getAccountName());
//                accountDetailsMap.put("accountNumber", accountBalance.getAccountNumber());
//                accountDetailsMap.put("category", accountBalance.getCategory());
//                accountDetailsMap.put("subCategory", accountBalance.getSubCategory());
//                accountDetailsMap.put("debit", accountBalance.getDebit() != null ? accountBalance.getDebit() : BigDecimal.ZERO);
//                accountDetailsMap.put("credit", accountBalance.getCredit() != null ? accountBalance.getCredit() : BigDecimal.ZERO);
//                BigDecimal balance = accountBalance.getDebit().subtract(accountBalance.getCredit());
//                accountDetailsMap.put("balance", balance);
//                accountDetails.add(accountDetailsMap);
//            }
//        }
//
//        return accountDetails;
//    }
//
//    private AccountBalance parseAccountBalance(String currentState) {
//        if (currentState == null) {
//            return null;
//        }
//
//        if (currentState.startsWith("{accountId:null,")) {
//            // Custom format: {accountId:null, accountNumber:1030, ...}
//            Map<String, String> accountMap = new HashMap<>();
//            String[] pairs = currentState.substring(1, currentState.length() - 1).split(", ");
//            for (String pair : pairs) {
//                String[] keyValue = pair.split(":");
//                accountMap.put(keyValue[0].trim(), keyValue[1].trim());
//            }
//
//            AccountBalance accountBalance = new AccountBalance();
//            accountBalance.setAccountName(accountMap.get("accountName"));
//            accountBalance.setAccountNumber(accountMap.get("accountNumber"));
//            accountBalance.setCategory(accountMap.get("category"));
//            accountBalance.setSubCategory(accountMap.get("subCategory"));
//
//            String debitString = accountMap.get("debit");
//            if (debitString != null) {
//                accountBalance.setDebit(new BigDecimal(debitString));
//            } else {
//                accountBalance.setDebit(BigDecimal.ZERO);
//            }
//
//            String creditString = accountMap.get("credit");
//            if (creditString != null) {
//                accountBalance.setCredit(new BigDecimal(creditString));
//            } else {
//                accountBalance.setCredit(BigDecimal.ZERO);
//            }
//
//            // Calculate the balance by subtracting credit from debit
//            BigDecimal balance = accountBalance.getDebit().subtract(accountBalance.getCredit());
//            accountBalance.setBalance(balance);
//
//            return accountBalance;
//        } else if (currentState.startsWith("{accountId:")) {
//            // Custom format: {accountId:1, accountNumber:1010, ...}
//            Map<String, String> accountMap = new HashMap<>();
//            String[] pairs = currentState.substring(1, currentState.length() - 1).split(", ");
//            for (String pair : pairs) {
//                String[] keyValue = pair.split(":");
//                accountMap.put(keyValue[0].trim(), keyValue[1].trim());
//            }
//
//            AccountBalance accountBalance = new AccountBalance();
//            String accountIdString = accountMap.get("accountId");
//            if (!"null".equals(accountIdString)) {
//                accountBalance.setAccountId(Long.parseLong(accountIdString));
//            }
//            accountBalance.setAccountName(accountMap.get("accountName"));
//            accountBalance.setAccountNumber(accountMap.get("accountNumber"));
//            accountBalance.setCategory(accountMap.get("category"));
//            accountBalance.setSubCategory(accountMap.get("subCategory"));
//
//            String debitString = accountMap.get("debit");
//            if (debitString != null) {
//                accountBalance.setDebit(new BigDecimal(debitString));
//            } else {
//                accountBalance.setDebit(BigDecimal.ZERO);
//            }
//
//            String creditString = accountMap.get("credit");
//            if (creditString != null) {
//                accountBalance.setCredit(new BigDecimal(creditString));
//            } else {
//                accountBalance.setCredit(BigDecimal.ZERO);
//            }
//
//            // Calculate the balance by subtracting credit from debit
//            BigDecimal balance = accountBalance.getDebit().subtract(accountBalance.getCredit());
//            accountBalance.setBalance(balance);
//
//            return accountBalance;
//        }
//
//        return null;
//    }
}