package com.ledgerlogic.dtos;

import java.util.Date;
import lombok.Data;

@Data
public class PendingUser {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;
    private Date birthday;
    private String securityQ1Answer;
    private String securityQ2Answer;

}