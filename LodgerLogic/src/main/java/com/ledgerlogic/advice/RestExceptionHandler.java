package com.ledgerlogic.advice;

import com.ledgerlogic.exceptions.InvalidRoleException;
import com.ledgerlogic.exceptions.NotLoggedInException;
import com.ledgerlogic.models.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(NotLoggedInException.class)
    public ResponseEntity<Object> handleNotLoggedInException(HttpServletRequest request, NotLoggedInException notLoggedInException) {

        String errorMessage = "Must be logged in to perform this action";

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
    }

    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<Object> handleInvalidRoleException(HttpServletRequest request, InvalidRoleException invalidRoleException) {

        String errorMessage = "Missing required role to perform this action";

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorMessage);
    }
}
