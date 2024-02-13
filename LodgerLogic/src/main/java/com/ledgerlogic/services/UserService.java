package com.ledgerlogic.services;

import com.ledgerlogic.models.Account;
import com.ledgerlogic.models.User;
import com.ledgerlogic.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AccountService accountService;

    public UserService(UserRepository userRepository, AccountService accountService){
        this.userRepository = userRepository;
        this.accountService = accountService;
    }

    public Optional<User> findByCredentials(String userName, String password){
        return userRepository.findByUsernameAndPassword(userName, password);
    }

    public User upsert(User user){
        return userRepository.save(user);
    }

    public User getByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User updateUser(User user){
        return userRepository.save(user);
    }

    public boolean emailIsTaken(String email){
        Optional<User> current = Optional.ofNullable(getByEmail(email));
        if(!current.isPresent()) return false;
        else return true;
    }

    public boolean userNameIsTaken(String userName){
        Optional<User> current = Optional.ofNullable(getByEmail(userName));
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

    public void delete(Long userId){
        this.userRepository.deleteById(userId);
    }

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



}
