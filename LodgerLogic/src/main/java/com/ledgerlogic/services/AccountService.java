package com.ledgerlogic.services;

import com.ledgerlogic.models.Account;
import com.ledgerlogic.repositories.AccountRepository;

import java.util.List;
import java.util.Optional;

public class AccountService {

    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public List<Account> getAll(){
        return this.accountRepository.findAll();
    }

    public Account upsert(Account account){
        return this.accountRepository.save(account);
    }

    public Optional<List<Account>> getAllByUserId(int id) {
        Optional<List<Account>> accounts = this.accountRepository.findAllByUserId(id);
        if(!accounts.isPresent()){
            return null;
        }else{
            return Optional.of(accounts.get());
        }
    }

    public void delete(Account account) {
        this.accountRepository.delete(account);
    }
}
