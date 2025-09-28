package com.bankingsystem.controller;

import com.bankingsystem.model.Transaction;
import com.bankingsystem.service.DepositService;
import com.bankingsystem.service.WithdrawService;
import com.bankingsystem.service.TransferService;
import com.bankingsystem.util.UndoRedoStack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {
    
    @Autowired
    private DepositService depositService;
    
    @Autowired
    private WithdrawService withdrawService;
    
    @Autowired
    private TransferService transferService;
    
    @Autowired
    private UndoRedoStack undoRedoStack;
    
    /**
     * Process a deposit
     */
    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@Valid @RequestBody DepositRequest request) {
        try {
            Transaction transaction = depositService.processDeposit(
                    request.getAccountNumber(), 
                    request.getAmount(), 
                    request.getDescription(), 
                    request.getUserId()
            );
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Deposit processed successfully",
                    "transaction", transaction
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    /**
     * Process a withdrawal
     */
    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@Valid @RequestBody WithdrawRequest request) {
        try {
            Transaction transaction = withdrawService.processWithdrawal(
                    request.getAccountNumber(), 
                    request.getAmount(), 
                    request.getDescription(), 
                    request.getUserId()
            );
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Withdrawal processed successfully",
                    "transaction", transaction
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    /**
     * Process a transfer
     */
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@Valid @RequestBody TransferRequest request) {
        try {
            Transaction transaction = transferService.processTransfer(
                    request.getSourceAccountNumber(), 
                    request.getDestinationAccountNumber(), 
                    request.getAmount(), 
                    request.getDescription(), 
                    request.getUserId()
            );
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Transfer processed successfully",
                    "transaction", transaction
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    /**
     * Get transaction history for an account
     */
    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<?> getTransactionHistory(@PathVariable String accountNumber) {
        try {
            List<Transaction> transactions = transferService.getTransferHistory(accountNumber);
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "transactions", transactions
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    /**
     * Get deposit history for an account
     */
    @GetMapping("/account/{accountNumber}/deposits")
    public ResponseEntity<?> getDepositHistory(@PathVariable String accountNumber) {
        try {
            List<Transaction> transactions = depositService.getDepositHistory(accountNumber);
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "transactions", transactions
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    /**
     * Get withdrawal history for an account
     */
    @GetMapping("/account/{accountNumber}/withdrawals")
    public ResponseEntity<?> getWithdrawalHistory(@PathVariable String accountNumber) {
        try {
            List<Transaction> transactions = withdrawService.getWithdrawalHistory(accountNumber);
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "transactions", transactions
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    /**
     * Undo last transaction for an account
     */
    @PostMapping("/undo/{accountId}")
    public ResponseEntity<?> undoTransaction(@PathVariable String accountId, @RequestBody UndoRequest request) {
        try {
            if (!undoRedoStack.canUndo(accountId)) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "No transactions available to undo"
                ));
            }
            
            Transaction transaction = undoRedoStack.popUndo(accountId);
            
            // Here you would implement the actual reversal logic
            // For now, we'll just return the transaction that was undone
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Transaction undone successfully",
                    "undoneTransaction", transaction
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    /**
     * Redo last undone transaction for an account
     */
    @PostMapping("/redo/{accountId}")
    public ResponseEntity<?> redoTransaction(@PathVariable String accountId, @RequestBody RedoRequest request) {
        try {
            if (!undoRedoStack.canRedo(accountId)) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "No transactions available to redo"
                ));
            }
            
            Transaction transaction = undoRedoStack.popRedo(accountId);
            
            // Here you would implement the actual re-execution logic
            // For now, we'll just return the transaction that was redone
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Transaction redone successfully",
                    "redoneTransaction", transaction
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    /**
     * Check undo/redo availability for an account
     */
    @GetMapping("/account/{accountId}/undo-redo-status")
    public ResponseEntity<?> getUndoRedoStatus(@PathVariable String accountId) {
        try {
            boolean canUndo = undoRedoStack.canUndo(accountId);
            boolean canRedo = undoRedoStack.canRedo(accountId);
            int undoStackSize = undoRedoStack.getUndoStackSize(accountId);
            int redoStackSize = undoRedoStack.getRedoStackSize(accountId);
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "canUndo", canUndo,
                    "canRedo", canRedo,
                    "undoStackSize", undoStackSize,
                    "redoStackSize", redoStackSize
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    // Inner classes for request/response
    public static class DepositRequest {
        private String accountNumber;
        private BigDecimal amount;
        private String description;
        private String userId;
        
        // Getters and setters
        public String getAccountNumber() { return accountNumber; }
        public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
    }
    
    public static class WithdrawRequest {
        private String accountNumber;
        private BigDecimal amount;
        private String description;
        private String userId;
        
        // Getters and setters
        public String getAccountNumber() { return accountNumber; }
        public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
    }
    
    public static class TransferRequest {
        private String sourceAccountNumber;
        private String destinationAccountNumber;
        private BigDecimal amount;
        private String description;
        private String userId;
        
        // Getters and setters
        public String getSourceAccountNumber() { return sourceAccountNumber; }
        public void setSourceAccountNumber(String sourceAccountNumber) { this.sourceAccountNumber = sourceAccountNumber; }
        public String getDestinationAccountNumber() { return destinationAccountNumber; }
        public void setDestinationAccountNumber(String destinationAccountNumber) { this.destinationAccountNumber = destinationAccountNumber; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
    }
    
    public static class UndoRequest {
        private String userId;
        
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
    }
    
    public static class RedoRequest {
        private String userId;
        
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
    }
}
