package com.ledgerlogic.controllers;

import com.ledgerlogic.models.EventLog;
import com.ledgerlogic.services.EventLogService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/eventLog")
public class EventLogController {

    EventLogService eventLogService;

    public EventLogController(EventLogService eventLogService){
        this.eventLogService = eventLogService;
    }

    @GetMapping("/getAll")
    public List<EventLog> getEventLog(){
        return this.eventLogService.getEventLog();
    }
}
