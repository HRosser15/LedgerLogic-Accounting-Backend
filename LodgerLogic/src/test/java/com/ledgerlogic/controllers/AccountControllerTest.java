package com.ledgerlogic.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ledgerlogic.models.Account;
import com.ledgerlogic.models.User;
import com.ledgerlogic.services.AccountService;
import com.ledgerlogic.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;

/**
 * The @WebMvcTest sets up a focused test environment for testing web controllers.
 * When you add (AccountController.class), you're telling Spring to only load
 * the specified controller and its dependencies into the test environment.
 */
@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired  // looks for MockMvc bean, initializes with right settings for testing this controller, injects it into test class
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private UserService userService;

    @ControllerAdvice
    public class GlobalExceptionHandler {
        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account with same name already exist!");
        }
    }


    // HTTP Responses
    @Test
    void getAllAccounts_ReturnsListOfAccounts() throws Exception {
        Account testAccount = new Account(
                1596, "Test Account", "Account for testing", "debit", "asset"
        );

        when(accountService.getAll()).thenReturn(List.of(testAccount));

        mockMvc.perform(get("/account/allAccounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountName").value(is("Test Account"))) // Use accountName, not name
                .andExpect(jsonPath("$[0].description").value(is("Account for testing")))
                .andExpect(jsonPath("$[0].normalSide").value(is("debit")))
                .andExpect(jsonPath("$[0].category").value(is("asset")));
    }

    @Test
    void createAccount_DuplicateName_Returns400BadRequest() throws Exception {
        Account newAccount = Account.builder()
                .accountNumber(1002)
                .accountName("Duplicate Account")
                .description("New Account")
                .normalSide("Debit")
                .category("Asset")
                .initialBalance(BigDecimal.ZERO)
                .debit(BigDecimal.ZERO)
                .credit(BigDecimal.ZERO)
                .balance(BigDecimal.ZERO)
                .build();

        when(accountService.getByAccountName("Duplicate Account"))
                .thenReturn(new Account());

        mockMvc.perform(post("/account/addAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newAccount)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Account with same name already exist!"));
    }
    @Test void getAccount_ValidNumber_Returns200WithAccount() throws Exception {
        Account testAccount = Account.builder()
                .accountNumber(1001)
                .accountName("Test Account")
                .description("Test Description")
                .normalSide("Debit")
                .category("Asset")
                .build();

        when(accountService.getAccountById(1L)).thenReturn(testAccount);

        mockMvc.perform(get("/account/viewAccount/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value(1001))
                .andExpect(jsonPath("$.accountName").value("Test Account"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.normalSide").value("Debit"))
                .andExpect(jsonPath("$.category").value("Asset"));

    }
    @Test void deactivateAccount_ZeroBalance_Returns200Success() throws Exception {
        Account testAccount = Account.builder()
                .accountId(1L)
                .accountNumber(1001)
                .accountName("Test Account")
                .balance(BigDecimal.ZERO)
                .build();

        when(accountService.getAccountById(1L)).thenReturn(testAccount);
        when(accountService.deactivate(1L)).thenReturn(testAccount);

        mockMvc.perform(patch("/account/deactivate/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Account deactivated!"));

        verify(accountService).deactivate(1L);
    }

    @Test
    void updateAccount_ValidInput_Returns200Success() throws Exception {
        Account existingAccount = Account.builder()
                .accountId(1L)
                .accountNumber(1001)
                .accountName("Original Account")
                .normalSide("Debit")
                .category("Asset")
                .build();

        // Create updated account data
        Account updatedAccount = Account.builder()
                .accountId(1L)
                .accountNumber(1001)
                .accountName("Updated Account")
                .normalSide("Debit")
                .category("Asset")
                .build();

        // Mock service responses
        when(accountService.getAccountById(1L)).thenReturn(existingAccount);
        when(accountService.update(eq(1L), any(Account.class))).thenReturn(updatedAccount);

        // Test
        mockMvc.perform(put("/account/updateAccount/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedAccount)))
                .andExpect(status().isOk())
                .andExpect(content().string("Account updated successfully!"));
    }

    @Test
    void createAccount_MissingNormalSide_Returns400BadRequest() throws Exception {
        Account invalidAccount = Account.builder()
                .accountNumber(1001)
                .accountName("Test Account")
                .category("Asset")
                .build();

        mockMvc.perform(post("/account/addAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidAccount)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAccount_ValidInput_Returns200Success() throws Exception {
        // Create test account with all required fields
        Account newAccount = Account.builder()
                .accountNumber(1001)
                .accountName("Test Account")
                .description("Test Description")
                .normalSide("Debit")
                .category("Asset")
                .subCategory("Current Assets")
                .initialBalance(BigDecimal.ZERO)
                .debit(BigDecimal.ZERO)
                .credit(BigDecimal.ZERO)
                .balance(BigDecimal.ZERO)
                .build();

        // Mock service to return null for getByAccountName (no duplicate)
        when(accountService.getByAccountName("Test Account")).thenReturn(null);
        when(accountService.getByAccountNumber(1001)).thenReturn(null);

        mockMvc.perform(post("/account/addAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newAccount)))
                .andExpect(status().isOk())
                .andExpect(content().string("Account added successfully!"));
    }

    @Test
    void getAccount_InvalidId_Returns404NotFound() throws Exception {
        when(accountService.getAccountById(999L)).thenReturn(null);

        mockMvc.perform(get("/account/viewAccount/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllUserAccounts_ValidUserId_ReturnsAccounts() throws Exception {
        // Create test user and accounts
        User testUser = new User();
        testUser.setUserId(1L);

        Account testAccount1 = Account.builder()
                .accountNumber(1001)
                .accountName("Test Account 1")
                .build();

        Account testAccount2 = Account.builder()
                .accountNumber(1002)
                .accountName("Test Account 2")
                .build();

        // Mock service responses
        when(userService.getById(1L)).thenReturn(testUser);
        when(accountService.getAccountByOwner(testUser))
                .thenReturn(List.of(testAccount1, testAccount2));

        mockMvc.perform(get("/account/getAllUserAccounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountName").value("Test Account 1"))
                .andExpect(jsonPath("$[1].accountName").value("Test Account 2"));
    }

    @Test
    void reactivateAccount_Success_Returns200() throws Exception {
        Account inactiveAccount = Account.builder()
                .accountId(1L)
                .accountNumber(1001)
                .accountName("Inactive Account")
                .active(false)
                .balance(BigDecimal.ZERO)
                .build();

        // Mock service responses
        when(accountService.getAccountById(1L)).thenReturn(inactiveAccount);
        when(accountService.reactivate(1L)).thenReturn(inactiveAccount);

        mockMvc.perform(patch("/account/reactivate/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Account reactivated!"));

        verify(accountService).reactivate(1L);
    }

    @Test
    void getByAccountNumber_NotFound_Returns404() throws Exception {
        // Mock service to return null for non-existent account number
        when(accountService.getByAccountNumber(9999)).thenReturn(null);

        mockMvc.perform(get("/account/getByAccountNumber/9999"))
                .andExpect(status().isNotFound());
    }
}