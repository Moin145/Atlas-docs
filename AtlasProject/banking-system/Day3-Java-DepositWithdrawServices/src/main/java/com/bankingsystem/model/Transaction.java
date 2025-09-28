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

@Document(collection = "transactions")
public class Transaction {
    @Id
    private String id;
    
    @NotBlank(message = "Transaction ID is required")
    @Indexed(unique = true)
    private String transactionId;
    
    @NotNull(message = "Transaction type is required")
    private TransactionType type;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
    
    private Currency currency;
    
    @DocumentReference
    private Account sourceAccount;
    
    @DocumentReference
    private Account destinationAccount;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    private TransactionStatus status;
    private String referenceNumber;
    private String remarks;
    private LocalDateTime transactionDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public Transaction() {
        this.currency = Currency.getInstance("INR");
        this.status = TransactionStatus.PENDING;
        this.transactionDate = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Transaction(String transactionId, TransactionType type, BigDecimal amount, 
                     Account sourceAccount, String description) {
        this();
        this.transactionId = transactionId;
        this.type = type;
        this.amount = amount;
        this.sourceAccount = sourceAccount;
        this.description = description;
    }
    
    public Transaction(String transactionId, TransactionType type, BigDecimal amount, 
                     Account sourceAccount, Account destinationAccount, String description) {
        this(transactionId, type, amount, sourceAccount, description);
        this.destinationAccount = destinationAccount;
    }
    
    // Enums
    public enum TransactionType {
        DEPOSIT("Deposit"),
        WITHDRAWAL("Withdrawal"),
        TRANSFER("Transfer"),
        INTEREST("Interest Credit"),
        FEE("Fee Deduction"),
        REFUND("Refund");
        
        private final String displayName;
        
        TransactionType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    public enum TransactionStatus {
        PENDING("Pending"),
        COMPLETED("Completed"),
        FAILED("Failed"),
        CANCELLED("Cancelled"),
        REVERSED("Reversed");
        
        private final String displayName;
        
        TransactionStatus(String displayName) {
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
    
    public String getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    public TransactionType getType() {
        return type;
    }
    
    public void setType(TransactionType type) {
        this.type = type;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public Currency getCurrency() {
        return currency;
    }
    
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
    
    public Account getSourceAccount() {
        return sourceAccount;
    }
    
    public void setSourceAccount(Account sourceAccount) {
        this.sourceAccount = sourceAccount;
    }
    
    public Account getDestinationAccount() {
        return destinationAccount;
    }
    
    public void setDestinationAccount(Account destinationAccount) {
        this.destinationAccount = destinationAccount;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public TransactionStatus getStatus() {
        return status;
    }
    
    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
    
    public String getReferenceNumber() {
        return referenceNumber;
    }
    
    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }
    
    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
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
    
    // Business methods
    public boolean isTransfer() {
        return type == TransactionType.TRANSFER && destinationAccount != null;
    }
    
    public boolean isReversible() {
        return status == TransactionStatus.COMPLETED && 
               (type == TransactionType.DEPOSIT || type == TransactionType.WITHDRAWAL || type == TransactionType.TRANSFER);
    }
    
    public void markAsCompleted() {
        this.status = TransactionStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void markAsFailed(String reason) {
        this.status = TransactionStatus.FAILED;
        this.remarks = reason;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void markAsCancelled(String reason) {
        this.status = TransactionStatus.CANCELLED;
        this.remarks = reason;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void markAsReversed(String reason) {
        this.status = TransactionStatus.REVERSED;
        this.remarks = reason;
        this.updatedAt = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", type=" + type +
                ", amount=" + amount +
                ", currency=" + currency +
                ", sourceAccount=" + (sourceAccount != null ? sourceAccount.getAccountNumber() : "null") +
                ", destinationAccount=" + (destinationAccount != null ? destinationAccount.getAccountNumber() : "null") +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", transactionDate=" + transactionDate +
                '}';
    }
}
