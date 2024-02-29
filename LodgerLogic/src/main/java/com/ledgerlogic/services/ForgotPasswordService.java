package com.ledgerlogic.services;

import com.ledgerlogic.models.Password;
import com.ledgerlogic.models.PasswordSecurityQuestion;
import com.ledgerlogic.models.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ForgotPasswordService {

    private final UserService userService;
    private final PasswordService passwordService;
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ForgotPasswordService(UserService userService, SecurityQuestionService securityQuestionService, PasswordService passwordService){
        this.userService =  userService;
        this.passwordService = passwordService;
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

    public User resetPassword(String email, String passwordContent) {
        Optional<User> optionalUser = Optional.ofNullable(userService.findByEmail(email));
        if (!optionalUser.isPresent()) {
            return null;
        }

        if(validatePassword(passwordContent)){
            User user = optionalUser.get();
            Password newPassword = user.getPassword();

            if(!passwordUsedInThePast(user, passwordContent)){
                newPassword.setContent(encryptPassword(passwordContent));
                user.setPassword(newPassword);

                return userService.upsert(user);

            }else{
                System.out.println("you can't use previously used passwords!");
                return null;
            }
        }else{
            System.out.println("Invalid password format!");
            return null;
        }
    }

    public boolean validatePassword(String password){
        Pattern pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public Boolean passwordUsedInThePast(User user, String newPasswordContent){
        List<String> previousPasswords = user.getPreviousPasswords();
        for(String passwordContent: previousPasswords){
            if(verifyPasswordContent(newPasswordContent, passwordContent)){
                return true;
            }
        }

        previousPasswords.add(encryptPassword(newPasswordContent));
        user.setPreviousPasswords(previousPasswords);
        return false;
    }

    public boolean verifyPasswordContent(String passwordContentProvided, String storedPasswordContentHash){
        return this.passwordEncoder.matches(passwordContentProvided, storedPasswordContentHash);
    }

    public String encryptPassword(String unEncryptedContent){
        return this.passwordEncoder.encode(unEncryptedContent);
    }
}
