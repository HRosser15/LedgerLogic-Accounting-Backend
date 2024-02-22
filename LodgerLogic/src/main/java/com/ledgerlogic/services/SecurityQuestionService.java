package com.ledgerlogic.services;

import com.ledgerlogic.models.SecurityQuestion;
import com.ledgerlogic.models.PasswordSecurityQuestion;
import com.ledgerlogic.repositories.SecurityQuestionRepository;
import com.ledgerlogic.repositories.PasswordSecurityQuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SecurityQuestionService {

    private final SecurityQuestionRepository securityQuestionRepository;
    private final PasswordSecurityQuestionRepository passwordSecurityQuestionRepository;


    public SecurityQuestionService(PasswordSecurityQuestionRepository passwordSecurityQuestionRepository,
                                   SecurityQuestionRepository securityQuestionRepository){
        this.securityQuestionRepository = securityQuestionRepository;
        this.passwordSecurityQuestionRepository = passwordSecurityQuestionRepository;
    }

    public SecurityQuestion createQuestion(SecurityQuestion question) {
        return securityQuestionRepository.save(question);
    }

    public List<PasswordSecurityQuestion> saveAll(List<PasswordSecurityQuestion> passwordSecurityQuestions){
        for(PasswordSecurityQuestion passwordSecurityQuestion: passwordSecurityQuestions){
            this.securityQuestionRepository.save(passwordSecurityQuestion.getQuestion());
        }
        return passwordSecurityQuestionRepository.saveAll(passwordSecurityQuestions);
    }

    public List<SecurityQuestion> getAll(){
        return this.securityQuestionRepository.findAll();
    }

    public PasswordSecurityQuestion setUserSecurityQuestion(Long userId, PasswordSecurityQuestion passwordSecurityQuestion) {
        return passwordSecurityQuestionRepository.save(passwordSecurityQuestion);
    }

    public Optional<PasswordSecurityQuestion> getUserSecurityQuestion(Long id){
        return passwordSecurityQuestionRepository.findById(id);
    }

    public void deleteSecurityQuestion(Long id){
        this.securityQuestionRepository.deleteById(id);
    }

    public SecurityQuestion updateSecurityQuestion(Long id, String content) {
        Optional<SecurityQuestion> currentQuestion = Optional.of(this.securityQuestionRepository.getById(id));
        if(currentQuestion.isPresent()){
            currentQuestion.get().setContent(content);
            return this.securityQuestionRepository.save(currentQuestion.get());
        }
        return null;
    }
}
