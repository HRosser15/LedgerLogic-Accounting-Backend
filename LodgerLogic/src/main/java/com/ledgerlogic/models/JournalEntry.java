package com.ledgerlogic.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

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
    private String              status="pending";
    private String              rejectionReason;

    @ManyToOne
    @JoinColumn(name = "accountId")
    private Account account;

    @OneToMany(mappedBy = "journalEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JournalLine> journalLines;

    @Setter
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
