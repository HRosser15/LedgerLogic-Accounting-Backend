package com.ledgerlogic.services;

import com.ledgerlogic.dtos.JournalDTO;
import com.ledgerlogic.models.*;
import com.ledgerlogic.repositories.JournalRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class JournalService {

    private JournalRepository   journalRepository;
    private AccountService      accountService;
    private EventLogService     eventLogService;

    private EmailService        emailService;

    public JournalService(JournalRepository journalRepository,EventLogService eventLogService, AccountService accountService) {
        this.journalRepository   = journalRepository;
        this.accountService      = accountService;
        this.eventLogService     = eventLogService;
    }

    public Journal addJournal(Journal journal){
        List<JournalEntry> journalEntries = journal.getJournalEntries();
        if (journalEntries.size() == 0) return null;

        EventLog userEventLog = new EventLog("Added new Journal", journal.getJournalId(), journal.getCreatedBy().getUserId(), LocalDateTime.now(), journal.toString(), null);
        this.eventLogService.saveEventLog(userEventLog);

        this.emailService.send(journal.getCreatedBy().getAdmin().getEmail(),"autoprocess@ledgerlogic.com", "New Journal Created", "New Journal is created by " + journal.getCreatedBy().getFirstName() + " " + journal.getCreatedBy().getLastName());

        return this.journalRepository.save(journal);
    }

    public Journal approveJournal(Long id, String newStatus) {
        Optional<Journal> optionalJournal = this.journalRepository.findById(id);
        if (optionalJournal.isPresent()) {
            Journal journal = optionalJournal.get();
            Journal previousState = journal;
            if (newStatus.toLowerCase().equals("approved")) {
                journal.setStatus("approved");

                for (JournalEntry journalEntry: journal.getJournalEntries()){
                    Account accountToUpdate = journalEntry.getAccount();
                    accountToUpdate.setBalance(journalEntry.getBalance());
                    BigDecimal newCredit = accountToUpdate.getCredit().subtract(journalEntry.getCredit());
                    BigDecimal newDebit  = accountToUpdate.getDebit().subtract(journalEntry.getDebit());
                    accountToUpdate.setCredit(newCredit);
                    accountToUpdate.setDebit(newDebit);
                    this.accountService.update(accountToUpdate.getAccountId(), accountToUpdate);
                }

                EventLog userEventLog = new EventLog("Approved New Journal", journal.getJournalId(), journal.getCreatedBy().getUserId(), LocalDateTime.now(), journal.toString(), previousState.toString());
                this.eventLogService.saveEventLog(userEventLog);

                this.journalRepository.save(journal);
            }
        }
        return null;
    }

    public Journal rejectJournal(JournalDTO journalDTO){
        Optional<Journal> optionalJournal = this.journalRepository.findById(journalDTO.getJournalId());
        if (optionalJournal.isPresent()){
            Journal previousState = optionalJournal.get();
            Journal updatedJournal = previousState;
            updatedJournal.setStatus("rejected");
            updatedJournal.setRejectionReason(journalDTO.getRejectionReason());

            EventLog userEventLog = new EventLog("Rejected New Journal", updatedJournal.getJournalId(), updatedJournal.getCreatedBy().getUserId(), LocalDateTime.now(), updatedJournal.toString(), previousState.toString());
            this.eventLogService.saveEventLog(userEventLog);

            return this.journalRepository.save(updatedJournal);
        }
        return null;
    }

    public List<Journal> getAllJournals(){
        return this.journalRepository.findAll();
    }

    public List<Journal> getByStatus(String status){
        return this.journalRepository.findByStatus(status);
    }

    public List<Journal> getByDate(Date date){
        return this.journalRepository.findByCreatedDate(date);
    }

}
