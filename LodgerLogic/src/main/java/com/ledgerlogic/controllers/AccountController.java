package com.ledgerlogic.controllers;

import com.ledgerlogic.annotations.Admin;
import com.ledgerlogic.models.Account;
import com.ledgerlogic.models.User;
import com.ledgerlogic.services.AccountService;
import com.ledgerlogic.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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

    @PostMapping("/addAccount")
    public ResponseEntity<String> create(@Valid @RequestBody Account account){
        // First check if account with same name exists
        Account accountWithSameName = this.accountService.getByAccountName(account.getAccountName());
        if (accountWithSameName == null){
            // Then check if account with same number exists
            Account accountWithSameAccountNumber = this.accountService.getByAccountNumber(account.getAccountNumber());
            if (accountWithSameAccountNumber == null){
                this.accountService.upsert(account);
                return ResponseEntity.status(HttpStatus.OK).body("Account added successfully!");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account number already used!");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account with same name already exist!");
    }

    @GetMapping("/viewAccount/{accountId}")
    public ResponseEntity<Account> view(@PathVariable Long accountId){
        Account account = this.accountService.getAccountById(accountId);
        if (account != null) {
            return ResponseEntity.ok().body(account);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(null);
    }

    @GetMapping("/getByAccountNumber/{accountNumber}")
    public ResponseEntity<Account> getByAccountNumber(@PathVariable int accountNumber){
        Account account = this.accountService.getByAccountNumber(accountNumber);
        if (account != null) {
            return ResponseEntity.ok().body(account);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(null);
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

    // replaced with method below to avoid null pointer exception
//    @GetMapping("/allAccounts")
//    public List<Account> getAllAccounts(){
//        List<Account> allAccounts = this.accountService.getAll();
//        if (allAccounts.isEmpty()) {
//            ResponseEntity.ok().body("No accounts found!");
//            return null;
//        }
//        return allAccounts;
//    }
    @GetMapping("/allAccounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> allAccounts = this.accountService.getAll();
        if (allAccounts.isEmpty()) {
            return ResponseEntity.ok()
                    .header("Accounts-Found", "false")
                    .body(List.of());
        }
        return ResponseEntity.ok().body(allAccounts);
    }

    @PutMapping("/updateAccount/{accountId}")
    public ResponseEntity<String> update(@PathVariable Long accountId, @RequestBody Account account){
        Account updatedAccount = this.accountService.update(accountId, account);
        if(updatedAccount != null)
            return ResponseEntity.status(HttpStatus.OK).body("Account updated successfully!");
        return ResponseEntity.status(HttpStatus.OK).body("Something went wrong!");
    }

    @PatchMapping("/deactivate/{accountId}")
    public ResponseEntity<String> deactivate(@PathVariable Long accountId){
        Account accountToDeactivate = this.accountService.getAccountById(accountId);
        if (accountToDeactivate != null){
            this.accountService.deactivate(accountId);
            return ResponseEntity.status(HttpStatus.OK).body("Account deactivated!");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong!");
    }

    @PatchMapping("/reactivate/{accountId}")
    public ResponseEntity<String> reactivate(@PathVariable Long accountId){
        Account accountToReactivate = this.accountService.getAccountById(accountId);
        if (accountToReactivate != null){
            this.accountService.reactivate(accountId);
            return ResponseEntity.status(HttpStatus.OK).body("Account reactivated!");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong!");
    }
}