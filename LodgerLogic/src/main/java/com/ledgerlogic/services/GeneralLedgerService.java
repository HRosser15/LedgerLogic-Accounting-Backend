package com.ledgerlogic.services;

import com.ledgerlogic.models.GeneralLedger;
import com.ledgerlogic.repositories.GeneralLedgerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeneralLedgerService {

    private GeneralLedgerRepository generalLedgerRepository;

    public GeneralLedgerService(GeneralLedgerRepository generalLedgerRepository){
        this.generalLedgerRepository = generalLedgerRepository;
    }

    public GeneralLedger add(GeneralLedger generalLedger){
        if (generalLedger.getJournal().equals(null) && generalLedger.getAccount().equals(null))
            return null;

        return this.generalLedgerRepository.save(generalLedger);
    }
    public GeneralLedger update(GeneralLedger generalLedger){
        return this.generalLedgerRepository.save(generalLedger);
    }

    public List<GeneralLedger> getByEntryType(GeneralLedger.EntryType entryType){
        return this.generalLedgerRepository.findByEntryType(entryType);
    }

    public List<GeneralLedger> getGeneralLedger(){
        return this.generalLedgerRepository.findAll();
    }

}
