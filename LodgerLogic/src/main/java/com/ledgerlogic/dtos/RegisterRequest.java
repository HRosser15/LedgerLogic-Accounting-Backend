package com.ledgerlogic.dtos;

import com.ledgerlogic.models.PasswordSecurityQuestion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String role = "accountant";
    private String passwordContent;
    private List<PasswordSecurityQuestion> passwordSecurityQuestions;
}
