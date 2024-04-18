package com.ledgerlogic.controllers;

import com.ledgerlogic.dtos.JournalDTO;
import com.ledgerlogic.models.Journal;
import com.ledgerlogic.models.JournalEntry;
import com.ledgerlogic.services.JournalService;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
//@CrossOrigin("*")
@RequestMapping("/journal")
public class JournalController {

    private JournalService journalService;
    public JournalController(JournalService journalService){
        this.journalService = journalService;
    }
    @PostMapping("/addJournal")
    public Journal addJournal(@RequestBody Journal journal, @RequestParam Long userId) {
        List<JournalEntry> journalEntries = journal.getJournalEntries();
        if (journalEntries != null) {
            for (JournalEntry entry : journalEntries) {
                entry.setJournal(journal);
                entry.setDescription(entry.getDescription()); // Add this line
            }
            journal.setJournalEntries(journalEntries);
        }
        return this.journalService.addJournal(journal, userId);
    }

    @PostMapping("/approveJournal/{journalId}/{newStatus}")
    public Journal approveJournal(@PathVariable Long journalId, @PathVariable String newStatus) {
        Journal.Status status = Journal.Status.valueOf(newStatus.toUpperCase());
        return this.journalService.approveJournal(journalId, status);
    }

    @PostMapping("/rejectJournal")
    public Journal rejectJournal(@RequestBody JournalDTO journalDTO){
        return this.journalService.rejectJournal(journalDTO);
    }

    @GetMapping("/getAll")
    public List<Journal> getAll(){
        return this.journalService.getAllJournals();
    }

    @GetMapping("/getByStatus/{status}")
    public List<Journal> getByStatus(@PathVariable Journal.Status status){
        return this.journalService.getByStatus(status);
    }

    @GetMapping("/getByDate")
    public List<Journal> getByDate(@RequestBody Date date){
        return this.journalService.getByDate(date);
    }
}
