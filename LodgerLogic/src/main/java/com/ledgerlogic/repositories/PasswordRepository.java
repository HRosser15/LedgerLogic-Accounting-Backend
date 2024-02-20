package com.ledgerlogic.repositories;

import com.ledgerlogic.models.Password;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordRepository extends JpaRepository<Password, Long> {
    Password findByContent(String content);
}
