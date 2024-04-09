package com.ledgerlogic.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {

    private int         accountNumber;
    private String      accountName;
    private String      description;
    private String      normalSide;
    private String      category;
    private boolean     active = true;
    private String      subCategory;
    private BigDecimal initialBalance  = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    private BigDecimal  debit           = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    private BigDecimal  credit          = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    private BigDecimal  balance         = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    private Date creationDate;
    private int         orderNumber;
    private String      statement;
    private String      comment;
}
