package com.ledgerlogic.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Journal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long             journalId;
    private Status           status = Status.PENDING;
    private String           rejectionReason;
    private BigDecimal       balance;
    @Lob
    private byte[]           attachments;
    private Date             createdDate;

    @ManyToOne
    private User             createdBy;

    @Setter
    @OneToMany(mappedBy = "journal")
    private List<JournalEntry> journalEntries;

    public Journal(String rejectionReason, byte[] attachments, Date createdDate, User createdBy, List<JournalEntry> journalEntries){
        this.rejectionReason = rejectionReason;
        this.attachments     = attachments;
        this.createdDate     = createdDate;
        this.createdBy       = createdBy;
        this.journalEntries  = journalEntries;
    }

    public Journal(String rejectionReason, byte[] attachments, Date createdDate, User createdBy) {
        this.rejectionReason = rejectionReason;
        this.attachments = attachments;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
    }

    public enum Status {
        PENDING, APPROVED, REJECTED
    }

    @Override
    public String toString() {
        return "Journal{" +
                "journalId=" + journalId +
                ", status=" + status +
                ", rejectionReason='" + rejectionReason + '\'' +
                ", balance=" + balance +
                ", createdDate=" + createdDate +
                '}';
    }
}
