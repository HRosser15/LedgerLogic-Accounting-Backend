INSERT INTO security_questions (security_question_id, content)
VALUES
    (1, 'What is your mother''s maiden name?'),
    (2, 'What was the make of your first car?'),
    (3, 'What street did you grow up on?'),
    (4, 'What was the name of your first pet?'),
    (5, 'What was the name of your childhood best friend?'),
    (6, 'What year was your grandmother on your mother''s side born?'),
    (7, 'What was your nickname growing up?'),
    (8, 'What is your favorite movie quote?'),
    (9, 'What school did you attend for first grade?');


INSERT INTO passwords (password_id, content, expiration_date)
VALUES
    (1, '$2a$10$wKKDgToC5uV..dEepLaMxe.aE53i3DSEIzVCroSSYQo6eDqXOeHLu', '2024-07-28 02:03:02.204'),
    (2, '$2a$10$Hla.HS5yNz4wLgONLYgiXu4iPWWAFUYekjQ5AOYoGldGrO.MELOgS', '2024-07-28 02:04:11.033'),
    (3, '$2a$10$WfnMJHzc5FOCZMuLnzxCEuL96GKHKzVMPo0BR34eG5DEUsypa1Zj.', '2024-07-28 02:04:14.247');


INSERT INTO password_security_questions (password_security_question_id, answer, question_security_question_id)
VALUES
    (1, 'Vance', 1),
    (2, 'Honda', 2),
    (3, '1425 London Ave', 3),
    (4, 'Sprinkles', 4),
    (5, 'Jimmy', 5),
    (6, '1946', 6),
    (7, 'Butterball', 7),
    (8, 'Stay Golden ponyboy', 8),
    (9, 'Morningside Elementary', 9);


INSERT INTO users (user_id, account_creation_date, birth_day, city, email, expiration_date, failed_login_attempt, first_name, image_url, last_login_date, last_name, previous_passwords, role, state, status, street_address, suspension_end_date, suspension_start_date, username, zip_code, admin_user_id, password_password_id)
VALUES
    (1, '2024-04-28 02:03:02.204', null, 'Atlanta', 'hrosser15@gmail.com', '2024-05-28', 0, 'Harrison', null, '2024-04-28', 'Rosser', '["$2a$10$AKEgDULg/UJyHOMMFpbW2eFBbthDkRbxLyeUjC8A/G0rUVj39uk7S"]', 'admin', 'GA', TRUE, null, null, null, 'hrosser0424', '30033', null, 1),
    (2, '2024-04-28 02:04:11.033', null, 'Atlanta', 'bw@gmail.com', '2024-05-28', 0, 'Benjamin', null, '2024-04-21', 'Wilson', '["$2a$10$a2Slcwis6Gq5DKaQWNtv1ezeIeihLkD09t7XqcvIOpWMYObfaRodO"]', 'manager', 'GA', TRUE, null, null, null, 'bwilson0424', '30033', 1, 2),
    (3, '2024-04-28 02:04:14.247', null, 'Atlanta', 'jsmith@gmail.com', '2024-05-28', 0, 'ken', null, '2024-04-25', 'smith', '["$2a$10$p8nZwcCYl/xJCPaH2YhR7OR3Xn5C6XZiaf0xlSHCeqJQArKUXEuAu"]', 'accountant', 'GA', TRUE, null, null, null, 'ksmith0424', '30033', 1, 3);


INSERT INTO passwords_password_security_questions (password_password_id, password_security_questions_password_security_question_id)
VALUES
    (1, 1),
    (1, 2),
    (1, 3),
    (2, 4),
    (2, 5),
    (2, 6),
    (3, 7),
    (3, 8),
    (3, 9);


