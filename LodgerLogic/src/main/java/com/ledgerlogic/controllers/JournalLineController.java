package com.ledgerlogic.controllers;

import com.ledgerlogic.models.JournalLine;
import com.ledgerlogic.services.JournalLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/journal-lines")
public class JournalLineController {

    private final JournalLineService journalLineService;

    @Autowired
    public JournalLineController(JournalLineService journalLineService) {
        this.journalLineService = journalLineService;
    }

    @PostMapping
    public ResponseEntity<JournalLine> createJournalLine(@RequestBody JournalLine journalLine) {
        JournalLine createdJournalLine = journalLineService.createJournalLine(journalLine);
        return new ResponseEntity<>(createdJournalLine, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<JournalLine>> getAllJournalLines() {
        List<JournalLine> journalLines = journalLineService.getAllJournalLines();
        return new ResponseEntity<>(journalLines, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JournalLine> getJournalLineById(@PathVariable Long id) {
        JournalLine journalLine = journalLineService.getJournalLineById(id);
        return new ResponseEntity<>(journalLine, HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<JournalLine> updateJournalLine(@PathVariable Long id, @RequestBody JournalLine journalLine) {
        JournalLine updatedJournalLine = journalLineService.updateJournalLine(id, journalLine);
        return new ResponseEntity<>(updatedJournalLine, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJournalLine(@PathVariable Long id) {
        journalLineService.deleteJournalLine(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}