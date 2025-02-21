package com.ledgerlogic.functional;

import com.ledgerlogic.models.Account;
import com.ledgerlogic.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)  // start entire application for testing
class AccountFunctionalTest {

    @Autowired
    private TestRestTemplate restTemplate;  // like a special client app that knows how to talk to my running server and convert between Java objects and HTTP requests/responses

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void createAndRetrieveAccount_Success() {
        // Arrange account
        Account newAccount = Account.builder()
                .accountNumber(1010)
                .accountName("Test Cash Account")
                .description("Test Description")
                .normalSide("Debit")
                .category("Assets")
                .subCategory("Current Assets")
                .initialBalance(new BigDecimal("1000.00"))
                .build();

        // Create account
        ResponseEntity<Account> createResponse = restTemplate
                .postForEntity("/account/addAccount", newAccount, Account.class);

        // Verify
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        assertNotNull(createResponse.getBody().getAccountId());

        // Retrieve account
        Long accountId = createResponse.getBody().getAccountId();
        ResponseEntity<Account> getResponse = restTemplate
                .getForEntity("/account/viewAccount/" + accountId, Account.class);

        // Verify
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        Account retrievedAccount = getResponse.getBody();
        assertNotNull(retrievedAccount);
        assertEquals("Test Cash Account", retrievedAccount.getAccountName());
        assertEquals("Assets", retrievedAccount.getCategory());
    }

    @Test
    void createDuplicateAccount_Failure() {
        // Create first account
        Account account1 = Account.builder()
                .accountNumber(1020)
                .accountName("Duplicate Test")
                .normalSide("Debit")
                .category("Assets")
                .build();

        // First creation should succeed
        ResponseEntity<String> response1 = restTemplate
                .postForEntity("/account/addAccount", account1, String.class);
        assertEquals(HttpStatus.OK, response1.getStatusCode());

        // Create duplicate account
        Account account2 = Account.builder()
                .accountNumber(1030)
                .accountName("Duplicate Test")
                .normalSide("Debit")
                .category("Assets")
                .build();

        // Second creation should fail
        ResponseEntity<String> response2 = restTemplate
                .postForEntity("/account/addAccount", account2, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());
        assertTrue(response2.getBody().contains("Account with same name already exist!"));
    }

    @Test
    void deactivateAccount_Success() {
        // Create account
        Account account = Account.builder()
                .accountNumber(1040)
                .accountName("Account To Deactivate")
                .normalSide("Debit")
                .category("Assets")
                .balance(BigDecimal.ZERO)
                .build();

        ResponseEntity<Account> createResponse = restTemplate
                .postForEntity("/account/addAccount", account, Account.class);
        Long accountId = createResponse.getBody().getAccountId();

        // Deactivate account
        restTemplate.patchForObject(
                "/account/deactivate/" + accountId,
                null,
                String.class);

        // Verify deactivation
        ResponseEntity<Account> getResponse = restTemplate
                .getForEntity("/account/viewAccount/" + accountId, Account.class);
        assertFalse(getResponse.getBody().isActive());

        /**
         * PLANNED TESTS
         * get all accounts
         * update accounts
         * error cases : invalid input data, non-existent ID, etc.
         */
    }
}