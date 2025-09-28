package com.bankingsystem.service;

import com.bankingsystem.model.Account;
import com.bankingsystem.model.Transaction;
import com.bankingsystem.repository.AccountRepository;
import com.bankingsystem.repository.TransactionRepository;
import com.bankingsystem.util.UndoRedoStack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class TransferService {
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private AuditService auditService;
    
    @Autowired
    private UndoRedoStack undoRedoStack;
    
    /**
     * Process a transfer transaction between two accounts
     */
    @Transactional
    public Transaction processTransfer(String sourceAccountNumber, String destinationAccountNumber, 
                                    BigDecimal amount, String description, String userId) {
        try {
            // Find source account
            Account sourceAccount = accountRepository.findByAccountNumber(sourceAccountNumber)
                    .orElseThrow(() -> new RuntimeException("Source account not found: " + sourceAccountNumber));
            
            // Find destination account
            Account destinationAccount = accountRepository.findByAccountNumber(destinationAccountNumber)
                    .orElseThrow(() -> new RuntimeException("Destination account not found: " + destinationAccountNumber));
            
            // Validate account statuses
            if (sourceAccount.getStatus() != Account.AccountStatus.ACTIVE) {
                throw new RuntimeException("Source account is not active: " + sourceAccountNumber);
            }
            
            if (destinationAccount.getStatus() != Account.AccountStatus.ACTIVE) {
                throw new RuntimeException("Destination account is not active: " + destinationAccountNumber);
            }
            
            // Validate amount
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("Transfer amount must be greater than zero");
            }
            
            // Check if source account has sufficient balance
            if (!sourceAccount.canWithdraw(amount)) {
                throw new RuntimeException("Insufficient balance in source account. Available: ₹" + 
                        sourceAccount.getBalance() + ", Requested: ₹" + amount);
            }
            
            // Prevent transfer to same account
            if (sourceAccountNumber.equals(destinationAccountNumber)) {
                throw new RuntimeException("Cannot transfer to the same account");
            }
            
            // Create transaction
            String transactionId = "TRF" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8);
            Transaction transaction = new Transaction(transactionId, Transaction.TransactionType.TRANSFER, 
                    amount, sourceAccount, destinationAccount, description);
            transaction.setReferenceNumber("REF" + System.currentTimeMillis());
            
            // Process the transfer
            sourceAccount.withdraw(amount);
            destinationAccount.deposit(amount);
            transaction.markAsCompleted();
            
            // Save to database
            accountRepository.save(sourceAccount);
            accountRepository.save(destinationAccount);
            transactionRepository.save(transaction);
            
            // Add to undo stack for both accounts
            undoRedoStack.pushUndo(sourceAccount.getId(), transaction);
            undoRedoStack.pushUndo(destinationAccount.getId(), transaction);
            
            // Log the action
            Map<String, Object> details = new HashMap<>();
            details.put("amount", amount);
            details.put("sourceAccountNumber", sourceAccountNumber);
            details.put("destinationAccountNumber", destinationAccountNumber);
            details.put("transactionId", transactionId);
            details.put("sourceNewBalance", sourceAccount.getBalance());
            details.put("destinationNewBalance", destinationAccount.getBalance());
            
            auditService.logAction(userId, "TRANSFER", "ACCOUNT", sourceAccount.getId(), 
                    "Transfer of ₹" + amount + " from " + sourceAccountNumber + " to " + destinationAccountNumber, 
                    details, null, null);
            
            return transaction;
            
        } catch (Exception e) {
            // Log the failure
            auditService.logFailure(userId, "TRANSFER", "ACCOUNT", sourceAccountNumber, 
                    "Failed to transfer ₹" + amount + " from " + sourceAccountNumber + " to " + destinationAccountNumber, 
                    e.getMessage());
            throw e;
        }
    }
    
    /**
     * Get transfer history for an account
     */
    public java.util.List<Transaction> getTransferHistory(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountNumber));
        
        return transactionRepository.findBySourceAccountOrDestinationAccount(account, account)
                .stream()
                .filter(tx -> tx.getType() == Transaction.TransactionType.TRANSFER)
                .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Get transfer history for an account within date range
     */
    public java.util.List<Transaction> getTransferHistory(String accountNumber, LocalDateTime startDate, LocalDateTime endDate) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountNumber));
        
        return transactionRepository.findAccountTransactionsByDateRange(account, startDate, endDate)
                .stream()
                .filter(tx -> tx.getType() == Transaction.TransactionType.TRANSFER)
                .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Get outgoing transfers for an account
     */
    public java.util.List<Transaction> getOutgoingTransfers(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountNumber));
        
        return transactionRepository.findBySourceAccount(account)
                .stream()
                .filter(tx -> tx.getType() == Transaction.TransactionType.TRANSFER)
                .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Get incoming transfers for an account
     */
    public java.util.List<Transaction> getIncomingTransfers(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountNumber));
        
        return transactionRepository.findByDestinationAccount(account)
                .stream()
                .filter(tx -> tx.getType() == Transaction.TransactionType.TRANSFER)
                .collect(java.util.stream.Collectors.toList());
    }
}
