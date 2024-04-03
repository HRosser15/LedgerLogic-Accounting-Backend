package com.ledgerlogic.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String           status = "pending";
    private String           rejectionReason;
    @Lob
    private byte[]           attachments;
    private Date             createdDate;

    @ManyToOne
    private User             createdBy;

    @OneToMany
    private List<JournalEntry> journalEntries;
}
