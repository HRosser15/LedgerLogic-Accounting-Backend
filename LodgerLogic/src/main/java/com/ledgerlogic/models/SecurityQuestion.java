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
@Table(name = "securityQuestions")
public class SecurityQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "security_question_seq")
    @SequenceGenerator(name = "security_question_seq", sequenceName = "security_question_seq", allocationSize = 1)
    private Long securityQuestionId;
    private String content;
}
