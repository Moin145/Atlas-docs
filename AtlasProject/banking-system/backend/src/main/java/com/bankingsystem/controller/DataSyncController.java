package com.bankingsystem.controller;

import com.bankingsystem.service.DataSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/sync")
@CrossOrigin(origins = "*")
public class DataSyncController {
    
    @Autowired
    private DataSyncService dataSyncService;
    
    /**
     * Sync all MongoDB data to DynamoDB
     */
    @PostMapping("/mongodb-to-dynamodb")
    public Map<String, Object> syncMongoDBToDynamoDB() {
        try {
            dataSyncService.syncAllDataToDynamoDB();
            
            return Map.of(
                "success", true,
                "message", "Data synced from MongoDB to DynamoDB successfully!",
                "timestamp", java.time.LocalDateTime.now()
            );
            
        } catch (Exception e) {
            return Map.of(
                "success", false,
                "message", "Error syncing data: " + e.getMessage(),
                "timestamp", java.time.LocalDateTime.now()
            );
        }
    }
    
    /**
     * Get sync status
     */
    @GetMapping("/status")
    public Map<String, Object> getSyncStatus() {
        return Map.of(
            "success", true,
            "message", "Data sync service is running",
            "mongodbCollections", new String[]{"customers", "accounts", "transactions", "audit_logs"},
            "dynamodbTables", new String[]{"BankingCustomers", "BankingAccounts", "BankingTransactions", "BankingAuditLogs"},
            "timestamp", java.time.LocalDateTime.now()
        );
    }
}
