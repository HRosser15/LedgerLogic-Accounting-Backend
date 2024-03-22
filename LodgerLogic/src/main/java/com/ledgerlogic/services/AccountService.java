package com.ledgerlogic.services;

import com.ledgerlogic.models.Account;
import com.ledgerlogic.models.User;
import com.ledgerlogic.repositories.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
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
        account.setCreationDate(new Date());
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
        this.accountRepository.delete(account);
    }

    public Account update(Long accountId, Account account) {
        Optional<Account> accountToUpdate = this.accountRepository.findById(accountId);
        if (accountToUpdate.isPresent())
            return this.accountRepository.save(account);
        return null;
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
        if (accountToDeactivateOptional.isPresent()){
            Account accountToDeactivate = accountToDeactivateOptional.get();
            if (!accountToDeactivate.getBalance().equals(0)){
                accountToDeactivate.setActive(false);
                return this.accountRepository.save(accountToDeactivate);
            }
            return null;
        }
        return null;
    }
}

