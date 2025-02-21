package com.ledgerlogic.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "passwordSecurityQuestions")
public class PasswordSecurityQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "password_security_question_seq")
    @SequenceGenerator(name = "password_security_question_seq", sequenceName = "password_security_question_seq", allocationSize = 1)
    private Long passwordSecurityQuestionId;
    private String answer;

    @OneToOne
    private SecurityQuestion question;
}