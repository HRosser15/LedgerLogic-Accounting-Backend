package com.ledgerlogic.controllers;

import com.ledgerlogic.annotations.Admin;
import com.ledgerlogic.models.Account;
import com.ledgerlogic.models.User;
import com.ledgerlogic.services.AccountService;
import com.ledgerlogic.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//@CrossOrigin("*")
@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;
    private final UserService userService;

    public AccountController(AccountService accountService, UserService userService){
        this.accountService = accountService;
        this.userService = userService;
    }

    @GetMapping("/viewAccount/{accountId}")
    public ResponseEntity<Account> view(@PathVariable Long accountId){
        Account account = this.accountService.getAccountById(accountId);
        if (account != null)return ResponseEntity.ok().body(account);
        ResponseEntity.status(HttpStatus.NOT_FOUND).body("The account with id " + accountId + "not found!");
        return null;
    }

    @GetMapping("/getByAccountNumber/{accountNumber}")
    public ResponseEntity<Account> getByAccountNumber(@PathVariable int accountNumber){
        Account account = this.accountService.getByAccountNumber(accountNumber);
        if (account != null)return ResponseEntity.ok().body(account);
        ResponseEntity.status(HttpStatus.NOT_FOUND).body("The account with id " + accountNumber + "not found!");
        return null;
    }

    @GetMapping("/getByAccountName/{accountName}")
    public ResponseEntity<Account> getByAccountName(@PathVariable String accountName){
        Account account = this.accountService.getByAccountName(accountName);
        if (account != null){
            return ResponseEntity.ok().body(account);
        }
        ResponseEntity.status(HttpStatus.NOT_FOUND).body("The account with id " + accountName + "not found!");
        return null;
    }

    @GetMapping("/getAllUserAccounts/{userId}")
    public ResponseEntity<List<Account>> getAllUserAccounts(@PathVariable Long userId){
        Optional<User> user = Optional.ofNullable(this.userService.getById(userId));
        if (user.isPresent()){
            List<Account> userAccounts = this.accountService.getAccountByOwner(user.get());
            return ResponseEntity.ok().body(userAccounts);
        }
        ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        return null;
    }

    @GetMapping("/allAccounts")
    public List<Account> getAllAccounts(){
        List<Account> allAccounts = this.accountService.getAll();
        if (allAccounts.isEmpty()) {
            ResponseEntity.ok().body("No accounts found!");
            return null;
        }
        return allAccounts;
    }

//    @Admin
    @PostMapping("/addAccount")
    public ResponseEntity<String> create(@RequestBody Account account){
        Account accountWithSameName = this.accountService.getByAccountName(account.getAccountName());

        if (accountWithSameName != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account with the same name already exists!");
        }

        Account accountWithSameAccountNumber = this.accountService.getByAccountNumber(account.getAccountNumber());

        if (accountWithSameAccountNumber != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account number already used!");
        }

        this.accountService.upsert(account);
        return ResponseEntity.status(HttpStatus.OK).body("Account added successfully!");
    }

//    @Admin
    @PutMapping("/updateAccount/{accountId}")
    public ResponseEntity<String> update(@PathVariable Long accountId, @RequestBody Account account){
       Account updatedAccount = this.accountService.update(accountId, account);
       if(updatedAccount != null)
           return ResponseEntity.status(HttpStatus.OK).body("Account updated successfully!");
       return ResponseEntity.status(HttpStatus.OK).body("Something went wrong!");
    }

//    @Admin
    @PatchMapping("/deactivate/{accountId}")
    public ResponseEntity<String> deactivate(@PathVariable Long accountId){
        Account accountToDeactivate = this.accountService.getAccountById(accountId);
        if (accountToDeactivate != null){
            this.accountService.deactivate(accountId);
            return ResponseEntity.status(HttpStatus.OK).body("Account deactivated!");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong!");
    }
}