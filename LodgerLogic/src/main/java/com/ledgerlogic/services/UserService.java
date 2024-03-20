package com.ledgerlogic.services;

import com.ledgerlogic.annotations.Admin;
import com.ledgerlogic.models.Account;
import com.ledgerlogic.models.Password;
import com.ledgerlogic.models.User;
import com.ledgerlogic.repositories.PasswordRepository;
import com.ledgerlogic.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordRepository passwordRepository;
    private final AccountService accountService;
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public UserService(UserRepository userRepository, PasswordRepository passwordRepository, AccountService accountService){
        this.userRepository = userRepository;
        this.passwordRepository = passwordRepository;
        this.accountService = accountService;
    }

    public Optional<User> findByCredentials(String userName, String password){
        Optional<User> optionalUser = Optional.ofNullable(this.userRepository.findByUsername(userName));
        if (!optionalUser.isPresent()){
            return null;
        }

        System.out.println("- optionalUser from UserService: " + optionalUser);

        String currentPasswordContentHash = optionalUser.get().getPassword().getContent();
        if (verifyPasswordContent(password, currentPasswordContentHash)){
            return optionalUser;
        }
        return null;
    }

    public User upsert(User user){
        return userRepository.save(user);
    }

    public User save(User user){
        return userRepository.save(user);
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public boolean emailIsTaken(String email){
        Optional<User> current = Optional.ofNullable(findByEmail(email));
        if(!current.isPresent()) return false;
        else return true;
    }

    public boolean userNameIsTaken(String userName){
        Optional<User> current = Optional.ofNullable(findByEmail(userName));
        if(!current.isPresent()) return false;
        else return true;
    }

    public User getById(Long id){
        Optional<User> userOptional = this.userRepository.findById(id);
        if(!userOptional.isPresent()){
            return null;
        }
        return userOptional.get();
    }

    public List<User> findByFirstName(String firstName) {
        if(!(userRepository.findByFirstName(firstName).isEmpty())) {
            return userRepository.findByFirstName(firstName);
        }
        else {
            return userRepository.findByFirstName(firstName);
        }
    }

    public List<User> findByLastName(String firstName) {
        if(!(userRepository.findByLastName(firstName).isEmpty())) {
            return userRepository.findByFirstName(firstName);
        }
        else {
            return userRepository.findByFirstName(firstName);
        }
    }

    public List<User> findByFullName(String firstName, String lastName) {
        return userRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    public Optional<User> findByUsername(String username){
        return Optional.ofNullable(userRepository.findByUsername(username));
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public List<User> getByRole(String role){
        return userRepository.findByRole(role);
    }

    // @Admin
    public User updateRole(Long userId, String role) {
        Optional<User> current = userRepository.findById(userId);
        current.get().setRole(role);
        return this.upsert(current.get());
    }

    // @Admin
    public void delete(Long userId){
        this.userRepository.deleteById(userId);
    }

    // @Admin
    public Optional<User> activate(Long userId){
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setStatus(true);
            userRepository.save(user);
            return userRepository.findById(userId);
        }
        return null;
    }

    // @Admin
    public Optional<User> deactivate(Long userId){
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setStatus(false);
            userRepository.save(user);
            return userRepository.findById(userId);
        }
        return null;
    }

    // @Admin
    public Optional<List<Account>> findAllUserAccounts(User user){
        return accountService.getAllByUser(user);
    }

    public Account deleteAccount(User user, Account account){
        Optional<List<Account>> optionalAccounts = this.findAllUserAccounts(user);
        if(optionalAccounts.filter(accounts -> accounts.contains(account)).isPresent()){
            this.accountService.delete(account);
            return account;
        }
        return null;
    }

    public boolean verifyPasswordContent(String passwordContentProvided, String storedPasswordContentHash){
        return this.passwordEncoder.matches(passwordContentProvided, storedPasswordContentHash);
    }

}
