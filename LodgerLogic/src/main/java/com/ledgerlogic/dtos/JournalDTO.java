package com.ledgerlogic.dtos;

import com.ledgerlogic.models.Journal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JournalDTO {

    private Long            journalId;
    private Journal.Status  status;
    private String          rejectionReason;
}
