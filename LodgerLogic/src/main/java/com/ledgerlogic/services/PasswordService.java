package com.ledgerlogic.services;

import com.ledgerlogic.models.Password;
import com.ledgerlogic.models.PasswordSecurityQuestion;
import com.ledgerlogic.repositories.PasswordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PasswordService {

    private final PasswordRepository passwordRepository;
    private final SecurityQuestionService securityQuestionService;

    public PasswordService(PasswordRepository passwordRepository, SecurityQuestionService securityQuestionService){
        this.passwordRepository = passwordRepository;
        this.securityQuestionService = securityQuestionService;
    }
    public Password addNewPassword(Password password){
        List<PasswordSecurityQuestion> passwordSecurityQuestionList = password.getPasswordSecurityQuestions();
        this.securityQuestionService.saveAll(passwordSecurityQuestionList);
        return this.passwordRepository.save(password);
    }
}
