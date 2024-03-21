package com.ledgerlogic.services;

import com.ledgerlogic.models.Account;
import com.ledgerlogic.models.EventLog;
import com.ledgerlogic.models.User;
import com.ledgerlogic.repositories.AccountRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final EventLogService eventLogService;


    public AccountService(AccountRepository accountRepository, EventLogService eventLogService) {
        this.accountRepository = accountRepository;
        this.eventLogService = eventLogService;
    }

    public Account getAccountById(Long accountId) {
        Optional<Account> account = this.accountRepository.findById(accountId);
        if(account.isPresent()){
            return account.get();
        }
        return null;
    }

    public List<Account> getAll() {
        return this.accountRepository.findAll();
    }

    public Account upsert(Account account) {
        Account previousState = this.accountRepository.getById(account.getAccountId());
        EventLog eventLog = new EventLog("Update Account", account.getAccountId(), getCurrentUserId(), LocalDateTime.now(), account.toString(), previousState.toString());
        this.eventLogService.saveEventLog(eventLog);

        return this.accountRepository.save(account);
    }

    public Optional<List<Account>> getAllByUser(User user) {
        Optional<List<Account>> accounts = this.accountRepository.findAllByOwner(user);
        if (!accounts.isPresent()) {
            return null;
        }
        return Optional.of(accounts.get());
    }

    public void delete(Account account) {
        Account previousState = this.accountRepository.getById(account.getAccountId());
        EventLog eventLog = new EventLog("Delete Account", account.getAccountId(), getCurrentUserId(), LocalDateTime.now(), null, previousState.toString());
        this.eventLogService.saveEventLog(eventLog);

        this.accountRepository.delete(account);
    }

    public Account update(Long accountId, Account account) {
        Optional<Account> accountToUpdate = this.accountRepository.findById(accountId);
        if (accountToUpdate.isPresent()){

            EventLog eventLog = new EventLog("Update Account", accountId, getCurrentUserId(), LocalDateTime.now(), account.toString(), accountToUpdate.toString());
            this.eventLogService.saveEventLog(eventLog);

            return this.accountRepository.save(account);
        }
        return null;
    }

    public Account getByAccountNumber(int accountNumber) {
        Optional<Account> userAccount = this.accountRepository.findByAccountNumber(accountNumber);
        if (!userAccount.isPresent())
            return null;
        return userAccount.get();
    }

    public Account getByAccountName(String accountName) {
        Optional<Account> userAccount = this.accountRepository.findByAccountName(accountName);
        if (!userAccount.isPresent())
            return null;
        return userAccount.get();
    }

    public List<Account> getAccountByOwner(User user) {
        Optional<List<Account>> userAccounts = this.accountRepository.findAllByOwner(user);
        if (userAccounts.isEmpty())
            return null;
        return userAccounts.get();
    }

    public Account deactivate(Long accountId) {
        Optional<Account> accountToDeactivateOptional = this.accountRepository.findById(accountId);
        if (accountToDeactivateOptional.isPresent()){
            Account accountToDeactivate = accountToDeactivateOptional.get();
            if (!accountToDeactivate.getBalance().equals(0)){

                EventLog eventLog = new EventLog("Deactivate Account", accountId, getCurrentUserId(), LocalDateTime.now(), "false", "true");
                this.eventLogService.saveEventLog(eventLog);

                accountToDeactivate.setActive(false);
                return this.accountRepository.save(accountToDeactivate);
            }
            return null;
        }
        return null;
    }

    public Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            return ((User) principal).getUserId();
        }
        return null;
    }
}

