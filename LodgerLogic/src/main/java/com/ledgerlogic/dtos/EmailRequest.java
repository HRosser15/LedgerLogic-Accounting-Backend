package com.ledgerlogic.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {

    private String to;
    private String from;
    private String subject;
    private String Body;
    private MultipartFile attachment;
}
