package com.ledgerlogic.services;

import com.ledgerlogic.dtos.JournalDTO;
import com.ledgerlogic.models.*;
import com.ledgerlogic.repositories.JournalEntryRepository;
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

    private JournalEntryRepository journalEntryRepository;

    public JournalService(JournalRepository journalRepository,
                          EventLogService eventLogService,
                          AccountService accountService,
                          EmailService emailService,
                          JournalEntryRepository journalEntryRepository) {
        this.journalRepository = journalRepository;
        this.accountService = accountService;
        this.eventLogService = eventLogService;
        this.emailService = emailService;
        this.journalEntryRepository = journalEntryRepository;
    }

    public Journal addJournal(Journal journal, Long userId) {
        List<JournalEntry> journalEntries = journal.getJournalEntries();
        if (journalEntries != null) {
            for (JournalEntry entry : journalEntries) {
                entry.setJournal(journal);
            }
            journal.setJournalEntries(journalEntries);
        }

        EventLog userEventLog = new EventLog("Added new Journal", journal.getJournalId(), userId, LocalDateTime.now(), journal.toString(), null);
        this.eventLogService.saveEventLog(userEventLog);

        this.emailService.send("bw@gmail.com", "autoprocess@ledgerlogic.com", "New Journal Created", "New Journal is created by user with ID: " + userId, null);


         journal.setCreatedDate(new Date());
        Journal savedJournal = this.journalRepository.save(journal);
        this.journalEntryRepository.saveAll(journalEntries);
        return savedJournal;
    }

    public Journal approveJournal(Long id, Journal.Status newStatus) {
        Optional<Journal> optionalJournal = this.journalRepository.findById(id);
        if (optionalJournal.isPresent()) {
            Journal journal = optionalJournal.get();
            Journal previousState = journal;
            if (newStatus.equals(Journal.Status.APPROVED)) {
                journal.setStatus(Journal.Status.APPROVED);

                List<JournalEntry> journalEntries = journal.getJournalEntries();
                if (journalEntries != null) {
                    for (JournalEntry journalEntry : journalEntries) {
                        Account accountToUpdate = journalEntry.getAccount();
                        accountToUpdate.setBalance(journalEntry.getBalance());
                        BigDecimal newCredit = accountToUpdate.getCredit().subtract(journalEntry.getCredit());
                        BigDecimal newDebit = accountToUpdate.getDebit().subtract(journalEntry.getDebit());
                        accountToUpdate.setCredit(newCredit);
                        accountToUpdate.setDebit(newDebit);
                        this.accountService.update(accountToUpdate.getAccountId(), accountToUpdate);
                    }
                }

                EventLog userEventLog = new EventLog("Approved New Journal", journal.getJournalId(), journal.getCreatedBy().getUserId(), LocalDateTime.now(), journal.toString(), previousState.toString());
                this.eventLogService.saveEventLog(userEventLog);

                return this.journalRepository.save(journal);
            }
        }
        System.out.println("............approveJournal from JournalService: something was null");
        System.out.println("optionalJournal.isPresent(): " + optionalJournal.isPresent());
        System.out.println("newStatus.equals(Journal.Status.APPROVED): " + newStatus.equals(Journal.Status.APPROVED));

        return null;
    }

    public Journal rejectJournal(JournalDTO journalDTO) {
        if (journalDTO != null && journalDTO.getJournalId() != null) {
            Optional<Journal> optionalJournal = this.journalRepository.findById(journalDTO.getJournalId());
            if (optionalJournal.isPresent()) {
                Journal previousState = optionalJournal.get();
                Journal updatedJournal = previousState;
                updatedJournal.setStatus(Journal.Status.REJECTED);
                updatedJournal.setRejectionReason(journalDTO.getRejectionReason());

                EventLog userEventLog = new EventLog("Rejected New Journal", updatedJournal.getJournalId(), updatedJournal.getCreatedBy().getUserId(), LocalDateTime.now(), updatedJournal.toString(), previousState.toString());
                this.eventLogService.saveEventLog(userEventLog);

                return this.journalRepository.save(updatedJournal);
            }
            System.out.println("optionalJournal.isPresent(): " + optionalJournal.isPresent());
        }
        System.out.println("............rejectJournal from JournalService: something was null");
        System.out.println("journalDTO: " + journalDTO);
        System.out.println("journalDTO.getJournalId(): " + journalDTO.getJournalId());
        return null;
    }

    public List<Journal> getAllJournals(){
        return this.journalRepository.findAll();
    }

    public List<Journal> getByStatus(Journal.Status status){
        return this.journalRepository.findByStatus(status);
    }

    public List<Journal> getByDate(Date date){
        return this.journalRepository.findByCreatedDate(date);
    }

}