INSERT INTO accounts (account_id, account_name, account_number, active, balance, category, comment, creation_date, credit, debit, description, initial_balance, normal_side, order_number, statement, sub_category, owner_user_id)
VALUES
    (1, 'Cash', 1010, TRUE, 25000.00, 'Assets', 'Used for daily operational expenses.', '2024-04-28 02:03:08.741', 40000.00, 65000.00, 'Cash on hand and in banks', 25000.00, 'Debit', 1, 'IS', 'Current Assets', 1),
    (2, 'Accounts Receivable', 1020, TRUE, 25000.00, 'Assets', 'Represents outstanding revenue to be collected.', '2024-04-28 02:03:11.734', 50000.00, 75000.00, 'Amounts customers owe for services', 25000.00, 'Debit', 2, 'BS', 'Current Assets', 1),
    (3, 'Supplies', 1030, TRUE, 20000.00, 'Assets', 'Essential for ongoing roofing projects.', '2024-04-28 02:03:14.401', 100000.00, 120000.00, 'Roofing materials and supplies', 20000.00, 'Debit', 3, 'IS', 'Current Assets', 1),
    (4, 'Office Building', 1040, TRUE, 470000.00, 'Assets', 'Long-term assets for efficient operations.', '2024-04-28 02:03:17.236', 10000.00, 480000.00, 'Office Building', 470000.00, 'Debit', 4, 'BS', 'Property Plant and Equipment', 1),
    (5, 'Vehicles', 1050, TRUE, 80000.00, 'Assets', 'Transportation to and from job sites.', '2024-04-28 02:03:20.257', 35000.00, 115000.00, 'Company vehicles', 80000.00, 'Debit', 5, 'BS', 'Property Plant and Equipment', 1),
    (6, 'Accounts Payable', 3010, TRUE, 20000.00, 'Liabilities', 'Reflects outstanding payables for materials and services.', '2024-04-28 02:03:22.975', 80000.00, 60000.00, 'Amounts owed to suppliers', 20000.00, 'Credit', 6, 'BS', 'Current Liabilities', 1),
    (7, 'Loans Payable', 3020, TRUE, 370000.00, 'Liabilities', 'Long-term financing for business growth.', '2024-04-28 02:03:25.88', 390000.00, 20000.00, 'Outstanding loans', 370000.00, 'Credit', 7, 'BS', 'Long-Term Liabilities', 1),
    (8, 'Accrued Expenses', 3030, TRUE, 5000.00, 'Liabilities', 'Represents accrued liabilities yet to be settled.', '2024-04-28 02:03:27.903', 7000.00, 2000.00, 'Unpaid expenses', 5000.00, 'Credit', 30, 'BS', 'Current Liabilities', 1),
    (9, 'Wages Payable', 3040, TRUE, 15000.00, 'Liabilities', 'Represents amounts owed to employees for work performed but not yet paid. It reflects the company''s obligation to compensate employees for their services.', '2024-04-28 02:03:30.338', 20000.00, 5000.00, 'Liabilities arising from unpaid wages owed to employees.', 15000.00, 'Credit', 8, 'BS', 'Current Liabilities', 1),
    (10, 'Owner''s Capital', 5010, TRUE, 530000.00, 'Equity', 'Capital contributed by the business owner.', '2024-04-28 02:03:33.38', 530000.00, 0.00, 'Owner''s investment in the business', 530000.00, 'Credit', 9, 'BS', 'Owner''s Equity', 1),
    (11, 'Roofing Services', 6010, TRUE, 320000.00, 'Revenue', 'Main revenue source from roofing services', '2024-04-28 02:03:36.036', 580000.00, 260000.00, 'Revenue from roofing services', 3200000.00, 'Credit', 11, 'IS', 'Service Revenue', 1),
    (12, 'Maintenance Contracts', 6020, TRUE, 50000.00, 'Revenue', 'Revenue generated from maintenance contracts', '2024-04-28 02:03:38.411', 50000.00, 0.00, 'Revenue from maintenance contracts', 50000.00, 'Credit', 12, 'IS', 'Service Revenue', 1),
    (13, 'Roof Inspection', 6030, TRUE, 5000.00, 'Revenue', 'Income generated from roof inspection services', '2024-04-28 02:03:41.288', 5000.00, 0.00, 'Revenue from roof inspections', 5000.00, 'Credit', 13, 'IS', 'Service Revenue', 1),
    (14, 'Utilities', 7010, TRUE, 15000.00, 'Expenses', 'Includes electricity, water, and other utility expenses', '2024-04-28 02:03:43.756', 0.00, 15000.00, 'Monthly utility expenses', 15000.00, 'Debit', 21, 'IS', 'Operating Expenses', 1),
    (15, 'Insurance', 7020, TRUE, 20000.00, 'Expenses', 'Annual insurance costs for business coverage', '2024-04-28 02:03:46.796', 20000.00, 40000.00, 'Annual insurance premiums', 20000.00, 'Debit', 22, 'IS', 'Operating Expenses', 1),
    (16, 'Employee Wages', 7030, TRUE, 200000.00, 'Expenses', 'Payment for staff and labor', '2024-04-28 02:03:49.728', 30000.00, 230000.00, 'Salaries and wages', 200000.00, 'Debit', 23, 'IS', 'Cost of Goods Sold', 1),
    (17, 'Used Supplies', 7070, TRUE, 20000.00, 'Expenses', 'Expenditure on promotional activities', '2024-04-28 02:03:53.148', 0.00, 20000.00, 'Supplies used for conducting business', 20000.00, 'Debit', 26, 'IS', 'Cost of Goods Sold', 1),
    (18, 'Vehicle Depreciation', 7050, TRUE, 10000.00, 'Expenses', 'Accounting for the reduction in value of assets over time', '2024-04-28 02:03:55.838', 0.00, 10000.00, 'Depreciation of vehicles', 10000.00, 'Debit', 25, 'IS', 'Other Expenses', 1),
    (19, 'Advertising', 7060, TRUE, 25000.00, 'Expenses', 'Expenditure on promotional activities', '2024-04-28 02:04:00.716', 0.00, 25000.00, 'Marketing and advertising expenses', 25000.00, 'Debit', 26, 'IS', 'Operating Expenses', 1),
    (20, 'Cash Dividends', 5020, TRUE, 205000.00, 'Equity', 'Represents cash paid out as dividends to shareholders', '2024-04-28 02:04:04.215', 0.00, 205000.00, 'Cash dividends paid to shareholders', 205000.00, 'Credit', 10, 'IS', 'Dividends', 1),
    (21, 'Stock Dividends', 5030, TRUE, 200000.00, 'Equity', 'Represents the value of stock issued as dividends to shareholders', '2024-04-28 02:04:07.149', 0.00, 200000.00, 'Stock dividends paid to shareholders', 200000.00, 'Credit', 14, 'IS', 'Dividends', 1);


