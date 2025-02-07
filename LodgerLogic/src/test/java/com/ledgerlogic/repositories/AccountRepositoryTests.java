/**
 *

 TODO
 - Edge cases:
 - Business rules: balance checks during deactivation, event logging.
 - find JPA function to rollback db after each test✅ (@Transactional)
 - do naive test --> output == input✅ (getAccountByAccountNumber_ShouldReturnExpectedAccountDetails)
     Change debit affect accountBalance? (not applicable at repository level as logic is handled at service level)
 - build tests based on requirements
 - test that accounts that act as foreign keys to journals cannot be deleted

 - CRUD Operations:
      * Save an account.
      * Find by ID, account number, account name.
      * Delete by account number.
      * Check if an account number exists.
 - Custom Queries:
      * Find all accounts by owner.
 - Edge Cases:
       * Search for non-existent account name/number.
       * invalid inputs
       * duplicate account names/numbers
       * empty responses
 */

package com.ledgerlogic.repositories;

import com.ledgerlogic.models.Account;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    void getAllAccounts_WhenAccountsExist_ReturnsNonEmptyList() {

        // Arrange - done is setup method

        // Act
        List<Account> accountList = accountRepository.findAll();

        // Assert
        assertThat(accountList).isNotNull().isNotEmpty();
        assertThat(accountList).contains(baseAccount);
        assertThat(baseAccount.getAccountId()).isNotNull().isPositive();


    }

    @Test
    void findAccountByName_ValidName_ReturnsExpectedAccount() {

        // Arrange - done is setup method

        // Act
        Optional<Account> retrievedAccount = accountRepository.findByAccountName(baseAccount.getAccountName());

        // Assert
        assertThat(retrievedAccount).isPresent();  // Ensures an account was found
        assertThat(retrievedAccount.get()).isEqualTo(baseAccount);
    }

    @Test
    void deleteAccount_ExistingAccount_RemovesFromRepository() {

        // Arrange - done is setup method

        // Act
        Account accountToDelete = accountRepository.findByAccountName(baseAccount.getAccountName()).get();
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

        // Arrange - done is setup method

        // Act
        Account retrievedAccount = accountRepository.findByAccountName(baseAccount.getAccountName()).get();
        retrievedAccount.setAccountName("Renamed Base Account");
        retrievedAccount.setDebit(BigDecimal.valueOf(50));
        retrievedAccount.setBalance(BigDecimal.valueOf(50));

        Account updatedAccount = accountRepository.save(retrievedAccount);

        // Assert
        assertThat(updatedAccount.getAccountName()).isNotNull();
        assertThat(updatedAccount.getBalance()).isEqualTo(BigDecimal.valueOf(50));
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

    @Test void saveAccount_DuplicateAccountNumber_ThrowsException() {
        Account duplicateAccount = accountRepository.save(Account.builder()
                .accountNumber(9999)  // Duplicate account number
                .accountName("Duplicate Account")
                .description("This is a duplicate test account")
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

        assertThatThrownBy(() -> {
            accountRepository.save(duplicateAccount);
            accountRepository.flush(); // Force the database to execute the save operation
        })
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("could not execute statement")
                .hasRootCauseInstanceOf(SQLException.class);
    }

    // Custom Queries
    @Test void findAllByOwner_ValidUser_ReturnsOwnedAccounts() {

    }
    @Test void findAllByOwner_NoAccounts_ReturnsEmptyList() {

    }


    // ======================= Edge Cases =======================
    @Test void findByAccountNumber_NonExistentNumber_ReturnsEmptyOptional() {

    }
    @Test void findByAccountName_NonExistentName_ReturnsEmptyOptional() {

    }
    @Test void existsByAccountNumber_InvalidNumber_ReturnsFalse() {

    }

    // Data Integrity
    @Test void saveAccount_InitialBalanceScale_HasTwoDecimalPrecision() {

    }
    @Test void saveAccount_NegativeDebit_ThrowsConstraintViolation() {

    }

}
