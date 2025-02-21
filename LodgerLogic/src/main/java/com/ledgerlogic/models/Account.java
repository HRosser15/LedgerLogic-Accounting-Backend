package com.ledgerlogic.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_seq")
    @SequenceGenerator(name = "account_seq", sequenceName = "account_seq", allocationSize = 1)
    private Long accountId;

    @NotNull(message = "Account number is required")
    @Column(unique = true)
    private int         accountNumber;

    @NotNull(message = "Account name is required")
    @Column(unique = true)
    private String      accountName;

    private String      description;

    @NotNull(message = "Normal side is required")
    private String normalSide;

    @NotNull(message = "Category is required")
    private String category;

    private boolean     active = true;
    private String      subCategory;

    @Column(precision = 19, scale = 2)
    private BigDecimal initialBalance = BigDecimal.ZERO;

    @Column(precision = 19, scale = 2)
    private BigDecimal debit = BigDecimal.ZERO;

    @Column(precision = 19, scale = 2)
    private BigDecimal credit = BigDecimal.ZERO;

    @Column(precision = 19, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    private Date creationDate;
    private int orderNumber;
    private String statement;
    private String comment;

    @ManyToOne
    private User owner;

    @PrePersist
    @PreUpdate
    private void validate() {
        // Validate decimals
        if (hasMoreThanTwoDecimals(initialBalance) ||
                hasMoreThanTwoDecimals(debit) ||
                hasMoreThanTwoDecimals(credit) ||
                hasMoreThanTwoDecimals(balance)) {
            throw new IllegalArgumentException("Monetary values must have at most 2 decimal places");
        }

        // Validate no negative values
        if (debit != null && debit.compareTo(BigDecimal.ZERO) < 0 ||
                credit != null && credit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Debit and Credit amounts cannot be negative");
        }
    }

    private boolean hasMoreThanTwoDecimals(BigDecimal value) {
        return value != null && value.scale() > 2;
    }

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
}