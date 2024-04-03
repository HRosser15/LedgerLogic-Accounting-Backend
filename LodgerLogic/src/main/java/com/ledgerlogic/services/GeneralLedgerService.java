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

    public GeneralLedger update(GeneralLedger generalLedger){
        return this.generalLedgerRepository.save(generalLedger);
    }

    public List<GeneralLedger> getGeneralLedger(){
        return this.generalLedgerRepository.findAll();
    }

}
