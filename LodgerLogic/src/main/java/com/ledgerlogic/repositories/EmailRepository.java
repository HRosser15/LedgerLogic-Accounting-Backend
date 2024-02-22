package com.ledgerlogic.repositories;

import com.ledgerlogic.models.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Long> {
}
