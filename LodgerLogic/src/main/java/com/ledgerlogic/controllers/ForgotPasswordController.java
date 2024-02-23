package com.ledgerlogic.controllers;

import com.ledgerlogic.models.Password;
import com.ledgerlogic.models.PasswordSecurityQuestion;
import com.ledgerlogic.models.User;
import com.ledgerlogic.services.ForgotPasswordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/forgotPassword")
public class ForgotPasswordController {

    private ForgotPasswordService forgotPasswordService;

    public ForgotPasswordController(ForgotPasswordService forgotPasswordService){
        this.forgotPasswordService = forgotPasswordService;
    }

    @GetMapping("/email")
    public User getEmail(@RequestParam("email") String email) {
        Optional<User> user = Optional.ofNullable(forgotPasswordService.getByEmail(email));
        if (!user.isPresent()){
            ResponseEntity.ok("No user with provided email");
            return null;
        }
        ResponseEntity.ok("User with provided email exist");
        return user.get();
    }

    @GetMapping("/passwordSecurityQuestion")
    public PasswordSecurityQuestion getSecurityQuestion(@RequestParam("email") String email) {
        return forgotPasswordService.getSecurityQuestion(email);
    }

    @PostMapping("/verifyAnswer")
    public Boolean verifyAnswer(@RequestParam("email") String email,
                                               @RequestParam("questionContent") String questionContent,
                                               @RequestParam("answer") String answer) {
        if (forgotPasswordService.verifyAnswer(email, questionContent, answer)) {
            ResponseEntity.ok("Answer is correct");
            return true;
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Answer is incorrect");
            return false;
        }
    }

    @PostMapping("/resetPassword")
    public Password resetPassword(@RequestParam("email") String email,
                                  @RequestParam("newPasswordContent") String newPasswordContent) {
        Password newPassword =  forgotPasswordService.resetPassword(email, newPasswordContent);
        if (newPassword.equals(null)){
            //I ignored the possibility of user being null putting in mind we should be sure by now considering previous required steps
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid password format!");
            return null;
        }
        ResponseEntity.ok("Password rested successfully!");
        return newPassword;
    }
}
