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
public class WithdrawService {
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private AuditService auditService;
    
    @Autowired
    private UndoRedoStack undoRedoStack;
    
    /**
     * Process a withdrawal transaction
     */
    @Transactional
    public Transaction processWithdrawal(String accountNumber, BigDecimal amount, String description, String userId) {
        try {
            // Find the account
            Account account = accountRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new RuntimeException("Account not found: " + accountNumber));
            
            // Validate account status
            if (account.getStatus() != Account.AccountStatus.ACTIVE) {
                throw new RuntimeException("Account is not active: " + accountNumber);
            }
            
            // Validate amount
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("Withdrawal amount must be greater than zero");
            }
            
            // Check if account has sufficient balance
            if (!account.canWithdraw(amount)) {
                throw new RuntimeException("Insufficient balance. Available: ₹" + account.getBalance() + 
                        ", Requested: ₹" + amount);
            }
            
            // Create transaction
            String transactionId = "WTH" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8);
            Transaction transaction = new Transaction(transactionId, Transaction.TransactionType.WITHDRAWAL, 
                    amount, account, description);
            transaction.setReferenceNumber("REF" + System.currentTimeMillis());
            
            // Process the withdrawal
            account.withdraw(amount);
            transaction.markAsCompleted();
            
            // Save to database
            accountRepository.save(account);
            transactionRepository.save(transaction);
            
            // Add to undo stack
            undoRedoStack.pushUndo(account.getId(), transaction);
            
            // Log the action
            Map<String, Object> details = new HashMap<>();
            details.put("amount", amount);
            details.put("accountNumber", accountNumber);
            details.put("transactionId", transactionId);
            details.put("newBalance", account.getBalance());
            
            auditService.logAction(userId, "WITHDRAWAL", "ACCOUNT", account.getId(), 
                    "Withdrawal of ₹" + amount + " from account " + accountNumber, details, null, null);
            
            return transaction;
            
        } catch (Exception e) {
            // Log the failure
            auditService.logFailure(userId, "WITHDRAWAL", "ACCOUNT", accountNumber, 
                    "Failed to withdraw ₹" + amount + " from account " + accountNumber, e.getMessage());
            throw e;
        }
    }
    
    /**
     * Get withdrawal history for an account
     */
    public java.util.List<Transaction> getWithdrawalHistory(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountNumber));
        
        return transactionRepository.findBySourceAccount(account)
                .stream()
                .filter(tx -> tx.getType() == Transaction.TransactionType.WITHDRAWAL)
                .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Get withdrawal history for an account within date range
     */
    public java.util.List<Transaction> getWithdrawalHistory(String accountNumber, LocalDateTime startDate, LocalDateTime endDate) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountNumber));
        
        return transactionRepository.findSourceTransactionsByDateRange(account, startDate, endDate)
                .stream()
                .filter(tx -> tx.getType() == Transaction.TransactionType.WITHDRAWAL)
                .collect(java.util.stream.Collectors.toList());
    }
}
