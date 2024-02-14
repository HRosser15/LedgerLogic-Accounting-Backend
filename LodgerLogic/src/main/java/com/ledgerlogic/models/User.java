package com.ledgerlogic.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long   userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
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
    private Boolean status = true; //true = active & false = inactive

    @Column(nullable = false)
    private Date   passwordExpirationDate;

    @Column(nullable = true)
    private Short  failedLoginAttempt;  //can't be null if primitive, so changed short -> Short

    @Column(nullable = true)
    private Date   suspensionStartDate;

    @Column(nullable = true)
    private Date   suspensionEndDate;

    @Column(nullable = true)
    private Date   lastLoginDate;

    @Column(nullable = false)
    private Date   userCreationDate;

    @Column(nullable = false)
    private String    securityQ1Answer;

    @Column(nullable = false)
    private String    securityQ2Answer;

    @Column(nullable = true)
    private String imageUrl;

    public User(String username, String firstName, String lastName, String email, String password,
                String role, String streetAddress, String city, String state, String zipCode,
                Date birthDay, Boolean status, Date passwordExpirationDate, Date userCreationDate,
                String securityQ1Answer, String securityQ2Answer) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.birthDay = birthDay;
        this.status = status;
        this.passwordExpirationDate = passwordExpirationDate;
        this.userCreationDate = userCreationDate;
        this.securityQ1Answer = securityQ1Answer;
        this.securityQ2Answer = securityQ2Answer;
    }


}
