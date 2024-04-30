package com.ledgerlogic.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ledgerlogic.dtos.JournalDTO;
import com.ledgerlogic.dtos.JournalResponseDTO;
import com.ledgerlogic.models.Journal;
import com.ledgerlogic.models.JournalEntry;
import com.ledgerlogic.services.JournalService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @PostMapping(value = "/addJournal", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JournalResponseDTO addJournal(@RequestParam("journal") String journalString,
                                         @RequestParam(value = "attachedFile", required = false) MultipartFile attachedFile,
                                         @RequestParam(value = "attachedFileContentType", required = false) String attachedFileContentType,
                                         @RequestParam("userId") Long userId) {
        System.out.println("Received journal: " + journalString);
        if (attachedFile != null) {
            System.out.println("Received file with size: " + attachedFile.getSize());
        }
        System.out.println("Received content type: " + attachedFileContentType);

        // Parse the journalString to a Journal object using Jackson
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Journal journal;
        try {
            journal = objectMapper.readValue(journalString, Journal.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse journal string", e);
        }

        Journal savedJournal = this.journalService.addJournal(journal, attachedFile, attachedFileContentType, userId);
        return JournalResponseDTO.fromJournal(savedJournal);
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
