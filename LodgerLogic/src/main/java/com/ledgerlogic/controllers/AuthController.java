package com.ledgerlogic.controllers;

import com.ledgerlogic.dtos.LoginRequest;
import com.ledgerlogic.dtos.RegisterRequest;
import com.ledgerlogic.exceptions.InvalidPasswordException;
import com.ledgerlogic.models.Password;
import com.ledgerlogic.models.User;
import com.ledgerlogic.services.AuthService;

import com.ledgerlogic.services.EmailService;
import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CrossOrigin("*")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;

    public AuthController(AuthService authService, EmailService emailService) {
        this.authService = authService;
        this.emailService = emailService;
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        Optional<User> optional = authService.findByCredentials(loginRequest.getUsername(), loginRequest.getPassword());

        if(!optional.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        session.setAttribute("user", optional.get());

        return ResponseEntity.ok(optional.get());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.removeAttribute("user");

        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest registerRequest) throws InvalidPasswordException {
        User created = new User(
                registerRequest.getFirstName(),
                registerRequest.getLastName(),
                registerRequest.getEmail(),
                registerRequest.getRole(),
                registerRequest.getPassword());
        if(validatePassword(created.getPassword().getContent())){
            emailService.sendApprovalRequestEmail("admin@gmail.com", created.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(created));
        }else{
            throw new InvalidPasswordException("invalid password format");
        }
    }

    public boolean validatePassword(String password){
        Pattern pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }
}
