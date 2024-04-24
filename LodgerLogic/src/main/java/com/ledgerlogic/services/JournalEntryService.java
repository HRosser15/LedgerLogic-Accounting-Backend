package com.ledgerlogic.services;

import com.ledgerlogic.exceptions.InvalidJournalEntryException;
import com.ledgerlogic.models.Account;
import com.ledgerlogic.models.JournalEntry;
import com.ledgerlogic.models.JournalLine;
import com.ledgerlogic.repositories.JournalEntryRepository;
import com.ledgerlogic.repositories.JournalLineRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public class JournalEntryService {

    private JournalEntryRepository  journalEntryRepository;
    private AccountService          accountService;

    private JournalLineRepository journalLineRepository;

    public JournalEntryService(JournalEntryRepository journalEntryRepository,
                               AccountService accountService,
                               JournalLineRepository journalLineRepository) {
        this.journalEntryRepository = journalEntryRepository;
        this.accountService = accountService;
        this.journalLineRepository = journalLineRepository;
    }



    public JournalEntry addJournalEntry(JournalEntry journalEntry) throws InvalidJournalEntryException{
        if (journalEntry.getCredit() != journalEntry.getDebit())
            throw new InvalidJournalEntryException("credit and debits not balanced!");
        Account account     = journalEntry.getAccount();
        BigDecimal credit   = journalEntry.getCredit();
        BigDecimal debit    = journalEntry.getDebit();
        String description = journalEntry.getDescription();

        String category     = account.getCategory().toLowerCase().trim();
        String subcategory  = account.getSubCategory().toLowerCase().trim();

        Account accountToUpdate = account;

        JournalEntry newJournalEntity = new JournalEntry(credit, debit, account);
        newJournalEntity.setDescription(journalEntry.getDescription());

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

    // commented out while testing the new method I made
//    public List<JournalEntry> getByAccount(Long accountId){
//        List<JournalEntry> journalEntriesByAccountId =  this.journalEntryRepository.findJournalEntriesByAccount_AccountId(accountId);
//        if (journalEntriesByAccountId.size() == 0) return null;
//        return journalEntriesByAccountId;
//    }
//    public List<JournalEntry> getJournalEntriesByAccount(Long accountId) {
//        return journalEntryRepository.findByJournalLinesAccount_Id(accountId);
//    }

    public List<JournalEntry> getJournalEntriesByAccountName(String accountName) {
        return journalEntryRepository.findByAccountAccountName(accountName);
    }

    public JournalEntry createJournalEntry(JournalEntry journalEntry) {
        for (JournalLine line : journalEntry.getJournalLines()) {
            line.setJournalEntry(journalEntry);
        }
        return journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getJournalEntriesByAccount(Long accountId) {
        return journalEntryRepository.findByJournalLinesAccountAccountId(accountId);
    }


}
