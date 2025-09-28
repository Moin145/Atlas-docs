package com.bankingsystem;

import com.bankingsystem.model.Transaction;
import com.bankingsystem.util.SettlementQueue;
import com.bankingsystem.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SettlementRunner implements CommandLineRunner {
    
    @Autowired
    private SettlementQueue settlementQueue;
    
    @Autowired
    private AuditService auditService;
    
    @Override
    public void run(String... args) throws Exception {
        // Check if this is a settlement run
        if (args.length > 0 && args[0].contains("settlement")) {
            runSettlement();
        }
    }
    
    public void runSettlement() {
        System.out.println("🔄 Starting batch settlement process...");
        
        try {
            // Get all pending transactions
            List<Transaction> pendingTransactions = settlementQueue.getPendingTransactions();
            
            if (pendingTransactions.isEmpty()) {
                System.out.println("✅ No pending transactions to settle.");
                return;
            }
            
            System.out.println("📊 Found " + pendingTransactions.size() + " transactions to settle");
            
            int processedCount = 0;
            int failedCount = 0;
            
            // Process each transaction
            for (Transaction transaction : pendingTransactions) {
                try {
                    // Simulate settlement processing
                    processSettlementTransaction(transaction);
                    settlementQueue.markAsProcessed(transaction.getTransactionId());
                    processedCount++;
                    
                    System.out.println("✅ Settled transaction: " + transaction.getTransactionId());
                    
                } catch (Exception e) {
                    failedCount++;
                    System.err.println("❌ Failed to settle transaction " + transaction.getTransactionId() + ": " + e.getMessage());
                }
            }
            
            // Log settlement results
            auditService.logSuccess("SYSTEM", "BATCH_SETTLEMENT", "SETTLEMENT", "BATCH", 
                    "Settlement completed. Processed: " + processedCount + ", Failed: " + failedCount);
            
            System.out.println("🎉 Settlement completed!");
            System.out.println("✅ Processed: " + processedCount);
            System.out.println("❌ Failed: " + failedCount);
            
        } catch (Exception e) {
            System.err.println("💥 Settlement process failed: " + e.getMessage());
            auditService.logFailure("SYSTEM", "BATCH_SETTLEMENT", "SETTLEMENT", "BATCH", 
                    "Settlement process failed", e.getMessage());
            throw e;
        }
    }
    
    private void processSettlementTransaction(Transaction transaction) {
        // Simulate settlement processing
        // In a real implementation, this would:
        // 1. Validate the transaction
        // 2. Update account balances
        // 3. Generate settlement reports
        // 4. Send notifications
        // 5. Update transaction status
        
        // For now, just simulate processing time
        try {
            Thread.sleep(100); // Simulate processing time
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Settlement interrupted", e);
        }
        
        // Mark transaction as settled
        transaction.setStatus(Transaction.TransactionStatus.COMPLETED);
    }
}
