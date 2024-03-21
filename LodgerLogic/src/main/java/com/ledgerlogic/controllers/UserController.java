package com.ledgerlogic.controllers;

import com.ledgerlogic.annotations.Admin;
import com.ledgerlogic.annotations.Authorized;
import com.ledgerlogic.annotations.Manager;
import com.ledgerlogic.models.Account;
import com.ledgerlogic.models.SecurityQuestion;
import com.ledgerlogic.models.User;
import com.ledgerlogic.services.EmailService;
import com.ledgerlogic.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CrossOrigin("*")
@RestController
@RequestMapping("/users")
public class UserController {
    public UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PutMapping("/update/{id}")
    public User update(@PathVariable("id") Long id, @RequestBody User user) {
        Optional<User> currentUser = Optional.ofNullable(this.userService.getById(id));
        if (!currentUser.isPresent()) {
            return null;
        } else {
            User currentUserToBeUpdated = currentUser.get();
            if (user.getEmail() != null) {
                if (userService.emailIsTaken(user.getEmail())) {
                    System.out.println("The email/username already exist");
                    return null;
                } else {
                    currentUserToBeUpdated.setEmail(user.getEmail());
                }
            }
            if (user.getFirstName() != null) {
                currentUserToBeUpdated.setFirstName(user.getFirstName());
            }
            if (user.getLastName() != null) {
                currentUserToBeUpdated.setLastName(user.getLastName());
            }
            if (user.getPassword() != null) {
                currentUserToBeUpdated.setPassword(user.getPassword());
            }
            return this.userService.upsert(currentUserToBeUpdated);
        }
    }

    @Admin
    @PutMapping("/createNewUser")
    public User createNewUser(@RequestBody User newUser) {
        Optional<User> existingUser = Optional.ofNullable((User) this.userService.findByFullName(newUser.getFirstName(), newUser.getLastName()));
        if (!existingUser.isPresent()) {
            System.out.println("User already exist");
            return null;
        } else if (newUser.getFirstName() != null && newUser.getLastName() != null && newUser.getPassword() != null) {
                if (newUser.getEmail() != null) {
                    if (userService.emailIsTaken(newUser.getEmail())) {
                        System.out.println("The email/username already exist");
                        return null;
                    } else {
                        return this.userService.upsert(newUser);
                    }
                }else{
                    System.out.println("email field is missing!");
                    return null;
                }
        } else {
            System.out.println("first name, last name, or password field is missing!");
            return null;
        }
    }

    @GetMapping("/searchById/{id}")
    public User getUserById(@PathVariable("id") Long id){
        return this.userService.getById(id);
    }

    @GetMapping("/searchByFirstname/{firstname}")
    public List<User> getUserById(@PathVariable("firstname") String firstname){
        return this.userService.findByFirstName(firstname);
    }

    @GetMapping("/searchByLastname/{lastname}")
    public List<User> getUserByLastname(@PathVariable("lastname") String lastname){
        return this.userService.findByLastName(lastname);
    }

    @GetMapping("/searchByFullName/{firstname}/{lastname}")
    public List<User> getUserByFullName(@PathVariable("firstname") String firstname, @PathVariable("lastname") String lastname){
        return this.userService.findByFullName(firstname, lastname);
    }

    @Admin
    @GetMapping("/allUsers")
    public List<User> getAllUsers(){
        return this.userService.getAll();
    }

    @Admin
    @GetMapping("/getByRole/{role}")
    public List<User> getByRole(@PathVariable("role") String role){
        return this.userService.getByRole(role);
    }

    @Admin
    @PutMapping("/updateRole/{userId}/{newRole}")
    public User updateUserRole(@PathVariable("userId") Long userId, @PathVariable("newRole") String role){
        return this.userService.updateRole(userId, role);
    }

    @Admin
    @PutMapping("/setAdmin/{id}")
    public User setUserAdmin(@PathVariable("userId") Long userId, @RequestBody User admin){
        return this.userService.setUserAdmin(userId, admin);
    }

    @DeleteMapping("/delete/{userId}")
    public void deleteUser(@PathVariable("userId") Long userId){
         this.userService.delete(userId);
    }

    @Admin
    @PutMapping("/activate/{id}")
    public Optional<User> activate(@PathVariable Long id) {
        return userService.activate(id);
    }

    @Admin
    @PutMapping("/deactivate/{id}")
    public Optional<User> deactivate(@PathVariable Long id) {
        return userService.deactivate(id);
    }

    @Admin
    @GetMapping("/Accounts")
    public Optional<List<Account>> getAllUserAccounts(@RequestBody User user){
        return userService.findAllUserAccounts(user);
    }

    @Admin
    @PutMapping("/suspend/{id}")
    public Optional<User> suspendUser(@PathVariable Long id, @RequestBody Date suspensionStartDate, @RequestBody Date suspentionEndDate){
        return userService.suspendUser(id, suspensionStartDate, suspentionEndDate);
    }

}
