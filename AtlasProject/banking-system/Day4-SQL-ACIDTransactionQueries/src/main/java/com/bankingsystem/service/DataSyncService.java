package com.bankingsystem.service;

import com.bankingsystem.model.Customer;
import com.bankingsystem.model.Account;
import com.bankingsystem.model.Transaction;
import com.bankingsystem.model.AuditLog;
import com.bankingsystem.repository.CustomerRepository;
import com.bankingsystem.repository.AccountRepository;
import com.bankingsystem.repository.TransactionRepository;
import com.bankingsystem.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.BillingMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataSyncService {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private AuditLogRepository auditLogRepository;
    
    @Autowired
    private DynamoDbClient dynamoDbClient;
    
    /**
     * Sync all MongoDB data to DynamoDB tables
     */
    public void syncAllDataToDynamoDB() {
        try {
            // Create tables if they don't exist
            createDynamoDBTables();
            
            // Sync customers
            syncCustomersToDynamoDB();
            
            // Sync accounts
            syncAccountsToDynamoDB();
            
            // Sync transactions
            syncTransactionsToDynamoDB();
            
            // Sync audit logs
            syncAuditLogsToDynamoDB();
            
            System.out.println("✅ All data synced to DynamoDB successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Error syncing data to DynamoDB: " + e.getMessage());
        }
    }
    
    /**
     * Create DynamoDB tables
     */
    private void createDynamoDBTables() {
        // Create Customers table
        createTable("BankingCustomers", "customerId");
        
        // Create Accounts table
        createTable("BankingAccounts", "accountNumber");
        
        // Create Transactions table
        createTable("BankingTransactions", "transactionId");
        
        // Create Audit Logs table
        createTable("BankingAuditLogs", "actionId");
    }
    
    /**
     * Create a DynamoDB table
     */
    private void createTable(String tableName, String keyAttribute) {
        try {
            CreateTableRequest createTableRequest = CreateTableRequest.builder()
                    .tableName(tableName)
                    .keySchema(KeySchemaElement.builder()
                            .attributeName(keyAttribute)
                            .keyType("HASH")
                            .build())
                    .attributeDefinitions(AttributeDefinition.builder()
                            .attributeName(keyAttribute)
                            .attributeType("S")
                            .build())
                    .billingMode(BillingMode.PAY_PER_REQUEST)
                    .build();
            
            CreateTableResponse response = dynamoDbClient.createTable(createTableRequest);
            System.out.println("✅ Created table: " + tableName);
            
        } catch (Exception e) {
            // Table might already exist
            System.out.println("ℹ️ Table " + tableName + " already exists or error: " + e.getMessage());
        }
    }
    
    /**
     * Sync customers from MongoDB to DynamoDB
     */
    private void syncCustomersToDynamoDB() {
        List<Customer> customers = customerRepository.findAll();
        
        for (Customer customer : customers) {
            Map<String, AttributeValue> item = new HashMap<>();
            
            // Only add non-null fields
            if (customer.getId() != null) {
                item.put("customerId", AttributeValue.builder().s(customer.getId()).build());
            }
            if (customer.getFirstName() != null && !customer.getFirstName().trim().isEmpty()) {
                item.put("firstName", AttributeValue.builder().s(customer.getFirstName()).build());
            }
            if (customer.getLastName() != null && !customer.getLastName().trim().isEmpty()) {
                item.put("lastName", AttributeValue.builder().s(customer.getLastName()).build());
            }
            if (customer.getEmail() != null && !customer.getEmail().trim().isEmpty()) {
                item.put("email", AttributeValue.builder().s(customer.getEmail()).build());
            }
            if (customer.getMobileNumber() != null && !customer.getMobileNumber().trim().isEmpty()) {
                item.put("mobileNumber", AttributeValue.builder().s(customer.getMobileNumber()).build());
            }
            if (customer.getAddress() != null && !customer.getAddress().trim().isEmpty()) {
                item.put("address", AttributeValue.builder().s(customer.getAddress()).build());
            }
            if (customer.getCity() != null && !customer.getCity().trim().isEmpty()) {
                item.put("city", AttributeValue.builder().s(customer.getCity()).build());
            }
            if (customer.getState() != null && !customer.getState().trim().isEmpty()) {
                item.put("state", AttributeValue.builder().s(customer.getState()).build());
            }
            if (customer.getPincode() != null && !customer.getPincode().trim().isEmpty()) {
                item.put("pincode", AttributeValue.builder().s(customer.getPincode()).build());
            }
            if (customer.getCreatedAt() != null) {
                item.put("createdAt", AttributeValue.builder().s(customer.getCreatedAt().toString()).build());
            }
            
            PutItemRequest putItemRequest = PutItemRequest.builder()
                    .tableName("BankingCustomers")
                    .item(item)
                    .build();
            
            dynamoDbClient.putItem(putItemRequest);
        }
        
        System.out.println("✅ Synced " + customers.size() + " customers to DynamoDB");
    }
    
    /**
     * Sync accounts from MongoDB to DynamoDB
     */
    private void syncAccountsToDynamoDB() {
        List<Account> accounts = accountRepository.findAll();
        
        for (Account account : accounts) {
            Map<String, AttributeValue> item = new HashMap<>();
            
            // Only add non-null fields
            if (account.getAccountNumber() != null && !account.getAccountNumber().trim().isEmpty()) {
                item.put("accountNumber", AttributeValue.builder().s(account.getAccountNumber()).build());
            }
            if (account.getCustomer() != null && account.getCustomer().getId() != null) {
                item.put("customerId", AttributeValue.builder().s(account.getCustomer().getId()).build());
            }
            if (account.getAccountType() != null) {
                item.put("accountType", AttributeValue.builder().s(account.getAccountType().toString()).build());
            }
            if (account.getBalance() != null) {
                item.put("balance", AttributeValue.builder().n(account.getBalance().toString()).build());
            }
            if (account.getStatus() != null) {
                item.put("status", AttributeValue.builder().s(account.getStatus().toString()).build());
            }
            if (account.getCreatedAt() != null) {
                item.put("createdAt", AttributeValue.builder().s(account.getCreatedAt().toString()).build());
            }
            
            PutItemRequest putItemRequest = PutItemRequest.builder()
                    .tableName("BankingAccounts")
                    .item(item)
                    .build();
            
            dynamoDbClient.putItem(putItemRequest);
        }
        
        System.out.println("✅ Synced " + accounts.size() + " accounts to DynamoDB");
    }
    
    /**
     * Sync transactions from MongoDB to DynamoDB
     */
    private void syncTransactionsToDynamoDB() {
        List<Transaction> transactions = transactionRepository.findAll();
        
        for (Transaction transaction : transactions) {
            Map<String, AttributeValue> item = new HashMap<>();
            
            // Only add non-null fields
            if (transaction.getTransactionId() != null && !transaction.getTransactionId().trim().isEmpty()) {
                item.put("transactionId", AttributeValue.builder().s(transaction.getTransactionId()).build());
            }
            if (transaction.getType() != null) {
                item.put("type", AttributeValue.builder().s(transaction.getType().toString()).build());
            }
            if (transaction.getAmount() != null) {
                item.put("amount", AttributeValue.builder().n(transaction.getAmount().toString()).build());
            }
            if (transaction.getDescription() != null && !transaction.getDescription().trim().isEmpty()) {
                item.put("description", AttributeValue.builder().s(transaction.getDescription()).build());
            }
            if (transaction.getStatus() != null) {
                item.put("status", AttributeValue.builder().s(transaction.getStatus().toString()).build());
            }
            if (transaction.getTransactionDate() != null) {
                item.put("transactionDate", AttributeValue.builder().s(transaction.getTransactionDate().toString()).build());
            }
            
            if (transaction.getSourceAccount() != null && transaction.getSourceAccount().getAccountNumber() != null) {
                item.put("sourceAccountNumber", AttributeValue.builder().s(transaction.getSourceAccount().getAccountNumber()).build());
            }
            
            if (transaction.getDestinationAccount() != null && transaction.getDestinationAccount().getAccountNumber() != null) {
                item.put("destinationAccountNumber", AttributeValue.builder().s(transaction.getDestinationAccount().getAccountNumber()).build());
            }
            
            PutItemRequest putItemRequest = PutItemRequest.builder()
                    .tableName("BankingTransactions")
                    .item(item)
                    .build();
            
            dynamoDbClient.putItem(putItemRequest);
        }
        
        System.out.println("✅ Synced " + transactions.size() + " transactions to DynamoDB");
    }
    
    /**
     * Sync audit logs from MongoDB to DynamoDB
     */
    private void syncAuditLogsToDynamoDB() {
        List<AuditLog> auditLogs = auditLogRepository.findAll();
        
        for (AuditLog auditLog : auditLogs) {
            Map<String, AttributeValue> item = new HashMap<>();
            
            // Only add non-null fields
            if (auditLog.getActionId() != null && !auditLog.getActionId().trim().isEmpty()) {
                item.put("actionId", AttributeValue.builder().s(auditLog.getActionId()).build());
            }
            if (auditLog.getUserId() != null && !auditLog.getUserId().trim().isEmpty()) {
                item.put("userId", AttributeValue.builder().s(auditLog.getUserId()).build());
            }
            if (auditLog.getAction() != null && !auditLog.getAction().trim().isEmpty()) {
                item.put("action", AttributeValue.builder().s(auditLog.getAction()).build());
            }
            if (auditLog.getEntityType() != null && !auditLog.getEntityType().trim().isEmpty()) {
                item.put("entityType", AttributeValue.builder().s(auditLog.getEntityType()).build());
            }
            if (auditLog.getEntityId() != null && !auditLog.getEntityId().trim().isEmpty()) {
                item.put("entityId", AttributeValue.builder().s(auditLog.getEntityId()).build());
            }
            if (auditLog.getDescription() != null && !auditLog.getDescription().trim().isEmpty()) {
                item.put("description", AttributeValue.builder().s(auditLog.getDescription()).build());
            }
            if (auditLog.getIpAddress() != null && !auditLog.getIpAddress().trim().isEmpty()) {
                item.put("ipAddress", AttributeValue.builder().s(auditLog.getIpAddress()).build());
            }
            if (auditLog.getUserAgent() != null && !auditLog.getUserAgent().trim().isEmpty()) {
                item.put("userAgent", AttributeValue.builder().s(auditLog.getUserAgent()).build());
            }
            if (auditLog.getTimestamp() != null) {
                item.put("timestamp", AttributeValue.builder().s(auditLog.getTimestamp().toString()).build());
            }
            if (auditLog.getResult() != null && !auditLog.getResult().trim().isEmpty()) {
                item.put("result", AttributeValue.builder().s(auditLog.getResult()).build());
            }
            if (auditLog.getErrorMessage() != null && !auditLog.getErrorMessage().trim().isEmpty()) {
                item.put("errorMessage", AttributeValue.builder().s(auditLog.getErrorMessage()).build());
            }
            
            PutItemRequest putItemRequest = PutItemRequest.builder()
                    .tableName("BankingAuditLogs")
                    .item(item)
                    .build();
            
            dynamoDbClient.putItem(putItemRequest);
        }
        
        System.out.println("✅ Synced " + auditLogs.size() + " audit logs to DynamoDB");
    }
}
