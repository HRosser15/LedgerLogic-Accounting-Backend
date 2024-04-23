package com.ledgerlogic.controllers;

import com.ledgerlogic.models.EventLog;
import com.ledgerlogic.services.EventLogService;
import com.ledgerlogic.services.AggregatedAccountBalanceService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eventLog")
public class EventLogController {

    private final EventLogService eventLogService;
    private final AggregatedAccountBalanceService aggregatedAccountBalanceService;

    public EventLogController(EventLogService eventLogService, AggregatedAccountBalanceService aggregatedAccountBalanceService) {
        this.eventLogService = eventLogService;
        this.aggregatedAccountBalanceService = aggregatedAccountBalanceService;
    }

    @GetMapping("/getAggregatedAccountBalancesByDateRange")
    public List<Map<String, Object>> getAggregatedAccountBalancesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return aggregatedAccountBalanceService.getAggregatedAccountBalancesByDateRange(startDate, endDate);
    }

    @GetMapping("/getAll")
    public List<EventLog> getEventLog() {
        return eventLogService.getEventLog();
    }
}