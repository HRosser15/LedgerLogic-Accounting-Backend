package com.ledgerlogic.services;

import com.ledgerlogic.models.EventLog;
import com.ledgerlogic.repositories.EventLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventLogService {

    private EventLogRepository eventLogRepository;

    public EventLogService(EventLogRepository eventLogRepository){
        this.eventLogRepository = eventLogRepository;
    }

    public List<EventLog> getEventLog(){
        return this.eventLogRepository.findAll();
    }

    public EventLog saveEventLog(EventLog userEventLog){
        return this.eventLogRepository.save(userEventLog);
    }
}