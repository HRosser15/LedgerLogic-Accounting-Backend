package com.ledgerlogic.dtos;

import com.ledgerlogic.models.Password;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String role = "accountant";
    private Password password;
}
