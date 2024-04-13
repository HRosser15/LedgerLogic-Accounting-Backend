package com.ledgerlogic.dtos;

import com.ledgerlogic.models.Journal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JournalDTO {
    private Long journalId;
    private String status;
    private String rejectionReason;
    private byte[] attachments;
    private Date createdDate;
    private Date transactionDate;
    private Long createdBy;
}
