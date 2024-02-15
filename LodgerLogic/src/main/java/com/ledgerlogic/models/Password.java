package com.ledgerlogic.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "password")
public class Password {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long passwordId;
    private String content;
    private Date expirationDate;
}
