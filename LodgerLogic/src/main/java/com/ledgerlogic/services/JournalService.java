package com.ledgerlogic.services;

import com.ledgerlogic.models.EventLog;
import com.ledgerlogic.models.Journal;
import com.ledgerlogic.models.JournalEntry;
import com.ledgerlogic.repositories.JournalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class JournalService {

    private JournalRepository   journalRepository;
    private JournalEntryService journalEntryService;
    private EventLogService     eventLogService;

    public JournalService(JournalRepository journalRepository,EventLogService eventLogService, JournalEntryService journalEntryService) {
        this.journalRepository   = journalRepository;
        this.journalEntryService = journalEntryService;
        this.eventLogService     = eventLogService;
    }

    public Journal approveJournal(Long id, String newStatus) {
        Optional<Journal> optionalJournal = this.journalRepository.findById(id);
        if (optionalJournal.isPresent()) {
            Journal journal = optionalJournal.get();
            if (newStatus.toLowerCase().equals("approved")) {
                List<JournalEntry> journalEntries = journal.getJournalEntries();
                for (JournalEntry journalEntry: journalEntries){
                    this.journalEntryService.saveJournalEntity(journalEntry.getCredit(), journalEntry.getDebit(), journalEntry.getAccount());
                }

                EventLog userEventLog = new EventLog("Approved New Journal", journal.getJournalId(), journal.getCreatedBy().getUserId(), LocalDateTime.now(), journal.toString(), null);
                this.eventLogService.saveEventLog(userEventLog);

                this.journalRepository.save(journal);
            }
        }
        return null;
    }

    public Journal rejectJournal(Long id, String status, String reasonForRejection){
        Optional<Journal> optionalJournal = this.journalRepository.findById(id);
        if (optionalJournal.isPresent()){
            Journal previousState = optionalJournal.get();
            Journal updatedJournal = previousState;
            updatedJournal.setStatus("rejected");
            updatedJournal.setRejectionReason(reasonForRejection);

            EventLog userEventLog = new EventLog("Rejected New Journal", updatedJournal.getJournalId(), updatedJournal.getCreatedBy().getUserId(), LocalDateTime.now(), updatedJournal.toString(), previousState.toString());
            this.eventLogService.saveEventLog(userEventLog);

            return this.journalRepository.save(updatedJournal);
        }
        return null;
    }

    public List<Journal> getByStatus(String status){
        return this.journalRepository.findByStatus(status);
    }

    public List<Journal> getByDate(Date date){
        return this.journalRepository.findByCreatedDate(date);
    }

}
