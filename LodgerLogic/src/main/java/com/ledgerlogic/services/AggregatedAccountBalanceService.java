package com.ledgerlogic.services;

import com.ledgerlogic.dtos.AccountBalance;
import com.ledgerlogic.models.Account;
import com.ledgerlogic.models.EventLog;
import com.ledgerlogic.repositories.EventLogRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AggregatedAccountBalanceService {
    private final AccountService accountService;
    private final EventLogRepository eventLogRepository;

    public AggregatedAccountBalanceService(AccountService accountService, EventLogRepository eventLogRepository) {
        this.accountService = accountService;
        this.eventLogRepository = eventLogRepository;
    }

    public List<Map<String, Object>> getAggregatedAccountBalancesByDateRange(LocalDate startDate, LocalDate endDate) {
        List<EventLog> eventLogs = eventLogRepository.findByModificationTimeBetween(startDate.atStartOfDay(), endDate.atStartOfDay().plusDays(1));
        Map<Long, EventLog> latestEventLogsByAccountId = new HashMap<>();

        // Find the latest event log entry for each account within the date range
        for (EventLog eventLog : eventLogs) {
            AccountBalance accountBalance = parseAccountBalance(eventLog.getCurrentState());

            if (accountBalance != null && accountBalance.getAccountId() != null) {
                Long accountId = accountBalance.getAccountId();
                EventLog existingEventLog = latestEventLogsByAccountId.get(accountId);

                if (existingEventLog == null || eventLog.getModificationTime().isAfter(existingEventLog.getModificationTime())) {
                    latestEventLogsByAccountId.put(accountId, eventLog);
                }
            }
        }

        // Get all accounts from the database
        List<Account> allAccounts = accountService.getAll();

        List<Map<String, Object>> accountDetails = new ArrayList<>();

        // Process the latest event log entries and create account details maps
        for (Account account : allAccounts) {
            EventLog eventLog = latestEventLogsByAccountId.get(account.getAccountId());
            AccountBalance accountBalance;

            if (eventLog != null) {
                accountBalance = parseAccountBalance(eventLog.getCurrentState());
            } else {
                // If no event log entry exists for the account, use the account details directly
                accountBalance = new AccountBalance();
                accountBalance.setAccountId(account.getAccountId());
                accountBalance.setAccountName(account.getAccountName());
                accountBalance.setAccountNumber(String.valueOf(account.getAccountNumber())); // Convert int to String
                accountBalance.setCategory(account.getCategory());
                accountBalance.setSubCategory(account.getSubCategory());
                accountBalance.setDebit(account.getDebit());
                accountBalance.setCredit(account.getCredit());
            }

            if (accountBalance != null) {
                Map<String, Object> accountDetailsMap = new HashMap<>();

                accountDetailsMap.put("accountId", accountBalance.getAccountId());
                accountDetailsMap.put("accountName", accountBalance.getAccountName());
                accountDetailsMap.put("accountNumber", accountBalance.getAccountNumber());
                accountDetailsMap.put("category", accountBalance.getCategory());
                accountDetailsMap.put("subCategory", accountBalance.getSubCategory());
                accountDetailsMap.put("debit", accountBalance.getDebit() != null ? accountBalance.getDebit() : BigDecimal.ZERO);
                accountDetailsMap.put("credit", accountBalance.getCredit() != null ? accountBalance.getCredit() : BigDecimal.ZERO);

                // Calculate the balance based on the account category
                BigDecimal balance;
                if (accountBalance.getCategory().equalsIgnoreCase("Liabilities") ||
                        accountBalance.getCategory().equalsIgnoreCase("Equity") ||
                        accountBalance.getCategory().equalsIgnoreCase("Revenue")) {
                    balance = accountBalance.getCredit().subtract(accountBalance.getDebit());
                } else {
                    balance = accountBalance.getDebit().subtract(accountBalance.getCredit());
                }
                accountDetailsMap.put("balance", balance);

                accountDetails.add(accountDetailsMap);
            }
        }

        return accountDetails;
    }
    private AccountBalance parseAccountBalance(String currentState) {
        if (currentState == null) {
            return null;
        }

        Map<String, String> accountMap = new HashMap<>();
        String[] pairs = currentState.substring(1, currentState.length() - 1).split(", ");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            if (keyValue.length == 2) {
                accountMap.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }

        AccountBalance accountBalance = new AccountBalance();
        accountBalance.setAccountName(accountMap.get("accountName"));
        accountBalance.setAccountNumber(accountMap.get("accountNumber"));
        accountBalance.setCategory(accountMap.get("category"));
        accountBalance.setSubCategory(accountMap.get("subCategory"));

        String debitString = accountMap.get("debit");
        if (debitString != null) {
            accountBalance.setDebit(new BigDecimal(debitString));
        } else {
            accountBalance.setDebit(BigDecimal.ZERO);
        }

        String creditString = accountMap.get("credit");
        if (creditString != null) {
            accountBalance.setCredit(new BigDecimal(creditString));
        } else {
            accountBalance.setCredit(BigDecimal.ZERO);
        }

        String category = accountMap.get("category");
        BigDecimal balance;
        if (category != null && (category.equalsIgnoreCase("Liabilities") ||
                category.equalsIgnoreCase("Revenue") ||
                category.equalsIgnoreCase("Equity"))) {
            balance = accountBalance.getCredit().subtract(accountBalance.getDebit());
        } else {
            balance = accountBalance.getDebit().subtract(accountBalance.getCredit());
        }
        accountBalance.setBalance(balance);

        String accountIdString = accountMap.get("accountId");
        if (accountIdString != null && !"null".equals(accountIdString)) {
            accountBalance.setAccountId(Long.parseLong(accountIdString));
        }

        return accountBalance;
    }
}
