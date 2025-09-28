package com.bankingsystem.repository;

import com.bankingsystem.model.AuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuditLogRepository extends MongoRepository<AuditLog, String> {
    
    Optional<AuditLog> findByActionId(String actionId);
    
    List<AuditLog> findByUserId(String userId);
    
    List<AuditLog> findByAction(String action);
    
    List<AuditLog> findByEntityType(String entityType);
    
    List<AuditLog> findByEntityId(String entityId);
    
    List<AuditLog> findByResult(String result);
    
    List<AuditLog> findByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("{'userId': ?0, 'timestamp': {$gte: ?1, $lte: ?2}}")
    List<AuditLog> findByUserIdAndTimestampBetween(String userId, LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("{'entityType': ?0, 'entityId': ?1, 'timestamp': {$gte: ?2, $lte: ?3}}")
    List<AuditLog> findByEntityAndTimestampBetween(String entityType, String entityId, LocalDateTime startDate, LocalDateTime endDate);
    
    List<AuditLog> findByEntityTypeAndEntityId(String entityType, String entityId);
    
    @Query("{'action': ?0, 'timestamp': {$gte: ?1, $lte: ?2}}")
    List<AuditLog> findByActionAndTimestampBetween(String action, LocalDateTime startDate, LocalDateTime endDate);
    
    boolean existsByActionId(String actionId);
}
