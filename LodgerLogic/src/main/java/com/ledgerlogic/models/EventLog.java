package com.ledgerlogic.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "userEventLog")
public class EventLog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_log_seq")
    @SequenceGenerator(name = "event_log_seq", sequenceName = "event_log_seq", allocationSize = 1)
    private Long            id;
    private String          title;
    private Long            entityId;
    private Long            modifiedById;
    private LocalDateTime   modificationTime;
    @Lob
    private String          currentState;
    @Lob
    private String          previousState;

    public EventLog(String title, Long entityId, Long modifiedById, LocalDateTime modificationTime, String currentState, String previousState) {
        this.title              = title;
        this.entityId           = entityId;
        this.modifiedById       = modifiedById;
        this.modificationTime   = modificationTime;
        this.currentState       = currentState;
        this.previousState      = previousState;
    }
}
