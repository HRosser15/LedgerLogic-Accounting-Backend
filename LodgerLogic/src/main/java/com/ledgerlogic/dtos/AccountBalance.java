package com.ledgerlogic.dtos;

import lombok.Setter;

import java.math.BigDecimal;

public class AccountBalance {
    @Setter
    private Long accountId;
    private String accountName;
    private BigDecimal balance;
    @Setter
    private String accountNumber;
    @Setter
    private String category;
    @Setter
    private String subCategory;
    @Setter
    private BigDecimal debit;
    @Setter
    private BigDecimal credit;

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Long getAccountId() {
        return accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getCategory() {
        return category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public BigDecimal getDebit() { return debit; }

    public BigDecimal getCredit() { return credit; }
}
