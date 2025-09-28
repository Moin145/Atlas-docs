# Banking System CLI - Complete Interface
# This provides all banking operations through a menu-driven CLI

$baseUrl = "http://localhost:8080/api"

function Show-MainMenu {
    Clear-Host
    Write-Host " INDIAN BANKING SYSTEM - CLI INTERFACE" -ForegroundColor Green
    Write-Host "=========================================" -ForegroundColor Green
    Write-Host ""
    Write-Host " DASHBOARD & VIEWING" -ForegroundColor Cyan
    Write-Host "1.  View Dashboard Statistics"
    Write-Host "2.  View All Customers"
    Write-Host "3.  View All Accounts"
    Write-Host "4.  View Transaction History"
    Write-Host "5.  View Audit Logs"
    Write-Host ""
    Write-Host " CUSTOMER MANAGEMENT" -ForegroundColor Yellow
    Write-Host "6.  Create New Customer"
    Write-Host "7.  View Customer Details"
    Write-Host ""
    Write-Host " ACCOUNT MANAGEMENT" -ForegroundColor Magenta
    Write-Host "8.  Create New Account"
    Write-Host "9.  View Account Balance"
    Write-Host "10. View Account Details"
    Write-Host ""
    Write-Host " TRANSACTION OPERATIONS" -ForegroundColor Red
    Write-Host "11. Deposit Money"
    Write-Host "12. Withdraw Money"
    Write-Host "13. Transfer Money"
    Write-Host "14. Undo Last Transaction"
    Write-Host "15. Redo Transaction"
    Write-Host ""
    Write-Host " SYSTEM OPERATIONS" -ForegroundColor Blue
    Write-Host "16. Data Sync Operations"
    Write-Host "17. System Status"
    Write-Host ""
    Write-Host "0.  Exit" -ForegroundColor DarkRed
    Write-Host "=========================================" -ForegroundColor Green
}

function Get-UserChoice {
    return Read-Host "
Enter your choice (0-17)"
}

function Show-Dashboard {
    Write-Host "
 DASHBOARD STATISTICS" -ForegroundColor Cyan
    Write-Host "=======================" -ForegroundColor Cyan

    try {
        # Get customers
        $customersResponse = Invoke-RestMethod -Uri "$baseUrl/customers" -Method GET
        $customerCount = if ($customersResponse.success) { $customersResponse.customers.Count } else { 0 }

        # Get accounts
        $accountsResponse = Invoke-RestMethod -Uri "$baseUrl/accounts" -Method GET
        $accountCount = if ($accountsResponse.success) { $accountsResponse.accounts.Count } else { 0 }

        # Calculate total balance
        $totalBalance = 0
        if ($accountsResponse.success) {
            $totalBalance = ($accountsResponse.accounts | Measure-Object -Property balance -Sum).Sum
        }

        Write-Host "Total Customers: $customerCount" -ForegroundColor Yellow
        Write-Host "Total Accounts: $accountCount" -ForegroundColor Yellow
        Write-Host "Total Balance: ₹$($totalBalance.ToString('N2'))" -ForegroundColor Yellow

        # Show recent customers
        if ($customerCount -gt 0) {
            Write-Host "
Recent Customers:" -ForegroundColor Green
            $customersResponse.customers | Select-Object -First 3 | ForEach-Object {
                Write-Host " $($_.firstName) $($_.lastName) - $($_.city)" -ForegroundColor White
            }
        }

    } catch {
        Write-Host " Error loading dashboard: $($_.Exception.Message)" -ForegroundColor Red
    }
}

function Show-AllCustomers {
    Write-Host "
 ALL CUSTOMERS" -ForegroundColor Cyan
    Write-Host "================" -ForegroundColor Cyan

    try {
        $response = Invoke-RestMethod -Uri "$baseUrl/customers" -Method GET
        if ($response.success -and $response.customers.Count -gt 0) {
            $response.customers | ForEach-Object {
                Write-Host "
Customer ID: $($_.id)" -ForegroundColor Yellow
                Write-Host "Name: $($_.firstName) $($_.lastName)"
                Write-Host "Email: $($_.email)"
                Write-Host "Mobile: $($_.mobileNumber)"
                Write-Host "Address: $($_.address), $($_.city), $($_.state) - $($_.pincode)"
                Write-Host "Status: $($_.status)"
                Write-Host "Created: $($_.createdAt)"
                Write-Host "----------------------------------------"
            }
        } else {
            Write-Host "No customers found" -ForegroundColor Red
        }
    } catch {
        Write-Host " Error: $($_.Exception.Message)" -ForegroundColor Red
    }
}

