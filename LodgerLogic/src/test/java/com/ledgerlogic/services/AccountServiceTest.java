package com.ledgerlogic.services;

import com.ledgerlogic.models.Account;
import com.ledgerlogic.models.EventLog;
import com.ledgerlogic.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    // Mocks are empty shells that record when their
    // methods are called and return whatever you program
    // them to return.
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private EventLogService eventLogService;

    @InjectMocks  //creates real AccountService and injects mocks into it
    private AccountService accountService;

    @Test
    void getAccountById_ExistingAccount_ReturnsAccount() {
        // Arrange
        Account testAccount = Account.builder()
                .accountId(1L)
                .accountName("Test Account")
                .accountNumber(1001)
                .build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));

        // Act
        Account result = accountService.getAccountById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Test Account", result.getAccountName());
        assertEquals(1001, result.getAccountNumber());
    }

    @Test
    void getAccountById_NonExistentAccount_ReturnsNull() {
        // Arrange
        when(accountRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Account result = accountService.getAccountById(999L);

        // Assert
        assertNull(result);
    }

    @Test
    void upsert_NewAccount_CreatesEventLogAndSavesAccount() {
        // Arrange
        Account newAccount = Account.builder()
                .accountName("New Account")
                .accountNumber(1001)
                .category("Asset")
                .normalSide("Debit")
                .build();

        when(accountRepository.save(any(Account.class))).thenReturn(newAccount);

        // Act
        Account result = accountService.upsert(newAccount);

        // Assert
        assertNotNull(result);
        assertEquals("New Account", result.getAccountName());

        // Verify
        verify(eventLogService).saveEventLog(any(EventLog.class));
        verify(accountRepository).save(newAccount);
    }

    @Test
    void deactivate_AccountWithNonZeroBalance_DeactivatesAccount() {
        // Arrange
        Account account = Account.builder()
                .accountId(1L)
                .accountName("Test Account")
                .active(true)
                .balance(new BigDecimal("100.00"))  // Non-zero balance
                .build();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Account result = accountService.deactivate(1L);

        // Assert
        assertNotNull(result);
        assertFalse(result.isActive());
        verify(eventLogService).saveEventLog(any(EventLog.class));
    }

    @Test
    void getByAccountName_ExistingName_ReturnsAccount() {
        // Arrange
        Account testAccount = Account.builder()
                .accountId(1L)
                .accountName("Cash")
                .accountNumber(1001)
                .build();

        when(accountRepository.findByAccountName("Cash")).thenReturn(Optional.of(testAccount));

        // Act
        Account result = accountService.getByAccountName("Cash");

        // Assert
        assertNotNull(result);
        assertEquals("Cash", result.getAccountName());
    }
}
