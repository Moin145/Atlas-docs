package com.bankingsystem.repository;

import com.bankingsystem.model.Account;
import com.bankingsystem.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {
    
    Optional<Account> findByAccountNumber(String accountNumber);
    
    List<Account> findByCustomer(Customer customer);
    
    List<Account> findByCustomerId(String customerId);
    
    List<Account> findByAccountType(Account.AccountType accountType);
    
    List<Account> findByStatus(Account.AccountStatus status);
    
    boolean existsByAccountNumber(String accountNumber);
    
    List<Account> findByCustomerAndStatus(Customer customer, Account.AccountStatus status);
}
