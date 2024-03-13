package com.ledgerlogic.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long   userId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String role = "accountant";
    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;
    private Date   birthDay;
    private Boolean status = false; //true = active & false = inactive
    private short  failedLoginAttempt;
    private Date   suspensionStartDate;
    private Date   suspensionEndDate;
    private Date   lastLoginDate;
    private Date   accountCreationDate = new Date();
    private String imageUrl;
    private List<String> previousPasswords = new ArrayList<>();

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @OneToOne
    private Password password;

    public User(String firstName, String lastName, String email, String role, Password password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.password = password;
        this.username = generateUsername(firstName, lastName, accountCreationDate);
        this.previousPasswords.add(encryptPassword(password.getContent()));
    }

    private String generateUsername(String firstName, String lastName, Date accountCreationDate){
        String twoDigitsMonth = new SimpleDateFormat("MM").format(accountCreationDate);
        String twoDigitsYear  = new SimpleDateFormat("yy").format(accountCreationDate);

        return Character.toString(firstName.charAt(0)).toLowerCase()+
                          lastName.toLowerCase()+
                          twoDigitsMonth+
                          twoDigitsYear;
    }

    public String encryptPassword(String unEncryptedContent){
        return this.passwordEncoder.encode(unEncryptedContent);
    }

}
