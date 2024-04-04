package com.ledgerlogic.controllers;

import com.ledgerlogic.models.GeneralLedger;
import com.ledgerlogic.services.GeneralLedgerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/generalLodger")
public class GeneralLedgerController {

    GeneralLedgerService generalLedgerService;

    public GeneralLedgerController(GeneralLedgerService generalLedgerService){
        this.generalLedgerService = generalLedgerService;
    }

    @GetMapping("/etAll")
    public List<GeneralLedger> getAll(){
        return this.generalLedgerService.getGeneralLedger();
    }

    @GetMapping("/getByEntryType/{entryType}")
    public List<GeneralLedger> getByEntryType(@PathVariable GeneralLedger.EntryType entryType){
        return this.generalLedgerService.getByEntryType(entryType);
    }

}