INSERT INTO journal (journal_id, attachments, balance, created_date, rejection_reason, status, transaction_date, created_by_user_id)
VALUES
    (1, null, null, '2024-04-28', null, 1, '2024-04-08 20:00:00', 1),
    (2, null, null, '2024-04-28', 'Duplicate Entry', 2, '2024-04-08 20:00:00', 1),
    (3, null, null, '2024-04-28', null, 0, '2024-04-08 20:00:00', 1),
    (4, null, null, '2024-04-28', null, 0, '2024-04-08 20:00:00', 1);


INSERT INTO journal_entry (journal_entry_id, balance, credit, debit, description, rejection_reason, status, transaction_date, account_id, journal_id)
VALUES
    (1, -500.00, 500.00, 0.00, 'Purchased equipment', null, 'approved', '2024-04-08 20:00:00', 1, 1),
    (2, 500.00, 0.00, 500.00, 'Purchased equipment', null, 'approved', '2024-04-08 20:00:00', 2, 1),
    -- Add the remaining journal entry records here
    (7, 10000.00, 0.00, 10000.00, 'Investment', null, 'pending', '2024-04-08 20:00:00', 1, 4),
    (8, -10000.00, 10000.00, 0.00, 'Investment', null, 'pending', '2024-04-08 20:00:00', 10, 4);


-- Insert data into the users_event_log table
INSERT INTO user_event_log (id, current_state, entity_id, modification_time, modified_by_id, previous_state, title)
VALUES
    (32, '{accountId:1, accountNumber:1010, accountName:Cash, description:Cash on hand and in banks, normalSide:Debit, category:Assets, active:true, subCategory:Current Assets, initialBalance:25000.00, debit:65000.00, credit:40500.00, balance:24500.00, creationDate:2024-04-28 02:03:08.741, orderNumber:1, statement:IS, comment:Used for daily operational expenses., owner:null}', 1, '2024-04-28 02:16:23.720955', 1, 'Account(accountId=null, accountNumber=1010, accountName=Cash, description=Cash on hand and in banks, normalSide=Debit, category=Assets, active=true, subCategory=Current Assets, initialBalance=25000.00, debit=65000.00, credit=40500.00, balance=24500.00, creationDate=2024-04-28 02:03:08.741, orderNumber=1, statement=IS, comment=Used for daily operational expenses., owner=null)', 'Update Account'),
    (33, '{accountId:2, accountNumber:1020, accountName:Accounts Receivable, description:Amounts customers owe for services, normalSide:Debit, category:Assets, active:true, subCategory:Current Assets, initialBalance:25000.00, debit:75500.00, credit:50000.00, balance:25500.00, creationDate:2024-04-28 02:03:11.734, orderNumber:2, statement:BS, comment:Represents outstanding revenue to be collected., owner:null}', 2, '2024-04-28 02:16:23.722957', 1, 'Account(accountId=null, accountNumber=1020, accountName=Accounts Receivable, description=Amounts customers owe for services, normalSide=Debit, category=Assets, active=true, subCategory=Current Assets, initialBalance=25000.00, debit=75500.00, credit=50000.00, balance=25500.00, creationDate=2024-04-28 02:03:11.734, orderNumber=2, statement=BS, comment=Represents outstanding revenue to be collected., owner=null)', 'Update Account'),
    (34, '{"journalId":1, "status":APPROVED, "rejectionReason":"", "balance":null, "createdDate":"2024-04-28 02:12:34.77"}, Entries: [Account: Cash, Debit: 0.00, Credit: 500.00; Account: Accounts Receivable, Debit: 500.00, Credit: 0.00}', 1, '2024-04-28 02:16:23.723957', 1, 'Journal{journalId=null, status=PENDING, rejectionReason='''', balance=null, createdDate=2024-04-28 02:12:34.77}, Entries: [Account: Cash, Debit: 0.00, Credit: 500.00; Account: Accounts Receivable, Debit: 500.00, Credit: 0.00]', 'Approved New Journal'),
    (35, '{"journalId":2, "status":REJECTED, "rejectionReason":"Duplicate Entry", "balance":null, "createdDate":"2024-04-28 02:13:42.032"}', 2, '2024-04-28 02:16:38.308042', 1, 'Journal{journalId=null, status=PENDING, rejectionReason='''', balance=null, createdDate=2024-04-28 02:13:42.032}', 'Rejected Journal');
