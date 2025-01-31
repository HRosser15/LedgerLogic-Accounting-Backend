package com.ledgerlogic.repositories;

import com.ledgerlogic.models.Account;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class AccountRepositoryTests {

    @Autowired
    AccountRepository accountRepository;

    private Account baseAccount;

    @BeforeEach
    void setup() {  // Acts as the ARRANGE for multiple tests
        if (!accountRepository.existsByAccountNumber(9999)) {  // Ensure base account exists
            baseAccount = accountRepository.save(Account.builder()
                    .accountNumber(9999)
                    .accountName("Base Account")
                    .description("This is a base test account")
                    .normalSide("Debit")
                    .category("Asset")
                    .subCategory("Hard Money")
                    .initialBalance(BigDecimal.ZERO)
                    .debit(BigDecimal.ZERO)
                    .credit(BigDecimal.ZERO)
                    .balance(BigDecimal.ZERO)
                    .creationDate(new Date())
                    .orderNumber(999)
                    .statement("Base statement")
                    .comment("Base comment")
                    .build());
        } else {
            baseAccount = accountRepository.findByAccountNumber(9999).get();  // Retrieve existing account
        }
    }



    @Test
    void AccountRepository_GetAll_ReturnAccounts() {

        // Arrange - done is setup method

        // Act
        List<Account> accountList = accountRepository.findAll();

        // Assert
        Assertions.assertThat(accountList).isNotNull().isNotEmpty();
        Assertions.assertThat(accountList).contains(baseAccount);
        Assertions.assertThat(baseAccount.getAccountId()).isNotNull().isPositive();
    }

    @Test
    void AccountRepository_FindByAccountName_ReturnAccountNotNull() {

        // Arrange - done is setup method

        // Act
        Optional<Account> retrievedAccount = accountRepository.findByAccountName(baseAccount.getAccountName());

        // Assert
        Assertions.assertThat(retrievedAccount).isPresent();  // Ensures an account was found
        Assertions.assertThat(retrievedAccount.get()).isEqualTo(baseAccount);
    }

    @Test
    void AccountRepository_UpdateAccount_ReturnAccountNotNull() {

        // Arrange - done is setup method

        // Act
        Account retrievedAccount = accountRepository.findByAccountName(baseAccount.getAccountName()).get();
        retrievedAccount.setAccountName("Renamed Base Account");
        retrievedAccount.setDebit(BigDecimal.valueOf(50));
        retrievedAccount.setBalance(BigDecimal.valueOf(50));

        Account updatedAccount = accountRepository.save(retrievedAccount);

        // Assert
        Assertions.assertThat(updatedAccount.getAccountName()).isNotNull();
        Assertions.assertThat(updatedAccount.getBalance()).isEqualTo(BigDecimal.valueOf(50));
    }

    @Test
    public void AccountRepository_DeleteAccount_ReturnAccountNotNull() {

        // Arrange - done is setup method

        // Act
        Account accountToDelete = accountRepository.findByAccountName(baseAccount.getAccountName()).get();
        accountRepository.delete(accountToDelete);

        // Assert
        Optional<Account> deletedAccount = accountRepository.findById(accountToDelete.getAccountId());
        Assertions.assertThat(deletedAccount).isEmpty();
    }

}
