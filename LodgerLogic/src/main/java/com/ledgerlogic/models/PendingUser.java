package com.ledgerlogic.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pendingUsers")

public class PendingUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long pendingUserId;

    private String username;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String role;

    @Column(nullable = false)
    private String streetAddress;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String zipCode;

    @Column(nullable = false)
    private Date   birthDay;

    @Column(nullable = false)
    private String    securityQ1Answer;

    @Column(nullable = false)
    private String    securityQ2Answer;

    private String imageUrl;


    public PendingUser(String firstName, String lastName, String email, String password,
                       String streetAddress, String city, String state, String zipCode,
                Date birthDay, String securityQ1Answer, String securityQ2Answer) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.birthDay = birthDay;
        this.securityQ1Answer = securityQ1Answer;
        this.securityQ2Answer = securityQ2Answer;
    }


}


