package com.ledgerlogic.services;

import com.ledgerlogic.exceptions.InvalidJournalEntryException;
import com.ledgerlogic.models.Account;
import com.ledgerlogic.models.JournalEntry;
import com.ledgerlogic.repositories.JournalEntryRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class JournalEntryService {

    private JournalEntryRepository  journalEntryRepository;
    private AccountService          accountService;

    public JournalEntryService(JournalEntryRepository journalEntryRepository,
                               AccountService accountService){
        this.journalEntryRepository = journalEntryRepository;
        this.accountService         = accountService;
    }

    public JournalEntry addJournalEntry(JournalEntry journalEntry) throws InvalidJournalEntryException{
        if (journalEntry.getCredit() != journalEntry.getDebit())
            throw new InvalidJournalEntryException("credit and debits not balanced!");
        Account account     = journalEntry.getAccount();
        BigDecimal credit   = journalEntry.getCredit();
        BigDecimal debit    = journalEntry.getDebit();

        String category     = account.getCategory().toLowerCase().trim();
        String subcategory  = account.getSubCategory().toLowerCase().trim();

        Account accountToUpdate = account;

        JournalEntry newJournalEntity = new JournalEntry(credit, debit, account);

        BigDecimal newBalance = BigDecimal.ZERO;
        BigDecimal currentBalance = accountToUpdate.getBalance();

        if (category.contains("asset")){
            newBalance.add(currentBalance.add(debit));
            newBalance.add(newBalance.subtract(credit));
        }else if(category.contains("liability")){
            newBalance.add(currentBalance.subtract(debit));
            newBalance.add(newBalance.add(credit));
        }else if(category.contains("equity")){
            if (subcategory.contains("drawing")){
                newBalance.add(currentBalance.add(debit));
                newBalance.add(newBalance.subtract(credit));
            }
            newBalance.add(currentBalance.subtract(debit));
            newBalance.add(newBalance.add(credit));
        }else if(category.contains("revenue")){
            newBalance.add(currentBalance.subtract(debit));
            newBalance.add(newBalance.add(credit));
        }else if(category.contains("expense")){
            newBalance.add(currentBalance.add(debit));
            newBalance.add(newBalance.subtract(credit));
        }

        newJournalEntity.setBalance(newBalance);
        return this.journalEntryRepository.save(newJournalEntity);
    }

    public List<JournalEntry> findAll(){
        return this.journalEntryRepository.findAll();
    }

    public List<JournalEntry> getByAccount(Long accountId){
        List<JournalEntry> journalEntriesByAccountId =  this.journalEntryRepository.findJournalEntriesByAccount_AccountId(accountId);
        if (journalEntriesByAccountId.size() == 0) return null;
        return journalEntriesByAccountId;
    }
}
