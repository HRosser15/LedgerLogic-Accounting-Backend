package com.ledgerlogic.repositories;

import com.ledgerlogic.models.EventLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventLogRepository extends JpaRepository<EventLog, Long> {
}
