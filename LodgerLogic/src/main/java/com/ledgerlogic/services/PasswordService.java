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
    private final UserService userService;

    public PasswordService(PasswordRepository passwordRepository,
                           SecurityQuestionService securityQuestionService, UserService userService){
        this.passwordRepository = passwordRepository;
        this.securityQuestionService = securityQuestionService;
        this.userService = userService;
    }
    public Password addNewPassword(Password password){
        List<PasswordSecurityQuestion> passwordSecurityQuestionList = password.getPasswordSecurityQuestions();
        this.securityQuestionService.saveAll(passwordSecurityQuestionList);
        return this.passwordRepository.save(password);
    }
}
