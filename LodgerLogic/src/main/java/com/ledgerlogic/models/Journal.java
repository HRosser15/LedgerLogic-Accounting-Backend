package com.ledgerlogic.models;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Journal {

    private Long id;
    private List<BigDecimal> debits;
    private List<BigDecimal> credits;
    private BigDecimal amount;
    private String description;
    private String status; // pending, approved, rejected
    private String rejectionReason;
    private List<String> attachments; // source documents
    private String createdBy; // User ID
    private Date createdDate;

    @OneToOne
    private Account account;
}