function Show-AllAccounts {
    Write-Host "
 ALL ACCOUNTS" -ForegroundColor Cyan
    Write-Host "===============" -ForegroundColor Cyan

    try {
        $response = Invoke-RestMethod -Uri "$baseUrl/accounts" -Method GET
        if ($response.success -and $response.accounts.Count -gt 0) {
            $response.accounts | ForEach-Object {
                Write-Host "
Account Number: $($_.accountNumber)" -ForegroundColor Yellow
                Write-Host "Type: $($_.accountType)"
                Write-Host "Balance: ₹$($_.balance)"
                Write-Host "Customer: $($_.customer.firstName) $($_.customer.lastName)"
                Write-Host "Status: $($_.status)"
                Write-Host "Opened: $($_.openedAt)"
                Write-Host "----------------------------------------"
            }
        } else {
            Write-Host "No accounts found" -ForegroundColor Red
        }
    } catch {
        Write-Host " Error: $($_.Exception.Message)" -ForegroundColor Red
    }
}

function Create-NewCustomer {
    Write-Host "
 CREATE NEW CUSTOMER" -ForegroundColor Cyan
    Write-Host "======================" -ForegroundColor Cyan

    $firstName = Read-Host "First Name"
    $lastName = Read-Host "Last Name"
    $email = Read-Host "Email"
    $mobileNumber = Read-Host "Mobile Number"
    $address = Read-Host "Address"
    $city = Read-Host "City"
    $state = Read-Host "State"
    $pincode = Read-Host "Pincode"

    $customerData = @{
        firstName = $firstName
        lastName = $lastName
        email = $email
        mobileNumber = $mobileNumber
        address = $address
        city = $city
        state = $state
        pincode = $pincode
    } | ConvertTo-Json

    try {
        $response = Invoke-RestMethod -Uri "$baseUrl/customers" -Method POST -Body $customerData -ContentType "application/json"
        if ($response.success) {
            Write-Host " Customer created successfully!" -ForegroundColor Green
            Write-Host "Customer ID: $($response.customer.id)" -ForegroundColor Yellow
            Write-Host "Full Name: $($response.customer.firstName) $($response.customer.lastName)" -ForegroundColor White
        } else {
            Write-Host " Failed to create customer: $($response.message)" -ForegroundColor Red
        }
    } catch {
        Write-Host " Error: $($_.Exception.Message)" -ForegroundColor Red
    }
}

function Create-NewAccount {
    Write-Host "
 CREATE NEW ACCOUNT" -ForegroundColor Cyan
    Write-Host "=====================" -ForegroundColor Cyan

    $customerId = Read-Host "Customer ID"

    Write-Host "
Account Types:"
    Write-Host "1. SAVINGS"
    Write-Host "2. CURRENT"
    Write-Host "3. FIXED_DEPOSIT"
    Write-Host "4. RECURRING_DEPOSIT"

    $accountTypeChoice = Read-Host "Select Account Type (1-4)"
    $accountTypes = @("", "SAVINGS", "CURRENT", "FIXED_DEPOSIT", "RECURRING_DEPOSIT")
    $accountType = $accountTypes[$accountTypeChoice]

    if (-not $accountType) {
        Write-Host " Invalid account type selection" -ForegroundColor Red
        return
    }

    $accountData = @{
        customerId = $customerId
        accountType = $accountType
    } | ConvertTo-Json

    try {
        $response = Invoke-RestMethod -Uri "$baseUrl/accounts" -Method POST -Body $accountData -ContentType "application/json"
        if ($response.success) {
            Write-Host " Account created successfully!" -ForegroundColor Green
            Write-Host "Account Number: $($response.account.accountNumber)" -ForegroundColor Yellow
            Write-Host "Account Type: $($response.account.accountType)" -ForegroundColor White
            Write-Host "Customer: $($response.account.customer.firstName) $($response.account.customer.lastName)" -ForegroundColor White
        } else {
            Write-Host " Failed to create account: $($response.message)" -ForegroundColor Red
        }
    } catch {
        Write-Host " Error: $($_.Exception.Message)" -ForegroundColor Red
    }
}

