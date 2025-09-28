package com.bankingsystem.util;

import com.bankingsystem.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

@Component
public class SettlementQueue {
    
    private final BlockingQueue<Transaction> settlementQueue = new LinkedBlockingQueue<>();
    private final Map<String, Transaction> pendingTransactions = new ConcurrentHashMap<>();
    
    /**
     * Add a transaction to the settlement queue
     */
    public void enqueue(Transaction transaction) {
        try {
            settlementQueue.put(transaction);
            pendingTransactions.put(transaction.getTransactionId(), transaction);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to enqueue transaction", e);
        }
    }
    
    /**
     * Remove and return the next transaction from the settlement queue
     */
    public Transaction dequeue() {
        try {
            Transaction transaction = settlementQueue.take();
            pendingTransactions.remove(transaction.getTransactionId());
            return transaction;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }
    
    /**
     * Get the next transaction without removing it from the queue
     */
    public Transaction peek() {
        return settlementQueue.peek();
    }
    
    /**
     * Get the current size of the settlement queue
     */
    public int size() {
        return settlementQueue.size();
    }
    
    /**
     * Check if the settlement queue is empty
     */
    public boolean isEmpty() {
        return settlementQueue.isEmpty();
    }
    
    /**
     * Get all pending transactions as a list
     */
    public List<Transaction> getPendingTransactions() {
        return new ArrayList<>(pendingTransactions.values());
    }
    
    /**
     * Check if a specific transaction is pending
     */
    public boolean isPending(String transactionId) {
        return pendingTransactions.containsKey(transactionId);
    }
    
    /**
     * Remove a specific transaction from pending list (if it was processed)
     */
    public void markAsProcessed(String transactionId) {
        pendingTransactions.remove(transactionId);
    }
    
    /**
     * Clear all pending transactions
     */
    public void clear() {
        settlementQueue.clear();
        pendingTransactions.clear();
    }
    
    /**
     * Get the number of pending transactions
     */
    public int getPendingCount() {
        return pendingTransactions.size();
    }
    
    /**
     * Drain all transactions from the queue to a list
     */
    public List<Transaction> drainAll() {
        List<Transaction> transactions = new ArrayList<>();
        settlementQueue.drainTo(transactions);
        pendingTransactions.clear();
        return transactions;
    }
    
    /**
     * Drain up to maxElements transactions from the queue to a list
     */
    public List<Transaction> drainTo(int maxElements) {
        List<Transaction> transactions = new ArrayList<>();
        settlementQueue.drainTo(transactions, maxElements);
        // Remove drained transactions from pending map
        transactions.forEach(tx -> pendingTransactions.remove(tx.getTransactionId()));
        return transactions;
    }
}
