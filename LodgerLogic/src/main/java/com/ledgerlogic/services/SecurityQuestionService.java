package com.ledgerlogic.services;

import com.ledgerlogic.models.SecurityQuestion;
import com.ledgerlogic.models.UserSecurityQuestion;
import com.ledgerlogic.repositories.SecurityQuestionRepository;
import com.ledgerlogic.repositories.UserSecurityQuestionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityQuestionService {

    private final SecurityQuestionRepository securityQuestionRepository;
    private final UserSecurityQuestionRepository userSecurityQuestionRepository;

    public SecurityQuestionService(UserSecurityQuestionRepository userSecurityQuestionRepository,
                                   SecurityQuestionRepository securityQuestionRepository){
        this.securityQuestionRepository = securityQuestionRepository;
        this.userSecurityQuestionRepository = userSecurityQuestionRepository;
    }

    public SecurityQuestion createQuestion(SecurityQuestion question) {
        return securityQuestionRepository.save(question);
    }

    public UserSecurityQuestion setUserSecurityQuestion(Long userId, UserSecurityQuestion userSecurityQuestion) {
        return userSecurityQuestionRepository.save(userSecurityQuestion);
    }

    public Optional<UserSecurityQuestion> getUserSecurityQuestion(Long id){
        return userSecurityQuestionRepository.findById(id);
    }
}
