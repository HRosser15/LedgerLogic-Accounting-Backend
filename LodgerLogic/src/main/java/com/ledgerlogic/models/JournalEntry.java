package com.ledgerlogic.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long                journalEntryId;
    private BigDecimal          credit;
    private BigDecimal          debit;
    private BigDecimal          balance;

    @OneToOne
    @JoinColumn(name = "accountId")
    private Account account;

    public JournalEntry(BigDecimal credit, BigDecimal debit, Account account){
        this.credit  = credit;
        this.debit   = debit;
        this.account = account;
    }
}
