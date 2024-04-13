package com.ledgerlogic.repositories;

import com.ledgerlogic.models.Journal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface JournalRepository extends JpaRepository<Journal, Long> {
    List<Journal> findByStatus(Journal.Status status);

    List<Journal> findByCreatedDate(Date date);



}
