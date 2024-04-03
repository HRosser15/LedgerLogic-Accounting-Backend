package com.ledgerlogic.services;

import com.ledgerlogic.models.Account;
import com.ledgerlogic.models.EventLog;
import com.ledgerlogic.models.JournalEntry;
import com.ledgerlogic.repositories.JournalEntryRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class JournalEntryService {

    private JournalEntryRepository  journalEntryRepository;
    private AccountService          accountService;

    public JournalEntryService(JournalEntryRepository journalEntryRepository,
                               AccountService accountService){
        this.journalEntryRepository = journalEntryRepository;
        this.accountService         = accountService;
    }

    public JournalEntry saveJournalEntity(BigDecimal credit, BigDecimal debit, Account account){

        String category     = account.getCategory().toLowerCase().trim();
        String subcategory  = account.getSubCategory().toLowerCase().trim();

        Account accountToUpdate = account;

        JournalEntry newJournalEntity = new JournalEntry(credit, debit, account);

        BigDecimal newBalance = BigDecimal.ZERO;
        BigDecimal currentBalance = accountToUpdate.getBalance();

        if (category.contains("asset")){
            newBalance.add(currentBalance.add(debit));
            newBalance.add(newBalance.subtract(credit));
            accountToUpdate.setBalance(newBalance);
        }else if(category.contains("liability")){
            newBalance.add(currentBalance.subtract(debit));
            newBalance.add(newBalance.add(credit));
            accountToUpdate.setBalance(newBalance);
        }else if(category.contains("equity")){
            if (subcategory.contains("drawing")){
                newBalance.add(currentBalance.add(debit));
                newBalance.add(newBalance.subtract(credit));
                accountToUpdate.setBalance(newBalance);
            }
            newBalance.add(currentBalance.subtract(debit));
            newBalance.add(newBalance.add(credit));
            accountToUpdate.setBalance(newBalance);

        }else if(category.contains("revenue")){
            newBalance.add(currentBalance.subtract(debit));
            newBalance.add(newBalance.add(credit));
            accountToUpdate.setBalance(newBalance);
        }else if(category.contains("expense")){
            newBalance.add(currentBalance.add(debit));
            newBalance.add(newBalance.subtract(credit));
            accountToUpdate.setBalance(newBalance);
        }

        this.accountService.update(accountToUpdate.getAccountId(), accountToUpdate);
        return this.journalEntryRepository.save(newJournalEntity);
    }
}
