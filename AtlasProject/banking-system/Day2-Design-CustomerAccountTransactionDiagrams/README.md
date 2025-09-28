# Day 2 – Design: Customer, Account, Transaction Diagrams

## 📋 Overview
This folder contains all design-related files for the Banking Transaction System, including UML class diagrams, entity models, and frontend visualization components for Customer, Account, and Transaction entities.

## 🏗️ Design Architecture

### Core Entities
- **Customer** - Represents banking customers with personal information
- **Account** - Represents bank accounts linked to customers
- **Transaction** - Represents financial transactions between accounts
- **Audit** - Represents audit trail for compliance and security

### Entity Relationships
- **Customer 1 → 1..* Account** - One customer can have multiple accounts
- **Transaction * → 0..1 Account (source)** - Transactions can have a source account
- **Transaction * → 0..1 Account (destination)** - Transactions can have a destination account
- **Audit * → Customer/Account/Transaction** - Audit logs track all entity changes

## 📊 UML Class Diagrams

### Customer Entity
```java
class Customer {
    +UUID customerId
    +string fullName
    +string email
    +string phone
    +string addressLine1
    +string addressLine2
    +string city
    +string country
    +ZonedDateTime createdAt
    +ZonedDateTime updatedAt
    +CustomerStatus status
}
```

### Account Entity
```java
class Account {
    +UUID accountId
    +string accountNumber
    +AccountType accountType
    +decimal balance
    +string currency
    +AccountStatus status
    +ZonedDateTime openedAt
    +ZonedDateTime closedAt (optional)
    +UUID customerId
}
```

### Transaction Entity
```java
class Transaction {
    +UUID transactionId
    +TransactionType type
    +decimal amount
    +string currency
    +UUID sourceAccountId (nullable)
    +UUID destinationAccountId (nullable)
    +TransactionStatus status
    +string description
    +string reference
    +ZonedDateTime createdAt
    +UUID createdByUserId
}
```

### Audit Entity
```java
class Audit {
    +UUID auditId
    +AuditAction action
    +string entityType
    +UUID entityId
    +UUID actorId
    +string actorType
    +ZonedDateTime performedAt
    +string ipAddress
    +string metadataJson
}
```

## 🔄 Enumerations

### AccountType Enum
- **CHECKING** - Regular checking account
- **SAVINGS** - Savings account with interest
- **FIXED_DEPOSIT** - Fixed deposit account
- **LOAN** - Loan account

### TransactionType Enum
- **DEPOSIT** - Money deposited to account
- **WITHDRAWAL** - Money withdrawn from account
- **TRANSFER** - Money transferred between accounts
- **REVERSAL** - Transaction reversal
- **FEE** - Fee deduction
- **INTEREST** - Interest credit

### CustomerStatus Enum
- **ACTIVE** - Active customer
- **SUSPENDED** - Suspended customer
- **CLOSED** - Closed customer account

### AccountStatus Enum
- **ACTIVE** - Active account
- **FROZEN** - Frozen account
- **CLOSED** - Closed account

### TransactionStatus Enum
- **PENDING** - Transaction pending
- **COMPLETED** - Transaction completed
- **FAILED** - Transaction failed
- **REVERSED** - Transaction reversed

### AuditAction Enum
- **CREATE** - Entity creation
- **UPDATE** - Entity update
- **DELETE** - Entity deletion
- **LOGIN** - User login
- **LOGOUT** - User logout
- **APPROVE** - Approval action
- **REJECT** - Rejection action
- **EXECUTE_TRANSACTION** - Transaction execution

## 🎨 Design Features

### UML Diagram Visualization
- **Interactive Mermaid Diagrams** - Rendered in web interface
- **SVG Export** - Downloadable SVG format
- **PNG Export** - Downloadable PNG format
- **Real-time Rendering** - Live diagram updates

### Entity Relationships
- **One-to-Many** - Customer to Accounts
- **Many-to-One** - Transactions to Accounts
- **Many-to-Many** - Audit to all entities
- **Optional Relationships** - Nullable foreign keys

### Design Patterns
- **Entity Pattern** - Core business entities
- **Repository Pattern** - Data access abstraction
- **Audit Pattern** - Comprehensive logging
- **Enum Pattern** - Type safety and validation

## 📁 File Structure

```
Day2-Design-CustomerAccountTransactionDiagrams/
├── src/main/java/com/bankingsystem/model/
│   ├── Customer.java
│   ├── Account.java
│   ├── Transaction.java
│   └── AuditLog.java
├── diagrams/
│   ├── banking-system-uml.mmd
│   └── banking-system-uml.svg
├── frontend/
│   ├── index.html
│   ├── style.css
│   └── script.js
└── README.md
```

