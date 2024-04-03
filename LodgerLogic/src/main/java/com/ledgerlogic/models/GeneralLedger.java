package com.ledgerlogic.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneralLedger {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long generalLedgerId;
    private String type;

}
