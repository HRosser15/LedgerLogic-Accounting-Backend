package com.ledgerlogic.services;

import com.ledgerlogic.models.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordService passwordService;
    private final EmailService emailService;

    public AuthService(UserService userService, PasswordService passwordService, EmailService emailService) {
        this.userService = userService;
        this.passwordService = passwordService;
        this.emailService = emailService;
    }

    public Optional<User> findByCredentials(String username, String password) {
        return userService.findByCredentials(username, password);
    }

    public User register(User user) {
        Optional<User> existingUser = userService.findByUsername(user.getUsername());
        if(existingUser.isPresent()){
            throw new IllegalArgumentException("User " + existingUser.get().getFirstName() + " " + existingUser.get().getLastName()+ " already exist");
        }
        this.passwordService.addNewPassword(user.getPassword());
        this.emailService.send("admins@legderlogic.com",user.getEmail(), "Activate User Account", "Please Activate " + user.getFirstName() + " " + user.getLastName() + " Account!");
        return userService.save(user);
    }

}
