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
public class DepositService {
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private AuditService auditService;
    
    @Autowired
    private UndoRedoStack undoRedoStack;
    
    /**
     * Process a deposit transaction
     */
    @Transactional
    public Transaction processDeposit(String accountNumber, BigDecimal amount, String description, String userId) {
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
                throw new RuntimeException("Deposit amount must be greater than zero");
            }
            
            // Create transaction
            String transactionId = "DEP" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8);
            Transaction transaction = new Transaction(transactionId, Transaction.TransactionType.DEPOSIT, 
                    amount, account, description);
            transaction.setReferenceNumber("REF" + System.currentTimeMillis());
            
            // Process the deposit
            account.deposit(amount);
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
            
            auditService.logAction(userId, "DEPOSIT", "ACCOUNT", account.getId(), 
                    "Deposit of ₹" + amount + " to account " + accountNumber, details, null, null);
            
            return transaction;
            
        } catch (Exception e) {
            // Log the failure
            auditService.logFailure(userId, "DEPOSIT", "ACCOUNT", accountNumber, 
                    "Failed to deposit ₹" + amount + " to account " + accountNumber, e.getMessage());
            throw e;
        }
    }
    
    /**
     * Get deposit history for an account
     */
    public java.util.List<Transaction> getDepositHistory(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountNumber));
        
        return transactionRepository.findBySourceAccount(account)
                .stream()
                .filter(tx -> tx.getType() == Transaction.TransactionType.DEPOSIT)
                .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Get deposit history for an account within date range
     */
    public java.util.List<Transaction> getDepositHistory(String accountNumber, LocalDateTime startDate, LocalDateTime endDate) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountNumber));
        
        return transactionRepository.findSourceTransactionsByDateRange(account, startDate, endDate)
                .stream()
                .filter(tx -> tx.getType() == Transaction.TransactionType.DEPOSIT)
                .collect(java.util.stream.Collectors.toList());
    }
}
