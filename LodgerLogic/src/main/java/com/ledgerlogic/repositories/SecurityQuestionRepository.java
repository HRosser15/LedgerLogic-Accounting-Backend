package com.ledgerlogic.repositories;

import com.ledgerlogic.models.SecurityQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityQuestionRepository extends JpaRepository<SecurityQuestion, Long> {
}
