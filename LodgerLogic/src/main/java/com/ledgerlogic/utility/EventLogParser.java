package com.ledgerlogic.utility;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventLogParser {
    private static final Pattern accountPattern = Pattern.compile(
            "Account\\(accountId=(\\d+), accountNumber=(\\d+), accountName=([^,]+), description=([^,]+), normalSide=([^,]+), category=([^,]+), active=([^,]+), subCategory=([^,]+), initialBalance=([^,]+), debit=([^,]+), credit=([^,]+), balance=([^,]+), creationDate=([^,]+), orderNumber=(\\d+), statement=([^,]+), comment=([^,]+), owner=(.+?)\\)"
    );

    private static final Pattern journalPattern = Pattern.compile(
            "Journal\\{journalId=(\\d+), status=([^,]+), rejectionReason='([^']+)', balance=([^,]+), createdDate=(.+?)\\}"
    );

    private static final Pattern userPattern = Pattern.compile(
            "User\\(userId=(\\d+), username=([^,]+), firstName=([^,]+), lastName=([^,]+), email=([^,]+), role=([^,]+), streetAddress=([^,]+), city=([^,]+), state=([^,]+), zipCode=([^,]+), birthDay=([^,]+), status=([^,]+), failedLoginAttempt=(\\d+), suspensionStartDate=([^,]+), suspensionEndDate=([^,]+), expirationDate=([^,]+), lastLoginDate=([^,]+), accountCreationDate=([^,]+), imageUrl=([^,]+), previousPasswords=(.+?), password=(.+?), admin=(.+?)\\)"
    );

    public static AccountData parseAccountData(String stateString) {
        Matcher matcher = accountPattern.matcher(stateString);
        if (matcher.find()) {
            return new AccountData(
                    Long.parseLong(matcher.group(1)),
                    Integer.parseInt(matcher.group(2)),
                    matcher.group(3),
                    matcher.group(4),
                    matcher.group(5),
                    matcher.group(6),
                    Boolean.parseBoolean(matcher.group(7)),
                    matcher.group(8),
                    new BigDecimal(matcher.group(9)),
                    new BigDecimal(matcher.group(10)),
                    new BigDecimal(matcher.group(11)),
                    new BigDecimal(matcher.group(12)),
                    matcher.group(13),
                    Integer.parseInt(matcher.group(14)),
                    matcher.group(15),
                    matcher.group(16),
                    matcher.group(17)
            );
        }
        return null;
    }

    public static JournalData parseJournalData(String stateString) {
        Matcher matcher = journalPattern.matcher(stateString);
        if (matcher.find()) {
            return new JournalData(
                    Long.parseLong(matcher.group(1)),
                    matcher.group(2),
                    matcher.group(3),
                    new BigDecimal(matcher.group(4)),
                    matcher.group(5)
            );
        }
        return null;
    }

    public static UserData parseUserData(String stateString) {
        Matcher matcher = userPattern.matcher(stateString);
        if (matcher.find()) {
            return new UserData(
                    Long.parseLong(matcher.group(1)),
                    matcher.group(2),
                    matcher.group(3),
                    matcher.group(4),
                    matcher.group(5),
                    matcher.group(6),
                    matcher.group(7),
                    matcher.group(8),
                    matcher.group(9),
                    matcher.group(10),
                    matcher.group(11) != null ? matcher.group(11) : null,
                    Boolean.parseBoolean(matcher.group(12)),
                    Integer.parseInt(matcher.group(13)),
                    matcher.group(14) != null ? matcher.group(14) : null,
                    matcher.group(15) != null ? matcher.group(15) : null,
                    matcher.group(16),
                    matcher.group(17) != null ? matcher.group(17) : null,
                    matcher.group(18),
                    matcher.group(19),
                    matcher.group(20),
                    matcher.group(21),
                    matcher.group(22)
            );
        }
        return null;
    }

    public static class AccountData {
        public final Long accountId;
        public final int accountNumber;
        public final String accountName;
        public final String description;
        public final String normalSide;
        public final String category;
        public final boolean active;
        public final String subCategory;
        public final BigDecimal initialBalance;
        public final BigDecimal debit;
        public final BigDecimal credit;
        public final BigDecimal balance;
        public final String creationDate;
        public final int orderNumber;
        public final String statement;
        public final String comment;
        public final String owner;

        public AccountData(Long accountId, int accountNumber, String accountName, String description, String normalSide, String category, boolean active, String subCategory, BigDecimal initialBalance, BigDecimal debit, BigDecimal credit, BigDecimal balance, String creationDate, int orderNumber, String statement, String comment, String owner) {
            this.accountId = accountId;
            this.accountNumber = accountNumber;
            this.accountName = accountName;
            this.description = description;
            this.normalSide = normalSide;
            this.category = category;
            this.active = active;
            this.subCategory = subCategory;
            this.initialBalance = initialBalance;
            this.debit = debit;
            this.credit = credit;
            this.balance = balance;
            this.creationDate = creationDate;
            this.orderNumber = orderNumber;
            this.statement = statement;
            this.comment = comment;
            this.owner = owner;
        }
    }

    public static class JournalData {
        public final Long journalId;
        public final String status;
        public final String rejectionReason;
        public final BigDecimal balance;
        public final String createdDate;

        public JournalData(Long journalId, String status, String rejectionReason, BigDecimal balance, String createdDate) {
            this.journalId = journalId;
            this.status = status;
            this.rejectionReason = rejectionReason;
            this.balance = balance;
            this.createdDate = createdDate;
        }
    }

    public static class UserData {
        public final Long userId;
        public final String username;
        public final String firstName;
        public final String lastName;
        public final String email;
        public final String role;
        public final String streetAddress;
        public final String city;
        public final String state;
        public final String zipCode;
        public final String birthDay;
        public final boolean status;
        public final int failedLoginAttempt;
        public final String suspensionStartDate;
        public final String suspensionEndDate;
        public final String expirationDate;
        public final String lastLoginDate;
        public final String accountCreationDate;
        public final String imageUrl;
        public final String previousPasswords;
        public final String password;
        public final String admin;

        public UserData(Long userId, String username, String firstName, String lastName, String email, String role, String streetAddress, String city, String state, String zipCode, String birthDay, boolean status, int failedLoginAttempt, String suspensionStartDate, String suspensionEndDate, String expirationDate, String lastLoginDate, String accountCreationDate, String imageUrl, String previousPasswords, String password, String admin) {
            this.userId = userId;
            this.username = username;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.role = role;
            this.streetAddress = streetAddress;
            this.city = city;
            this.state = state;
            this.zipCode = zipCode;
            this.birthDay = birthDay;
            this.status = status;
            this.failedLoginAttempt = failedLoginAttempt;
            this.suspensionStartDate = suspensionStartDate;
            this.suspensionEndDate = suspensionEndDate;
            this.expirationDate = expirationDate;
            this.lastLoginDate = lastLoginDate;
            this.accountCreationDate = accountCreationDate;
            this.imageUrl = imageUrl;
            this.previousPasswords = previousPasswords;
            this.password = password;
            this.admin = admin;
        }
    }
}
