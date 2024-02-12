package com.ledgerlogic.repositories;

import com.ledgerlogic.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<List<Account>> findAllByUserId(int id);
}
