package com.ledgerlogic.controllers;

import com.ledgerlogic.models.SecurityQuestion;
import com.ledgerlogic.models.UserSecurityQuestion;
import com.ledgerlogic.services.SecurityQuestionService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/securityQuestions")
public class SecurityQuestionController {

    private SecurityQuestionService securityQuestionService;

    @PostMapping("/questions")
    public SecurityQuestion createQuestion(@RequestBody SecurityQuestion question) {
        return securityQuestionService.createQuestion(question);
    }

    @PostMapping("/users/{userId}/questions")
    public UserSecurityQuestion setUserSecurityQuestion(@PathVariable Long userId,
                                                        @RequestBody UserSecurityQuestion userSecurityQuestion) {
        SecurityQuestionController securityQuestionService = null;
        return securityQuestionService.setUserSecurityQuestion(userId, userSecurityQuestion);
    }
}
