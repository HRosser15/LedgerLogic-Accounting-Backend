package com.ledgerlogic.controllers;

import com.ledgerlogic.annotations.Admin;
import com.ledgerlogic.annotations.Authorized;
import com.ledgerlogic.annotations.Manager;
import com.ledgerlogic.models.Account;
import com.ledgerlogic.models.User;
import com.ledgerlogic.services.UserService;
import com.ledgerlogic.util.PasswordValidator;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/users")
public class UserController {
    public UserService userService;
    public PasswordValidator passwordValidator;

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
                passwordValidator = new PasswordValidator();
                if(passwordValidator.validatePassword(user.getPassword())){
                    currentUserToBeUpdated.setPassword(user.getPassword());
                }else{
                    System.out.println("Invalid password!");
                    return null;
                }
            }

            return this.userService.upsert(currentUserToBeUpdated);
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

    @GetMapping("/allUsers")
    public List<User> getAllUsers(){
        return this.userService.getAll();
    }

    @GetMapping
    public List<User> getByRole(@PathVariable("role") String role){
        return this.userService.getByRole(role);
    }

    @PutMapping("/updateRole/{userId}/{newRole}")
    public User updateUserRole(@PathVariable("userId") Long userId, @PathVariable("role") String role){
        return this.userService.updateRole(userId, role);
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

    @GetMapping("/Accounts")
    public Optional<List<Account>> getAllUserAccounts(@RequestBody User user){
        return userService.findAllUserAccounts(user);
    }
}
