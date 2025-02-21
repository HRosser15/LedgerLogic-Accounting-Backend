package com.ledgerlogic.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "journal_entry_seq")
    @SequenceGenerator(name = "journal_entry_seq", sequenceName = "journal_entry_seq", allocationSize = 1)
    private Long                journalEntryId;
    private BigDecimal          credit;
    private BigDecimal          debit;
    private BigDecimal          balance;
    private String              status="pending";
    private String              rejectionReason;
    private Date                transactionDate;

    @Column(name = "description")
    private String              description;

    @ManyToOne
    @JoinColumn(name = "accountId")
    private Account account;

    @OneToMany(mappedBy = "journalEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JournalLine> journalLines;

    @ManyToOne
    @JoinColumn(name = "journal_id")
    @JsonIgnore
    private Journal journal;

    public JournalEntry(BigDecimal credit, BigDecimal debit, Account account){
        this.credit  = credit;
        this.debit   = debit;
        this.account = account;
    }

    @PostLoad
    @PostPersist
    @PostUpdate
    private void calculateBalance() {
        this.balance = this.debit.subtract(this.credit);
    }

    @Override
    public String toString() {
        return "JournalEntry{" +
                "journalEntryId=" + journalEntryId +
                ", credit=" + credit +
                ", debit=" + debit +
                ", balance=" + balance +
                ", status='" + status + '\'' +
                ", rejectionReason='" + rejectionReason + '\'' +
                ", account=" + (account != null ? account.getAccountId() : null) +
                '}';
    }


}
