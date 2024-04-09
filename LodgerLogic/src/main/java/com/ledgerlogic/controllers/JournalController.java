package com.ledgerlogic.controllers;

import com.ledgerlogic.dtos.JournalDTO;
import com.ledgerlogic.models.Journal;
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
    public Journal addJournal(@RequestBody Journal journal){
        return this.journalService.addJournal(journal);
    }

    @PostMapping("/approveJournal/{journalId}/{newStatus}")
    public Journal approveJournal(@PathVariable Long journalId, @PathVariable Journal.Status newStatus){
        return this.journalService.approveJournal(journalId, newStatus);
    }

    @PostMapping("/rejectJournal")
    public Journal rejectJournal(@RequestBody JournalDTO journalDTO){
        return this.journalService.rejectJournal(journalDTO);
    }

    @GetMapping("/getAll")
    public List<Journal> getAlll(){
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
