package com.ledgerlogic.dtos;

import com.ledgerlogic.models.Journal;
import com.ledgerlogic.models.JournalEntry;
import com.ledgerlogic.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class JournalResponseDTO {
    @Setter
    private Long id;
    @Setter
    private Journal.Status status;
    @Setter
    private String rejectionReason;
    @Setter
    private BigDecimal balance;
    @Setter
    private Date createdDate;
    @Setter
    private Date transactionDate;
    @Setter
    private User createdBy;
    @Setter
    private List<JournalEntry> journalEntries;

    public Long getId() {
        return id;
    }

    public Journal.Status getStatus() {
        return status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public List<JournalEntry> getJournalEntries() {
        return journalEntries;
    }

    public static JournalResponseDTO fromJournal(Journal journal) {
        JournalResponseDTO dto = new JournalResponseDTO();
        dto.setId(journal.getJournalId());
        dto.setStatus(journal.getStatus());
        dto.setRejectionReason(journal.getRejectionReason());
        dto.setBalance(journal.getBalance());
        dto.setCreatedDate(journal.getCreatedDate());
        dto.setTransactionDate(journal.getTransactionDate());
        dto.setCreatedBy(journal.getCreatedBy());
        dto.setJournalEntries(journal.getJournalEntries());
        return dto;
    }

}