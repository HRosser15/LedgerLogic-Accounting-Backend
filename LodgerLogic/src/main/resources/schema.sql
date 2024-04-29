CREATE SEQUENCE event_log_seq START WITH 5;
CREATE SEQUENCE journal_seq START WITH 5;
CREATE SEQUENCE journal_entry_seq START WITH 9;
CREATE SEQUENCE user_seq START WITH 4;
CREATE SEQUENCE security_question_seq START WITH 10;
CREATE SEQUENCE password_security_question_seq START WITH 10;
CREATE SEQUENCE password_seq START WITH 4;

CREATE TABLE USERS (
                       USER_ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                       ACCOUNT_CREATION_DATE TIMESTAMP,
                       BIRTH_DAY TIMESTAMP,
                       CITY CHARACTER VARYING,
                       EMAIL CHARACTER VARYING,
                       EXPIRATION_DATE DATE,
                       FAILED_LOGIN_ATTEMPT SMALLINT,
                       FIRST_NAME CHARACTER VARYING,
                       IMAGE_URL CHARACTER VARYING,
                       LAST_LOGIN_DATE TIMESTAMP,
                       LAST_NAME CHARACTER VARYING,
                       PREVIOUS_PASSWORDS VARCHAR(255),
                       ROLE CHARACTER VARYING,
                       STATE CHARACTER VARYING,
                       STATUS BOOLEAN,
                       STREET_ADDRESS CHARACTER VARYING,
                       SUSPENSION_END_DATE TIMESTAMP,
                       SUSPENSION_START_DATE TIMESTAMP,
                       USERNAME CHARACTER VARYING,
                       ZIP_CODE CHARACTER VARYING,
                       ADMIN_USER_ID BIGINT,
                       PASSWORD_PASSWORD_ID BIGINT
);

CREATE TABLE ACCOUNTS (
                          ACCOUNT_ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                          ACCOUNT_NAME CHARACTER VARYING,
                          ACCOUNT_NUMBER INTEGER,
                          ACTIVE BOOLEAN,
                          BALANCE NUMERIC,
                          CATEGORY CHARACTER VARYING,
                          COMMENT CHARACTER VARYING,
                          CREATION_DATE TIMESTAMP,
                          CREDIT NUMERIC,
                          DEBIT NUMERIC,
                          DESCRIPTION CHARACTER VARYING,
                          INITIAL_BALANCE NUMERIC,
                          NORMAL_SIDE CHARACTER VARYING,
                          ORDER_NUMBER INTEGER,
                          STATEMENT CHARACTER VARYING,
                          SUB_CATEGORY CHARACTER VARYING,
                          OWNER_USER_ID BIGINT
);

CREATE TABLE EMAILS (
                        EMAIL_ID BIGINT,
                        BODY CHARACTER VARYING,
                        SUBJECT CHARACTER VARYING,
                        RECEIVER_ID BIGINT,
                        SENDER_ID BIGINT
);

CREATE TABLE PASSWORDS (
                           PASSWORD_ID BIGINT,
                           CONTENT CHARACTER VARYING,
                           EXPIRATION_DATE TIMESTAMP
);

CREATE TABLE PASSWORDS_PASSWORD_SECURITY_QUESTIONS (
                                                       PASSWORD_PASSWORD_ID BIGINT,
                                                       PASSWORD_SECURITY_QUESTIONS_PASSWORD_SECURITY_QUESTION_ID BIGINT
);

CREATE TABLE PASSWORD_SECURITY_QUESTIONS (
                                             PASSWORD_SECURITY_QUESTION_ID BIGINT,
                                             ANSWER CHARACTER VARYING,
                                             QUESTION_SECURITY_QUESTION_ID BIGINT
);

CREATE TABLE SECURITY_QUESTIONS (
                                    SECURITY_QUESTION_ID BIGINT,
                                    CONTENT CHARACTER VARYING
);

CREATE TABLE JOURNAL (
                         JOURNAL_ID BIGINT,
                         ATTACHMENTS BINARY LARGE OBJECT,
                         BALANCE NUMERIC,
                         CREATED_DATE TIMESTAMP,
                         REJECTION_REASON CHARACTER VARYING,
                         STATUS TINYINT,
                         TRANSACTION_DATE TIMESTAMP,
                         CREATED_BY_USER_ID BIGINT
);

CREATE TABLE JOURNAL_ENTRY (
                               JOURNAL_ENTRY_ID BIGINT,
                               BALANCE NUMERIC,
                               CREDIT NUMERIC,
                               DEBIT NUMERIC,
                               DESCRIPTION CHARACTER VARYING,
                               REJECTION_REASON CHARACTER VARYING,
                               STATUS CHARACTER VARYING,
                               TRANSACTION_DATE TIMESTAMP,
                               ACCOUNT_ID BIGINT,
                               JOURNAL_ID BIGINT
);

CREATE TABLE JOURNAL_LINES (
                               JOURNAL_LINE_ID BIGINT,
                               CREDIT NUMERIC,
                               DEBIT NUMERIC,
                               ACCOUNT_ID BIGINT,
                               JOURNAL_ENTRY_ID BIGINT
);

CREATE TABLE GENERAL_LEDGER (
                                ID BIGINT,
                                ADDED_DATE TIMESTAMP,
                                ENTRY_TYPE CHARACTER VARYING,
                                ACCOUNT_ID BIGINT,
                                JOURNAL_ID BIGINT
);

CREATE TABLE USER_EVENT_LOG (
                                ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                                CURRENT_STATE CHARACTER LARGE OBJECT,
                                ENTITY_ID BIGINT,
                                MODIFICATION_TIME TIMESTAMP,
                                MODIFIED_BY_ID BIGINT,
                                PREVIOUS_STATE CHARACTER LARGE OBJECT,
                                TITLE CHARACTER VARYING
);