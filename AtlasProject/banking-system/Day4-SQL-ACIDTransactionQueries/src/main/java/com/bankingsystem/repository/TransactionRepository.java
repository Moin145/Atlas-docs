package com.bankingsystem.repository;

import com.bankingsystem.model.Account;
import com.bankingsystem.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
    
    Optional<Transaction> findByTransactionId(String transactionId);
    
    List<Transaction> findBySourceAccount(Account sourceAccount);
    
    List<Transaction> findByDestinationAccount(Account destinationAccount);
    
    List<Transaction> findBySourceAccountOrDestinationAccount(Account sourceAccount, Account destinationAccount);
    
    List<Transaction> findByType(Transaction.TransactionType type);
    
    List<Transaction> findByStatus(Transaction.TransactionStatus status);
    
    List<Transaction> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("{'sourceAccount': ?0, 'transactionDate': {$gte: ?1, $lte: ?2}}")
    List<Transaction> findSourceTransactionsByDateRange(Account account, LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("{'destinationAccount': ?0, 'transactionDate': {$gte: ?1, $lte: ?2}}")
    List<Transaction> findDestinationTransactionsByDateRange(Account account, LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("{'$or': [{'sourceAccount': ?0}, {'destinationAccount': ?0}], 'transactionDate': {$gte: ?1, $lte: ?2}}")
    List<Transaction> findAccountTransactionsByDateRange(Account account, LocalDateTime startDate, LocalDateTime endDate);
    
    boolean existsByTransactionId(String transactionId);
}
