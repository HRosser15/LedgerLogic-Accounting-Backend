package com.ledgerlogic.controllers;

import com.ledgerlogic.annotations.Admin;
import com.ledgerlogic.annotations.Authorized;
import com.ledgerlogic.models.SecurityQuestion;
import com.ledgerlogic.models.PasswordSecurityQuestion;
import com.ledgerlogic.services.SecurityQuestionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/securityQuestions")
public class SecurityQuestionController {

    private SecurityQuestionService securityQuestionService;

    public SecurityQuestionController(SecurityQuestionService securityQuestionService){
        this.securityQuestionService = securityQuestionService;
    }

    @Admin
    @GetMapping("/getAll")
    public List<SecurityQuestion> getAllSecurityQuestions(){
        return this.securityQuestionService.getAll();
    }

    @Admin
    @PostMapping("/addNewQuestion")
    public SecurityQuestion createQuestion(@RequestBody SecurityQuestion question) {
        return securityQuestionService.createQuestion(question);
    }

    @Admin
    @PostMapping("/updateQuestionContent/{id}")
    public SecurityQuestion updateQuestion(@PathVariable("id") Long id, @RequestBody String content){
        return securityQuestionService.updateSecurityQuestion(id, content);
    }

    @Admin
    @DeleteMapping("/deleteQuestion/{id}")
    public void deleteById(@PathVariable("id") Long questionId){
        this.securityQuestionService.deleteSecurityQuestion(questionId);
    }

    @Admin
    @PostMapping("/users/{userId}/questions")
    public PasswordSecurityQuestion setUserSecurityQuestion(@PathVariable Long userId,
                                                            @RequestBody PasswordSecurityQuestion passwordSecurityQuestion) {
        SecurityQuestionController securityQuestionService = null;
        return securityQuestionService.setUserSecurityQuestion(userId, passwordSecurityQuestion);
    }
}
