package com.ledgerlogic.services;

import com.ledgerlogic.dtos.SuspensionDTO;
import com.ledgerlogic.models.Account;
import com.ledgerlogic.models.EventLog;
import com.ledgerlogic.models.User;
import com.ledgerlogic.repositories.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final AccountService accountService;
    private final EmailService   emailService;

    private final EventLogService eventLogService;

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository, AccountService accountService, EmailService emailService, EventLogService eventLogService){
        this.userRepository = userRepository;
        this.accountService = accountService;
        this.emailService = emailService;
        this.eventLogService = eventLogService;
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
        User previousState = this.userRepository.getById(user.getUserId());
        EventLog eventLog = new EventLog("Update User", user.getUserId(), getCurrentUserId(), LocalDateTime.now(), user.toString(), previousState.toString());
        this.eventLogService.saveEventLog(eventLog);

        return userRepository.save(user);
    }

    public User save(User user){
        List<User> admins = this.getByRole("admin");
        User newUser = user;
        if (admins.size() != 0){
            User admin = admins.get(0);
            newUser.setAdmin(admin);
        }

//        emailService.sendApprovalRequestEmail(newUser.getAdmin().getEmail(), newUser.getEmail());
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

        String previousRole = current.get().getRole();
        EventLog eventLog = new EventLog("Update Role", userId, getCurrentUserId(), LocalDateTime.now(), role, previousRole);
        this.eventLogService.saveEventLog(eventLog);

        current.get().setRole(role);
        return this.upsert(current.get());
    }

    public void delete(Long userId){
        User previousState = this.userRepository.getById(userId);
        EventLog eventLog = new EventLog("Deleted User", userId, getCurrentUserId(), LocalDateTime.now(), null, previousState.toString());
        this.eventLogService.saveEventLog(eventLog);

        this.userRepository.deleteById(userId);
    }

    public Optional<User> activate(Long userId){
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            EventLog eventLog = new EventLog("Activate User Status", userId, getCurrentUserId(), LocalDateTime.now(), user.getState(), "true");
            this.eventLogService.saveEventLog(eventLog);

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

            EventLog eventLog = new EventLog("Deactivate User Status", userId, getCurrentUserId(), LocalDateTime.now(), user.getState(), "false");
            this.eventLogService.saveEventLog(eventLog);

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

    public Optional<User> suspendUser(Long id, SuspensionDTO suspensionDTO){
        Optional<User> userOptional = this.userRepository.findById(id);
        if(!userOptional.isPresent()) return null;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String todayString          = dateFormat.format(new Date());
        Date today                  = parseDate(todayString);

        Date suspensionStartDate = parseDate(suspensionDTO.getSuspensionStartDate());

        Date suspensionEndDate   = parseDate(suspensionDTO.getSuspensionEndDate());

        User user = userOptional.get();
        user.setSuspensionStartDate(suspensionStartDate);
        user.setSuspensionEndDate(suspensionEndDate);

        if (suspensionStartDate.equals(today))
            user.setStatus(false);

        this.userRepository.save(user);
        return Optional.of(user);
    }

    public List<User> getUsersWithExpiredPasswords() {
        LocalDate currentDate = LocalDate.now();
        return userRepository.findByExpirationDateBefore(currentDate);
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    private void updateUserStatus(){
        List<User> usersToUpdate = this.userRepository.findAll();
        Date today = new Date();

        for (User user: usersToUpdate){
            if(user.getSuspensionStartDate().equals(today)) {
                deactivate(user.getUserId());

                EventLog eventLog = new EventLog("deactivate User Status", user.getUserId(), getCurrentUserId(), LocalDateTime.now(), user.getState(), "false");
                this.eventLogService.saveEventLog(eventLog);
            }
            if (user.getSuspensionEndDate().equals(today)){
               emailService.endOfSuspensionNotification(user.getAdmin().getEmail(), user.getFirstName() + " " + user.getLastName() + " suspension period end today!");
            }
        }
    }

    public User setUserAdmin(Long userId, User admin) {
        Optional<User> optionalUser = this.userRepository.findById(userId);
        if (!optionalUser.isPresent()) return null;
        User user = optionalUser.get();

        EventLog eventLog = new EventLog("Change User Admin", userId, getCurrentUserId(), LocalDateTime.now(), admin.toString(), user.getAdmin().toString());
        this.eventLogService.saveEventLog(eventLog);

        user.setAdmin(admin);
        return this.userRepository.save(user);
    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                User user = (User) principal;
                return user.getUserId();
            }
        }
        return null;
    }

    private static Date parseDate(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(dateString);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
