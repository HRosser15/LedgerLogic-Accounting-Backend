package com.ledgerlogic.services;

import com.ledgerlogic.exceptions.ResourceNotFoundException;
import com.ledgerlogic.models.Account;
import com.ledgerlogic.models.EventLog;
import com.ledgerlogic.models.User;
import com.ledgerlogic.repositories.AccountRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
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
        return account.orElse(null);
    }

    public List<Account> getAll() {
        return this.accountRepository.findAll();
    }


    public Account upsert(Account account) {
        account.setCreationDate(new Date());
        Account previousState = null;
        if (account.getAccountId() != null) {
            previousState = this.accountRepository.getById(account.getAccountId());
        }

        String previousStateString = previousState != null ? previousState.toString() : null;
        EventLog eventLog = new EventLog("Update Account", account.getAccountId(), getCurrentUserId(), LocalDateTime.now(), account.toString(), previousStateString);
        this.eventLogService.saveEventLog(eventLog);


        return this.accountRepository.save(account);
    }

    public Optional<List<Account>> getAllByUser(User user) {
        Optional<List<Account>> accounts = this.accountRepository.findAllByOwner(user);
        return accounts.map(Optional::of).orElse(null);
    }

    public void delete(Account account) {
        Account previousState = this.accountRepository.getById(account.getAccountId());
        EventLog userEventLog = new EventLog("Delete Account", account.getAccountId(), getCurrentUserId(), LocalDateTime.now(), null, previousState.toString());
        this.eventLogService.saveEventLog(userEventLog);

        this.accountRepository.delete(account);
    }

    public Account update(Long accountId, Account account) {
        Account existingAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        // Create a copy of the existing account to represent the previous state
        Account previousAccountState = new Account(
                existingAccount.getAccountNumber(),
                existingAccount.getAccountName(),
                existingAccount.getDescription(),
                existingAccount.getNormalSide(),
                existingAccount.getCategory(),
                existingAccount.isActive(),
                existingAccount.getSubCategory(),
                existingAccount.getInitialBalance(),
                existingAccount.getDebit(),
                existingAccount.getCredit(),
                existingAccount.getBalance(),
                existingAccount.getCreationDate(),
                existingAccount.getOrderNumber(),
                existingAccount.getStatement(),
                existingAccount.getComment()
        );

        existingAccount.setAccountNumber(account.getAccountNumber());
        existingAccount.setAccountName(account.getAccountName());
        existingAccount.setDescription(account.getDescription());
        existingAccount.setNormalSide(account.getNormalSide());
        existingAccount.setCategory(account.getCategory());
        existingAccount.setSubCategory(account.getSubCategory());
        existingAccount.setInitialBalance(account.getInitialBalance());
        existingAccount.setDebit(account.getDebit());
        existingAccount.setCredit(account.getCredit());
        existingAccount.setBalance(account.getBalance());
        existingAccount.setOrderNumber(account.getOrderNumber());
        existingAccount.setStatement(account.getStatement());
        existingAccount.setComment(account.getComment());

        Account updatedAccount = accountRepository.save(existingAccount);

        EventLog userEventLog = new EventLog("Update Account", accountId, getCurrentUserId(), LocalDateTime.now(),
                updatedAccount.toString(), previousAccountState.toString());
        eventLogService.saveEventLog(userEventLog);

        return updatedAccount;
    }

    public Account getByAccountNumber(int accountNumber) {
        Optional<Account> userAccount = this.accountRepository.findByAccountNumber(accountNumber);
        return userAccount.orElse(null);
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
        if (accountToDeactivateOptional.isPresent()) {
            Account accountToDeactivate = accountToDeactivateOptional.get();
            if (!accountToDeactivate.getBalance().equals(BigDecimal.ZERO)) {
                String previousState = accountToDeactivate.isActive() ? "true" : "false";
                String currentState = "false";

                EventLog userEventLog = new EventLog("Deactivate Account", accountId, getCurrentUserId(), LocalDateTime.now(), currentState, previousState);
                this.eventLogService.saveEventLog(userEventLog);

                accountToDeactivate.setActive(false);
                Account updatedAccount = this.accountRepository.save(accountToDeactivate);

                return updatedAccount;
            }
            return null;
        }
        return null;
    }

    public Account reactivate(Long accountId) {
        Optional<Account> accountToReactivateOptional = this.accountRepository.findById(accountId);
        if (accountToReactivateOptional.isPresent()) {
            Account accountToReactivate = accountToReactivateOptional.get();
            if (!accountToReactivate.getBalance().equals(BigDecimal.ZERO)) {
                String previousState = accountToReactivate.isActive() ? "false" : "true";
                String currentState = "true";

                EventLog userEventLog = new EventLog("Reactivate Account", accountId, getCurrentUserId(), LocalDateTime.now(), currentState, previousState);
                this.eventLogService.saveEventLog(userEventLog);

                accountToReactivate.setActive(true);
                Account updatedAccount = this.accountRepository.save(accountToReactivate);

                return updatedAccount;
            }
            return null;
        }
        return null;
    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                User user = (User) principal;
                return user.getUserId();
            }
        }
        return null;
    }


}