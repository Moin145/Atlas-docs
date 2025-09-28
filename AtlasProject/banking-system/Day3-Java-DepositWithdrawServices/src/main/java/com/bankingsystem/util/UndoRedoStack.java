package com.bankingsystem.util;

import com.bankingsystem.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Component
public class UndoRedoStack {
    
    private final Map<String, Stack<Transaction>> undoStacks = new ConcurrentHashMap<>();
    private final Map<String, Stack<Transaction>> redoStacks = new ConcurrentHashMap<>();
    
    /**
     * Push a completed transaction to the undo stack for a specific account
     */
    public void pushUndo(String accountId, Transaction transaction) {
        undoStacks.computeIfAbsent(accountId, k -> new Stack<>()).push(transaction);
        // Clear redo stack when new transaction is added
        redoStacks.remove(accountId);
    }
    
    /**
     * Pop the last transaction from undo stack for reversal
     */
    public Transaction popUndo(String accountId) {
        Stack<Transaction> undoStack = undoStacks.get(accountId);
        if (undoStack != null && !undoStack.isEmpty()) {
            Transaction transaction = undoStack.pop();
            // Move to redo stack
            redoStacks.computeIfAbsent(accountId, k -> new Stack<>()).push(transaction);
            return transaction;
        }
        return null;
    }
    
    /**
     * Pop the last reversed transaction from redo stack for re-execution
     */
    public Transaction popRedo(String accountId) {
        Stack<Transaction> redoStack = redoStacks.get(accountId);
        if (redoStack != null && !redoStack.isEmpty()) {
            Transaction transaction = redoStack.pop();
            // Move back to undo stack
            undoStacks.computeIfAbsent(accountId, k -> new Stack<>()).push(transaction);
            return transaction;
        }
        return null;
    }
    
    /**
     * Check if undo is available for an account
     */
    public boolean canUndo(String accountId) {
        Stack<Transaction> undoStack = undoStacks.get(accountId);
        return undoStack != null && !undoStack.isEmpty();
    }
    
    /**
     * Check if redo is available for an account
     */
    public boolean canRedo(String accountId) {
        Stack<Transaction> redoStack = redoStacks.get(accountId);
        return redoStack != null && !redoStack.isEmpty();
    }
    
    /**
     * Get the size of undo stack for an account
     */
    public int getUndoStackSize(String accountId) {
        Stack<Transaction> undoStack = undoStacks.get(accountId);
        return undoStack != null ? undoStack.size() : 0;
    }
    
    /**
     * Get the size of redo stack for an account
     */
    public int getRedoStackSize(String accountId) {
        Stack<Transaction> redoStack = redoStacks.get(accountId);
        return redoStack != null ? redoStack.size() : 0;
    }
    
    /**
     * Clear all stacks for an account
     */
    public void clearStacks(String accountId) {
        undoStacks.remove(accountId);
        redoStacks.remove(accountId);
    }
    
    /**
     * Clear all stacks for all accounts
     */
    public void clearAllStacks() {
        undoStacks.clear();
        redoStacks.clear();
    }
    
    /**
     * Get the last transaction that can be undone for an account
     */
    public Transaction peekUndo(String accountId) {
        Stack<Transaction> undoStack = undoStacks.get(accountId);
        return (undoStack != null && !undoStack.isEmpty()) ? undoStack.peek() : null;
    }
    
    /**
     * Get the last transaction that can be redone for an account
     */
    public Transaction peekRedo(String accountId) {
        Stack<Transaction> redoStack = redoStacks.get(accountId);
        return (redoStack != null && !redoStack.isEmpty()) ? redoStack.peek() : null;
    }
}
