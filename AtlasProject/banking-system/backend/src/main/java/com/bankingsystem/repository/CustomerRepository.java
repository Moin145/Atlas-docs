package com.bankingsystem.repository;

import com.bankingsystem.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {
    
    Optional<Customer> findByEmail(String email);
    
    Optional<Customer> findByMobileNumber(String mobileNumber);
    
    Optional<Customer> findByPanNumber(String panNumber);
    
    Optional<Customer> findByAadharNumber(String aadharNumber);
    
    boolean existsByEmail(String email);
    
    boolean existsByMobileNumber(String mobileNumber);
    
    boolean existsByPanNumber(String panNumber);
    
    boolean existsByAadharNumber(String aadharNumber);
}
