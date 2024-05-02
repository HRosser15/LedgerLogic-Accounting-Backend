package com.ledgerlogic.repositories;

import com.ledgerlogic.models.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
    List<JournalEntry> findByJournalLinesAccountAccountId(Long accountId);
    List<JournalEntry> findByAccountAccountName(String accountName);

}
