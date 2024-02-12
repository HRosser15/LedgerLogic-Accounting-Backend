package com.ledgerlogic.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterClass {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
