package com.ledgerlogic.controllers;

import com.ledgerlogic.annotations.Authorized;
import com.ledgerlogic.models.Account;
import com.ledgerlogic.services.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    @Authorized
    @GetMapping("/userAccounts")
    public ResponseEntity<List<Account>> getAllUserAccounts(){
        return ResponseEntity.ok(this.accountService.getAll());
    }

    @Authorized
    @PutMapping("/updateAccount")
    public ResponseEntity<Account> upsertAccount(@RequestBody Account account){
        return ResponseEntity.ok(this.accountService.upsert(account));
    }
}
