package com.ledgerlogic.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "passwords")
public class Password {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long passwordId;
    private String content;
    private Date expirationDate;

    @OneToMany
    private List<PasswordSecurityQuestion> passwordSecurityQuestions;

    public Password(String content, List<PasswordSecurityQuestion> passwordSecurityQuestions){
        this.content = content;
        this.expirationDate = calculateExpirationDate(new Date());
        this.passwordSecurityQuestions = passwordSecurityQuestions;
    }

    public Date calculateExpirationDate(Date creationDate){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(creationDate);
        calendar.add(Calendar.MONTH, 3); //password expires after 3 months
        return calendar.getTime();
    }
}
