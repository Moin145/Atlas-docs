package com.bankingsystem.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "audit_logs")
public class AuditLog {
    @Id
    private String id;
    
    @NotBlank(message = "Action ID is required")
    @Indexed(unique = true)
    private String actionId;
    
    @NotBlank(message = "User ID is required")
    private String userId;
    
    @NotBlank(message = "Action is required")
    private String action;
    
    @NotBlank(message = "Entity type is required")
    private String entityType;
    
    private String entityId;
    private String description;
    private Map<String, Object> details;
    private String ipAddress;
    private String userAgent;
    private LocalDateTime timestamp;
    private String result;
    private String errorMessage;
    
    // Constructors
    public AuditLog() {
        this.timestamp = LocalDateTime.now();
    }
    
    public AuditLog(String actionId, String userId, String action, String entityType, String entityId) {
        this();
        this.actionId = actionId;
        this.userId = userId;
        this.action = action;
        this.entityType = entityType;
        this.entityId = entityId;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getActionId() {
        return actionId;
    }
    
    public void setActionId(String actionId) {
        this.actionId = actionId;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
    
    public String getEntityType() {
        return entityType;
    }
    
    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
    
    public String getEntityId() {
        return entityId;
    }
    
    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Map<String, Object> getDetails() {
        return details;
    }
    
    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    public String getUserAgent() {
        return userAgent;
    }
    
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getResult() {
        return result;
    }
    
    public void setResult(String result) {
        this.result = result;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    // Business methods
    public void markAsSuccess(String description) {
        this.result = "SUCCESS";
        this.description = description;
    }
    
    public void markAsFailure(String errorMessage) {
        this.result = "FAILURE";
        this.errorMessage = errorMessage;
    }
    
    public boolean isSuccess() {
        return "SUCCESS".equals(result);
    }
    
    public boolean isFailure() {
        return "FAILURE".equals(result);
    }
    
    @Override
    public String toString() {
        return "AuditLog{" +
                "id='" + id + '\'' +
                ", actionId='" + actionId + '\'' +
                ", userId='" + userId + '\'' +
                ", action='" + action + '\'' +
                ", entityType='" + entityType + '\'' +
                ", entityId='" + entityId + '\'' +
                ", description='" + description + '\'' +
                ", timestamp=" + timestamp +
                ", result='" + result + '\'' +
                '}';
    }
}
