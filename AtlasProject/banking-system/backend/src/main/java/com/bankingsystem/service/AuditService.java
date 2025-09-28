package com.bankingsystem.service;

import com.bankingsystem.model.AuditLog;
import com.bankingsystem.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class AuditService {
    
    @Autowired
    private AuditLogRepository auditLogRepository;
    
    @Autowired
    private DynamoDbClient dynamoDbClient;
    
    private static final String DYNAMODB_TABLE_NAME = "BankingAuditLogs";
    
    /**
     * Log an action to both MongoDB and DynamoDB
     */
    public void logAction(String userId, String action, String entityType, String entityId, 
                         String description, Map<String, Object> details, String ipAddress, String userAgent) {
        
        String actionId = UUID.randomUUID().toString();
        AuditLog auditLog = new AuditLog(actionId, userId, action, entityType, entityId);
        auditLog.setDescription(description);
        auditLog.setDetails(details);
        auditLog.setIpAddress(ipAddress);
        auditLog.setUserAgent(userAgent);
        auditLog.markAsSuccess(description);
        
        // Save to MongoDB
        auditLogRepository.save(auditLog);
        
        // Save to DynamoDB asynchronously
        saveToDynamoDBAsync(auditLog);
    }
    
    /**
     * Log a successful action
     */
    public void logSuccess(String userId, String action, String entityType, String entityId, String description) {
        logAction(userId, action, entityType, entityId, description, null, null, null);
    }
    
    /**
     * Log a failed action
     */
    public void logFailure(String userId, String action, String entityType, String entityId, 
                          String description, String errorMessage) {
        String actionId = UUID.randomUUID().toString();
        AuditLog auditLog = new AuditLog(actionId, userId, action, entityType, entityId);
        auditLog.setDescription(description);
        auditLog.markAsFailure(errorMessage);
        
        // Save to MongoDB
        auditLogRepository.save(auditLog);
        
        // Save to DynamoDB asynchronously
        saveToDynamoDBAsync(auditLog);
    }
    
    /**
     * Get audit logs by user ID
     */
    public List<AuditLog> getAuditLogsByUser(String userId) {
        return auditLogRepository.findByUserId(userId);
    }
    
    /**
     * Get audit logs by date range
     */
    public List<AuditLog> getAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return auditLogRepository.findByTimestampBetween(startDate, endDate);
    }
    
    /**
     * Get audit logs by entity
     */
    public List<AuditLog> getAuditLogsByEntity(String entityType, String entityId) {
        return auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId);
    }
    
    /**
     * Get audit logs by action
     */
    public List<AuditLog> getAuditLogsByAction(String action) {
        return auditLogRepository.findByAction(action);
    }
    
    /**
     * Get all audit logs
     */
    public List<AuditLog> getAllAuditLogs() {
        return auditLogRepository.findAll();
    }
    
    /**
     * Save audit log to DynamoDB asynchronously
     */
    private void saveToDynamoDBAsync(AuditLog auditLog) {
        CompletableFuture.runAsync(() -> {
            try {
                Map<String, AttributeValue> item = new HashMap<>();
                item.put("ActionID", AttributeValue.builder().s(auditLog.getActionId()).build());
                item.put("Timestamp", AttributeValue.builder().s(auditLog.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).build());
                item.put("UserId", AttributeValue.builder().s(auditLog.getUserId()).build());
                item.put("Action", AttributeValue.builder().s(auditLog.getAction()).build());
                item.put("EntityType", AttributeValue.builder().s(auditLog.getEntityType()).build());
                item.put("EntityId", AttributeValue.builder().s(auditLog.getEntityId() != null ? auditLog.getEntityId() : "").build());
                item.put("Description", AttributeValue.builder().s(auditLog.getDescription() != null ? auditLog.getDescription() : "").build());
                item.put("Result", AttributeValue.builder().s(auditLog.getResult() != null ? auditLog.getResult() : "").build());
                item.put("ErrorMessage", AttributeValue.builder().s(auditLog.getErrorMessage() != null ? auditLog.getErrorMessage() : "").build());
                item.put("IpAddress", AttributeValue.builder().s(auditLog.getIpAddress() != null ? auditLog.getIpAddress() : "").build());
                item.put("UserAgent", AttributeValue.builder().s(auditLog.getUserAgent() != null ? auditLog.getUserAgent() : "").build());
                
                if (auditLog.getDetails() != null) {
                    item.put("Details", AttributeValue.builder().s(auditLog.getDetails().toString()).build());
                }
                
                PutItemRequest putItemRequest = PutItemRequest.builder()
                        .tableName(DYNAMODB_TABLE_NAME)
                        .item(item)
                        .build();
                
                PutItemResponse response = dynamoDbClient.putItem(putItemRequest);
                System.out.println("Audit log saved to DynamoDB: " + response.toString());
                
            } catch (Exception e) {
                System.err.println("Failed to save audit log to DynamoDB: " + e.getMessage());
            }
        });
    }
}
