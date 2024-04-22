package com.ledgerlogic.repositories;

import com.ledgerlogic.models.EventLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventLogRepository extends JpaRepository<EventLog, Long> {

        List<EventLog> findByModificationTimeBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

}
