# 🏦 Indian Banking System

A comprehensive banking transaction system built with Java Spring Boot, featuring both CLI and Web interfaces, MongoDB for data storage, and DynamoDB for audit logging.

## 🌟 Features

### Core Banking Operations
- **Account Management**: Create and manage customer accounts
- **Money Transfers**: Transfer funds between accounts
- **Deposits & Withdrawals**: Process financial transactions
- **Transaction History**: View complete transaction records
- **Undo/Redo**: Reverse and redo transactions
- **Audit Logging**: Comprehensive audit trail in both MongoDB and DynamoDB

### Technical Features
- **Dual Interface**: Both Command Line (CLI) and Web UI
- **Database Integration**: MongoDB for core data, DynamoDB for audit logs
- **Indian Localization**: INR currency, Indian names, and formatting
- **BDD Testing**: Cucumber-based behavior-driven testing
- **Automated Settlements**: Batch processing with cron jobs
- **Real-time Notifications**: User feedback and error handling

## 🏗️ Architecture

```
banking-system/
├── backend/                 # Spring Boot Backend
│   ├── src/main/java/
│   │   └── com/bankingsystem/
│   │       ├── model/       # Data models (Customer, Account, Transaction, AuditLog)
│   │       ├── repository/  # MongoDB repositories
│   │       ├── service/     # Business logic services
│   │       ├── controller/  # REST API controllers
│   │       ├── config/      # Configuration classes
│   │       └── util/        # Utility classes (UndoRedoStack, SettlementQueue)
│   └── src/main/resources/
│       └── application.properties
├── frontend/                # Web Interface
│   ├── index.html          # Main HTML page
│   ├── style.css           # Modern CSS styling
│   └── script.js           # JavaScript functionality
├── tests/                   # BDD Tests
│   ├── features/           # Gherkin feature files
│   └── stepdefs/           # Java step definitions
├── scripts/                 # Automation Scripts
│   └── settlement.sh       # Batch settlement script
└── README.md
```

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MongoDB 4.4+
- AWS CLI configured (for DynamoDB)
- Node.js (for frontend development, optional)

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd banking-system
   ```

2. **Start MongoDB**
   ```bash
   # Using Docker
   docker run -d -p 27017:27017 --name mongodb mongo:latest
   
   # Or install MongoDB locally
   # Follow MongoDB installation guide for your OS
   ```

3. **Configure AWS DynamoDB**
   ```bash
   # Create DynamoDB table
   aws dynamodb create-table \
     --table-name BankingAuditLogs \
     --attribute-definitions \
       AttributeName=ActionID,AttributeType=S \
       AttributeName=Timestamp,AttributeType=S \
     --key-schema \
       AttributeName=ActionID,KeyType=HASH \
       AttributeName=Timestamp,KeyType=RANGE \
     --billing-mode PAY_PER_REQUEST
   ```

4. **Build and Run**

   **For Windows:**
   ```cmd
   # Option 1: Using Batch file
   start.bat
   
   # Option 2: Using PowerShell
   .\start.ps1
   
   # Option 3: Manual build and run
   cd backend
   mvn clean install
   mvn spring-boot:run
   ```

   **For Linux/Mac:**
   ```bash
   # Make executable and run
   chmod +x start.sh
   ./start.sh
   
   # Or manual build and run
   cd backend
   mvn clean install
   mvn spring-boot:run
   ```

5. **Access the Application**
   - **Web Interface**: http://localhost:8080
   - **CLI Interface**: The application will start in CLI mode by default
   - **API Endpoints**: http://localhost:8080/api

## 💻 Usage

### CLI Interface
The CLI provides an interactive menu system:

```
🏦 Indian Banking System - Main Menu
====================================
1.  Create Customer
2.  Create Account
3.  Deposit Money
4.  Withdraw Money
5.  Transfer Money
6.  View Account Balance
7.  View Transaction History
8.  Undo Last Transaction
9.  Redo Transaction
10. View Audit Logs
11. View All Customers
12. View All Accounts
0.  Exit
```

### Web Interface
The web interface provides a modern, responsive UI with:
- Dashboard with system statistics
- Customer and account management
- Transaction processing forms
- Real-time transaction history
- Audit log viewing
- Undo/Redo functionality

### API Endpoints

#### Customers
- `GET /api/customers` - List all customers
- `POST /api/customers` - Create new customer
- `GET /api/customers/{id}` - Get customer by ID

#### Accounts
- `GET /api/accounts` - List all accounts
- `POST /api/accounts` - Create new account
- `GET /api/accounts/{accountNumber}` - Get account by number
- `GET /api/accounts/customer/{customerId}` - Get accounts by customer

#### Transactions
- `POST /api/transactions/deposit` - Process deposit
- `POST /api/transactions/withdraw` - Process withdrawal
- `POST /api/transactions/transfer` - Process transfer
- `GET /api/transactions/account/{accountNumber}` - Get transaction history
- `POST /api/transactions/undo/{accountId}` - Undo last transaction
- `POST /api/transactions/redo/{accountId}` - Redo transaction

#### Audit Logs
- `GET /api/audit/logs` - Get all audit logs
- `GET /api/audit/logs/user/{userId}` - Get logs by user
- `GET /api/audit/logs/date-range` - Get logs by date range

## 🧪 Testing

### BDD Tests with Cucumber
```bash
cd backend
mvn test -Dtest=CucumberTestRunner
```

### Manual Testing
1. Start the application
2. Use the CLI or web interface to perform operations
3. Check MongoDB for data persistence
4. Verify DynamoDB for audit logs

## 🔧 Configuration

### MongoDB Configuration
```properties
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=banking_system
```

### DynamoDB Configuration
```properties
aws.region=ap-south-1
aws.dynamodb.table-name=BankingAuditLogs
```

### Custom Properties
```properties
banking.system.currency=INR
banking.system.timezone=Asia/Kolkata
banking.transaction.max-amount=1000000
banking.transaction.daily-limit=500000
```

## 📊 Data Models

### Customer
- Personal information (name, email, mobile, address)
- Indian-specific fields (PAN, Aadhar)
- Validation for Indian mobile numbers and email

### Account
- Account number, type, and balance
- Customer relationship
- Status management (Active, Inactive, Suspended, Closed)
- INR currency support

### Transaction
- Transaction types (Deposit, Withdrawal, Transfer)
- Source and destination accounts
- Amount and currency
- Status tracking and reference numbers

### AuditLog
- Action tracking and user identification
- Timestamp and result logging
- Detailed information storage
- Dual storage in MongoDB and DynamoDB

## 🔄 Automation

### Batch Settlement
The system includes automated batch settlement processing:

```bash
# Make script executable
chmod +x scripts/settlement.sh