function Deposit-Money {
    Write-Host "
 DEPOSIT MONEY" -ForegroundColor Cyan
    Write-Host "================" -ForegroundColor Cyan

    $accountNumber = Read-Host "Account Number"
    $amount = Read-Host "Amount (₹)"
    $description = Read-Host "Description"

    $depositData = @{
        accountNumber = $accountNumber
        amount = [decimal]$amount
        description = $description
        userId = "CLI_USER"
    } | ConvertTo-Json

    try {
        $response = Invoke-RestMethod -Uri "$baseUrl/transactions/deposit" -Method POST -Body $depositData -ContentType "application/json"
        if ($response.success) {
            Write-Host " Deposit successful!" -ForegroundColor Green
            Write-Host "Transaction ID: $($response.transaction.transactionId)" -ForegroundColor Yellow
            Write-Host "Amount: ₹$($response.transaction.amount)" -ForegroundColor White
            Write-Host "New Balance: ₹$($response.transaction.destinationAccount.balance)" -ForegroundColor White
        } else {
            Write-Host " Deposit failed: $($response.message)" -ForegroundColor Red
        }
    } catch {
        Write-Host " Error: $($_.Exception.Message)" -ForegroundColor Red
    }
}

function Withdraw-Money {
    Write-Host "
 WITHDRAW MONEY" -ForegroundColor Cyan
    Write-Host "=================" -ForegroundColor Cyan

    $accountNumber = Read-Host "Account Number"
    $amount = Read-Host "Amount (₹)"
    $description = Read-Host "Description"

    $withdrawData = @{
        accountNumber = $accountNumber
        amount = [decimal]$amount
        description = $description
        userId = "CLI_USER"
    } | ConvertTo-Json

    try {
        $response = Invoke-RestMethod -Uri "$baseUrl/transactions/withdraw" -Method POST -Body $withdrawData -ContentType "application/json"
        if ($response.success) {
            Write-Host " Withdrawal successful!" -ForegroundColor Green
            Write-Host "Transaction ID: $($response.transaction.transactionId)" -ForegroundColor Yellow
            Write-Host "Amount: ₹$($response.transaction.amount)" -ForegroundColor White
            Write-Host "New Balance: ₹$($response.transaction.sourceAccount.balance)" -ForegroundColor White
        } else {
            Write-Host " Withdrawal failed: $($response.message)" -ForegroundColor Red
        }
    } catch {
        Write-Host " Error: $($_.Exception.Message)" -ForegroundColor Red
    }
}

function Transfer-Money {
    Write-Host "
 TRANSFER MONEY" -ForegroundColor Cyan
    Write-Host "=================" -ForegroundColor Cyan

    $sourceAccount = Read-Host "Source Account Number"
    $destinationAccount = Read-Host "Destination Account Number"
    $amount = Read-Host "Amount (₹)"
    $description = Read-Host "Description"

    $transferData = @{
        sourceAccountNumber = $sourceAccount
        destinationAccountNumber = $destinationAccount
        amount = [decimal]$amount
        description = $description
        userId = "CLI_USER"
    } | ConvertTo-Json

    try {
        $response = Invoke-RestMethod -Uri "$baseUrl/transactions/transfer" -Method POST -Body $transferData -ContentType "application/json"
        if ($response.success) {
            Write-Host " Transfer successful!" -ForegroundColor Green
            Write-Host "Transaction ID: $($response.transaction.transactionId)" -ForegroundColor Yellow
            Write-Host "Amount: ₹$($response.transaction.amount)" -ForegroundColor White
            Write-Host "From: $($response.transaction.sourceAccount.accountNumber)" -ForegroundColor White
            Write-Host "To: $($response.transaction.destinationAccount.accountNumber)" -ForegroundColor White
        } else {
            Write-Host " Transfer failed: $($response.message)" -ForegroundColor Red
        }
    } catch {
        Write-Host " Error: $($_.Exception.Message)" -ForegroundColor Red
    }
}

