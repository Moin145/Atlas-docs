package com.bankingsystem.controller;

import com.bankingsystem.model.AuditLog;
import com.bankingsystem.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/audit")
@CrossOrigin(origins = "*")
public class AuditController {
    
    @Autowired
    private AuditService auditService;
    
    /**
     * Get all audit logs
     */
    @GetMapping("/logs")
    public ResponseEntity<?> getAllAuditLogs() {
        try {
            List<AuditLog> logs = auditService.getAllAuditLogs();
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "logs", logs
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    /**
     * Get audit logs by user ID
     */
    @GetMapping("/logs/user/{userId}")
    public ResponseEntity<?> getAuditLogsByUser(@PathVariable String userId) {
        try {
            List<AuditLog> logs = auditService.getAuditLogsByUser(userId);
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "logs", logs
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    /**
     * Get audit logs by date range
     */
    @GetMapping("/logs/date-range")
    public ResponseEntity<?> getAuditLogsByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDateTime start = LocalDateTime.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            LocalDateTime end = LocalDateTime.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            
            List<AuditLog> logs = auditService.getAuditLogsByDateRange(start, end);
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "logs", logs
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    /**
     * Get audit logs by entity
     */
    @GetMapping("/logs/entity/{entityType}/{entityId}")
    public ResponseEntity<?> getAuditLogsByEntity(@PathVariable String entityType, @PathVariable String entityId) {
        try {
            List<AuditLog> logs = auditService.getAuditLogsByEntity(entityType, entityId);
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "logs", logs
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    /**
     * Get audit logs by action
     */
    @GetMapping("/logs/action/{action}")
    public ResponseEntity<?> getAuditLogsByAction(@PathVariable String action) {
        try {
            List<AuditLog> logs = auditService.getAuditLogsByAction(action);
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "logs", logs
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
    
    /**
     * Get audit log statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getAuditStats() {
        try {
            List<AuditLog> allLogs = auditService.getAllAuditLogs();
            
            long totalLogs = allLogs.size();
            long successLogs = allLogs.stream().filter(AuditLog::isSuccess).count();
            long failureLogs = allLogs.stream().filter(AuditLog::isFailure).count();
            
            Map<String, Long> actionCounts = allLogs.stream()
                    .collect(java.util.stream.Collectors.groupingBy(
                            AuditLog::getAction,
                            java.util.stream.Collectors.counting()
                    ));
            
            Map<String, Long> entityTypeCounts = allLogs.stream()
                    .collect(java.util.stream.Collectors.groupingBy(
                            AuditLog::getEntityType,
                            java.util.stream.Collectors.counting()
                    ));
            
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "totalLogs", totalLogs,
                    "successLogs", successLogs,
                    "failureLogs", failureLogs,
                    "actionCounts", actionCounts,
                    "entityTypeCounts", entityTypeCounts
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
}
