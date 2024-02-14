package com.ledgerlogic.services;

import com.ledgerlogic.annotations.Admin;
import com.ledgerlogic.annotations.Manager;
import com.ledgerlogic.models.Account;
import com.ledgerlogic.models.User;
import com.ledgerlogic.repositories.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    @Admin
    @Manager
    public List<Account> getAll(){
        return this.accountRepository.findAll();
    }

    public Account upsert(Account account){
        return this.accountRepository.save(account);
    }

    public Optional<List<Account>> getAllByUser(User user) {
        Optional<List<Account>> accounts = this.accountRepository.findAllByOwner(user);
        if(!accounts.isPresent()){
            return null;
        }else{
            return Optional.of(accounts.get());
        }
    }

    @Admin
    public void delete(Account account) {
        this.accountRepository.delete(account);
    }
}
