package com.ledgerlogic.repositories;

import com.ledgerlogic.models.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {

    List<JournalEntry> findJournalEntriesByAccount_AccountId(Long accountId);
}
