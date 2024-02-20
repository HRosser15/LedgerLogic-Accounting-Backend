package com.ledgerlogic.services;

import com.ledgerlogic.models.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordService passwordService;

    public AuthService(UserService userService, PasswordService passwordService) {
        this.userService = userService;
        this.passwordService = passwordService;
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
        return userService.save(user);
    }

}
