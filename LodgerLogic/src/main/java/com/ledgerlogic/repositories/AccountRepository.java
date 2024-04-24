package com.ledgerlogic.repositories;

import com.ledgerlogic.models.Account;
import com.ledgerlogic.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<List<Account>> findAllByOwner(User owner);

    Optional<Account> findByAccountName(String accountName);

    Optional<Account> findByAccountNumber(int accountNumber);
}