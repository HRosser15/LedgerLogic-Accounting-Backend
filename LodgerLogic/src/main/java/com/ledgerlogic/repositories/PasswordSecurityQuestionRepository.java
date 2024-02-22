package com.ledgerlogic.repositories;

import com.ledgerlogic.models.PasswordSecurityQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordSecurityQuestionRepository extends JpaRepository<PasswordSecurityQuestion, Long> {
}
