package com.ledgerlogic.repositories;

import com.ledgerlogic.models.Account;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Transactional // This annotation does automatic rollback after each test
class AccountRepositoryTests {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserRepository userRepository;

    private Account baseAccount;

    @BeforeEach
    void setup() {  // ARRANGES for multiple tests
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
            baseAccount = accountRepository.findByAccountNumber(9999)
                    .orElseThrow(() -> new AssertionError("Account not found"));  // Retrieve existing account
        }
    }

    // ======================= CRUD Operations =======================

    @Test
    void saveAccount_ValidAccount_PersistsSuccessfully() {

        // Arrange
        int accountNumber = (int) (Math.random() * 100000);
        while (accountRepository.existsByAccountNumber(accountNumber)) {
            accountNumber = (int) (Math.random() * 100000);
        }

        Account account = accountRepository.save(Account.builder()
                .accountNumber(accountNumber)
                .accountName("Saved Account")
                .description("This is a save test account")
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

        accountRepository.save(account);

// Act
        Optional<Account> foundAccount = accountRepository.findByAccountNumber(account.getAccountNumber());

// Assert
        assertThat(foundAccount).isPresent();
        assertThat(foundAccount.get())
                .usingRecursiveComparison()
                .ignoringFields("creationDate")
                .isEqualTo(account);
    }

    @Test
    void findAllAccounts_WhenAccountsExist_ReturnsNonEmptyList() {

        // Act
        List<Account> accountList = accountRepository.findAll();

        // Assert
        assertThat(accountList).isNotNull().isNotEmpty()
                .contains(baseAccount);
        assertThat(baseAccount.getAccountId()).isNotNull().isPositive();


    }

    @Test
    void findAccountByName_ValidName_ReturnsExpectedAccount() {

        // Act
        Optional<Account> retrievedAccount = accountRepository.findByAccountName(baseAccount.getAccountName());

        // Assert
        assertThat(retrievedAccount).isPresent().contains(baseAccount);
    }

    @Test
    void getAccountByAccountNumber_ValidNumber_ReturnsExpectedAccount() {
        Optional<Account> foundAccount = accountRepository.findByAccountNumber(9999);

        //Assert
        assertThat(foundAccount).isPresent();
        Account account = foundAccount.get();
        assertThat(account)
                .usingRecursiveComparison()
                .isEqualTo(baseAccount);

    }

    @Test
    void deleteAccount_ValidDeletion_RemovesAccount() {
        assertThat(accountRepository.existsByAccountNumber(9999)).isTrue();

        accountRepository.deleteByAccountNumber(9999);
        accountRepository.flush();

        assertThat(accountRepository.existsByAccountNumber(9999)).isFalse();

    }

    @Test
    void deleteAccount_ExistingAccount_RemovesFromRepository() {

        // Act
        Account accountToDelete = accountRepository.findByAccountName(baseAccount.getAccountName())
                .orElseThrow(() -> new AssertionError("Account not found"));
        accountRepository.delete(accountToDelete);

        // Assert
        Optional<Account> deletedAccount = accountRepository.findById(accountToDelete.getAccountId());
        assertThat(deletedAccount).isEmpty();
    }

    // ======================= Update Operations =======================
    @Test
    void updateDebitForAccount_ValidAmount_UpdatesDebitSuccessfully() {
        Optional<Account> foundAccount = accountRepository.findByAccountNumber(9999);

        //Assert
        assertThat(foundAccount).isPresent();
        Account account = foundAccount.get();

        BigDecimal initialDebit = account.getDebit();
        account.setDebit(BigDecimal.TEN);
        BigDecimal newDebit = account.getDebit();

        assertThat(account.getDebit()).isEqualTo(BigDecimal.TEN);
        assertThat(newDebit).isGreaterThan(initialDebit);

    }

    @Test
    void updateCreditForAccount_ValidAmount_UpdatesCreditSuccessfully() {
        Optional<Account> foundAccount = accountRepository.findByAccountNumber(9999);

        //Assert
        assertThat(foundAccount).isPresent();
        Account account = foundAccount.get();

        BigDecimal initialCredit = account.getCredit();
        account.setCredit(BigDecimal.TEN);
        BigDecimal newCredit = account.getCredit();

        assertThat(account.getCredit()).isEqualTo(BigDecimal.TEN);
        assertThat(newCredit).isGreaterThan(initialCredit);

    }

    @Test
    void updateAccount_ValidUpdates_ReturnsUpdatedAccount() {

        // Act
        Account retrievedAccount = accountRepository.findByAccountName(baseAccount.getAccountName())
                .orElseThrow(() -> new AssertionError("Account not found"));
        retrievedAccount.setAccountName("Renamed Base Account");
        retrievedAccount.setDebit(BigDecimal.valueOf(50));
        retrievedAccount.setBalance(BigDecimal.valueOf(50));

        Account updatedAccount = accountRepository.save(retrievedAccount);

        // Assert
        assertThat(updatedAccount.getAccountName()).isNotNull();
        assertThat(updatedAccount.getBalance()).isEqualTo(BigDecimal.valueOf(50));
    }

    @SuppressWarnings("java:S5778") // Multiple DB operations required for proper test
    @Test
    void updateAccount_NonExistentName_ThrowsException() {
        // Verify the account doesn't exist
        assertThat(accountRepository.findByAccountName("NonExistentAccount")).isEmpty();

        // Assert throws an exception
        assertThatThrownBy(() -> {
            accountRepository.findByAccountName("NonExistentAccount")
                    .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        }).isInstanceOf(EntityNotFoundException.class);
    }

    // ======================= Business Rules =======================

    @Test
    void deleteAccountReferencedByJournalEntries_AccountHasForeignKeyConstraint_ThrowsDataIntegrityViolation() {
        Account accountToDelete = accountRepository.findByAccountName("Cash")
                .orElseThrow(() -> new AssertionError("Account not found"));

        accountRepository.delete(accountToDelete);

        assertThatThrownBy(() -> accountRepository.flush())  // since JPA batches db operations, it may not execute them immediately, flush forces the delete operation
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("could not execute statement")
                .hasRootCauseInstanceOf(SQLException.class);
    }

//    @Test
//    void saveAccount_DuplicateAccountNumber_IsHandledByController() {
//        // Arrange - create an account with a unique number we control
//        int uniqueNumber = 99999;
//        Account testAccount = Account.builder()
//                .accountNumber(uniqueNumber)
//                .accountName("Test Account")
//                .normalSide("Debit")
//                .category("Asset")
//                .subCategory("Test")
//                .build();
//
//        accountRepository.saveAndFlush(testAccount);
//
//        // Now we know an account with this number exists
//
//        // Verify the controller rejects duplicate numbers
//        Account duplicateAccount = Account.builder()
//                .accountNumber(uniqueNumber)
//                .accountName("Different Name")  // Different name
//                .normalSide("Debit")
//                .category("Asset")
//                .subCategory("Test")
//                .build();
//
//        // This might succeed at the database level (no constraint)
//        // but should be rejected by service/controller logic
//        accountRepository.save(duplicateAccount);
//
//        // Verify we have the expected state
//        List<Account> accounts = accountRepository.findAll();
//        long count = accounts.stream()
//                .filter(a -> a.getAccountNumber() == uniqueNumber)
//                .count();
//
//        // Should have exactly one account with this number if our service
//        // layer or controller is preventing duplicates
//        assertEquals(1, count);
//    }

    // ======================= Edge Cases =======================
    @Test void findByAccountNumber_NonExistentNumber_ReturnsEmptyOptional() {
        Optional<Account> nonExistentAccount = accountRepository.findByAccountNumber(60606);
        assertThat(nonExistentAccount).isEmpty();
    }

    @Test void findByAccountName_NonExistentName_ReturnsEmptyOptional() {
        Optional<Account> nonExistentAccount = accountRepository.findByAccountName("DoesNotExist");
        assertThat(nonExistentAccount).isEmpty();
    }
    @Test void existsByAccountNumber_InvalidNumber_ReturnsFalse() {
        int invalidAccountNumber = 9999888;
        assertThat(accountRepository.existsByAccountNumber(invalidAccountNumber)).isFalse();
    }

    // Data Integrity
    @Test void saveAccount_InitialBalanceScale_HasTwoDecimalPrecision() {
        Account failedAccount = Account.builder()
                .accountNumber(90009)
                .accountName("Failed Account")
                .description("This is a test account with too many decimals")
                .normalSide("Debit")
                .category("Asset")
                .subCategory("Hard Money")
                .initialBalance(BigDecimal.valueOf(50.0089))
                .debit(BigDecimal.valueOf(50.0089)) // More than 2 decimal places
                .credit(BigDecimal.ZERO)
                .balance(BigDecimal.valueOf(50.0089))
                .creationDate(new Date())
                .orderNumber(999)
                .statement("Base statement")
                .comment("Base comment")
                .build();

        // Act & Assert
        assertThatThrownBy(() -> {
            accountRepository.saveAndFlush(failedAccount); // Force the database to execute the save operation
        })
                .isInstanceOf(InvalidDataAccessApiUsageException.class)
                .hasMessageContaining("Monetary values must have")
                .hasRootCauseInstanceOf(IllegalArgumentException.class);
    }
    @Test void saveAccount_NegativeDebit_ThrowsConstraintViolation() {
        Account failedAccount = Account.builder()
                .accountNumber(90009)
                .accountName("Failed Account")
                .description("This is a test account with negative debit")
                .normalSide("Debit")
                .category("Asset")
                .subCategory("Hard Money")
                .initialBalance(BigDecimal.valueOf(50.00))
                .debit(BigDecimal.valueOf(-50.00))
                .credit(BigDecimal.ZERO)
                .balance(BigDecimal.valueOf(-50.00))
                .creationDate(new Date())
                .orderNumber(999)
                .statement("Base statement")
                .comment("Base comment")
                .build();

        // Act & Assert
        assertThatThrownBy(() -> {
            accountRepository.saveAndFlush(failedAccount); // Force the database to execute the save operation
        })
                .isInstanceOf(InvalidDataAccessApiUsageException.class)
                .hasMessageContaining("Debit and Credit amounts cannot be negative")
                .hasRootCauseInstanceOf(IllegalArgumentException.class);
    }

}
