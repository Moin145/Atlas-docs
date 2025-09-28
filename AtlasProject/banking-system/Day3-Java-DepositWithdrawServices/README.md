Day 3 – Java Deposit & Withdraw Services
Overview

This module handles deposit, withdrawal, and transfer operations in the Banking Transaction System. It ensures secure, validated transactions with audit logging and undo/redo support.

Architecture

Service Layer

DepositService.java – Handles deposits

WithdrawService.java – Handles withdrawals

TransferService.java – Handles account transfers

AuditService.java – Records all transactions

Controller Layer

TransactionController.java – REST API endpoints

Model Layer

Transaction.java, Account.java, AuditLog.java – Core entities

Utility Layer

UndoRedoStack.java – Undo/Redo support

Repository Layer

AccountRepository.java, TransactionRepository.java, AuditLogRepository.java – Database access

Key Features

Deposit & Withdraw – Validates amounts, checks account status, updates balances atomically, logs audits.

Transfer – Validates both accounts, performs atomic updates, logs transactions.

Undo/Redo – Reverse or re-execute transactions per account.

Audit Trail – Logs user, timestamp, result, and errors.

API Endpoints

Deposit

POST /api/transactions/deposit


Withdraw

POST /api/transactions/withdraw


Transfer

POST /api/transactions/transfer


Undo/Redo

GET /api/transactions/account/{accountId}/undo-redo-status
POST /api/transactions/undo/{accountId}
POST /api/transactions/redo/{accountId}

Business Rules

Deposits & Withdrawals: ₹0.01 – ₹10,00,000 per transaction, only active accounts.

Transfers: Validate both accounts, sufficient balance, cannot transfer to same account.

Daily & transaction limits enforced.

Testing

Unit Tests: DepositServiceTest, WithdrawServiceTest, TransferServiceTest, TransactionControllerTest

Integration Tests: End-to-end flows, database, audit verification

File Structure
Day3-Java-DepositWithdrawServices/
├── src/main/java/com/bankingsystem/
│   ├── service/
│   ├── controller/
│   ├── model/
│   ├── util/
│   └── repository/
└── README.md

Dependencies

Spring Boot, Spring Data MongoDB, Spring Web, Jakarta Validation

Utilities: BigDecimal, LocalDateTime, UUID

Future Enhancements

Batch processing & scheduled transactions

Fraud detection & notifications

Performance: caching, async processing, database optimization