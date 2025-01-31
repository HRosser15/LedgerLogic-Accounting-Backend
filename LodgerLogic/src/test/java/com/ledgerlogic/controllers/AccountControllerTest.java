package com.ledgerlogic.controllers;

import com.ledgerlogic.models.Account;
import com.ledgerlogic.services.AccountService;
import com.ledgerlogic.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private UserService userService; // Mock UserService since it's a dependency

    @Test
    void getAllAccounts_ReturnsListOfAccounts() throws Exception {
        // Create a test account
        Account testAccount = new Account(
                1596, "Test Account", "Account for testing", "debit", "asset"
        );

        // Mock the service response
        when(accountService.getAll()).thenReturn(List.of(testAccount));

        // Simulate HTTP GET to /account/allAccounts
        mockMvc.perform(get("/account/allAccounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountName").value(is("Test Account"))) // Use accountName, not name
                .andExpect(jsonPath("$[0].description").value(is("Account for testing")))
                .andExpect(jsonPath("$[0].normalSide").value(is("debit")))
                .andExpect(jsonPath("$[0].category").value(is("asset")));
    }

    @Test
    void getAllAccounts_ReturnsNullListOfAccounts() throws Exception {
        // write a test for when the accounts list is empty
    }
}