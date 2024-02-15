package com.ledgerlogic.services;

import com.ledgerlogic.models.Password;
import com.ledgerlogic.models.User;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Optional;

@Service
public class AuthService {

    private final UserService userService;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public Optional<User> findByCredentials(String username, Password password) {
        return userService.findByCredentials(username, password);
    }

    public User register(User user) {
        Optional<User> existingUser = userService.findByUsername(user.getUsername());
        if(existingUser.isPresent()){
            throw new IllegalArgumentException("User " + existingUser.get().getFirstName() + " " + existingUser.get().getLastName()+ " already exist");
        }
        return userService.upsert(user);
    }

}
