package com.ledgerlogic.controllers;

import com.ledgerlogic.models.EventLog;
import com.ledgerlogic.services.EventLogService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import com.ledgerlogic.dtos.AccountBalance;

//@CrossOrigin("*")
@RestController
@RequestMapping("/eventLog")
public class EventLogController {

    EventLogService eventLogService;

    public EventLogController(EventLogService eventLogService){
        this.eventLogService = eventLogService;
    }

    @GetMapping("/getAggregatedAccountBalancesByDateRange")
    public List<AccountBalance> getAggregatedAccountBalancesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return eventLogService.getAggregatedAccountBalancesByDateRange(startDate, endDate);
    }
    @GetMapping("/getAll")
    public List<EventLog> getEventLog(){
        return this.eventLogService.getEventLog();
    }
}
