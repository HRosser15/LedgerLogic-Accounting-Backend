package com.ledgerlogic.repositories;

import com.ledgerlogic.models.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
    List<JournalEntry> findByJournalLinesAccountAccountId(Long accountId);
    List<JournalEntry> findByAccountAccountName(String accountName);

    // SPRINT 4 NOTE: this was my attempt at returning the journalId with the journal entry to make approving/rejecting easier.
//    @Query("SELECT je FROM JournalEntry je JOIN je.account a WHERE a.accountName = :accountName")
//    List<JournalEntry> findByAccountAccountName(@Param("accountName") String accountName);

    // commented this line out as it may not be needed. left it here in case we revert to older methods.
//    List<JournalEntry> findJournalEntriesByAccount_AccountId(Long accountId);
}
