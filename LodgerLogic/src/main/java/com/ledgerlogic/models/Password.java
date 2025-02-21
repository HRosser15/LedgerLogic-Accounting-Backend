package com.ledgerlogic.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "passwords")
public class Password {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "password_seq")
    @SequenceGenerator(name = "password_seq", sequenceName = "password_seq", allocationSize = 1)
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
        calendar.add(Calendar.MONTH, 3);
        return calendar.getTime();
    }
}
