package com.ledgerlogic.controllers;

import com.ledgerlogic.models.PasswordSecurityQuestion;
import com.ledgerlogic.services.ForgotPasswordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/forgotPassword")
public class ForgotPasswordController {

    private ForgotPasswordService forgotPasswordService;

    public ForgotPasswordController(ForgotPasswordService forgotPasswordService){
        this.forgotPasswordService = forgotPasswordService;
    }

    @GetMapping("/email")
    public String getEmail(@RequestParam("email") String email) {
        return forgotPasswordService.getEmail(email);
    }

    @GetMapping("/passwordSecurityQuestion")
    public PasswordSecurityQuestion getSecurityQuestion(@RequestParam("email") String email) {
        return forgotPasswordService.getSecurityQuestion(email);
    }

    @PostMapping("/verifyAnswer")
    public ResponseEntity<String> verifyAnswer(@RequestParam("email") String email,
                                               @RequestParam("question") String question,
                                               @RequestParam("answer") String answer) {
        if (forgotPasswordService.verifyAnswer(email, question, answer)) {
            return ResponseEntity.ok("Answer is correct");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Answer is incorrect");
        }
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam("email") String email,
                                @RequestParam("newPassword") String newPassword) {
        return forgotPasswordService.resetPassword(email, newPassword);
    }
}
