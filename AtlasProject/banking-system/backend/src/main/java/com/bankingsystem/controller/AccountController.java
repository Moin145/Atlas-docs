package com.bankingsystem.controller;

import com.bankingsystem.model.Account;
import com.bankingsystem.model.Customer;
import com.bankingsystem.repository.AccountRepository;
import com.bankingsystem.repository.CustomerRepository;
import com.bankingsystem.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "*")
public class AccountController {
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private AuditService auditService;
    
    /**
     * List all accounts
     */
    @GetMapping
    public ResponseEntity<?> listAccounts() {
        try {
            List<Account> accounts = accountRepository.findAll();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "accounts", accounts
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * Create a new account
     */
    @PostMapping
    public ResponseEntity<?> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        try {
            // Find customer
            Customer customer = customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            
            // Generate account number
            String accountNumber = "ACC" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 6);
            
            // Create account
            Account account = new Account(accountNumber, customer, request.getAccountType());
            account.setStatus(Account.AccountStatus.ACTIVE);
            
            // Save account
            accountRepository.save(account);
            
            // Log the action
            auditService.logSuccess(request.getUserId(), "CREATE_ACCOUNT", "ACCOUNT", account.getId(), 
                    "Created new " + request.getAccountType().getDisplayName() + " for customer " + customer.getFullName());
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Account created successfully",
                    "account", account
            ));
            
        } catch (Exception e) {
            auditService.logFailure(request.getUserId(), "CREATE_ACCOUNT", "CUSTOMER", request.getCustomerId(), 
                    "Failed to create account", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    /**
     * Get account by account number
     */
    @GetMapping("/{accountNumber}")
    public ResponseEntity<?> getAccount(@PathVariable String accountNumber) {
        try {
            Account account = accountRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new RuntimeException("Account not found"));
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "account", account
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    /**
     * Get all accounts for a customer
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getAccountsByCustomer(@PathVariable String customerId) {
        try {
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            
            List<Account> accounts = accountRepository.findByCustomer(customer);
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "accounts", accounts
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    /**
     * Get account balance
     */
    @GetMapping("/{accountNumber}/balance")
    public ResponseEntity<?> getAccountBalance(@PathVariable String accountNumber) {
        try {
            Account account = accountRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new RuntimeException("Account not found"));
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "accountNumber", accountNumber,
                    "balance", account.getBalance(),
                    "currency", account.getCurrency().getCurrencyCode()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    /**
     * Update account status
     */
    @PutMapping("/{accountNumber}/status")
    public ResponseEntity<?> updateAccountStatus(@PathVariable String accountNumber, 
                                               @RequestBody UpdateStatusRequest request) {
        try {
            Account account = accountRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new RuntimeException("Account not found"));
            
            Account.AccountStatus oldStatus = account.getStatus();
            account.setStatus(request.getStatus());
            account.setUpdatedAt(LocalDateTime.now());
            
            accountRepository.save(account);
            
            // Log the action
            auditService.logSuccess(request.getUserId(), "UPDATE_ACCOUNT_STATUS", "ACCOUNT", account.getId(), 
                    "Changed account status from " + oldStatus + " to " + request.getStatus());
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Account status updated successfully",
                    "account", account
            ));
            
        } catch (Exception e) {
            auditService.logFailure(request.getUserId(), "UPDATE_ACCOUNT_STATUS", "ACCOUNT", accountNumber, 
                    "Failed to update account status", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    // Inner classes for request/response
    public static class CreateAccountRequest {
        private String customerId;
        private Account.AccountType accountType;
        private String userId;
        
        // Getters and setters
        public String getCustomerId() { return customerId; }
        public void setCustomerId(String customerId) { this.customerId = customerId; }
        public Account.AccountType getAccountType() { return accountType; }
        public void setAccountType(Account.AccountType accountType) { this.accountType = accountType; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
    }
    
    public static class UpdateStatusRequest {
        private Account.AccountStatus status;
        private String userId;
        
        // Getters and setters
        public Account.AccountStatus getStatus() { return status; }
        public void setStatus(Account.AccountStatus status) { this.status = status; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
    }
}
