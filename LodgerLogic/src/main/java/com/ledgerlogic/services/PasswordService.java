package com.ledgerlogic.services;

import com.ledgerlogic.models.Password;
import com.ledgerlogic.models.PasswordSecurityQuestion;
import com.ledgerlogic.models.User;
import com.ledgerlogic.repositories.PasswordRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
public class PasswordService {

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final PasswordRepository passwordRepository;
    private final SecurityQuestionService securityQuestionService;
    private final UserService userService;

    public PasswordService(PasswordRepository passwordRepository,
                           SecurityQuestionService securityQuestionService,
                           UserService userService){
        this.passwordRepository = passwordRepository;
        this.securityQuestionService = securityQuestionService;
        this.userService = userService;
    }

    public Password addNewPassword(Password password){
        List<PasswordSecurityQuestion> passwordSecurityQuestionList = password.getPasswordSecurityQuestions();
        this.securityQuestionService.saveAll(passwordSecurityQuestionList);

        String unEncryptedContent = password.getContent();
        password.setContent(encryptPassword(password.getContent()));
        return this.passwordRepository.save(password);
    }

    public String encryptPassword(String unEncryptedContent){
        return this.passwordEncoder.encode(unEncryptedContent);
    }

}
