package com.ledgerlogic.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.NotFound;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long        accountId;
    @NonNull
    private int         accountNumber;
    @NonNull
    private String      accountName;
    @NonNull
    private String      description = "";
    @NonNull
    private String      normalSide;
    @NonNull
    private String      category;
    private String      subCategory;
    private BigDecimal  initialBalance  = BigDecimal.ZERO;
    private BigDecimal  debit           = BigDecimal.ZERO;
    private BigDecimal  credit          = BigDecimal.ZERO;
    private BigDecimal  balance         = BigDecimal.ZERO;
    private Date        creationDate;
    private int         orderNumber;
    private String      statement;
    private String      comment;

    public Account(int accountNumber, String accountName, String description, String normalSide, String category){
        this.accountNumber  = accountNumber;
        this.accountName    = accountName;
        this.description    = description;
        this.normalSide     = normalSide;
        this.category       = category;
    }

    @ManyToOne
    @JoinColumn(name = "userId")
    private User owner;

}
