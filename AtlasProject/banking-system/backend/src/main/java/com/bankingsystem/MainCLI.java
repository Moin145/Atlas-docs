package com.bankingsystem;

import com.bankingsystem.model.Account;
import com.bankingsystem.model.Customer;
import com.bankingsystem.model.Transaction;
import com.bankingsystem.repository.AccountRepository;
import com.bankingsystem.repository.CustomerRepository;
import com.bankingsystem.service.*;
import com.bankingsystem.util.UndoRedoStack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

@Component
public class MainCLI implements CommandLineRunner {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private DepositService depositService;
    
    @Autowired
    private WithdrawService withdrawService;
    
    @Autowired
    private TransferService transferService;
    
    @Autowired
    private AuditService auditService;
    
    @Autowired
    private UndoRedoStack undoRedoStack;
    
    private final Scanner scanner = new Scanner(System.in);
    private String currentUserId = "CLI_USER";
    
    @Override
    public void run(String... args) throws Exception {
        System.out.println("🏦 Welcome to Indian Banking System (CLI Mode)");
        System.out.println("================================================");
        
        // Initialize with sample data
        initializeSampleData();
        
        // Main menu loop
        while (true) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    createCustomer();
                    break;
                case 2:
                    createAccount();
                    break;
                case 3:
                    depositMoney();
                    break;
                case 4:
                    withdrawMoney();
                    break;
                case 5:
                    transferMoney();
                    break;
                case 6:
                    viewAccountBalance();
                    break;
                case 7:
                    viewTransactionHistory();
                    break;
                case 8:
                    undoTransaction();
                    break;
                case 9:
                    redoTransaction();
                    break;
                case 10:
                    viewAuditLogs();
                    break;
                case 11:
                    viewAllCustomers();
                    break;
                case 12:
                    viewAllAccounts();
                    break;
                case 0:
                    System.out.println("Thank you for using Indian Banking System! 🙏");
                    return;
                default:
                    System.out.println("❌ Invalid choice. Please try again.");
            }
            
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }
    
    private void displayMainMenu() {
        System.out.println("\n🏦 Indian Banking System - Main Menu");
        System.out.println("====================================");
        System.out.println("1.  Create Customer");
        System.out.println("2.  Create Account");
        System.out.println("3.  Deposit Money");
        System.out.println("4.  Withdraw Money");
        System.out.println("5.  Transfer Money");
        System.out.println("6.  View Account Balance");
        System.out.println("7.  View Transaction History");
        System.out.println("8.  Undo Last Transaction");
        System.out.println("9.  Redo Transaction");
        System.out.println("10. View Audit Logs");
        System.out.println("11. View All Customers");
        System.out.println("12. View All Accounts");
        System.out.println("0.  Exit");
        System.out.println("====================================");
    }
    
    private void createCustomer() {
        System.out.println("\n👤 Create New Customer");
        System.out.println("======================");
        
        try {
            String firstName = getStringInput("Enter First Name: ");
            String lastName = getStringInput("Enter Last Name: ");
            String email = getStringInput("Enter Email: ");
            String mobileNumber = getStringInput("Enter Mobile Number (10 digits): ");
            String address = getStringInput("Enter Address: ");
            String city = getStringInput("Enter City: ");
            String state = getStringInput("Enter State: ");
            String pincode = getStringInput("Enter Pincode: ");
            
            Customer customer = new Customer(firstName, lastName, email, mobileNumber, address, city, state, pincode);
            customer = customerRepository.save(customer);
            
            System.out.println("✅ Customer created successfully!");
            System.out.println("Customer ID: " + customer.getId());
            System.out.println("Full Name: " + customer.getFullName());
            
            auditService.logSuccess(currentUserId, "CREATE_CUSTOMER", "CUSTOMER", customer.getId(), 
                    "Created new customer: " + customer.getFullName());
            
        } catch (Exception e) {
            System.out.println("❌ Error creating customer: " + e.getMessage());
        }
    }
    
    private void createAccount() {
        System.out.println("\n🏦 Create New Account");
        System.out.println("=====================");
        
        try {
            String customerId = getStringInput("Enter Customer ID: ");
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            
            System.out.println("Account Types:");
            System.out.println("1. SAVINGS");
            System.out.println("2. CURRENT");
            System.out.println("3. FIXED_DEPOSIT");
            System.out.println("4. RECURRING_DEPOSIT");
            
            int accountTypeChoice = getIntInput("Select Account Type (1-4): ");
            Account.AccountType accountType;
            
            switch (accountTypeChoice) {
                case 1: accountType = Account.AccountType.SAVINGS; break;
                case 2: accountType = Account.AccountType.CURRENT; break;
                case 3: accountType = Account.AccountType.FIXED_DEPOSIT; break;
                case 4: accountType = Account.AccountType.RECURRING_DEPOSIT; break;
                default: throw new RuntimeException("Invalid account type selection");
            }
            
            String accountNumber = "ACC" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 6);
            Account account = new Account(accountNumber, customer, accountType);
            account = accountRepository.save(account);
            
            System.out.println("✅ Account created successfully!");
            System.out.println("Account Number: " + account.getAccountNumber());
            System.out.println("Account Type: " + account.getAccountType().getDisplayName());
            System.out.println("Customer: " + customer.getFullName());
            
            auditService.logSuccess(currentUserId, "CREATE_ACCOUNT", "ACCOUNT", account.getId(), 
                    "Created new " + accountType.getDisplayName() + " for " + customer.getFullName());
            
        } catch (Exception e) {
            System.out.println("❌ Error creating account: " + e.getMessage());
        }
    }
    
    private void depositMoney() {
        System.out.println("\n💰 Deposit Money");
        System.out.println("================");
        
        try {
            String accountNumber = getStringInput("Enter Account Number: ");
            BigDecimal amount = getBigDecimalInput("Enter Amount (₹): ");
            String description = getStringInput("Enter Description: ");
            
            Transaction transaction = depositService.processDeposit(accountNumber, amount, description, currentUserId);
            
            System.out.println("✅ Deposit successful!");
            System.out.println("Transaction ID: " + transaction.getTransactionId());
            System.out.println("Amount: ₹" + amount);
            System.out.println("Account: " + accountNumber);
            
        } catch (Exception e) {
            System.out.println("❌ Error processing deposit: " + e.getMessage());
        }
    }
    
    private void withdrawMoney() {
        System.out.println("\n💸 Withdraw Money");
        System.out.println("=================");
        
        try {
            String accountNumber = getStringInput("Enter Account Number: ");
            BigDecimal amount = getBigDecimalInput("Enter Amount (₹): ");
            String description = getStringInput("Enter Description: ");
            
            Transaction transaction = withdrawService.processWithdrawal(accountNumber, amount, description, currentUserId);
            
            System.out.println("✅ Withdrawal successful!");
            System.out.println("Transaction ID: " + transaction.getTransactionId());
            System.out.println("Amount: ₹" + amount);
            System.out.println("Account: " + accountNumber);
            
        } catch (Exception e) {
            System.out.println("❌ Error processing withdrawal: " + e.getMessage());
        }
    }
    
    private void transferMoney() {
        System.out.println("\n🔄 Transfer Money");
        System.out.println("=================");
        
        try {
            String sourceAccount = getStringInput("Enter Source Account Number: ");
            String destinationAccount = getStringInput("Enter Destination Account Number: ");
            BigDecimal amount = getBigDecimalInput("Enter Amount (₹): ");
            String description = getStringInput("Enter Description: ");
            
            // Confirmation prompt
            System.out.println("\nTransfer Details:");
            System.out.println("From: " + sourceAccount);
            System.out.println("To: " + destinationAccount);
            System.out.println("Amount: ₹" + amount);
            System.out.println("Description: " + description);
            
            String confirm = getStringInput("\nConfirm transfer? (yes/no): ");
            if (!confirm.equalsIgnoreCase("yes")) {
                System.out.println("❌ Transfer cancelled.");
                return;
            }
            
            Transaction transaction = transferService.processTransfer(sourceAccount, destinationAccount, amount, description, currentUserId);
            
            System.out.println("✅ Transfer successful!");
            System.out.println("Transaction ID: " + transaction.getTransactionId());
            System.out.println("Amount: ₹" + amount);
            System.out.println("From: " + sourceAccount);
            System.out.println("To: " + destinationAccount);
            
        } catch (Exception e) {
            System.out.println("❌ Error processing transfer: " + e.getMessage());
        }
    }
    
    private void viewAccountBalance() {
        System.out.println("\n💳 Account Balance");
        System.out.println("==================");
        
        try {
            String accountNumber = getStringInput("Enter Account Number: ");
            Account account = accountRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new RuntimeException("Account not found"));
            
            System.out.println("Account Details:");
            System.out.println("Account Number: " + account.getAccountNumber());
            System.out.println("Account Type: " + account.getAccountType().getDisplayName());
            System.out.println("Customer: " + account.getCustomer().getFullName());
            System.out.println("Balance: ₹" + account.getBalance());
            System.out.println("Status: " + account.getStatus().getDisplayName());
            System.out.println("Last Transaction: " + 
                    (account.getLastTransactionDate() != null ? 
                            account.getLastTransactionDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) : 
                            "No transactions"));
            
        } catch (Exception e) {
            System.out.println("❌ Error retrieving account balance: " + e.getMessage());
        }
    }
    
    private void viewTransactionHistory() {
        System.out.println("\n📊 Transaction History");
        System.out.println("======================");
        
        try {
            String accountNumber = getStringInput("Enter Account Number: ");
            List<Transaction> transactions = transferService.getTransferHistory(accountNumber);
            
            if (transactions.isEmpty()) {
                System.out.println("No transactions found for this account.");
                return;
            }
            
            System.out.println("Transaction History for Account: " + accountNumber);
            System.out.println("================================================");
            System.out.printf("%-15s %-12s %-15s %-20s %-10s %-15s%n", 
                    "Transaction ID", "Type", "Amount", "Description", "Status", "Date");
            System.out.println("================================================");
            
            for (Transaction tx : transactions) {
                System.out.printf("%-15s %-12s ₹%-14s %-20s %-10s %-15s%n",
                        tx.getTransactionId(),
                        tx.getType().getDisplayName(),
                        tx.getAmount(),
                        tx.getDescription().length() > 20 ? tx.getDescription().substring(0, 17) + "..." : tx.getDescription(),
                        tx.getStatus().getDisplayName(),
                        tx.getTransactionDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error retrieving transaction history: " + e.getMessage());
        }
    }
    
    private void undoTransaction() {
        System.out.println("\n↩️ Undo Last Transaction");
        System.out.println("========================");
        
        try {
            String accountId = getStringInput("Enter Account ID: ");
            
            if (!undoRedoStack.canUndo(accountId)) {
                System.out.println("❌ No transactions available to undo for this account.");
                return;
            }
            
            Transaction transaction = undoRedoStack.popUndo(accountId);
            System.out.println("✅ Transaction undone successfully!");
            System.out.println("Undone Transaction ID: " + transaction.getTransactionId());
            System.out.println("Type: " + transaction.getType().getDisplayName());
            System.out.println("Amount: ₹" + transaction.getAmount());
            
            auditService.logSuccess(currentUserId, "UNDO_TRANSACTION", "TRANSACTION", transaction.getId(), 
                    "Undone transaction: " + transaction.getTransactionId());
            
        } catch (Exception e) {
            System.out.println("❌ Error undoing transaction: " + e.getMessage());
        }
    }
    
    private void redoTransaction() {
        System.out.println("\n↪️ Redo Transaction");
        System.out.println("==================");
        
        try {
            String accountId = getStringInput("Enter Account ID: ");
            
            if (!undoRedoStack.canRedo(accountId)) {
                System.out.println("❌ No transactions available to redo for this account.");
                return;
            }
            
            Transaction transaction = undoRedoStack.popRedo(accountId);
            System.out.println("✅ Transaction redone successfully!");
            System.out.println("Redone Transaction ID: " + transaction.getTransactionId());
            System.out.println("Type: " + transaction.getType().getDisplayName());
            System.out.println("Amount: ₹" + transaction.getAmount());
            
            auditService.logSuccess(currentUserId, "REDO_TRANSACTION", "TRANSACTION", transaction.getId(), 
                    "Redone transaction: " + transaction.getTransactionId());
            
        } catch (Exception e) {
            System.out.println("❌ Error redoing transaction: " + e.getMessage());
        }
    }
    
    private void viewAuditLogs() {
        System.out.println("\n📋 Audit Logs");
        System.out.println("==============");
        
        try {
            List<com.bankingsystem.model.AuditLog> logs = auditService.getAllAuditLogs();
            
            if (logs.isEmpty()) {
                System.out.println("No audit logs found.");
                return;
            }
            
            System.out.println("Recent Audit Logs:");
            System.out.println("================================================");
            System.out.printf("%-20s %-15s %-20s %-15s %-10s%n", 
                    "Timestamp", "User ID", "Action", "Entity Type", "Result");
            System.out.println("================================================");
            
            // Show last 20 logs
            logs.stream()
                    .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
                    .limit(20)
                    .forEach(log -> {
                        System.out.printf("%-20s %-15s %-20s %-15s %-10s%n",
                                log.getTimestamp().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                                log.getUserId(),
                                log.getAction(),
                                log.getEntityType(),
                                log.getResult());
                    });
            
        } catch (Exception e) {
            System.out.println("❌ Error retrieving audit logs: " + e.getMessage());
        }
    }
    
    private void viewAllCustomers() {
        System.out.println("\n👥 All Customers");
        System.out.println("================");
        
        try {
            List<Customer> customers = customerRepository.findAll();
            
            if (customers.isEmpty()) {
                System.out.println("No customers found.");
                return;
            }
            
            System.out.printf("%-20s %-25s %-15s %-20s%n", "Customer ID", "Name", "Mobile", "Email");
            System.out.println("========================================================================");
            
            for (Customer customer : customers) {
                System.out.printf("%-20s %-25s %-15s %-20s%n",
                        customer.getId(),
                        customer.getFullName(),
                        customer.getMobileNumber(),
                        customer.getEmail());
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error retrieving customers: " + e.getMessage());
        }
    }
    
    private void viewAllAccounts() {
        System.out.println("\n🏦 All Accounts");
        System.out.println("===============");
        
        try {
            List<Account> accounts = accountRepository.findAll();
            
            if (accounts.isEmpty()) {
                System.out.println("No accounts found.");
                return;
            }
            
            System.out.printf("%-20s %-15s %-25s %-15s %-15s%n", "Account Number", "Type", "Customer", "Balance", "Status");
            System.out.println("========================================================================================");
            
            for (Account account : accounts) {
                System.out.printf("%-20s %-15s %-25s ₹%-14s %-15s%n",
                        account.getAccountNumber(),
                        account.getAccountType().getDisplayName(),
                        account.getCustomer().getFullName(),
                        account.getBalance(),
                        account.getStatus().getDisplayName());
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error retrieving accounts: " + e.getMessage());
        }
    }
    
    private void initializeSampleData() {
        try {
            // Create sample customers with Indian names
            if (customerRepository.count() == 0) {
                Customer customer1 = new Customer("Rajesh", "Kumar", "rajesh.kumar@email.com", 
                        "9876543210", "123 MG Road", "Mumbai", "Maharashtra", "400001");
                customer1.setPanNumber("ABCDE1234F");
                customer1.setAadharNumber("123456789012");
                customerRepository.save(customer1);
                
                Customer customer2 = new Customer("Priya", "Sharma", "priya.sharma@email.com", 
                        "9876543211", "456 Brigade Road", "Bangalore", "Karnataka", "560001");
                customer2.setPanNumber("FGHIJ5678K");
                customer2.setAadharNumber("123456789013");
                customerRepository.save(customer2);
                
                Customer customer3 = new Customer("Amit", "Patel", "amit.patel@email.com", 
                        "9876543212", "789 Park Street", "Kolkata", "West Bengal", "700016");
                customer3.setPanNumber("KLMNO9012P");
                customer3.setAadharNumber("123456789014");
                customerRepository.save(customer3);
                
                // Create sample accounts
                Account account1 = new Account("ACC" + System.currentTimeMillis() + "001", customer1, Account.AccountType.SAVINGS);
                account1.deposit(new BigDecimal("50000"));
                accountRepository.save(account1);
                
                Account account2 = new Account("ACC" + System.currentTimeMillis() + "002", customer2, Account.AccountType.CURRENT);
                account2.deposit(new BigDecimal("100000"));
                accountRepository.save(account2);
                
                Account account3 = new Account("ACC" + System.currentTimeMillis() + "003", customer3, Account.AccountType.SAVINGS);
                account3.deposit(new BigDecimal("75000"));
                accountRepository.save(account3);
                
                System.out.println("✅ Sample data initialized successfully!");
                System.out.println("Sample customers and accounts created with Indian names and INR currency.");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Warning: Could not initialize sample data: " + e.getMessage());
        }
    }
    
    // Helper methods
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input. Please enter a number.");
            }
        }
    }
    
    private BigDecimal getBigDecimalInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return new BigDecimal(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input. Please enter a valid amount.");
            }
        }
    }
}
