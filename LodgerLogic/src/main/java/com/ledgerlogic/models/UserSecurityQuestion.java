package com.ledgerlogic.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "userSecurityQuestions")
public class UserSecurityQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSecurityQuestionId;
    private String answer;

    @OneToOne
    private SecurityQuestion question;
}
