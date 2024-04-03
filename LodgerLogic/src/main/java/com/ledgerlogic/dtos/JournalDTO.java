package com.ledgerlogic.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JournalDTO {

    private Long    journalId;
    private String  status;
    private String  rejectionReason;
}
