package com.ledgerlogic.dtos;

import com.ledgerlogic.models.Password;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    private String username;
    private Password password;
}