# Run manually
./scripts/settlement.sh

# Schedule with cron (daily at 2 AM)
0 2 * * * /path/to/banking-system/scripts/settlement.sh
```

## 🛡️ Security Features

- Input validation and sanitization
- Transaction confirmation prompts
- Audit logging for all operations
- Error handling and logging
- CORS configuration for web interface

## 📈 Monitoring

### Health Checks
- Spring Boot Actuator endpoints
- Database connectivity monitoring
- Service health indicators

### Logging
- Comprehensive logging with different levels
- Audit trail maintenance
- Error tracking and reporting

## 🚀 Deployment

### Production Deployment
1. Configure production database connections
2. Set up AWS credentials for DynamoDB
3. Configure logging and monitoring
4. Set up automated backups
5. Deploy using Docker or traditional methods

### Docker Deployment
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/banking-system-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Check the documentation
- Review the test cases for usage examples

## 🎯 Roadmap

### Completed Features ✅
- [x] Core banking operations
- [x] MongoDB integration
- [x] DynamoDB audit logging
- [x] CLI interface
- [x] Web interface
- [x] BDD testing
- [x] Undo/Redo functionality
- [x] Batch settlement
- [x] Indian localization

### Future Enhancements 🚀
- [ ] Mobile app interface
- [ ] Advanced reporting
- [ ] Real-time notifications
- [ ] Multi-currency support
- [ ] Advanced security features
- [ ] Performance optimization
- [ ] Microservices architecture

---

**Built with ❤️ for the Indian Banking System**
