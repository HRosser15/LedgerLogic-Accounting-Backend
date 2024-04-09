package com.ledgerlogic.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "general_ledger")
public class GeneralLedger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EntryType entryType;

    @ManyToOne
    @JoinColumn(name = "journal_id")
    private Journal journal;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    private Date addedDate;

    public GeneralLedger(EntryType entryType, Journal journal, Account account, Date addedDate) {
        this.entryType = entryType;
        this.journal = journal;
        this.account = account;
        this.addedDate = addedDate;
    }

    public enum EntryType {
        JOURNAL, ACCOUNT
    }
}
