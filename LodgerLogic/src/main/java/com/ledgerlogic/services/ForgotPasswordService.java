package com.ledgerlogic.services;

import com.ledgerlogic.models.Password;
import com.ledgerlogic.models.PasswordSecurityQuestion;
import com.ledgerlogic.models.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class ForgotPasswordService {

    private UserService userService;

    public ForgotPasswordService(UserService userService, SecurityQuestionService securityQuestionService){
        this.userService =  userService;
    }

    public String getEmail(String email) {
        User user = userService.findByEmail(email);
        if (user == null) {
            return "User not found";
        }
        return user.getEmail();
    }

    public PasswordSecurityQuestion getSecurityQuestion(String email) {
        User user = userService.findByEmail(email);
        if (user == null) {
            return null;
        }

        return user.getPassword().getPasswordSecurityQuestions().get(new Random().nextInt(user.getPassword().getPasswordSecurityQuestions().size()));
    }

    public boolean verifyAnswer(String email, String questionContent, String answer) {
        User user = userService.findByEmail(email);
        if (user == null) {
            return false;
        }

        List<PasswordSecurityQuestion> passwordSecurityQuestions = user.getPassword().getPasswordSecurityQuestions();
        for(PasswordSecurityQuestion question:passwordSecurityQuestions){
            if(question.getQuestion().equals(questionContent)){
                return question.getAnswer().equals(answer);
            }
        }
        return false;
    }

    public String resetPassword(String email, String passwordContent) {
        User user = userService.findByEmail(email);
        if (user == null) {
            return "User not found";
        }
        Password newPassword = user.getPassword();
        newPassword.setContent(passwordContent);
        user.setPassword(newPassword);
        userService.upsert(user);
        return "Password updated successfully";
    }
}
