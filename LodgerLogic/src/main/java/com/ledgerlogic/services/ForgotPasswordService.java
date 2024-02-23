package com.ledgerlogic.services;

import com.ledgerlogic.models.Password;
import com.ledgerlogic.models.PasswordSecurityQuestion;
import com.ledgerlogic.models.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ForgotPasswordService {

    private UserService userService;

    public ForgotPasswordService(UserService userService, SecurityQuestionService securityQuestionService){
        this.userService =  userService;
    }

    public User getByEmail(String email) {
        Optional<User> user = Optional.ofNullable(userService.findByEmail(email));
        System.out.println(email);
        if (!user.isPresent()) {
            System.out.println("User not found");
            return null;
        }
        return user.get();
    }

    public PasswordSecurityQuestion getSecurityQuestion(String email) {
        User user = userService.findByEmail(email);
        if (user == null) {
            return null;
        }

        return user.getPassword().getPasswordSecurityQuestions().get(new Random().nextInt(user.getPassword().getPasswordSecurityQuestions().size()));
    }

    public boolean verifyAnswer(String email, String questionContent, String answer) {
        Optional<User> user = Optional.ofNullable(userService.findByEmail(email));
        if (!user.isPresent()) {
            System.out.println("user doesn't exist!");
            return false;
        }

        List<PasswordSecurityQuestion> passwordSecurityQuestions = user.get().getPassword().getPasswordSecurityQuestions();
        for(PasswordSecurityQuestion question:passwordSecurityQuestions){
            if(question.getQuestion().getContent().equals(questionContent)){
                return question.getAnswer().equals(answer);
            }
        }
        return false;
    }

    public Password resetPassword(String email, String passwordContent) {
        Optional<User> optionalUser = Optional.ofNullable(userService.findByEmail(email));
        if (!optionalUser.isPresent()) {
            return null;
        }

        boolean validPassword = validatePassword(passwordContent);

        if(validPassword){
            User user = optionalUser.get();
            Password newPassword = user.getPassword();
            newPassword.setContent(passwordContent);
            user.setPassword(newPassword);
            userService.upsert(user);
            return newPassword;
        }

        return null;
    }

    public boolean validatePassword(String password){
        Pattern pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
