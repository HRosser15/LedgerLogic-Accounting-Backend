package com.ledgerlogic.repositories;

import com.ledgerlogic.models.SecurityQuestion;
import com.ledgerlogic.models.User;
import com.ledgerlogic.models.UserSecurityQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSecurityQuestionRepository extends JpaRepository<UserSecurityQuestion, Long> {
}
