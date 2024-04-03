package com.ledgerlogic.repositories;

import com.ledgerlogic.models.GeneralLedger;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GeneralLedgerRepository extends JpaRepository<GeneralLedger, Long> {
}
