package com.ledgerlogic.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_seq")
    @SequenceGenerator(name = "account_seq", sequenceName = "account_seq", allocationSize = 1)
    private Long accountId;
    private int         accountNumber;
    private String      accountName;
    private String      description;
    private String      normalSide;
    private String      category;
    private boolean     active = true;
    private String      subCategory;
    private BigDecimal  initialBalance  = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    private BigDecimal  debit           = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    private BigDecimal  credit          = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    private BigDecimal  balance         = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
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
        this.creationDate   = new Date();
    }

    public Account(int accountNumber, String accountName, String description, String normalSide, String category,
                   boolean active, String subCategory, BigDecimal initialBalance, BigDecimal debit, BigDecimal credit,
                   BigDecimal balance, Date creationDate, int orderNumber, String statement, String comment) {
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.description = description;
        this.normalSide = normalSide;
        this.category = category;
        this.active = active;
        this.subCategory = subCategory;
        this.initialBalance = initialBalance;
        this.debit = debit;
        this.credit = credit;
        this.balance = balance;
        this.creationDate = creationDate;
        this.orderNumber = orderNumber;
        this.statement = statement;
        this.comment = comment;
    }

    @ManyToOne
    private User owner;

}