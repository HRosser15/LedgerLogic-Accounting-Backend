package com.ledgerlogic.controllers;

import com.ledgerlogic.models.JournalEntry;
import com.ledgerlogic.models.JournalLine;
import com.ledgerlogic.services.JournalEntryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/journalEntries")
public class JournalEntryController {

    private final JournalEntryService journalEntryService;

    public JournalEntryController(JournalEntryService journalEntryService) {
        this.journalEntryService = journalEntryService;
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createJournalEntry(@RequestBody JournalEntry journalEntry) {
        JournalEntry createdJournalEntry = journalEntryService.createJournalEntry(journalEntry);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdJournalEntry);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<List<JournalEntry>> getJournalEntriesByAccount(@PathVariable Long accountId) {
        List<JournalEntry> journalEntries = journalEntryService.getJournalEntriesByAccount(accountId);
        return ResponseEntity.ok(journalEntries);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<JournalEntry>> getJournalEntriesByAccountId(@PathVariable Long accountId) {
        List<JournalEntry> journalEntries = journalEntryService.getJournalEntriesByAccount(accountId);
        return ResponseEntity.ok(journalEntries);
    }

    @GetMapping("/getByAccountName/{accountName}")
    public ResponseEntity<List<JournalEntry>> getJournalEntriesByAccountName(@PathVariable String accountName) {
        List<JournalEntry> journalEntries = journalEntryService.getJournalEntriesByAccountName(accountName);
        return ResponseEntity.ok(journalEntries);
    }
}
