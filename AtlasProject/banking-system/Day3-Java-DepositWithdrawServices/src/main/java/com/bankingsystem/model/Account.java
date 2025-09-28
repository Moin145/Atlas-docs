package com.bankingsystem.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.index.Indexed;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

@Document(collection = "accounts")
public class Account {
    @Id
    private String id;
    
    @NotBlank(message = "Account number is required")
    @Indexed(unique = true)
    private String accountNumber;
    
    @DocumentReference
    @NotNull(message = "Customer is required")
    private Customer customer;
    
    @NotNull(message = "Account type is required")
    private AccountType accountType;
    
    @DecimalMin(value = "0.0", message = "Balance cannot be negative")
    private BigDecimal balance;
    
    private Currency currency;
    private AccountStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastTransactionDate;
    
    // Constructors
    public Account() {
        this.balance = BigDecimal.ZERO;
        this.currency = Currency.getInstance("INR");
        this.status = AccountStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Account(String accountNumber, Customer customer, AccountType accountType) {
        this();
        this.accountNumber = accountNumber;
        this.customer = customer;
        this.accountType = accountType;
    }
    
    // Enums
    public enum AccountType {
        SAVINGS("Savings Account"),
        CURRENT("Current Account"),
        FIXED_DEPOSIT("Fixed Deposit"),
        RECURRING_DEPOSIT("Recurring Deposit");
        
        private final String displayName;
        
        AccountType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    public enum AccountStatus {
        ACTIVE("Active"),
        INACTIVE("Inactive"),
        SUSPENDED("Suspended"),
        CLOSED("Closed");
        
        private final String displayName;
        
        AccountStatus(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public AccountType getAccountType() {
        return accountType;
    }
    
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
    
    public BigDecimal getBalance() {
        return balance;
    }
    
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    
    public Currency getCurrency() {
        return currency;
    }
    
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
    
    public AccountStatus getStatus() {
        return status;
    }
    
    public void setStatus(AccountStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public LocalDateTime getLastTransactionDate() {
        return lastTransactionDate;
    }
    
    public void setLastTransactionDate(LocalDateTime lastTransactionDate) {
        this.lastTransactionDate = lastTransactionDate;
    }
    
    // Business methods
    public boolean canWithdraw(BigDecimal amount) {
        return status == AccountStatus.ACTIVE && 
               balance.compareTo(amount) >= 0 && 
               amount.compareTo(BigDecimal.ZERO) > 0;
    }
    
    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            this.balance = this.balance.add(amount);
            this.lastTransactionDate = LocalDateTime.now();
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    public void withdraw(BigDecimal amount) {
        if (canWithdraw(amount)) {
            this.balance = this.balance.subtract(amount);
            this.lastTransactionDate = LocalDateTime.now();
            this.updatedAt = LocalDateTime.now();
        }
    }
    
    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", customer=" + (customer != null ? customer.getFullName() : "null") +
                ", accountType=" + accountType +
                ", balance=" + balance +
                ", currency=" + currency +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}
