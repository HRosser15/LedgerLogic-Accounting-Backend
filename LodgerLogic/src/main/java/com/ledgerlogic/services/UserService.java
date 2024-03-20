package com.ledgerlogic.services;

import com.ledgerlogic.models.Account;
import com.ledgerlogic.models.User;
import com.ledgerlogic.repositories.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AccountService accountService;
    private final EmailService   emailService;
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository, AccountService accountService, EmailService emailService){
        this.userRepository = userRepository;
        this.accountService = accountService;
        this.emailService = emailService;
    }

    public Optional<User> findByCredentials(String userName, String password){
        Optional<User> optionalUser = Optional.ofNullable(this.userRepository.findByUsername(userName));
        if (!optionalUser.isPresent()){
            return null;
        }

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
        List<User> admins = this.getByRole("admin");
        User newUser = user;
        if (admins.size() != 0){
            User admin = admins.get(0);
            newUser.setAdmin(admin);
        }
        return this.userRepository.save(newUser);
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
        return true;
    }

    public User getById(Long id){
        Optional<User> userOptional = this.userRepository.findById(id);
        if(!userOptional.isPresent()) return null;
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

    public User updateRole(Long userId, String role) {
        Optional<User> current = userRepository.findById(userId);
        if (!current.isPresent()) return null;
        current.get().setRole(role);
        return this.upsert(current.get());
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

    public boolean verifyPasswordContent(String passwordContentProvided, String storedPasswordContentHash){
        return this.passwordEncoder.matches(passwordContentProvided, storedPasswordContentHash);
    }

    public Optional<User> suspendUser(Long id, Date suspensionStartDate, Date suspentionEndDate) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if(!userOptional.isPresent()) return null;

        User user = userOptional.get();
        user.setSuspensionStartDate(suspensionStartDate);
        user.setSuspensionEndDate(suspentionEndDate);

        LocalDate today = LocalDate.now();
        if (suspensionStartDate.equals(today))
            user.setStatus(false);

        this.userRepository.save(user);
        return Optional.of(user);
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    private void updateUserStatus(){
        List<User> usersToUpdate = this.userRepository.findAll();
        LocalDate today = LocalDate.now();

        for (User user: usersToUpdate){
            if(user.getSuspensionStartDate().equals(today))
                deactivate(user.getUserId());
            if (user.getSuspensionEndDate().equals(today)){
               emailService.endOfSuspensionNotification(user.getAdmin().getEmail(), user.getFirstName() + " " + user.getLastName() + " suspension period end today!");
            }
        }
    }

    public User setUserAdmin(Long userId, User admin) {
        Optional<User> optionalUser = this.userRepository.findById(userId);
        if (!optionalUser.isPresent()) return null;
        User user = optionalUser.get();
        user.setAdmin(admin);
        return this.userRepository.save(user);
    }
}
