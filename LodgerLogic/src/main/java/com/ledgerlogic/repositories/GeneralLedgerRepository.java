package com.ledgerlogic.repositories;

import com.ledgerlogic.models.GeneralLedger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface GeneralLedgerRepository extends JpaRepository<GeneralLedger, Long> {
    List<GeneralLedger> findByEntryType(GeneralLedger.EntryType entryType);
}