function View-AccountBalance {
    Write-Host "
 VIEW ACCOUNT BALANCE" -ForegroundColor Cyan
    Write-Host "=======================" -ForegroundColor Cyan

    $accountNumber = Read-Host "Account Number"

    try {
        $response = Invoke-RestMethod -Uri "$baseUrl/accounts" -Method GET
        if ($response.success) {
            $account = $response.accounts | Where-Object { $_.accountNumber -eq $accountNumber }
            if ($account) {
                Write-Host "
Account Details:" -ForegroundColor Green
                Write-Host "Account Number: $($account.accountNumber)" -ForegroundColor Yellow
                Write-Host "Account Type: $($account.accountType)" -ForegroundColor White
                Write-Host "Balance: ₹$($account.balance)" -ForegroundColor Green
                Write-Host "Customer: $($account.customer.firstName) $($account.customer.lastName)" -ForegroundColor White
                Write-Host "Status: $($account.status)" -ForegroundColor White
            } else {
                Write-Host " Account not found" -ForegroundColor Red
            }
        } else {
            Write-Host " Error loading accounts" -ForegroundColor Red
        }
    } catch {
        Write-Host " Error: $($_.Exception.Message)" -ForegroundColor Red
    }
}

function Show-SystemStatus {
    Write-Host "
 SYSTEM STATUS" -ForegroundColor Cyan
    Write-Host "================" -ForegroundColor Cyan

    try {
        # Test backend connection
        $response = Invoke-RestMethod -Uri "$baseUrl/customers" -Method GET
        Write-Host " Backend API: Connected" -ForegroundColor Green
        Write-Host " Database: Connected" -ForegroundColor Green
        Write-Host " System: Running" -ForegroundColor Green
    } catch {
        Write-Host " Backend API: Not Connected" -ForegroundColor Red
        Write-Host " Database: Not Connected" -ForegroundColor Red
        Write-Host " System: Not Running" -ForegroundColor Red
    }
}

# Main execution loop
while ($true) {
    Show-MainMenu
    $choice = Get-UserChoice

    switch ($choice) {
        "1" { Show-Dashboard }
        "2" { Show-AllCustomers }
        "3" { Show-AllAccounts }
        "4" { Write-Host "Transaction History feature - Use web interface at http://localhost:3000" -ForegroundColor Yellow }
        "5" { Write-Host "Audit Logs feature - Use web interface at http://localhost:3000" -ForegroundColor Yellow }
        "6" { Create-NewCustomer }
        "7" { Write-Host "View Customer Details - Use option 2 to see all customers" -ForegroundColor Yellow }
        "8" { Create-NewAccount }
        "9" { View-AccountBalance }
        "10" { Write-Host "View Account Details - Use option 3 to see all accounts" -ForegroundColor Yellow }
        "11" { Deposit-Money }
        "12" { Withdraw-Money }
        "13" { Transfer-Money }
        "14" { Write-Host "Undo Transaction - Use web interface at http://localhost:3000" -ForegroundColor Yellow }
        "15" { Write-Host "Redo Transaction - Use web interface at http://localhost:3000" -ForegroundColor Yellow }
        "16" { Write-Host "Data Sync - Use web interface at http://localhost:3000" -ForegroundColor Yellow }
        "17" { Show-SystemStatus }
        "0" {
            Write-Host "
 Thank you for using Indian Banking System CLI!" -ForegroundColor Green
            Write-Host "Web interface available at: http://localhost:3000" -ForegroundColor Cyan
            exit
        }
        default {
            Write-Host " Invalid choice. Please try again." -ForegroundColor Red
        }
    }

    Read-Host "
Press Enter to continue..."
}