## 🔧 Technical Implementation

### Model Annotations
- **@Document** - MongoDB collection mapping
- **@Id** - Primary key identification
- **@Indexed** - Database indexing
- **@DocumentReference** - MongoDB references
- **@NotBlank** - Validation constraints
- **@NotNull** - Null validation
- **@DecimalMin** - Numeric validation

### Data Types
- **UUID** - Unique identifier
- **String** - Text fields
- **BigDecimal** - Precise decimal arithmetic
- **ZonedDateTime** - Timezone-aware timestamps
- **Currency** - Currency handling
- **Enum** - Type-safe constants

### Validation Rules
- **Customer** - Required fields: fullName, email, phone
- **Account** - Required fields: accountNumber, customer, accountType
- **Transaction** - Required fields: transactionId, type, amount
- **Audit** - Required fields: auditId, action, entityType

## 🎯 Design Principles

### Single Responsibility
- **Customer** - Manages customer information
- **Account** - Manages account details and balance
- **Transaction** - Manages transaction records
- **Audit** - Manages audit trail

### Open/Closed Principle
- **Extensible Enums** - Easy to add new types
- **Flexible Relationships** - Optional foreign keys
- **Audit Trail** - Tracks all entity changes

### Interface Segregation
- **Focused Entities** - Each entity has specific purpose
- **Clear Boundaries** - Well-defined entity relationships
- **Minimal Coupling** - Loose coupling between entities

## 🛡️ Security & Compliance

### Audit Trail
- **Complete Logging** - Every entity change tracked
- **User Tracking** - Who performed what action
- **Timestamp Recording** - When actions occurred
- **IP Address Logging** - Security tracking
- **Metadata Storage** - Additional context

### Data Integrity
- **Primary Keys** - Unique identifiers
- **Foreign Keys** - Referential integrity
- **Validation Rules** - Data validation
- **Enum Constraints** - Type safety

## 📈 Scalability Considerations

### Database Design
- **Indexed Fields** - Optimized queries
- **Reference Fields** - Efficient relationships
- **Audit Partitioning** - Time-based partitioning
- **Soft Deletes** - Data retention

### Performance
- **Lazy Loading** - On-demand data loading
- **Caching Strategy** - Frequently accessed data
- **Batch Operations** - Bulk data processing
- **Async Logging** - Non-blocking audit logs

## 🧪 Testing Strategy

### Unit Tests
- **Entity Validation** - Field validation testing
- **Enum Testing** - Enum value verification
- **Relationship Testing** - Entity relationship validation

### Integration Tests
- **Database Operations** - CRUD operations
- **Relationship Integrity** - Foreign key constraints
- **Audit Logging** - Audit trail verification

## 🔗 Dependencies

### Spring Framework
- **Spring Data MongoDB** - Database operations
- **Spring Validation** - Input validation
- **Spring Boot** - Application framework

### Validation
- **Jakarta Validation** - Bean validation
- **Custom Validators** - Business rule validation

### Utilities
- **UUID** - Unique identifier generation
- **BigDecimal** - Precise decimal arithmetic
- **ZonedDateTime** - Timezone handling

## 📊 Diagram Formats

### Mermaid Format (.mmd)
- **Text-based** - Version control friendly
- **Rendered** - Visual representation
- **Interactive** - Web-based viewing

### SVG Format (.svg)
- **Vector Graphics** - Scalable diagrams
- **Downloadable** - Offline viewing
- **Editable** - Design tool compatibility

### PNG Format
- **Raster Graphics** - Image format
- **Portable** - Easy sharing
- **Print-friendly** - Document inclusion

## 🎨 Frontend Integration

### Web Interface
- **Interactive Diagrams** - Clickable elements
- **Real-time Updates** - Live diagram rendering
- **Export Options** - Download formats
- **Responsive Design** - Mobile-friendly

### Visualization Features
- **Zoom & Pan** - Diagram navigation
- **Color Coding** - Entity type differentiation
- **Relationship Lines** - Clear connections
- **Legend** - Diagram explanation

## 📈 Future Enhancements

### Design Improvements
- **Sequence Diagrams** - Process flows
- **State Diagrams** - Entity state changes
- **Activity Diagrams** - Business processes
- **Component Diagrams** - System architecture

### Visualization Features
- **3D Diagrams** - Enhanced visualization
- **Animation** - Dynamic relationships
- **Interactive Elements** - Clickable entities
- **Export Options** - Multiple formats

---

**Note**: This folder contains all design-related files for the Banking Transaction System. The diagrams and models form the foundation for the complete system implementation.
